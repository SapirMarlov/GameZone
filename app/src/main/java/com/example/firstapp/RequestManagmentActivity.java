package com.example.firstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RequestManagmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_management);


        Button close_request_button = findViewById(R.id.close_request_button);


        close_request_button.setOnClickListener(v ->
                Toast.makeText(RequestManagmentActivity.this, "הפנייה נסגרה!", Toast.LENGTH_SHORT).show()
        );


    }
}
