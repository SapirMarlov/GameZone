package com.example.firstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class SubmitShiftsActivity extends AppCompatActivity {

    private EditText nameEditText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_shifts);

        // אתחול Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // אתחול התצוגה
        nameEditText = findViewById(R.id.name_edit_text);
        Button send_shifts = findViewById(R.id.submit_button);

        // טעינת שם העובד/מנהל
        loadEmployeeName();

        // כפתור שליחה
        send_shifts.setOnClickListener(v ->
                Toast.makeText(SubmitShiftsActivity.this, "נשלח בהצלחה!", Toast.LENGTH_SHORT).show()
        );
    }

    // פונקציה לטעינת שם העובד/מנהל מ-Firestore
    private void loadEmployeeName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // קריאת נתוני המשתמש מ-Firestore
            db.collection("users").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // ניסיון לקחת את השדה username
                                String username = document.getString("username");

                                if (username != null && !username.isEmpty()) {
                                    // אם יש username, השתמש בו
                                    nameEditText.setText(username);
                                } else {
                                    // אם אין username, נסה displayName
                                    String displayName = document.getString("displayName");

                                    if (displayName != null && !displayName.isEmpty()) {
                                        nameEditText.setText(displayName);
                                    } else {
                                        // כאפשרות אחרונה, השתמש באימייל
                                        String email = document.getString("email");
                                        if (email != null && email.contains("@")) {
                                            String nameFromEmail = email.substring(0, email.indexOf("@"));
                                            nameEditText.setText(nameFromEmail);
                                        } else {
                                            nameEditText.setText("עובד לא מזוהה");
                                        }
                                    }
                                }
                            } else {
                                // אם המסמך לא קיים
                                nameEditText.setText("עובד לא מזוהה");
                            }
                        } else {
                            // אם יש שגיאה בטעינה
                            android.util.Log.e("SubmitShifts", "Error loading user data: " + task.getException());
                            nameEditText.setText("שגיאה בטעינה");
                        }
                    });
        } else {
            // אם אין משתמש מחובר
            nameEditText.setText("לא מחובר");
        }
    }
}