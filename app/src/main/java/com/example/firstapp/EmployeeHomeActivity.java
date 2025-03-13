package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_home);

        // כפתור ניהול משימות
        Button manageTasksButton = findViewById(R.id.manage_tasks);
        manageTasksButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeHomeActivity.this, ManageTasksActivity.class);
            startActivity(intent);
        });

        // כפתור הצגת לוח זמנים
        Button viewScheduleButton = findViewById(R.id.view_schedule);
        viewScheduleButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeHomeActivity.this, ViewScheduleActivity.class);
            startActivity(intent);
        });

        // כפתור הגשת משמרות לשבוע
        Button submitShiftsButton = findViewById(R.id.submit_shifts);
        submitShiftsButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeHomeActivity.this, SubmitShiftsActivity.class);
            startActivity(intent);
        });

        // כפתור יצירת קשר עם המנהל
        Button communicateWithAdminButton = findViewById(R.id.communicate_with_admin);
        communicateWithAdminButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeHomeActivity.this, CommunicateWithAdminActivity.class);
            startActivity(intent);
        });
    }
}
