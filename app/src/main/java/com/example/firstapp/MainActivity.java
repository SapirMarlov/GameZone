package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // activity_main.xml



        Button add_ps5_to_cart = findViewById(R.id.add_ps5_to_cart);
        Button add_nintendo_to_cart = findViewById(R.id.add_nintendo_to_cart);
        Button add_asus_to_cart = findViewById(R.id.add_asus_to_cart);
        Button add_razer_to_cart = findViewById(R.id.add_razer_to_cart);




        // כפתור התחברות
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // כפתור אזור אישי
        Button userProfileButton = findViewById(R.id.user_profile_button);
        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomerHomeActivity.class);
                startActivity(intent);
            }
        });


        Button cartbutton = findViewById(R.id.cart_button);
        cartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        Button categoriessbutton = findViewById(R.id.categories_button);
        categoriessbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        });


        Button promotionsbutton = findViewById(R.id.promotions_button);
        promotionsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PromotionsActivity.class);
                startActivity(intent);
            }
        });

        Button searchplayersbutton = findViewById(R.id.search_players_button);
        searchplayersbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GamePartnerActivity.class);
                startActivity(intent);
            }
        });



        ImageView ps5Image = findViewById(R.id.ps5_image);
        ps5Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                startActivity(intent);
            }
        });



        add_ps5_to_cart.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "המוצר נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );

        add_nintendo_to_cart.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "המוצר נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );

        add_asus_to_cart.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "המוצר נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );

        add_razer_to_cart.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "המוצר נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );
    }
}
