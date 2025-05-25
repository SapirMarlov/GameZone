package com.example.firstapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ManagePricesActivity extends AppCompatActivity {

    private ListView pricesListView;
    private ArrayList<String> pricesList;
    private Button btnUpdatePrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_prices_screen);

        pricesListView = findViewById(R.id.prices_list);
        btnUpdatePrices = findViewById(R.id.btn_update_prices);

        // יצירת רשימה של מוצרים ומחירים
        pricesList = new ArrayList<>();
        pricesList.add("מחשב נייד דל - מחיר: 3,500 ש\"ח");
        pricesList.add("פלייסטיישן 5 - מחיר: 2,000 ש\"ח");
        pricesList.add("אוזניות רייזר - מחיר: 450 ש\"ח");
        pricesList.add("כרטיס גרפי NVIDIA RTX 3080 - מחיר: 5,000 ש\"ח");
        pricesList.add("גיימפד Xbox - מחיר: 300 ש\"ח");
        pricesList.add("מסך גיימינג 27 אינץ' - מחיר: 1,200 ש\"ח");

        // חיבור הנתונים לרשימה
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pricesList);
        pricesListView.setAdapter(adapter);

        // לחיצה על כפתור עדכון מחירים
        btnUpdatePrices.setOnClickListener(view -> {
            Toast.makeText(this, "המחירים עודכנו בהצלחה!", Toast.LENGTH_SHORT).show();
        });
    }
}
