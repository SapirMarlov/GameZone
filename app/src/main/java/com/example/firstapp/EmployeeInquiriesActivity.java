package com.example.firstapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeInquiriesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_inquiries); // קישור ל-XML של פניות עובדים
    }
}
