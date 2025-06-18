package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String userId;
    private double totalPrice;
    private String paymentMethod = "credit_card";

    private EditText idInput, cardInput, expiryInput, cvvInput;
    private LinearLayout creditCardFields; // עוטף את כל שדות האשראי

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

        // שדות טופס
        idInput = findViewById(R.id.user_id);
        cardInput = findViewById(R.id.card_number);
        expiryInput = findViewById(R.id.card_expiry);
        cvvInput = findViewById(R.id.card_cvv);
        creditCardFields = findViewById(R.id.credit_card_fields); // עוטף כל השדות

        Button confirmButton = findViewById(R.id.confirm_credit_payment);
        confirmButton.setOnClickListener(v -> confirmPayment());
    }

    private void confirmPayment() {
        Map<String, Object> order = new HashMap<>();
        order.put("userId", userId);
        order.put("totalPrice", totalPrice);
        order.put("timestamp", FieldValue.serverTimestamp());
        order.put("paymentMethod", paymentMethod);

        db.collection("orders").add(order).addOnSuccessListener(docRef -> {
            db.collection("carts").document(userId).collection("items")
                    .get().addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            db.collection("orders").document(docRef.getId())
                                    .collection("items").add(doc.getData());
                            doc.getReference().delete();
                        }

                        Toast.makeText(this, "תשלום הצליח!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, ThankYouActivity.class));
                        finish();
                    });
        }).addOnFailureListener(e ->
                Toast.makeText(this, "שגיאה: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }
}
