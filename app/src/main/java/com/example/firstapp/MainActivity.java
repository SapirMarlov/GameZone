package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView cartCounterTextView;
    private EditText searchBar;
    private ListenerRegistration cartListener;

    private List<DocumentSnapshot> allProducts = new ArrayList<>();
    private HomeProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginButton = findViewById(R.id.login_button);
        cartCounterTextView = findViewById(R.id.cart_counter);
        searchBar = findViewById(R.id.search_bar);

        RecyclerView recyclerView = findViewById(R.id.recycler_home_products);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new HomeProductAdapter(this, allProducts);
        recyclerView.setAdapter(adapter);

        setupListeners();
        loadProducts();
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                checkUserRoleAndNavigate();
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        findViewById(R.id.user_profile_button).setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                checkUserRoleAndNavigate();
            } else {
                Toast.makeText(this, "עליך להתחבר כדי לגשת לאזור האישי", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        findViewById(R.id.cart_button).setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, CartActivity.class));
            } catch (Exception e) {
                Toast.makeText(this, "עגלת קניות - בפיתוח", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.categories_button).setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, CategoriesActivity.class));
            } catch (Exception e) {
                Toast.makeText(this, "קטגוריות - בפיתוח", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.promotions_button).setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, PromotionsActivity.class));
            } catch (Exception e) {
                Toast.makeText(this, "מבצעים - בפיתוח", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.search_players_button).setOnClickListener(v -> {
            try {
                startActivity(new Intent(this, GamePartnerActivity.class));
            } catch (Exception e) {
                Toast.makeText(this, "חיפוש שחקנים - בפיתוח", Toast.LENGTH_SHORT).show();
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
        });
    }

    private void loadProducts() {
        db.collection("product")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allProducts.clear();
                    allProducts.addAll(queryDocumentSnapshots.getDocuments());
                    adapter.updateData(allProducts);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, "שגיאה בטעינת מוצרים", Toast.LENGTH_SHORT).show());
    }

    private void filterProducts(String keyword) {
        keyword = keyword.toLowerCase(Locale.ROOT);
        List<DocumentSnapshot> filtered = new ArrayList<>();
        for (DocumentSnapshot doc : allProducts) {
            String name = doc.getString("name");
            if (name != null && name.toLowerCase(Locale.ROOT).contains(keyword)) {
                filtered.add(doc);
            }
        }
        adapter.updateData(filtered);
    }


    private boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    private void checkUserRoleAndNavigate() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        String userId = currentUser.getUid();
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        String role = document.getString("role");
                        if (role == null || role.isEmpty()) {
                            createUserDocument(userId);
                            role = "customer";
                        }
                        navigateByRole(normalizeRole(role));
                    } else {
                        Toast.makeText(this, "שגיאה בבדיקת הרשאות", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String normalizeRole(String role) {
        if (role == null) return "customer";
        switch (role.toLowerCase(Locale.ROOT)) {
            case "admin":
            case "מנהל":
                return "admin";
            case "employee":
            case "עובד":
                return "employee";
            case "customer":
            case "לקוח":
            default:
                return "customer";
        }
    }

    private void navigateByRole(String role) {
        Intent intent;
        switch (role) {
            case "admin":
                intent = new Intent(this, AdminDashboardActivity.class);
                Toast.makeText(this, "ברוך הבא לאזור המנהל", Toast.LENGTH_SHORT).show();
                break;
            case "employee":
                intent = new Intent(this, EmployeeHomeActivity.class);
                Toast.makeText(this, "ברוך הבא לאזור העובד", Toast.LENGTH_SHORT).show();
                break;
            case "customer":
            default:
                intent = new Intent(this, CustomerHomeActivity.class);
                Toast.makeText(this, "ברוך הבא לאזור האישי", Toast.LENGTH_SHORT).show();
                break;
        }
        startActivity(intent);
    }

    private void createUserDocument(String userId) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        Map<String, Object> user = new HashMap<>();
        user.put("email", currentUser.getEmail());
        user.put("createdAt", Timestamp.now());

        // 👇 שמירה רק אם אין כבר username
        db.collection("users").document(userId)
                .set(user, SetOptions.merge()) // ✅ לא מוחק שדות קיימים
                .addOnFailureListener(e ->
                        Toast.makeText(this, "שגיאה ביצירת פרופיל משתמש", Toast.LENGTH_SHORT).show()
                );
    }

    private void updateLoginButtonState() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            loginButton.setText("התחבר");
            return;
        }

        db.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(document -> {
                    String name = document.getString("username");
                    if (name != null && !name.isEmpty()) {
                        loginButton.setText("שלום " + name);
                    } else {
                        loginButton.setText("שלום משתמש");
                    }
                })
                .addOnFailureListener(e -> loginButton.setText("שלום משתמש"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateLoginButtonState();     // תמיד טוען שם המשתמש
        setupCartCounterListener();   // מאזין לעגלה
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLoginButtonState();     // גם כשחוזרים ממסכים אחרים
        setupCartCounterListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cartListener != null) {
            cartListener.remove();
        }
    }
    private void setupCartCounterListener() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            cartCounterTextView.setVisibility(View.GONE);
            return;
        }

        // מאזין לשדה cartCount במסמך orders/userId
        cartListener = db.collection("orders")
                .document(user.getUid())
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null || snapshot == null || !snapshot.exists()) {
                        cartCounterTextView.setVisibility(View.GONE);
                        return;
                    }

                    Long count = snapshot.getLong("cartCount");
                    if (count != null && count > 0) {
                        cartCounterTextView.setText(String.valueOf(count));
                        cartCounterTextView.setVisibility(View.VISIBLE);
                    } else {
                        cartCounterTextView.setVisibility(View.GONE);
                    }
                });
    }

}
