package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerPostCreationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_post_creation);

        Button publish_profile_button = findViewById(R.id.publish_profile_button);


        publish_profile_button.setOnClickListener(v ->
                Toast.makeText(PlayerPostCreationActivity.this, "המודעה פורסמה!", Toast.LENGTH_SHORT).show()
        );


    }
}