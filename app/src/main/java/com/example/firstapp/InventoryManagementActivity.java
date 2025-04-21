package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InventoryManagementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_management_screen);

        // כפתור עדכון מלאי PS5
        Button updatestockPS5button = findViewById(R.id.update_stock_PS5_button);
        updatestockPS5button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventoryManagementActivity.this, UpdateItemDetailsActivity.class);
                startActivity(intent);  // פותח את UpdateInventoryActivity
            }
        });

        // כפתור עדכון מלאי Xbox Controller X
        Button updatestockXboxcontrollerXbutton = findViewById(R.id.update_stock_Xbox_controller_X_button);
        updatestockXboxcontrollerXbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventoryManagementActivity.this, UpdateInventoryActivity.class);
                startActivity(intent);  // פותח את UpdateInventoryActivity
            }
        });

        // כפתור עדכון מלאי Nintendo Switch
        Button updatestockNintendoSwitchbutton = findViewById(R.id.update_stock_Nintendo_Switch_button);
        updatestockNintendoSwitchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventoryManagementActivity.this, UpdateInventoryActivity.class);
                startActivity(intent);  // פותח את UpdateInventoryActivity
            }
        });

        // כפתור עדכון מלאי Razer Keyboard
        Button updatestockRazerKeyboardbutton = findViewById(R.id.update_stock_Razer_Keyboard_button);
        updatestockRazerKeyboardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventoryManagementActivity.this, UpdateInventoryActivity.class);
                startActivity(intent);  // פותח את UpdateInventoryActivity
            }
        });

        // כפתור הוסף מוצר חדש
        Button addnewproduct = findViewById(R.id.add_new_product);
        addnewproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventoryManagementActivity.this, UpdateInventoryActivity.class);
                startActivity(intent);  // פותח את UpdateInventoryActivity
            }
        });

    }
}
