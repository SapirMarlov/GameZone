package com.example.firstapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViewOrdersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_orders_history);

        TextView customerNameTextView = findViewById(R.id.customer_name);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // קובע את שם המשתמש
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                customerNameTextView.setText("שלום " + displayName + ", אלו הם היסטוריית ההזמנות שלך");
            } else {
                // אם אין שם תצוגה, משתמש בשם מהאימייל
                String email = currentUser.getEmail();
                if (email != null && email.contains("@")) {
                    displayName = email.substring(0, email.indexOf("@"));
                    customerNameTextView.setText("שלום " + displayName + ", אלו הם היסטוריית ההזמנות שלך");
                }
            }
        } else {
            // למקרה שאין משתמש מחובר
            customerNameTextView.setText("היסטוריית הזמנות");
        }
    }
}