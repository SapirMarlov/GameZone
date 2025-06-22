package com.example.firstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<DocumentSnapshot> cartItems;
    private OnTotalPriceCalculated listener;

    public interface OnTotalPriceCalculated {
        void onCalculated(double total);
    }

    public CartAdapter(Context context, List<DocumentSnapshot> cartItems, OnTotalPriceCalculated listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
        calculateTotal(); // נחשב את הסכום פעם אחת בהתחלה
    }

    private void calculateTotal() {
        double total = 0;
        for (DocumentSnapshot item : cartItems) {
            Double price = item.getDouble("price");
            if (price != null) total += price;
        }
        if (listener != null) {
            listener.onCalculated(total);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        DocumentSnapshot product = cartItems.get(position);

        String name = product.getString("name");
        Double price = product.getDouble("price");
        String imageUrl = product.getString("imageUrl");

        holder.nameText.setText(name);
        holder.priceText.setText("₪" + (price != null ? price : 0));
        Glide.with(context).load(imageUrl).into(holder.imageView);

        holder.removeButton.setOnClickListener(v -> {
            holder.removeButton.setEnabled(false); // מונע לחיצה כפולה

            FirebaseFirestore.getInstance()
                    .collection("carts")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("items")
                    .document(product.getId())
                    .delete()
                    .addOnSuccessListener(unused -> {
                        // לא נוגעים ב-cartItems או ב-notifyItemRemoved – פיירבייס יטפל בזה
                        calculateTotal(); // נחשב מחדש את הסכום
                        holder.removeButton.setEnabled(true); // מאפשר שוב לחיצה
                    })
                    .addOnFailureListener(e -> {
                        holder.removeButton.setEnabled(true); // מאפשר שוב לחיצה גם במקרה של כשל
                    });
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameText, priceText;
        Button removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cart_product_image);
            nameText = itemView.findViewById(R.id.cart_product_name);
            priceText = itemView.findViewById(R.id.cart_product_price);
            removeButton = itemView.findViewById(R.id.remove_from_cart_button);
        }
    }
}
