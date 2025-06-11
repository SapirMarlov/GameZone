package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);


        Button manageOrdersButton = findViewById(R.id.manage_orders);
        Button manageInventoryButton = findViewById(R.id.manage_inventory);
        Button customerRequestsButton = findViewById(R.id.customer_requests);
        Button viewReportsButton = findViewById(R.id.view_reports);
        Button manageEmployeesButton = findViewById(R.id.manage_employees);
        Button employeesScreenButton = findViewById(R.id.employee_home);
        Button manegerAddEmployeeButton = findViewById(R.id.employee_add_tp_system);

        manageOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך ניהול הזמנות
                Intent intent = new Intent(AdminDashboardActivity.this, ManageOrdersActivity.class);
                startActivity(intent);
            }
        });

        manageInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך ניהול מלאי
                Intent intent = new Intent(AdminDashboardActivity.this, InventoryManagementActivity.class);
                startActivity(intent);
            }
        });

        customerRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך פניות לקוחות
                Intent intent = new Intent(AdminDashboardActivity.this, CustomerRequestsActivity.class);
                startActivity(intent);
            }
        });

        viewReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך דוחות עסקיים
                Intent intent = new Intent(AdminDashboardActivity.this, BusinessReportsActivity.class);
                startActivity(intent);
            }
        });

        manageEmployeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך ניהול עובדים
                Intent intent = new Intent(AdminDashboardActivity.this, ManageEmployeesActivity.class);
                startActivity(intent);
            }
        });

        employeesScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך עובדים
                Intent intent = new Intent(AdminDashboardActivity.this, EmployeeHomeActivity.class);
                startActivity(intent);
            }
        });
        manegerAddEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך הרשמת עובד חדש למערכת
                Intent intent = new Intent(AdminDashboardActivity.this, RegisterAdminActivityToSystemActivity.class);
                startActivity(intent);
            }
        });
        Button bLogOut = findViewById(R.id.log_out);
        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ניתוק משתמש מה-Firebase
                FirebaseAuth.getInstance().signOut();

                // הודעה למשתמש
                Toast.makeText(AdminDashboardActivity.this, "התנתקת בהצלחה", Toast.LENGTH_SHORT).show();

                // מעבר למסך הראשי וניקוי המחסנית
                Intent intent = new Intent(AdminDashboardActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // סגירת המסך הנוכחי
            }
        });
    }
}
