package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class CategoriesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);


        LinearLayout categorygames = findViewById(R.id.category_games);
        categorygames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, GameCategoryActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout categorycomputers = findViewById(R.id.category_computers);
        categorycomputers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, ComputerCategoryActivity.class);
                startActivity(intent);
            }
        });


        LinearLayout categoryheadphones = findViewById(R.id.category_headphones);
        categoryheadphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, HeadphonesCategoryActivity.class);
                startActivity(intent);
            }
        });


        LinearLayout categorychairs = findViewById(R.id.category_chairs);
        categorychairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, GamingChairCategoryActivity.class);
                startActivity(intent);
            }
        });







    }
}
