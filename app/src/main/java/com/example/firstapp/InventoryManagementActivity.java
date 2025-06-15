package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class InventoryManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<DocumentSnapshot> fullProductList = new ArrayList<>();
    private List<DocumentSnapshot> filteredProductList = new ArrayList<>();
    private EditText searchInventory;
    private Button addNewProduct;

    private Button buttonStockHistory;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = db.collection("product");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_management_screen);

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        productAdapter = new ProductAdapter(this, filteredProductList);
        recyclerViewProducts.setAdapter(productAdapter);

        searchInventory = findViewById(R.id.search_inventory);
        addNewProduct = findViewById(R.id.add_new_product);
        buttonStockHistory = findViewById(R.id.button_stock_history);
        addNewProduct.setOnClickListener(v -> {
            Intent intent = new Intent(InventoryManagementActivity.this, UpdateInventoryActivity.class);
            startActivity(intent);
        });

        buttonStockHistory.setOnClickListener(v -> {
            Intent intent = new Intent(InventoryManagementActivity.this, StockHistoryActivity.class);
            startActivity(intent);
        });

        searchInventory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadProducts();
    }

    private void loadProducts() {
        productsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null || snapshots == null) return;

                fullProductList.clear();
                for (QueryDocumentSnapshot doc : snapshots) {
                    fullProductList.add(doc);
                }
                filterList(searchInventory.getText().toString());
            }
        });
    }

    private void filterList(String query) {
        filteredProductList.clear();
        for (DocumentSnapshot doc : fullProductList) {
            String name = doc.getString("name");
            if (name != null && name.toLowerCase().contains(query.toLowerCase())) {
                filteredProductList.add(doc);
            }
        }
        productAdapter.notifyDataSetChanged();
    }
}
