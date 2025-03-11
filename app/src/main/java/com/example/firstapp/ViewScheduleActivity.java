package com.example.firstapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ViewScheduleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_schedule);  // קישור ל-XML של לוח זמנים
    }
}
