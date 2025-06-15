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
import com.google.firebase.firestore.DocumentSnapshot;

public class EmployeeHomeActivity extends AppCompatActivity {

    private TextView empNameTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_home);

        // אתחול Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // אתחול התצוגה
        empNameTextView = findViewById(R.id.employee_name);

        // טעינת שם העובד מ-Firestore
        loadEmployeeName();

        // כפתור הצגת לוח זמנים
        Button viewScheduleButton = findViewById(R.id.view_schedule);
        viewScheduleButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeHomeActivity.this, ViewScheduleActivity.class);
            startActivity(intent);
        });

        // כפתור הגשת משמרות לשבוע
        Button submitShiftsButton = findViewById(R.id.submit_shifts);
        submitShiftsButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeHomeActivity.this, SubmitShiftsActivity.class);
            startActivity(intent);
        });

        // כפתור התנתקות - מתוקן!
        Button bLogOut = findViewById(R.id.log_out);
        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ניתוק משתמש מה-Firebase
                FirebaseAuth.getInstance().signOut();

                // הודעה למשתמש
                Toast.makeText(EmployeeHomeActivity.this, "התנתקת בהצלחה", Toast.LENGTH_SHORT).show();

                // מעבר למסך הראשי וניקוי המחסנית
                Intent intent = new Intent(EmployeeHomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // סגירת המסך הנוכחי
            }
        });
    }

    // פונקציה לטעינת שם העובד מ-Firestore
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
                                    empNameTextView.setText("שלום " + username + "!");
                                } else {
                                    // אם אין username, נסה displayName
                                    String displayName = document.getString("displayName");

                                    if (displayName != null && !displayName.isEmpty()) {
                                        empNameTextView.setText("שלום " + displayName + "!");
                                    } else {
                                        // כאפשרות אחרונה, השתמש באימייל
                                        String email = document.getString("email");
                                        if (email != null && email.contains("@")) {
                                            String nameFromEmail = email.substring(0, email.indexOf("@"));
                                            empNameTextView.setText("שלום " + nameFromEmail + "!");
                                        } else {
                                            empNameTextView.setText("שלום עובד!");
                                        }
                                    }
                                }
                            } else {
                                // אם המסמך לא קיים
                                empNameTextView.setText("שלום עובד!");
                            }
                        } else {
                            // אם יש שגיאה בטעינה
                            android.util.Log.e("EmployeeHome", "Error loading user data: " + task.getException());
                            empNameTextView.setText("שלום עובד!");
                        }
                    });
        } else {
            // אם אין משתמש מחובר
            empNameTextView.setText("שלום אורח!");
        }
    }
}