package com.example.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_requests);  // קישור ל-XML של פניות לקוחות

        Button manageRequestButton = findViewById(R.id.manage_request_button);

        // מאזין ללחיצה על כפתור ניהול פנייה
        manageRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // הגדר את פעולת ניהול פנייה
            }
        });
    }
}
