package com.example.firstapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.HomeProductViewHolder> {

    private Context context;
    private List<DocumentSnapshot> productList;


    public HomeProductAdapter(Context context, List<DocumentSnapshot> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public HomeProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_product, parent, false);
        return new HomeProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeProductViewHolder holder, int position) {
        DocumentSnapshot product = productList.get(position);

        String name = product.getString("name");
        String description = product.getString("description");
        Double price = product.getDouble("price");
        String imageUrl = product.getString("imageUrl");

        holder.nameText.setText(name != null ? name : "שם לא ידוע");
        holder.descriptionText.setText(description != null ? description : "אין תיאור");
        holder.priceText.setText(price != null ? "₪" + price : "לא זמין");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context).load(imageUrl).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.notfound);
        }

        holder.addToCartButton.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(context, "יש להתחבר כדי להוסיף לעגלה", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = user.getUid();
            String productId = product.getId();
            String productName = product.getString("name");
            Double productPrice = product.getDouble("price");
            String imageUrlVal = product.getString("imageUrl");

            // יצירת מידע המוצר לעגלה
            Map<String, Object> cartItem = new HashMap<>();
            cartItem.put("productId", productId);
            cartItem.put("name", productName);
            cartItem.put("price", productPrice);
            cartItem.put("imageUrl", imageUrlVal);
            cartItem.put("quantity", 1);

            FirebaseFirestore.getInstance()
                    .collection("carts")
                    .document(userId)
                    .collection("items")
                    .add(cartItem)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(context, productName + " נוסף לעגלה!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "שגיאה בהוספת המוצר לעגלה", Toast.LENGTH_SHORT).show();
                    });
        });


        // 💡 לחיצה על כרטיס המוצר כולו תעביר למסך פרטי מוצר
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            String productId = product.getId();

            intent.putExtra("productId", productId);
            intent.putExtra("name", name);
            intent.putExtra("description", description);
            intent.putExtra("price", price);
            intent.putExtra("imageUrl", imageUrl);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class HomeProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameText, descriptionText, priceText;
        Button addToCartButton;

        public HomeProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            nameText = itemView.findViewById(R.id.product_name);
            descriptionText = itemView.findViewById(R.id.product_description);
            priceText = itemView.findViewById(R.id.product_price);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}
