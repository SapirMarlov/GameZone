package com.example.firstapp; // שנה לפי השם שלך

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class StockHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StockHistoryAdapter adapter;
    private ArrayList<StockLog> stockLogs;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);

        recyclerView = findViewById(R.id.recyclerViewStockLogs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        stockLogs = new ArrayList<>();
        adapter = new StockHistoryAdapter(stockLogs);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        db.collection("stock_logs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        StockLog log = document.toObject(StockLog.class);
                        stockLogs.add(log);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // כאן תוכל לשים Toast או הודעה אחרת
                });
    }
}
