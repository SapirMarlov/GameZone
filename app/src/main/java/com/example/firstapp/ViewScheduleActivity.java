package com.example.firstapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewScheduleActivity extends AppCompatActivity {

    private TextView greetingTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_schedule);

        greetingTextView = findViewById(R.id.greetingTextView);

        // אתחול Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // טעינת שם המשתמש
        loadEmployeeName();
    }

    private void loadEmployeeName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("users").document(uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String username = document.getString("username");

                                if (username != null && !username.isEmpty()) {
                                    greetingTextView.setText("היי " + username + ", אלו הם המשימות שלך לשבוע הקרוב");
                                } else {
                                    String displayName = document.getString("displayName");

                                    if (displayName != null && !displayName.isEmpty()) {
                                        greetingTextView.setText("היי " + displayName + ", אלו הם המשימות שלך לשבוע הקרוב");
                                    } else {
                                        String email = document.getString("email");
                                        if (email != null && email.contains("@")) {
                                            String nameFromEmail = email.substring(0, email.indexOf("@"));
                                            greetingTextView.setText("היי " + nameFromEmail + ", אלו הם המשימות שלך לשבוע הקרוב");
                                        } else {
                                            greetingTextView.setText("היי, לא זוהה שם משתמש");
                                        }
                                    }
                                }
                            } else {
                                greetingTextView.setText("היי, לא נמצא פרופיל משתמש");
                            }
                        } else {
                            Log.e("ViewSchedule", "שגיאה בטעינת המשתמש: " + task.getException());
                            greetingTextView.setText("שגיאה בטעינת נתוני המשתמש");
                        }
                    });
        } else {
            greetingTextView.setText("היי, המשתמש אינו מחובר");
        }
    }
}
