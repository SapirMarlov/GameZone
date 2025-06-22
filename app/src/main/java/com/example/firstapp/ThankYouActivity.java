package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ThankYouActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        Button backButton = findViewById(R.id.button_back_home);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class); // או CartActivity
            startActivity(intent);
            finish();
        });
    }
}
