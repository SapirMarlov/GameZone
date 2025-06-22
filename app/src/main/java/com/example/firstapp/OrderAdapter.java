package com.example.firstapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<OrderModel> orderList;

    public OrderAdapter(List<OrderModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);
        holder.orderIdText.setText("מס' הזמנה: " + order.getOrderId());
        holder.totalPriceText.setText("סכום: ₪" + order.getTotalPrice());
        holder.dateText.setText("תאריך: " + order.getDate());

        List<String> itemNames = order.getItemNames();
        StringBuilder itemList = new StringBuilder();
        for (String item : itemNames) {
            itemList.append("\u2022 ").append(item).append("\n");
        }
        holder.itemsText.setText(itemList.toString().trim());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText, totalPriceText, dateText, itemsText;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.order_id);
            totalPriceText = itemView.findViewById(R.id.total_price);
            dateText = itemView.findViewById(R.id.order_date);
            itemsText = itemView.findViewById(R.id.order_items);
        }
    }
}
