package com.example.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BusinessReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_reports);  // קישור ל-XML של דוחות עסקיים

        Button viewReportButton = findViewById(R.id.view_report_button);

        // מאזין ללחיצה על כפתור צפייה בדוח
        viewReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // הגדר את פעולת צפייה בדוח
            }
        });
    }
}
