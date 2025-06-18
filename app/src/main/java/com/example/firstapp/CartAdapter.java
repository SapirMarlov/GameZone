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
    private double totalPrice = 0;
    private OnTotalPriceCalculated listener;

    public interface OnTotalPriceCalculated {
        void onCalculated(double total);
    }

    public CartAdapter(Context context, List<DocumentSnapshot> cartItems, OnTotalPriceCalculated listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
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
        holder.priceText.setText("₪" + price);
        Glide.with(context).load(imageUrl).into(holder.imageView);

        totalPrice += price != null ? price : 0;

        // לאחר העדכון האחרון, רק באייטם האחרון נעדכן את הסכום הכולל
        if (position == cartItems.size() - 1 && listener != null) {
            listener.onCalculated(totalPrice);
        }

        holder.removeButton.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("carts")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("items")
                    .document(product.getId()) // ← תיקון כאן
                    .delete()
                    .addOnSuccessListener(unused -> {
                        cartItems.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartItems.size());

                        // עדכון סכום כולל
                        totalPrice = 0;
                        for (DocumentSnapshot item : cartItems) {
                            Double itemPrice = item.getDouble("price");
                            totalPrice += itemPrice != null ? itemPrice : 0;
                        }
                        if (listener != null) listener.onCalculated(totalPrice);
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
