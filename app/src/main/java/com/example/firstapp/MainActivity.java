package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // activity_main.xml

        // אתחול Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Button add_ps5_to_cart = findViewById(R.id.add_ps5_to_cart);
        Button add_nintendo_to_cart = findViewById(R.id.add_nintendo_to_cart);
        Button add_asus_to_cart = findViewById(R.id.add_asus_to_cart);
        Button add_razer_to_cart = findViewById(R.id.add_razer_to_cart);

        // כפתור התחברות
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn()) {
                    // בדיקה אם המשתמש הוא מנהל ומעבר לאזור המתאים
                    checkUserRoleAndNavigate();
                } else {
                    // אם לא מחובר, נעבור למסך התחברות
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        // כפתור אזור אישי - רק אם מחובר
        Button userProfileButton = findViewById(R.id.user_profile_button);
        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn()) {
                    // בדיקה אם המשתמש הוא מנהל ומעבר לאזור האישי המתאים
                    checkUserRoleAndNavigate();
                } else {
                    Toast.makeText(MainActivity.this, "עליך להתחבר כדי לגשת לאזור האישי", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        // כפתור עגלה
        Button cartbutton = findViewById(R.id.cart_button);
        cartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // בדוק אם CartActivity קיים
                try {
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "עגלת קניות - בפיתוח", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // כפתור קטגוריות
        Button categoriessbutton = findViewById(R.id.categories_button);
        categoriessbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "קטגוריות - בפיתוח", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // כפתור מבצעים
        Button promotionsbutton = findViewById(R.id.promotions_button);
        promotionsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this, PromotionsActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "מבצעים - בפיתוח", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // כפתור חיפוש שחקנים
        Button searchplayersbutton = findViewById(R.id.search_players_button);
        searchplayersbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this, GamePartnerActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "חיפוש שחקנים - בפיתוח", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // תמונת PS5 עם מעבר לדף פרטי מוצר
        ImageView ps5Image = findViewById(R.id.ps5_image);
        ps5Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "פרטי מוצר - בפיתוח", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // הוספה לעגלה - PS5
        add_ps5_to_cart.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "PS5 נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );

        // הוספה לעגלה - Nintendo
        add_nintendo_to_cart.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Nintendo נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );

        // הוספה לעגלה - ASUS
        add_asus_to_cart.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "ASUS נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );

        // הוספה לעגלה - Razer
        add_razer_to_cart.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Razer נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );
    }

    // בדיקה אם המשתמש מחובר באמצעות Firebase
    private boolean isUserLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    // בדיקה של תפקיד המשתמש ומעבר לאזור המתאים
    private void checkUserRoleAndNavigate() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // הוספת log כדי לראות מה קורה
            android.util.Log.d("UserRole", "Checking role for user: " + userId);

            // בדיקה ב-Firestore של תפקיד המשתמש
            db.collection("users").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String userRole = "customer"; // ברירת מחדל - לקוח

                            if (document.exists()) {
                                // הוספת log כדי לראות מה יש במסמך
                                android.util.Log.d("UserRole", "Document exists. Data: " + document.getData());

                                // אם יש מסמך, נבדוק אם יש תפקיד
                                String roleFromDB = document.getString("role");
                                android.util.Log.d("UserRole", "Role from DB: " + roleFromDB);

                                if (roleFromDB != null && !roleFromDB.isEmpty()) {
                                    userRole = roleFromDB;
                                    android.util.Log.d("UserRole", "Using role from DB: " + userRole);
                                } else {
                                    android.util.Log.d("UserRole", "No role found, using default: customer");
                                }
                            } else {
                                // אם המסמך לא קיים, ניצור אותו כלקוח
                                android.util.Log.d("UserRole", "Document doesn't exist, creating new user");
                                createUserDocument(userId);
                            }

                            // הוספת log לפני הניווט
                            android.util.Log.d("UserRole", "Final role decision: " + userRole);

                            // ניווט לפי תפקיד
                            navigateByRole(userRole);

                        } else {
                            // אם יש שגיאה בגישה ל-Firestore
                            android.util.Log.e("UserRole", "Error getting document: " + task.getException());
                            Toast.makeText(MainActivity.this, "שגיאה בבדיקת הרשאות", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // פונקציה נפרדת לניווט לפי תפקיד
    private void navigateByRole(String userRole) {
        android.util.Log.d("UserRole", "Navigating with role: " + userRole);

        // נרמול התפקיד - תמיכה בעברית ואנגלית
        String normalizedRole = normalizeRole(userRole);
        android.util.Log.d("UserRole", "Normalized role: " + normalizedRole);

        switch (normalizedRole) {
            case "admin":
                android.util.Log.d("UserRole", "Navigating to AdminDashboard");
                // מנהל - מעבר למסך AdminDashboard
                try {
                    Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "ברוך הבא לאזור המנהל", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    android.util.Log.e("UserRole", "Error starting AdminDashboard: " + e.getMessage());
                    Toast.makeText(MainActivity.this, "מסך מנהל - בפיתוח", Toast.LENGTH_SHORT).show();
                }
                break;

            case "employee":
                android.util.Log.d("UserRole", "Navigating to EmployeeDashboard");
                // עובד - מעבר למסך Employee
                try {
                    Intent intent = new Intent(MainActivity.this, EmployeeHomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "ברוך הבא לאזור העובד", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    android.util.Log.e("UserRole", "Error starting EmployeeDashboard: " + e.getMessage());
                    Toast.makeText(MainActivity.this, "מסך עובד - בפיתוח", Toast.LENGTH_SHORT).show();
                }
                break;

            case "customer":
            default:
                android.util.Log.d("UserRole", "Navigating to CustomerHome");
                // לקוח רגיל (ברירת מחדל לכל מי שאין לו תפקיד מוגדר)
                try {
                    Intent intent = new Intent(MainActivity.this, CustomerHomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "ברוך הבא לאזור האישי", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    android.util.Log.e("UserRole", "Error starting CustomerHome: " + e.getMessage());
                    Toast.makeText(MainActivity.this, "אזור אישי לקוח - בפיתוח", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // נרמול תפקיד - המרה מעברית לאנגלית
    private String normalizeRole(String role) {
        if (role == null || role.isEmpty()) {
            return "customer";
        }

        switch (role.toLowerCase().trim()) {
            case "מנהל":
            case "admin":
            case "administrator":
                return "admin";

            case "עובד":
            case "employee":
            case "worker":
                return "employee";

            case "לקוח":
            case "customer":
            case "client":
            default:
                return "customer";
        }
    }

    // יצירת מסמך משתמש חדש ב-Firestore (ללא תפקיד - יהיה לקוח בברירת מחדל)
    private void createUserDocument(String userId) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String displayName = currentUser.getDisplayName();

            // יצירת אובייקט משתמש - ללא שדה role כדי שיהיה לקוח בברירת מחדל
            java.util.Map<String, Object> user = new java.util.HashMap<>();
            user.put("email", email);
            user.put("username", displayName);
            user.put("createdAt", com.google.firebase.Timestamp.now());
            // בכוונה לא מוסיפים שדה "role" - כך הוא יהיה לקוח בברירת מחדל

            // שמירה ב-Firestore
            db.collection("users").document(userId)
                    .set(user)
                    .addOnSuccessListener(aVoid -> {
                        // המסמך נוצר בהצלחה ללא תפקיד (= לקוח)
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "שגיאה ביצירת פרופיל משתמש", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // עדכון מצב כפתור ההתחברות בהתאם למצב ההתחברות
    private void updateLoginButtonState() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");
                            if (username != null && !username.isEmpty()) {
                                loginButton.setText("שלום " + username);
                            } else {
                                loginButton.setText("שלום משתמש");
                            }
                        } else {
                            loginButton.setText("שלום משתמש");
                        }
                    })
                    .addOnFailureListener(e -> {
                        loginButton.setText("שלום משתמש");
                    });
        } else {
            loginButton.setText("התחבר");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // בדיקת מצב ההתחברות בכל פעם שהאפליקציה נפתחת
        updateLoginButtonState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // עדכון מצב כפתור ההתחברות בכל חזרה למסך
        updateLoginButtonState();
    }

}