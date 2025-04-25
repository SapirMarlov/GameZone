package com.example.firstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SubmitShiftsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_shifts);

        Button send_shifts = findViewById(R.id.submit_button);


        send_shifts.setOnClickListener(v ->
                Toast.makeText(SubmitShiftsActivity.this, "נשלח בהצלחה!", Toast.LENGTH_SHORT).show()
        );



    }
}
