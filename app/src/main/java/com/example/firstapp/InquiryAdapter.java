package com.example.firstapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class InquiryAdapter extends RecyclerView.Adapter<InquiryAdapter.InquiryViewHolder> {

    public interface OnInquiryClickListener {
        void onInquiryClick(DocumentSnapshot document);
    }

    private Context context;
    private List<DocumentSnapshot> inquiries;
    private OnInquiryClickListener listener;

    public InquiryAdapter(Context context, List<DocumentSnapshot> inquiries, OnInquiryClickListener listener) {
        this.context = context;
        this.inquiries = inquiries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InquiryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inquiry, parent, false);
        return new InquiryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InquiryViewHolder holder, int position) {
        DocumentSnapshot doc = inquiries.get(position);
        String name = doc.getString("name");
        String message = doc.getString("message");
        String status = doc.getString("status");

        holder.nameText.setText("שם לקוח: " + name);
        holder.messageText.setText("פנייה: " + message);
        holder.statusText.setText("מצב פנייה: " + status);

        // צבע סטטוס לפי ערך (צבעים חיים)
        if ("new".equalsIgnoreCase(status)) {
            holder.statusText.setTextColor(Color.parseColor("#E53E3E")); // אדום עז
        } else if ("בטיפול".equalsIgnoreCase(status) || "in_progress".equalsIgnoreCase(status)) {
            holder.statusText.setTextColor(Color.parseColor("#F6AD55")); // כתום-צהוב
        } else if ("הושלמה".equalsIgnoreCase(status) || "resolved".equalsIgnoreCase(status)) {
            holder.statusText.setTextColor(Color.parseColor("#38A169")); // ירוק
        } else {
            holder.statusText.setTextColor(Color.DKGRAY); // ברירת מחדל
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onInquiryClick(doc);
            }
        });
    }


    @Override
    public int getItemCount() {
        return inquiries.size();
    }

    public static class InquiryViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, messageText, statusText;

        public InquiryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.inquiry_customer_name);
            messageText = itemView.findViewById(R.id.inquiry_message);
            statusText = itemView.findViewById(R.id.inquiry_status);
        }
    }
}
