package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView emptyCartMessage;
    private Button continueToPayment;
    private CartAdapter adapter;
    private List<DocumentSnapshot> cartItems = new ArrayList<>();
    private FirebaseFirestore db;
    private String userId;
    private double totalPrice = 0;

    private ListenerRegistration cartListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_checkout);

        recyclerView = findViewById(R.id.recycler_cart_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderSummaryText = findViewById(R.id.order_summary);
        emptyCartMessage = findViewById(R.id.empty_cart_message);
        continueToPayment = findViewById(R.id.continue_to_payment);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "לא נמצא משתמש מחובר", Toast.LENGTH_SHORT).show();
            Log.d("CartActivity", "User is null – staying on page");
            // אל תסגרי – תישארי על המסך כדי לא לבעוט את המשתמש בטעות
            return;
        }

        userId = user.getUid();
        db = FirebaseFirestore.getInstance();

        // השבתת כפתור תשלום עד שתיטען העגלה
        continueToPayment.setEnabled(false);
        continueToPayment.setAlpha(0.5f);

        loadCartItems();

        continueToPayment.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            intent.putExtra("totalPrice", totalPrice);
            startActivity(intent);
        });
    }

    private void loadCartItems() {
        cartListener = db.collection("carts")
                .document(userId)
                .collection("items")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        orderSummaryText.setText("שגיאה בטעינת העגלה");
                        emptyCartMessage.setVisibility(View.GONE);
                        continueToPayment.setEnabled(false);
                        continueToPayment.setAlpha(0.5f);
                        return;
                    }

                    cartItems.clear();
                    if (querySnapshot != null) {
                        cartItems.addAll(querySnapshot.getDocuments());
                    }

                    int itemCount = cartItems.size();

                    // עדכון מונה במסמך orders
                    db.collection("orders")
                            .document(userId)
                            .update("cartCount", itemCount)
                            .addOnFailureListener(e -> {
                                db.collection("orders")
                                        .document(userId)
                                        .set(new CartCountHelper(itemCount));
                            });

                    if (cartItems.isEmpty()) {
                        recyclerView.setAdapter(null);
                        orderSummaryText.setText("");
                        emptyCartMessage.setVisibility(View.VISIBLE);
                        continueToPayment.setEnabled(false);
                        continueToPayment.setAlpha(0.5f);
                    } else {
                        emptyCartMessage.setVisibility(View.GONE);
                        adapter = new CartAdapter(this, cartItems, total -> {
                            totalPrice = total;
                            orderSummaryText.setText("סכום כולל: ₪" + total);
                        });
                        recyclerView.setAdapter(adapter);
                        continueToPayment.setEnabled(true);
                        continueToPayment.setAlpha(1.0f);
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
