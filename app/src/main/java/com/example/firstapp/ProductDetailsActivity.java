package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        ImageView productImage = findViewById(R.id.product_image);
        TextView productName = findViewById(R.id.product_name);
        TextView productDescription = findViewById(R.id.product_description);
        TextView productPrice = findViewById(R.id.product_price);
        Button addToCartButton = findViewById(R.id.add_to_cart);
        Button goToCartButton = findViewById(R.id.go_to_cart);

        // קבלת נתונים מה-Intent
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        double price = getIntent().getDoubleExtra("price", 0.0);
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String productId = getIntent().getStringExtra("productId");

        // הצגת נתונים במסך
        productName.setText(name != null ? name : "שם לא ידוע");
        productDescription.setText(description != null ? description : "אין תיאור");
        productPrice.setText("₪" + price);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(productImage);
        } else {
            productImage.setImageResource(R.drawable.notfound);
        }

        // לחצן הוספה לעגלה
        addToCartButton.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();

                Map<String, Object> cartItem = new HashMap<>();
                cartItem.put("productId", productId);
                cartItem.put("name", name);
                cartItem.put("description", description);
                cartItem.put("price", price);
                cartItem.put("imageUrl", imageUrl);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("carts")
                        .document(userId)
                        .collection("items")
                        .document(productId)
                        .set(cartItem)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "המוצר נוסף לעגלה!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "שגיאה בהוספת המוצר לעגלה", Toast.LENGTH_SHORT).show();
                        });

            } else {
                Toast.makeText(this, "יש להתחבר כדי להוסיף לעגלה", Toast.LENGTH_SHORT).show();
            }
        });

        // לחצן מעבר לעגלה
        goToCartButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }
}
