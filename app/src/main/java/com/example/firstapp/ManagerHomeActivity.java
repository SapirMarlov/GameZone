package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ManagerHomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button manageOrdersBtn, manageInventoryBtn, customerRequestsBtn,
            viewReportsBtn, manageEmployeesBtn, employeeHomeBtn,
            employeeAddToSystemBtn, logOutBtn;
    private UserPermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        mAuth = FirebaseAuth.getInstance();
        permissionManager = UserPermissionManager.getInstance();

        initViews();
        setupClickListeners();
        checkPermissions();
    }

    private void initViews() {
        manageOrdersBtn = findViewById(R.id.manage_orders);
        manageInventoryBtn = findViewById(R.id.manage_inventory);
        customerRequestsBtn = findViewById(R.id.customer_requests);
        viewReportsBtn = findViewById(R.id.view_reports);
        manageEmployeesBtn = findViewById(R.id.manage_employees);
        employeeHomeBtn = findViewById(R.id.employee_home);
        employeeAddToSystemBtn = findViewById(R.id.employee_add_tp_system);
        logOutBtn = findViewById(R.id.log_out);
    }

    private void setupClickListeners() {
        manageOrdersBtn.setOnClickListener(v -> {
            checkPermissionAndNavigate("canViewAllOrders", ManageOrdersActivity.class, "ניהול הזמנות");
        });

        manageInventoryBtn.setOnClickListener(v -> {
            checkPermissionAndNavigate("canManageProducts", InventoryManagementActivity.class, "ניהול מלאי");
        });

        customerRequestsBtn.setOnClickListener(v -> {
            checkPermissionAndNavigate("canApproveRequests", CustomerRequestsActivity.class, "פניות לקוחות");
        });

        viewReportsBtn.setOnClickListener(v -> {
            checkPermissionAndNavigate("canViewReports", BusinessReportsActivity.class, "דוחות עסקיים");
        });

        manageEmployeesBtn.setOnClickListener(v -> {
            checkPermissionAndNavigate("canCreateUsers", ManageEmployeesActivity.class, "ניהול עובדים");
        });

        employeeHomeBtn.setOnClickListener(v -> {
            // מעבר למסך עובדים - לא צריך הרשאה מיוחדת
            Intent intent = new Intent(this, EmployeeHomeActivity.class);
            intent.putExtra("userRole", "מנהל");
            startActivity(intent);
        });

        employeeAddToSystemBtn.setOnClickListener(v -> {
            checkPermissionAndNavigate("canCreateUsers", RegisterAdminActivityToSystemActivity.class, "הוספת עובד חדש");
        });

        logOutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ManagerHomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            Toast.makeText(this, "התנתקת בהצלחה", Toast.LENGTH_SHORT).show();
        });
    }

    private void checkPermissions() {
        // בדיקת הרשאות ועדכון ממשק בהתאם
        permissionManager.isAdmin(new UserPermissionManager.PermissionCallback() {
            @Override
            public void onResult(boolean isAdmin) {
                if (!isAdmin) {
                    // אם לא מנהל - הסתר כפתורים רגישים
                    manageEmployeesBtn.setVisibility(View.GONE);
                    employeeAddToSystemBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ManagerHomeActivity.this, "שגיאה בבדיקת הרשאות: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        // בדיקת הרשאות ספציפיות
        checkButtonPermission("canManageProducts", manageInventoryBtn);
        checkButtonPermission("canViewReports", viewReportsBtn);
        checkButtonPermission("canApproveRequests", customerRequestsBtn);
        checkButtonPermission("canViewAllOrders", manageOrdersBtn);
    }

    private void checkButtonPermission(String permission, Button button) {
        permissionManager.checkUserPermission(permission, new UserPermissionManager.PermissionCallback() {
            @Override
            public void onResult(boolean hasPermission) {
                button.setVisibility(hasPermission ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(String error) {
                // במקרה של שגיאה - הסתר כפתור
                button.setVisibility(View.GONE);
            }
        });
    }

    private void checkPermissionAndNavigate(String permission, Class<?> targetActivity, String actionName) {
        permissionManager.checkUserPermission(permission, new UserPermissionManager.PermissionCallback() {
            @Override
            public void onResult(boolean hasPermission) {
                if (hasPermission) {
                    Intent intent = new Intent(ManagerHomeActivity.this, targetActivity);
                    intent.putExtra("userRole", "מנהל");
                    startActivity(intent);
                } else {
                    Toast.makeText(ManagerHomeActivity.this, "אין לך הרשאה ל" + actionName, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ManagerHomeActivity.this, "שגיאה בבדיקת הרשאות: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // בדיקה שהמשתמש עדיין מחובר
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}