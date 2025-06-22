package com.example.firstapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;

public class RequestManagmentActivity extends AppCompatActivity {

    private TextView statusTextView, customerNameTextView, customerEmailTextView,
            customerPhoneTextView, customerProductTextView, requestMessageTextView, requestDateTextView;
    private Button updateStatusButton, addCommentButton;
    private EditText commentInput;
    private CheckBox contactAttemptCheckbox;
    private LinearLayout historyLayout;
    private FirebaseFirestore db;
    private String inquiryId;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_management);

        statusTextView = findViewById(R.id.status_text);
        customerNameTextView = findViewById(R.id.customer_name);
        customerEmailTextView = findViewById(R.id.customer_email);
        customerPhoneTextView = findViewById(R.id.customer_phone);
        customerProductTextView = findViewById(R.id.customer_product);
        requestMessageTextView = findViewById(R.id.request_message);
        requestDateTextView = findViewById(R.id.request_date);
        updateStatusButton = findViewById(R.id.update_status_button);
        addCommentButton = findViewById(R.id.add_comment_button);
        commentInput = findViewById(R.id.comment_input);
        contactAttemptCheckbox = findViewById(R.id.contact_attempt_checkbox);
        historyLayout = findViewById(R.id.history_layout);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        inquiryId = intent.getStringExtra("inquiryId");
        if (inquiryId == null || inquiryId.trim().isEmpty()) {
            Toast.makeText(this, "שגיאה: לא התקבל מזהה פנייה תקף", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        docRef = db.collection("inquiries").document(inquiryId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                String status = documentSnapshot.getString("status");
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String phone = documentSnapshot.getString("phone");
                String product = documentSnapshot.getString("product");
                String message = documentSnapshot.getString("message");
                Date timestamp = documentSnapshot.getDate("timestamp");

                if (status != null) {
                    statusTextView.setText("סטטוס: " + status);
                    switch (status.toLowerCase()) {
                        case "new":
                            statusTextView.setTextColor(Color.RED);
                            break;
                        case "בטיפול":
                            statusTextView.setTextColor(Color.parseColor("#FFA500"));
                            break;
                        case "הושלמה":
                            statusTextView.setTextColor(Color.parseColor("#228B22"));
                            break;
                        default:
                            statusTextView.setTextColor(Color.DKGRAY);
                    }
                }
                if (name != null) {
                    customerNameTextView.setText("שם: " + name);
                }
                if (email != null) {
                    customerEmailTextView.setText("אימייל: " + email);
                }
                if (phone != null) {
                    customerPhoneTextView.setText("טלפון: " + phone);
                }
                if (product != null) {
                    customerProductTextView.setText("מוצר: " + product);
                }
                if (message != null) {
                    requestMessageTextView.setText("תיאור: " + message);
                }
                if (timestamp != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    requestDateTextView.setText("תאריך: " + sdf.format(timestamp));
                }
            } else {
                Toast.makeText(this, "הפנייה לא קיימת במסד הנתונים", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "שגיאה בטעינת הפנייה: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        });

        updateStatusButton.setOnClickListener(v -> showStatusDialog());

        addCommentButton.setOnClickListener(v -> {
            String comment = commentInput.getText().toString().trim();
            if (!comment.isEmpty()) {
                String fullComment = comment;
                if (contactAttemptCheckbox.isChecked()) {
                    fullComment = "\uD83D\uDCDE ניסיון יצירת קשר: " + comment;
                }
                Map<String, Object> note = new HashMap<>();
                note.put("timestamp", FieldValue.serverTimestamp());
                note.put("text", fullComment);

                docRef.collection("history").add(note).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "ההערה נוספה להיסטוריה", Toast.LENGTH_SHORT).show();
                    commentInput.setText("");
                    contactAttemptCheckbox.setChecked(false);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "שגיאה בהוספת הערה", Toast.LENGTH_SHORT).show();
                });
            }
        });

        loadHistory();
    }

    private void showStatusDialog() {
        final String[] options = { "בטיפול", "הושלמה"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר סטטוס חדש")
                .setItems(options, (dialog, which) -> {
                    String selectedStatus = options[which];
                    updateStatus(selectedStatus);
                })
                .show();
    }

    private void updateStatus(String newStatus) {
        docRef.update("status", newStatus).addOnSuccessListener(unused -> {
            statusTextView.setText("סטטוס: " + newStatus);
            Toast.makeText(this, "הסטטוס עודכן ל" + newStatus, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadHistory() {
        docRef.collection("history").orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null || value == null) return;
                        historyLayout.removeAllViews();
                        for (QueryDocumentSnapshot doc : value) {
                            String text = doc.getString("text");
                            Date date = doc.getDate("timestamp");
                            String formattedDate = "";
                            if (date != null) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                formattedDate = sdf.format(date);
                            }

                            TextView historyItem = new TextView(RequestManagmentActivity.this);
                            historyItem.setText(formattedDate + " | " + text);
                            historyItem.setTextSize(14);
                            historyItem.setPadding(8, 4, 8, 4);
                            historyLayout.addView(historyItem);
                        }
                    }
                });
    }
}
