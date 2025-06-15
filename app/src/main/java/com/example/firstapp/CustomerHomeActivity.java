package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CustomerHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);

        // עדכון שם המשתמש בראש המסך
        TextView customerNameTextView = findViewById(R.id.customer_name);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();

            // אם אין שם תצוגה, ננסה להשתמש באימייל
            if (displayName == null || displayName.isEmpty()) {
                String email = currentUser.getEmail();
                if (email != null && email.contains("@")) {
                    displayName = email.substring(0, email.indexOf("@"));
                }
            }

            // הצגת "שלום" ושם המשתמש
            customerNameTextView.setText("שלום " + displayName + "!");
        }

        findViewById(R.id.view_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך הצגת פרופיל
                Intent intent = new Intent(CustomerHomeActivity.this, ViewProfileActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.view_orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך הצגת הזמנות
                Intent intent = new Intent(CustomerHomeActivity.this, ViewOrdersActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.customer_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר למסך תמיכה ללקוח
                Intent intent = new Intent(CustomerHomeActivity.this, CustomerSupportActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.personal_suggestion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //מעבר למסך המלצות אישיות
                Intent intent = new Intent(CustomerHomeActivity.this, PersonalSuggestionActivity.class);
                startActivity(intent);
            }
        });

        Button gamepartner = findViewById(R.id.game_partner);
        gamepartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerHomeActivity.this, GamePartnerActivity.class);
                startActivity(intent);
            }
        });

        Button bLogOut = findViewById(R.id.log_out);
        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ניתוק משתמש מה-Firebase
                FirebaseAuth.getInstance().signOut();

                // הודעה למשתמש
                Toast.makeText(CustomerHomeActivity.this, "התנתקת בהצלחה", Toast.LENGTH_SHORT).show();

                // מעבר למסך הראשי וניקוי המחסנית
                Intent intent = new Intent(CustomerHomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // סגירת המסך הנוכחי
            }
        });
    }
}