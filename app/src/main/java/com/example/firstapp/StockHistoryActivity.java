package com.example.firstapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StockHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchInput;
    private StockHistoryAdapter adapter;
    private List<StockLog> stockLogs = new ArrayList<>();
    private List<StockLog> allLogs = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);

        recyclerView = findViewById(R.id.recyclerViewStockLogs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchInput = findViewById(R.id.editTextSearch);

        adapter = new StockHistoryAdapter(stockLogs);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // האזנה לשדה חיפוש
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLogs(s.toString());
            }
        });

        // שליפת כל הלוגים עם מיון לפי תאריך (מהחדש לישן)
        db.collection("stock_logs")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    stockLogs.clear();
                    allLogs.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        StockLog log = document.toObject(StockLog.class);
                        stockLogs.add(log);
                        allLogs.add(log);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                });
    }

    // סינון לפי שם מוצר
    private void filterLogs(String keyword) {
        List<StockLog> filtered = new ArrayList<>();
        for (StockLog log : allLogs) {
            if (
                    log.getProductName().toLowerCase().contains(keyword.toLowerCase()) ||
                            (log.getProductId() != null && log.getProductId().contains(keyword))
            ) {
                filtered.add(log);
            }
        }
        adapter.updateData(filtered);
    }
}
