package com.example.firstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateInventoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_inventory);

        Button buttonSave = findViewById(R.id.buttonSave);


        buttonSave.setOnClickListener(v ->
                Toast.makeText(UpdateInventoryActivity.this, "מלאי עודכן בהצלחה!", Toast.LENGTH_SHORT).show()
        );

    }
}
