package com.example.firstapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.*;

public class BusinessReportsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView totalSalesText, totalTransactionsText, bestProductText, goalStatusText, reportHeader;
    private Button generateReportButton, selectReportRangeButton;
    private TextView selectedRangeLabel;
    private String selectedRange = "daily";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_reports_screen);

        db = FirebaseFirestore.getInstance();

        reportHeader = findViewById(R.id.report_header);
        totalSalesText = findViewById(R.id.total_sales);
        totalTransactionsText = findViewById(R.id.total_transactions);
        bestProductText = findViewById(R.id.best_selling_product);
        goalStatusText = findViewById(R.id.total_profit);
        generateReportButton = findViewById(R.id.generate_report_button);
        selectReportRangeButton = findViewById(R.id.select_report_range_button);
        selectedRangeLabel = findViewById(R.id.selected_report_range_label);

        setHeaderTimestamp();

        selectReportRangeButton.setOnClickListener(v -> showRangeDialog());

        generateReportButton.setOnClickListener(v -> {
            Intent intent = new Intent(BusinessReportsActivity.this, BusinessReportsChoiceActivity.class);
            intent.putExtra("reportRange", selectedRange);
            startActivity(intent);
        });

        loadSalesReportForRange(selectedRange);
    }

    private void showRangeDialog() {
        final String[] options = {"דו\"ח יומי", "דו\"ח חודשי", "דו\"ח רבעוני", "דו\"ח שנתי"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר טווח דוח");
        builder.setItems(options, (dialog, which) -> {
            selectedRange = getReportRangeFromText(options[which]);
            selectedRangeLabel.setText("נבחר: " + options[which]);
            loadSalesReportForRange(selectedRange);
            selectReportRangeButton.setEnabled(false); // הפיכת הכפתור ללא פעיל
        });
        builder.show();
    }

    private void setHeaderTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy | HH:mm", Locale.getDefault());
        String formatted = sdf.format(new Date());
        reportHeader.setText("\uD83D\uDCCA נתונים עדכניים נכון ל: " + formatted);
    }

    private void loadSalesReportForRange(String range) {
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

        final double[] totalSales = {0};
        final int[] totalTransactions = {0};
        Map<String, Integer> productSales = new HashMap<>();

        db.collection("orders")
                .whereGreaterThanOrEqualTo("timestamp", new Timestamp(startDate))
                .whereLessThanOrEqualTo("timestamp", new Timestamp(endDate))
                .get()
                .addOnSuccessListener(orderSnapshots -> {
                    if (orderSnapshots.isEmpty()) {
                        updateTodayUI(0, 0, new HashMap<>());
                        return;
                    }

                    for (QueryDocumentSnapshot doc : orderSnapshots) {
                        Object priceObj = doc.get("totalPrice");
                        double price = 0;
                        if (priceObj instanceof Number) {
                            price = ((Number) priceObj).doubleValue();
                        } else if (priceObj instanceof String) {
                            try {
                                price = Double.parseDouble((String) priceObj);
                            } catch (NumberFormatException e) {
                                // התעלמות
                            }
                        }
                        totalSales[0] += price;
                        totalTransactions[0]++;

                        doc.getReference().collection("items")
                                .get()
                                .addOnSuccessListener(items -> {
                                    for (QueryDocumentSnapshot item : items) {
                                        String name = item.getString("name");
                                        Long qty = item.getLong("quantity");
                                        if (name != null && qty != null) {
                                            productSales.put(name, productSales.getOrDefault(name, 0) + qty.intValue());
                                        }
                                    }
                                    updateTodayUI(totalSales[0], totalTransactions[0], productSales);
                                });
                    }
                });
    }

    private void updateTodayUI(double totalSales, int totalTransactions, Map<String, Integer> productSales) {
        String bestProduct = null;
        int maxQty = 0;
        StringBuilder goalStatusBuilder = new StringBuilder();

        for (Map.Entry<String, Integer> entry : productSales.entrySet()) {
            String product = entry.getKey();
            int sold = entry.getValue();
            int target = getTargetForProduct(product);
            int remaining = Math.max(0, target - sold);
            goalStatusBuilder.append(product)
                    .append(": נמכר ")
                    .append(sold)
                    .append(" מתוך יעד של ")
                    .append(target)
                    .append(" (נותרו: ")
                    .append(remaining)
                    .append(")\n");

            if (sold > maxQty) {
                maxQty = sold;
                bestProduct = product;
            }
        }

        totalSalesText.setText("\uD83D\uDCB0 סה\"כ מכירות: " + totalSales + "₪");
        totalTransactionsText.setText("\uD83D\uDED2 מספר עסקאות: " + totalTransactions);
        bestProductText.setText("\uD83D\uDD25 הכי נמכר: " + (bestProduct != null ? bestProduct : "---"));
        goalStatusText.setText("\uD83C\uDFC6 סטטוס יעדים:\n" + goalStatusBuilder);
    }

    private int getTargetForProduct(String product) {
        Map<String, Integer> targets = new HashMap<>();
        targets.put("razor screen 144p", 15);
        targets.put("macbook", 10);
        targets.put("steel sieres", 25);
        return targets.getOrDefault(product, 20);
    }

    private String getReportRangeFromText(String selectedText) {
        switch (selectedText) {
            case "דו\"ח יומי": return "daily";
            case "דו\"ח חודשי": return "monthly";
            case "דו\"ח רבעוני": return "quarterly";
            case "דו\"ח שנתי": return "yearly";
            default: return "daily";
        }
    }
}
