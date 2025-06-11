package com.example.firstapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewProfileActivity extends AppCompatActivity {
    private TextView customerNameTextView;
    private TextView userEmailTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        // אתחול Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // מציאת רכיבי UI
        customerNameTextView = findViewById(R.id.customer_name);
        userEmailTextView = findViewById(R.id.user_email);

        // הצגת אימייל
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null && !userEmail.isEmpty()) {
                userEmailTextView.setText(userEmail + "  אימייל ");
            } else {
                userEmailTextView.setText("אין אימייל זמין");
            }
        }

        // הצגת שם
        loadCustomerName();
    }

    private void loadCustomerName() {
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
                                    String email = document.getString("email");
                                    if (email != null && email.contains("@")) {
                                        String nameFromEmail = email.substring(0, email.indexOf("@"));
                                        customerNameTextView.setText("שלום " + nameFromEmail + "!");
                                    } else {
                                        customerNameTextView.setText("שלום אורח!");
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
