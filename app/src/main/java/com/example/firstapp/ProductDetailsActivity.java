package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details); // קובץ ה-XML של פרטי המוצר

        // מציאת רכיבי ה-XML
        ImageView productImage = findViewById(R.id.product_image);
        TextView productName = findViewById(R.id.product_name);
        TextView productDescription = findViewById(R.id.product_description);
        TextView productPrice = findViewById(R.id.product_price);
        Button addToCartButton = findViewById(R.id.add_to_cart);
        Button goToCartButton = findViewById(R.id.go_to_cart);

        // עדכון הפרטים של ה-PS5
        productImage.setImageResource(R.drawable.ps5);
        productName.setText("Sony PlayStation 5");
        productDescription.setText("קונסולת משחקים מהדור הבא עם מעבד עוצמתי ואחסון SSD מהיר במיוחד.");
        productPrice.setText("₪2,499");

        // כפתור הוספה לעגלה
        addToCartButton.setOnClickListener(v ->
                Toast.makeText(ProductDetailsActivity.this, "המוצר נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );

        // כפתור מעבר לעגלת קניות
        goToCartButton.setOnClickListener(v -> {
            // מעבר לעגלת קניות
            Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }
}
