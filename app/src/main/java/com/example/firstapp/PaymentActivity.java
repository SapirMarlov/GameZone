package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String userId;
    private double totalPrice;
    private String paymentMethod = "credit_card";

    private EditText idInput, cardInput, expiryInput, cvvInput;
    private LinearLayout creditCardFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_card_payment_screen);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getUid();
        totalPrice = getIntent().getDoubleExtra("totalPrice", 0);

        TextView amountText = findViewById(R.id.amount_text);
        amountText.setText("סכום לתשלום: ₪" + totalPrice);

        RadioGroup methodGroup = findViewById(R.id.payment_method_group);
        methodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_cash) {
                paymentMethod = "cash";
                creditCardFields.setVisibility(View.GONE);
            } else {
                paymentMethod = "credit_card";
                creditCardFields.setVisibility(View.VISIBLE);
            }
        });

        idInput = findViewById(R.id.user_id);
        cardInput = findViewById(R.id.card_number);
        expiryInput = findViewById(R.id.card_expiry);
        cvvInput = findViewById(R.id.card_cvv);
        creditCardFields = findViewById(R.id.credit_card_fields);

        Button confirmButton = findViewById(R.id.confirm_credit_payment);
        confirmButton.setOnClickListener(v -> confirmPayment());
    }

    private void confirmPayment() {
        db.collection("carts").document(userId).collection("items").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Map<String, Object>> itemsList = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        itemsList.add(doc.getData());
                    }

                    String orderId = db.collection("orders").document().getId();

                    Map<String, Object> order = new HashMap<>();
                    order.put("orderId", orderId);
                    order.put("userId", userId);
                    order.put("items", itemsList);
                    order.put("totalPrice", totalPrice);
                    order.put("paymentMethod", paymentMethod);
                    order.put("date", Timestamp.now());

                    db.collection("orders").document(orderId).set(order)
                            .addOnSuccessListener(docRef -> {
                                for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                    db.collection("carts").document(userId)
                                            .collection("items").document(doc.getId()).delete();
                                }
                                Toast.makeText(this, "תשלום הצליח!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(this, ThankYouActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "שגיאה: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                });
    }
}
