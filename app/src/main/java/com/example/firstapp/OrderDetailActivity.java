package com.example.firstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrderDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);


        Button update_order_button = findViewById(R.id.update_order_button);

        update_order_button.setOnClickListener(v ->
                Toast.makeText(OrderDetailActivity.this, "עודכן סטטוס הזמנה!", Toast.LENGTH_SHORT).show()
        );


    }
}
