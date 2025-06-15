package com.example.firstapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        // מוצא את ה-TextViews
        TextView customerNameTextView = findViewById(R.id.customer_name);
        TextView userEmailTextView = findViewById(R.id.user_email);

        // מקבל את המשתמש הנוכחי מפיירבייס
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // קובע את שם המשתמש
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                customerNameTextView.setText("שלום " + displayName);
            } else {
                // אם אין שם תצוגה, משתמש בשם מהאימייל
                String email = currentUser.getEmail();
                if (email != null && email.contains("@")) {
                    displayName = email.substring(0, email.indexOf("@"));
                    customerNameTextView.setText(" שלום " + displayName);
                }
            }

            // קובע את האימייל
            String userEmail = currentUser.getEmail();
            if (userEmail != null && !userEmail.isEmpty()) {
                userEmailTextView.setText(userEmail + "  אימייל ");
            } else {
                userEmailTextView.setText("אין אימייל זמין");
            }
        }
    }
}