package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);

        findViewById(R.id.view_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך הצגת פרופיל
                Intent intent = new Intent(CustomerHomeActivity.this, ViewProfileActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.view_orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך הצגת הזמנות
                Intent intent = new Intent(CustomerHomeActivity.this, ViewOrdersActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.customer_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך תמיכה ללקוח
                Intent intent = new Intent(CustomerHomeActivity.this, CustomerSupportActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.personal_suggestion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //מעבר למסך המלצות אישיות

                Intent intent = new Intent(CustomerHomeActivity.this, PersonalSuggestionActivity.class);
                startActivity(intent);
            }
        });

        Button gamepartner = findViewById(R.id.game_partner);
        gamepartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerHomeActivity.this, GamePartnerActivity.class);
                startActivity(intent);
            }
        });
    }
}
