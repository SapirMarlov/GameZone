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
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

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

        holder.addToCartButton.setOnClickListener(v ->
                Toast.makeText(context, name + " נוסף לעגלה!", Toast.LENGTH_SHORT).show()
        );

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
