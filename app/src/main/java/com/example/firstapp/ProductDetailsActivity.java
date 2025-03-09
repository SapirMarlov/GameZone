package com.example.firstapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_screen);

        // קבלת פרטי המוצר שהועברו
        String productName = getIntent().getStringExtra("product_name");
        String productPrice = getIntent().getStringExtra("product_price");
        int productImage = getIntent().getIntExtra("product_image", -1);

        // הצגת שם המוצר
        TextView nameTextView = findViewById(R.id.product_name);
        nameTextView.setText(productName);

        // הצגת מחיר המוצר
        TextView priceTextView = findViewById(R.id.product_price);
        priceTextView.setText(productPrice);

        // הצגת תמונת המוצר
        ImageView imageView = findViewById(R.id.product_image);
        imageView.setImageResource(productImage);
    }
}
