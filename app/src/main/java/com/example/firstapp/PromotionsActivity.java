package com.example.firstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PromotionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotions);


        Button add_ps5_to_cart = findViewById(R.id.add_ps5_to_cart);
        Button add_headphones_to_cart = findViewById(R.id.add_headphones_to_cart);
        Button add_keyboard_to_cart = findViewById(R.id.add_keyboard_to_cart);
        Button add_mouse_to_cart = findViewById(R.id.add_mouse_to_cart);


        add_ps5_to_cart.setOnClickListener(v ->
                Toast.makeText(PromotionsActivity.this, "המוצר נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );

        add_headphones_to_cart.setOnClickListener(v ->
                Toast.makeText(PromotionsActivity.this, "המוצר נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );

        add_keyboard_to_cart.setOnClickListener(v ->
                Toast.makeText(PromotionsActivity.this, "המוצר נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );

        add_mouse_to_cart.setOnClickListener(v ->
                Toast.makeText(PromotionsActivity.this, "המוצר נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );



    }
}
