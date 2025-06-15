package com.example.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateItemDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productPrice, currentQuantity;
    private EditText editQuantity, editReason;
    private RadioGroup actionRadioGroup;
    private Button buttonConfirm, buttonIncrease, buttonDecrease;

    private FirebaseFirestore db;
    private String productId;
    private int stock = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updete_item_details);

        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        currentQuantity = findViewById(R.id.current_quantity);
        editQuantity = findViewById(R.id.edit_quantity);
        editReason = findViewById(R.id.edit_reason);
        actionRadioGroup = findViewById(R.id.action_radio_group);
        buttonConfirm = findViewById(R.id.button_confirm);
        buttonIncrease = findViewById(R.id.button_increase);
        buttonDecrease = findViewById(R.id.button_decrease);

        db = FirebaseFirestore.getInstance();

        // מקבל נתונים מה־Intent
        productId = getIntent().getStringExtra("productId");
        String name = getIntent().getStringExtra("name");
        Double price = getIntent().getDoubleExtra("price", 0.0);
        Long currentStock = getIntent().getLongExtra("stock", 0);
        String imageUrl = getIntent().getStringExtra("imageUrl");

        stock = currentStock != null ? currentStock.intValue() : 0;

        // מציג מידע על המוצר
        productName.setText(name != null ? name : "לא ידוע");
        productPrice.setText("₪" + price);
        currentQuantity.setText("במלאי: " + stock + " יחידות");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.notfound) // מוצג לפני שהטעינה הסתיימה
                    .error(R.drawable.notfound)       // מוצג אם יש שגיאה
                    .into(productImage);
        } else {
            productImage.setImageResource(R.drawable.notfound);
        }


        // לחצן פלוס
        buttonIncrease.setOnClickListener(v -> {
            int quantity = getQuantity();
            if (quantity < 999) {
                editQuantity.setText(String.valueOf(quantity + 1));
            }
        });

        // לחצן מינוס
        buttonDecrease.setOnClickListener(v -> {
            int quantity = getQuantity();
            if (quantity > 1) {
                editQuantity.setText(String.valueOf(quantity - 1));
            }
        });

        // אישור העדכון
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
                        Toast.makeText(this, "המלאי עודכן בהצלחה!", Toast.LENGTH_SHORT).show();
                        finish();
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
}
