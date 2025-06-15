package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView orderSummaryText;
    private CartAdapter adapter;
    private List<DocumentSnapshot> cartItems = new ArrayList<>();
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_checkout);

        recyclerView = findViewById(R.id.recycler_cart_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderSummaryText = findViewById(R.id.order_summary);
        Button continueToPayment = findViewById(R.id.continue_to_payment);
        RadioGroup paymentMethodGroup = findViewById(R.id.payment_method_group);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
            return;
        }

        userId = user.getUid();
        db = FirebaseFirestore.getInstance();

        loadCartItems();

        continueToPayment.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            startActivity(intent);
        });
    }

    private ListenerRegistration cartListener; // מחוץ ל-method

    private void loadCartItems() {
        cartListener = db.collection("carts")
                .document(userId)
                .collection("items")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null || querySnapshot == null) {
                        orderSummaryText.setText("שגיאה בטעינת העגלה");
                        return;
                    }

                    cartItems.clear();
                    cartItems.addAll(querySnapshot.getDocuments());

                    if (cartItems.isEmpty()) {
                        orderSummaryText.setText("העגלה ריקה");
                    } else {
                        adapter = new CartAdapter(this, cartItems, total -> {
                            orderSummaryText.setText("סכום כולל: ₪" + total);
                        });
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cartListener != null) {
            cartListener.remove();
        }
    }


}
