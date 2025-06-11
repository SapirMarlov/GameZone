package com.example.firstapp;

import static com.example.firstapp.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterAdminActivityToSystemActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText usernameInput, emailInput, passwordInput;
    private Spinner roleSpinner; // Spinner לבחירת תפקיד
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_admin_to_system);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // קישור לרכיבי הממשק
        usernameInput = findViewById(R.id.reg_username);
        emailInput = findViewById(R.id.reg_email);
        passwordInput = findViewById(R.id.reg_password);
        roleSpinner = findViewById(R.id.reg_userRole); // קישור ל-Spinner
        registerBtn = findViewById(R.id.register_submit);

        // הגדרת Spinner
        setupRoleSpinner();

        registerBtn.setOnClickListener(v -> registerStaff());
    }

    private void setupRoleSpinner() {
        String[] roles = {"עובד", "מנהל"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
    }

    private void registerStaff() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // בדיקה שיש משהו נבחר ב-Spinner
        String role = roleSpinner.getSelectedItem() != null ?
                roleSpinner.getSelectedItem().toString() : "";

        // בדיקת תקינות הנתונים
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "הסיסמה חייבת להכיל לפחות 6 תווים", Toast.LENGTH_SHORT).show();
            return;
        }

        // משתנה final עבור השימוש ב-lambda
        final String selectedRole = role;

        // יצירת המשתמש ב-Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // שמירת פרטי המשתמש ב-Firestore
                        String userId = mAuth.getCurrentUser().getUid();
                        saveStaffToFirestore(userId, username, email, selectedRole);
                    } else {
                        Toast.makeText(this, "שגיאה ברישום: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveStaffToFirestore(String userId, String username, String email, String role) {
        Map<String, Object> staff = new HashMap<>();
        staff.put("username", username);
        staff.put("email", email);
        staff.put("role", role);
        staff.put("permissions", getPermissionsByRole(role));
        staff.put("createdAt", System.currentTimeMillis());

        db.collection("users").document(userId)
                .set(staff)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "נוצר " + role + " חדש בהצלחה!", Toast.LENGTH_SHORT).show();
                    finish(); // חזרה למסך המנהל
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "שגיאה בשמירת הנתונים: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    private Map<String, Boolean> getPermissionsByRole(String role) {
        Map<String, Boolean> permissions = new HashMap<>();

        if (role.equals("מנהל")) {
            permissions.put("canCreateUsers", true);
            permissions.put("canDeleteUsers", true);
            permissions.put("canEditUsers", true);
            permissions.put("canViewReports", true);
            permissions.put("canManageSystem", true);
            permissions.put("canApproveRequests", true);
            permissions.put("canViewAllOrders", true);
            permissions.put("canManageProducts", true);
            permissions.put("canViewCustomers", true);
        } else if (role.equals("עובד")) {
            permissions.put("canCreateUsers", false);
            permissions.put("canDeleteUsers", false);
            permissions.put("canEditUsers", false);
            permissions.put("canViewReports", true);
            permissions.put("canManageSystem", false);
            permissions.put("canApproveRequests", true);
            permissions.put("canViewAllOrders", true);
            permissions.put("canManageProducts", false);
            permissions.put("canViewCustomers", true);
        }

        return permissions;
    }
}