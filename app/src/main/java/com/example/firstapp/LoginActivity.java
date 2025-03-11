package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);  // קישור ל-XML של מסך ההתחברות

        // הגדרת שדות הקלט
        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);

        // כפתור התחברות
        Button loginButton = findViewById(R.id.login_button);

        // מאזין ללחיצה על כפתור ההתחברות
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // קבלת שם המשתמש והסיסמה
                String username = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // בדיקת סוג המשתמש
                String userType = getUserType(username, password);
                Intent intent;

                if (userType.equals("admin")) {
                    intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                } else if (userType.equals("employee")) {
                    intent = new Intent(LoginActivity.this, EmployeeHomeActivity.class);
                } else if (userType.equals("customer")) {
                    intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                } else {
                    // במקרה של נתונים לא תקינים – ניתן להציג הודעת שגיאה
                    return;
                }
                startActivity(intent);
            }
        });

        // מעבר למסך הרשמה
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // מעבר למסך ההרשמה
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // פונקציה לקביעת סוג המשתמש בהתאם לקרדנציות
    private String getUserType(String username, String password) {
        if (username.equals("admin") && password.equals("admin123")) {
            return "admin";
        } else if (username.equals("employee") && password.equals("employee123")) {
            return "employee";
        } else if (username.equals("customer") && password.equals("customer123")) {
            return "customer";
        }
        return "invalid";
    }
}
