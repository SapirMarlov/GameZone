package com.example.firstapp;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainFunctionality extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard); // ברירת מחדל למסך הראשי

        setupButtons();
    }

    private void setupButtons() {
        findViewById(R.id.manage_orders).setOnClickListener(v -> openScreen(R.layout.employee_order_management));
        findViewById(R.id.manage_inventory).setOnClickListener(v -> openScreen(R.layout.inventory_management_screen));
        findViewById(R.id.customer_requests).setOnClickListener(v -> openScreen(R.layout.customer_support_screen));
        findViewById(R.id.view_reports).setOnClickListener(v -> openScreen(R.layout.business_reports_screen));
        findViewById(R.id.manage_employees).setOnClickListener(v -> showMessage("ניהול עובדים יתווסף בקרוב"));
        findViewById(R.id.system_settings).setOnClickListener(v -> showMessage("הגדרות מערכת יתווספו בקרוב"));
    }

    private void openScreen(int layoutId) {
        setContentView(layoutId);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // עדכון סטטוס הזמנה
    public void updateOrderStatus(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("עדכון סטטוס הזמנה");
        final EditText input = new EditText(this);
        input.setHint("הזן סטטוס חדש");
        builder.setView(input);

        builder.setPositiveButton("עדכן", (dialog, which) ->
                showMessage("הסטטוס עודכן ל: " + input.getText().toString()));
        builder.setNegativeButton("ביטול", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // שליחת פנייה ללקוח
    public void sendCustomerRequest(View view) {
        showMessage("הפנייה נשלחה בהצלחה!");
    }

    // תשלום בכרטיס אשראי
    public void confirmCreditPayment(View view) {
        showMessage("התשלום בוצע בהצלחה!");
    }
}
