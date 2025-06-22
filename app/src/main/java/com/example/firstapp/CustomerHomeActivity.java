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
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerHomeActivity extends AppCompatActivity {

    private TextView customerNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);

        customerNameTextView = findViewById(R.id.customer_name);
        loadUsernameFromFirestore();

        findViewById(R.id.view_profile).setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, ViewProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.view_orders).setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, ViewOrdersActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.customer_support).setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, CustomerSupportActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.personal_suggestion).setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, PersonalSuggestionActivity.class);
            startActivity(intent);
        });

        Button gamepartner = findViewById(R.id.game_partner);
        gamepartner.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHomeActivity.this, GamePartnerActivity.class);
            startActivity(intent);
        });

        Button bLogOut = findViewById(R.id.log_out);
        bLogOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(CustomerHomeActivity.this, "התנתקת בהצלחה", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(CustomerHomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUsernameFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            customerNameTextView.setText("שלום אורח");
            return;
        }

        String uid = currentUser.getUid();
        FirebaseFirestore.getInstance().collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String username = documentSnapshot.getString("username");

                    if (username != null && !username.isEmpty()) {
                        customerNameTextView.setText("שלום " + username + "!");
                    } else {
                        // אם אין username, נ fallback לאימייל
                        String email = currentUser.getEmail();
                        if (email != null && email.contains("@")) {
                            String fallback = email.substring(0, email.indexOf("@"));
                            customerNameTextView.setText("שלום " + fallback + "!");
                        } else {
                            customerNameTextView.setText("שלום משתמש!");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    customerNameTextView.setText("שלום משתמש!");
                });
    }
}
