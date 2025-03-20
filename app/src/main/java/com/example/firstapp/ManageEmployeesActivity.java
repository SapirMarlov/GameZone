package com.example.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ManageEmployeesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_employees);  // קישור ל-XML של ניהול עובדים

        Button manageEmployeeButton = findViewById(R.id.create_schedule_button);

        // מאזין ללחיצה על כפתור ניהול עובד
        manageEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // הגדר את פעולת ניהול עובד
            }
        });
    }
}
