package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;



public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailInput, passwordInput;
    private Button loginBtn, goToRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // תואם לקובץ שלך

        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_button);
        goToRegisterBtn = findViewById(R.id.register_button);

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
                return;
            }


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        // progressBar.setVisibility(View.GONE);  // אם הוספת תצוגת טעינה

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                String uid = user.getUid();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference userRef = db.collection("users").document(uid);

                                userRef.get().addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        String username = documentSnapshot.getString("username");
                                        if (username == null || username.isEmpty()) {
                                            username = user.getEmail().split("@")[0]; // fallback
                                        }

                                        Toast.makeText(this, "התחברת בהצלחה, " + username + "!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("username", username); // אם את רוצה להשתמש בזה ב-MainActivity
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(this, "המשתמש לא נמצא במסד הנתונים", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(this, "שגיאה בגישה למסד הנתונים", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }

                    });
        });

        goToRegisterBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}