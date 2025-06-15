package com.example.firstapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<DocumentSnapshot> productList;

    public ProductAdapter(Context context, List<DocumentSnapshot> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        DocumentSnapshot product = productList.get(position);

        String name = product.getString("name");
        Double priceValue = product.getDouble("price");
        Long stockValue = product.getLong("stock");
        String imageUrl = product.getString("imageUrl");

        String price = (priceValue != null ? priceValue + " ₪" : "לא זמין");
        String stockText = (stockValue != null && stockValue > 0) ? "במלאי" : "אין במלאי";

        holder.textViewName.setText(name != null ? name : "לא ידוע");
        holder.textViewPrice.setText(price);
        holder.textViewStock.setText(stockText);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context).load(imageUrl).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.notfound); // ודא שיש לך placeholder אם אין תמונה
        }

        holder.buttonUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateItemDetailsActivity.class);
            intent.putExtra("productId", product.getId());
            intent.putExtra("name", name);
            intent.putExtra("price", priceValue);
            intent.putExtra("stock", stockValue);
            intent.putExtra("imageUrl", imageUrl);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName, textViewPrice, textViewStock;
        Button buttonUpdate;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            textViewName = itemView.findViewById(R.id.product_name);
            textViewPrice = itemView.findViewById(R.id.product_price);
            textViewStock = itemView.findViewById(R.id.product_stock_status);
            buttonUpdate = itemView.findViewById(R.id.update_stock_button);
        }
    }
}
