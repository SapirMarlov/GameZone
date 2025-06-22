package com.example.firstapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StockHistoryAdapter extends RecyclerView.Adapter<StockHistoryAdapter.ViewHolder> {

    private List<StockLog> logs;

    public StockHistoryAdapter(List<StockLog> logs) {
        this.logs = logs;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            textDetails = itemView.findViewById(R.id.textLogDetails);
        }
    }

    @NonNull
    @Override
    public StockHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stock_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockHistoryAdapter.ViewHolder holder, int position) {
        StockLog log = logs.get(position);

        // עיצוב תאריך
        String formattedDate = "";
        Timestamp ts = log.getTimestamp();
        if (ts != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            formattedDate = sdf.format(ts.toDate());
        }

        String info = "מוצר: " + log.getProductName() +
                "\nמקט: " + log.getProductId() +
                "\nפעולה: " + log.getAction() +
                "\nלפני: " + log.getQuantityBefore() +
                "  אחרי: " + log.getQuantityAfter() +
                "\nעל ידי: " + log.getUpdatedBy() +
                "\nזמן: " + formattedDate;


        holder.textDetails.setText(info);
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public void updateData(List<StockLog> newLogs) {
        this.logs = newLogs;
        notifyDataSetChanged();
    }
}
