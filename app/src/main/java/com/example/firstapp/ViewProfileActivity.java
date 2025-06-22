package com.example.firstapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView customerNameTextView, userEmailTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        customerNameTextView = findViewById(R.id.customer_name);
        userEmailTextView = findViewById(R.id.user_email);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // שליפת השם מתוך Firestore
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(document -> {
                        String username = document.getString("username");
                        if (username != null && !username.isEmpty()) {
                            customerNameTextView.setText("שלום " + username);
                        } else {
                            customerNameTextView.setText("שלום משתמש");
                        }
                    })
                    .addOnFailureListener(e -> customerNameTextView.setText("שלום משתמש"));

            // הצגת האימייל
            String email = currentUser.getEmail();
            if (email != null && !email.isEmpty()) {
                userEmailTextView.setText(email + "  אימייל");
            } else {
                userEmailTextView.setText("אין אימייל זמין");
            }
        }
    }
}
