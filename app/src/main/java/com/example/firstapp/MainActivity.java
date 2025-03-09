package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // מציאת כפתור התחברות
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך ההתחברות
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // פונקציה שתיקרא כאשר לוחצים על אחד הפריטים
    public void openProductPage(View view) {
        // קבלת שם המוצר או מזהה המוצר מתוך ה-TextView
        TextView productName = view.findViewById(R.id.product_name);
        String productId = productName.getText().toString();

        // יצירת אינטנט להעברת פרטי המוצר למסך פרטי המוצר
        Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
        intent.putExtra("product_id", productId);
        startActivity(intent);
    }
}

