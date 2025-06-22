package com.example.firstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomerSupportActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, phoneInput, productInput, messageInput;
    private Button sendButton;

    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_support);

        nameInput = findViewById(R.id.customer_name);
        emailInput = findViewById(R.id.customer_email);
        phoneInput = findViewById(R.id.customer_phone);
        productInput = findViewById(R.id.customer_product); // 🆕 שדה מוצר
        messageInput = findViewById(R.id.customer_message);
        sendButton = findViewById(R.id.send_message_button);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "יש להתחבר כדי לשלוח פנייה", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        emailInput.setText(user.getEmail());

        sendButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String product = productInput.getText().toString().trim(); // 🆕 שדה מוצר
            String message = messageInput.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || product.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "אנא מלאי את כל השדות", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> inquiry = new HashMap<>();
            inquiry.put("name", name);
            inquiry.put("email", email);
            inquiry.put("phone", phone);
            inquiry.put("product", product); // 🆕 הוספת מוצר לפנייה
            inquiry.put("message", message);
            inquiry.put("status", "new");
            inquiry.put("timestamp", FieldValue.serverTimestamp());
            inquiry.put("userId", user.getUid());

            db.collection("inquiries")
                    .add(inquiry)
                    .addOnSuccessListener(docRef -> {
                        Toast.makeText(this, "הפנייה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                        nameInput.setText("");
                        phoneInput.setText("");
                        productInput.setText(""); // 🆕 ניקוי שדה מוצר
                        messageInput.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "שגיאה בשליחת הפנייה", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
