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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerHomeActivity extends AppCompatActivity {
    private TextView customerNameTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);

        // אתחול רכיבים
        customerNameTextView = findViewById(R.id.customer_name);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // טעינת שם המשתמש
        loadcustomerName();

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

    private void loadcustomerName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String username = document.getString("username");

                                if (username != null && !username.isEmpty()) {
                                    customerNameTextView.setText("שלום " + username + "!");
                                } else {
                                    String displayName = document.getString("displayName");

                                    if (displayName != null && !displayName.isEmpty()) {
                                        customerNameTextView.setText("שלום " + displayName + "!");
                                    } else {
                                        String email = document.getString("email");
                                        if (email != null && email.contains("@")) {
                                            String nameFromEmail = email.substring(0, email.indexOf("@"));
                                            customerNameTextView.setText("שלום " + nameFromEmail + "!");
                                        } else {
                                            customerNameTextView.setText("שלום אורח!");
                                        }
                                    }
                                }
                            } else {
                                customerNameTextView.setText("שלום אורח!");
                            }
                        } else {
                            customerNameTextView.setText("שלום אורח!");
                        }
                    });
        } else {
            customerNameTextView.setText("שלום אורח!");
        }
    }
}
