package com.example.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);  // קישור ל-XML של לוח הבקרה

        // הגדרת כפתורים במסך
        Button manageOrdersButton = findViewById(R.id.manage_orders);
        Button manageInventoryButton = findViewById(R.id.manage_inventory);
        Button customerRequestsButton = findViewById(R.id.customer_requests);
        Button viewReportsButton = findViewById(R.id.view_reports);
        Button manageEmployeesButton = findViewById(R.id.manage_employees);


        // מאזין ללחיצה על כל כפתור
        manageOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך ניהול הזמנות
            }
        });

        manageInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך ניהול מלאי
            }
        });

        customerRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך פניות לקוחות
            }
        });

        viewReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך דוחות עסקיים
            }
        });

        manageEmployeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך ניהול עובדים
            }
        });

    }}
