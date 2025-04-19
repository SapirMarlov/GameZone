package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

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
    }
}
