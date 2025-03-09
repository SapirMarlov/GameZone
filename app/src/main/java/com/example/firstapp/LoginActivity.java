package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);  // שם ה-XML של מסך ההתחברות

        // חלק 1 - הגדרת ה-onClick
        Button registerButton = findViewById(R.id.register_button);

        // הגדרת מאזין ללחיצה על כפתור ההרשמה
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // יצירת Intent לעבור למסך ההרשמה
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);  // התחלת האקטיביטי של ההרשמה
            }
        });
    }
}
