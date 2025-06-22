package com.example.firstapp;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class UpdateItemDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productPrice, currentQuantity, serialNumber;
    private EditText editQuantity, editReason;
    private RadioGroup actionRadioGroup;
    private Button buttonConfirm, buttonIncrease, buttonDecrease;

    private FirebaseFirestore db;
    private String productId;
    private int stock = 0;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updete_item_details);

        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        currentQuantity = findViewById(R.id.current_quantity);
        serialNumber = findViewById(R.id.serial_number); // ✅ הוספנו
        editQuantity = findViewById(R.id.edit_quantity);
        editReason = findViewById(R.id.edit_reason);
        actionRadioGroup = findViewById(R.id.action_radio_group);
        buttonConfirm = findViewById(R.id.button_confirm);
        buttonIncrease = findViewById(R.id.button_increase);
        buttonDecrease = findViewById(R.id.button_decrease);

        db = FirebaseFirestore.getInstance();

        // קבלת נתונים מה־Intent
        productId = getIntent().getStringExtra("productId");
        name = getIntent().getStringExtra("name");
        double price = getIntent().getDoubleExtra("price", 0.0);
        Long currentStock = getIntent().getLongExtra("stock", 0);
        String imageUrl = getIntent().getStringExtra("imageUrl");

        stock = currentStock != null ? currentStock.intValue() : 0;

        productName.setText(name != null ? name : "לא ידוע");
        productPrice.setText("₪" + price);
        currentQuantity.setText("במלאי: " + stock + " יחידות");
        serialNumber.setText("מקט: " + productId); // ✅ מציג את ה-ID כמספר סידורי

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.notfound)
                    .error(R.drawable.notfound)
                    .into(productImage);
        } else {
            productImage.setImageResource(R.drawable.notfound);
        }

        buttonIncrease.setOnClickListener(v -> {
            int quantity = getQuantity();
            if (quantity < 999) editQuantity.setText(String.valueOf(quantity + 1));
        });

        buttonDecrease.setOnClickListener(v -> {
            int quantity = getQuantity();
            if (quantity > 1) editQuantity.setText(String.valueOf(quantity - 1));
        });

        buttonConfirm.setOnClickListener(v -> {
            int quantityToChange = getQuantity();
            if (quantityToChange <= 0) {
                Toast.makeText(this, "הכמות צריכה להיות מספר חיובי", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean addStock = (actionRadioGroup.getCheckedRadioButtonId() == R.id.radio_add);
            int newStock = addStock ? stock + quantityToChange : stock - quantityToChange;

            if (newStock < 0) {
                Toast.makeText(this, "לא ניתן להסיר יותר ממה שקיים במלאי", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("product").document(productId)
                    .update("stock", newStock)
                    .addOnSuccessListener(unused -> {
                        String action = addStock ? "הוספה" : "הסרה";
                        String reason = editReason.getText().toString().trim();

                        getUpdatedByFromUsers(updatedBy -> {
                            Map<String, Object> log = new HashMap<>();
                            log.put("productId", productId);
                            log.put("productName", name != null ? name : "לא ידוע");
                            log.put("action", action);
                            log.put("quantityBefore", stock);
                            log.put("quantityAfter", newStock);
                            log.put("updatedBy", updatedBy);
                            log.put("timestamp", FieldValue.serverTimestamp());
                            if (!reason.isEmpty()) {
                                log.put("reason", reason);
                            }

                            db.collection("stock_logs").add(log);
                            Toast.makeText(this, "המלאי עודכן בהצלחה!", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "אירעה שגיאה בעדכון", Toast.LENGTH_SHORT).show()
                    );
        });
    }

    private int getQuantity() {
        String quantityStr = editQuantity.getText().toString().trim();
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void getUpdatedByFromUsers(UserCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(doc -> {
                        String username = doc.getString("username");
                        if (username != null && !username.isEmpty()) {
                            callback.onUserResolved(username);
                        } else {
                            callback.onUserResolved("לא ידוע");
                        }
                    })
                    .addOnFailureListener(e -> callback.onUserResolved("לא ידוע"));
        } else {
            callback.onUserResolved("לא ידוע");
        }
    }

    private interface UserCallback {
        void onUserResolved(String name);
    }
}
