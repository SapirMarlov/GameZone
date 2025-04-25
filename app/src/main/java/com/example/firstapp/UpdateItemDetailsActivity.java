package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateItemDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updete_item_details);



        Button buttonconfirm = findViewById(R.id.button_confirm);




        buttonconfirm.setOnClickListener(v ->
                Toast.makeText(UpdateItemDetailsActivity.this, "מלאי עודכן בהצלחה!", Toast.LENGTH_SHORT).show()
        );


    }
}