package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BusinessReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_reports_screen);  //

        // כפתור התחברות
        Button generatereportbutton = findViewById(R.id.generate_report_button);
        generatereportbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessReportsActivity.this, BusinessReportsChoiceActivity.class);
                startActivity(intent);
            }
        });


    }
}
