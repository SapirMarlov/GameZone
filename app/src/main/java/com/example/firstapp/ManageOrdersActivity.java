package com.example.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ManageOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_orders);  // קישור ל-XML של ניהול הזמנות

        Button manageOrderButton = findViewById(R.id.manage_order_button);

        // מאזין ללחיצה על כפתור ניהול הזמנה
        manageOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // הגדר את פעולת ניהול הזמנה (למשל לעבור למסך פרטי הזמנה)
            }
        });
    }
}
