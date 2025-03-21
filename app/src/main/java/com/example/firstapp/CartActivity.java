package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_checkout);

        Button continueToPaymentButton = findViewById(R.id.continue_to_payment);

        continueToPaymentButton.setOnClickListener(v -> {
            // מעבר למסך התשלום
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            startActivity(intent);
        });
    }
}
