package com.example.firstapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomerRequestsActivity extends AppCompatActivity implements InquiryAdapter.OnInquiryClickListener {

    private RecyclerView recyclerView;
    private InquiryAdapter adapter;
    private List<DocumentSnapshot> inquiryList = new ArrayList<>();
    private FirebaseFirestore db;
    private Spinner filterSpinner;
    private String selectedStatusFilter = "כל הפניות";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_requests);

        recyclerView = findViewById(R.id.recycler_inquiries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        filterSpinner = findViewById(R.id.filter_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"כל הפניות", "פניות חדשות", "פניות בטיפול", "פניות שהושלמו"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatusFilter = parent.getItemAtPosition(position).toString();
                loadInquiries();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        db = FirebaseFirestore.getInstance();
        adapter = new InquiryAdapter(this, inquiryList, this);
        recyclerView.setAdapter(adapter);
    }

    private void loadInquiries() {
        Query query = db.collection("inquiries").orderBy("timestamp", Query.Direction.DESCENDING);

        switch (selectedStatusFilter) {
            case "פניות חדשות":
                query = query.whereEqualTo("status", "new");
                break;
            case "פניות בטיפול":
                query = query.whereEqualTo("status", "בטיפול");
                break;
            case "פניות שהושלמו":
                query = query.whereEqualTo("status", "הושלמה");
                break;
            default:
                break;
        }

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                inquiryList.clear();
                if (value != null) {
                    inquiryList.addAll(value.getDocuments());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onInquiryClick(DocumentSnapshot document) {
        Intent intent = new Intent(this, RequestManagmentActivity.class);
        intent.putExtra("inquiryId", document.getId());
        intent.putExtra("name", document.getString("name"));
        intent.putExtra("message", document.getString("message"));
        intent.putExtra("status", document.getString("status"));
        intent.putExtra("phone", document.getString("phone"));
        intent.putExtra("product", document.getString("product"));
        startActivity(intent);
    }
}
