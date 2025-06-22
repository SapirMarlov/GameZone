package com.example.firstapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.*;
import java.text.SimpleDateFormat;

public class BusinessReportsChoiceActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView summaryTextView;
    private PieChart pieChart;
    private BarChart barChart;
    private Spinner reportTypeSpinner;
    private Button closeButton;
    private String reportRange = "daily";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_reports_choice);

        summaryTextView = findViewById(R.id.text_summary);
        pieChart = findViewById(R.id.pie_chart);
        barChart = findViewById(R.id.bar_chart);
        reportTypeSpinner = findViewById(R.id.report_category_spinner);
        closeButton = findViewById(R.id.close_button);

        db = FirebaseFirestore.getInstance();

        pieChart.getDescription().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("reportRange")) {
            reportRange = intent.getStringExtra("reportRange");
        }

        reportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedReport = parent.getItemAtPosition(position).toString();
                if (selectedReport.contains("מכירות")) {
                    fetchSalesReport(reportRange);
                } else if (selectedReport.contains("מלאי")) {
                    fetchInventoryReport();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        closeButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(BusinessReportsChoiceActivity.this, BusinessReportsActivity.class);
            startActivity(backIntent);
            finish();
        });

        reportTypeSpinner.post(() -> {
            String selectedReport = reportTypeSpinner.getSelectedItem().toString();
            if (selectedReport.contains("מכירות")) {
                fetchSalesReport(reportRange);
            } else if (selectedReport.contains("מלאי")) {
                fetchInventoryReport();
            }
        });
    }

    private void fetchSalesReport(String range) {
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        Date startDate;

        switch (range) {
            case "daily":
                cal.add(Calendar.DAY_OF_YEAR, -1);
                break;
            case "monthly":
                cal.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case "quarterly":
                int currentMonth = cal.get(Calendar.MONTH);
                int startQuarterMonth = (currentMonth / 3) * 3;
                cal.set(Calendar.MONTH, startQuarterMonth);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case "yearly":
                cal.set(Calendar.MONTH, Calendar.JANUARY);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                break;
        }

        startDate = cal.getTime();

        db.collection("orders")
                .whereGreaterThanOrEqualTo("timestamp", new Timestamp(startDate))
                .whereLessThanOrEqualTo("timestamp", new Timestamp(endDate))
                .get()
                .addOnSuccessListener(query -> {
                    float totalSales = 0;
                    int count = 0;

                    for (QueryDocumentSnapshot doc : query) {
                        Object priceObj = doc.get("totalPrice");
                        float price = 0;
                        if (priceObj instanceof Number) {
                            price = ((Number) priceObj).floatValue();
                        } else if (priceObj instanceof String) {
                            try {
                                price = Float.parseFloat((String) priceObj);
                            } catch (NumberFormatException e) {
                                Log.w("ParseError", "Invalid price format: " + priceObj);
                            }
                        }
                        totalSales += price;
                        count++;
                    }

                    String formattedSales = String.format(Locale.getDefault(), "%.2f", totalSales);
                    summaryTextView.setText("דוח " + getHebrewRangeLabel(range) + ": " + count + " עסקאות, סה\"כ מכירות: ₪" + formattedSales);
                    showCharts(totalSales, count, "סה\"כ מכירות", "מספר עסקאות");
                })
                .addOnFailureListener(e -> Log.e("BusinessReportsChoice", "שגיאה בטעינת דוח", e));
    }

    private void fetchInventoryReport() {
        db.collection("product")
                .get()
                .addOnSuccessListener(query -> {
                    int totalItems = 0;
                    int totalLowStock = 0;
                    List<String> lowStockNames = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : query) {
                        Long quantity = doc.getLong("stock");
                        String name = doc.getString("name");
                        if (quantity != null) {
                            totalItems += quantity;
                            if (quantity < 5) {
                                totalLowStock++;
                                if (name != null) {
                                    lowStockNames.add(name);
                                }
                            }
                        }
                    }

                    StringBuilder lowStockText = new StringBuilder();
                    if (!lowStockNames.isEmpty()) {
                        lowStockText.append("\nפריטים במלאי נמוך: ");
                        for (String name : lowStockNames) {
                            lowStockText.append("\n• ").append(name);
                        }
                    }

                    summaryTextView.setText("סה\"כ מלאי נוכחי: " + totalItems + " פריטים, " + totalLowStock + " מוצרים במלאי נמוך" + lowStockText);
                    showCharts(totalItems, totalLowStock, "סה\"כ פריטים", "פריטים במלאי נמוך");
                })
                .addOnFailureListener(e -> Log.e("BusinessReportsChoice", "שגיאה בטעינת דוח מלאי", e));
    }

    private void showCharts(float val1, int val2, String label1, String label2) {
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(val1, label1));
        pieEntries.add(new PieEntry(val2, label2));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "פילוח נתונים");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieChart.setData(new PieData(pieDataSet));
        pieChart.invalidate();

        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1f, val1));
        barEntries.add(new BarEntry(2f, val2));

        BarDataSet barDataSet = new BarDataSet(barEntries, "פילוח נתונים");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(14f);
        barDataSet.setValueTextColor(Color.BLACK);
        barChart.setData(new BarData(barDataSet));
        barChart.invalidate();
    }

    private String getHebrewRangeLabel(String range) {
        switch (range) {
            case "daily": return "יומי";
            case "monthly": return "חודשי";
            case "quarterly": return "רבעוני";
            case "yearly": return "שנתי";
            default: return "";
        }
    }
}
