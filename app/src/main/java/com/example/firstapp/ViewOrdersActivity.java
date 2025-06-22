package com.example.firstapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewOrdersActivity extends AppCompatActivity {

    private TextView customerNameTextView;
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<OrderModel> orderList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_orders_history);

        customerNameTextView = findViewById(R.id.customer_name);
        ordersRecyclerView = findViewById(R.id.recycler_orders);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        ordersRecyclerView.setAdapter(orderAdapter);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("users").document(uid).get()
                    .addOnSuccessListener(document -> {
                        String username = document.getString("username");
                        if (username != null && !username.isEmpty()) {
                            customerNameTextView.setText("שלום " + username + ", אלו הם היסטוריית ההזמנות שלך");
                        } else {
                            customerNameTextView.setText("שלום משתמש, אלו הם היסטוריית ההזמנות שלך");
                        }
                    });

            db.collection("orders")
                    .whereEqualTo("userId", uid)
                    .get()
                    .addOnSuccessListener(this::onOrdersLoaded);
        } else {
            customerNameTextView.setText("היסטוריית הזמנות");
        }
    }

    private void onOrdersLoaded(QuerySnapshot queryDocumentSnapshots) {
        orderList.clear();
        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
            OrderModel order = new OrderModel();
            order.setOrderId(doc.getId());
            order.setTotalPrice(doc.getDouble("totalPrice"));

            Timestamp timestamp = doc.getTimestamp("timestamp");
            if (timestamp != null) {
                Date date = timestamp.toDate();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                order.setDate(sdf.format(date));
            } else {
                order.setDate("לא ידוע");
            }

            // שליפת פריטים מתוך תת-אוסף items
            db.collection("orders").document(doc.getId()).collection("items").get()
                    .addOnSuccessListener(itemSnapshots -> {
                        List<String> itemNames = new ArrayList<>();
                        for (DocumentSnapshot itemDoc : itemSnapshots) {
                            String name = itemDoc.getString("name");
                            if (name != null) {
                                itemNames.add(name);
                            }
                        }
                        order.setItemNames(itemNames);
                        // הוספה תתבצע לאחר שליפת הפריטים
                        // נוסף לאחר שליפת הפריטים
                        orderAdapter.notifyItemInserted(orderList.size() - 1);
                    });

            orderList.add(order);
        }
    }
}
