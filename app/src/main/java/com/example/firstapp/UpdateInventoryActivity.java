package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UpdateInventoryActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private StorageReference storageRef;
    private FirebaseAuth mAuth;

    private TextInputEditText editTextProductId, editTextProductName, editTextCategory,
            editTextDescription, editTextPrice, editTextQuantity;
    private TextView textViewCurrentQuantity, textViewImageStatus;
    private Button buttonSave, buttonAdd, buttonRemove, buttonSelectImage;

    private int currentQuantity = 0;
    private Uri selectedImageUri = null;
    private String lastStockAction = "עדכון";

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_inventory);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("product_images");
        mAuth = FirebaseAuth.getInstance();

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        textViewImageStatus.setText("✔ תמונה נבחרה");
                    }
                }
        );

        editTextProductId = findViewById(R.id.editTextProductId);
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        textViewCurrentQuantity = findViewById(R.id.textViewCurrentQuantity);
        textViewImageStatus = findViewById(R.id.textViewImageStatus);

        buttonSave = findViewById(R.id.buttonSave);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonRemove = findViewById(R.id.buttonRemove);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);

        editTextProductId.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) loadProduct();
        });

        buttonAdd.setOnClickListener(v -> {
            int toAdd = getEnteredQuantity();
            currentQuantity += toAdd;
            updateQuantityText();
            lastStockAction = "הוספה";
        });

        buttonRemove.setOnClickListener(v -> {
            int toRemove = getEnteredQuantity();
            currentQuantity = Math.max(0, currentQuantity - toRemove);
            updateQuantityText();
            lastStockAction = "הסרה";
        });

        buttonSelectImage.setOnClickListener(v -> openFileChooser());

        buttonSave.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                uploadImageAndSaveData();
            } else {
                saveData(null);
            }
        });
    }

    private void loadProduct() {
        String productId = editTextProductId.getText().toString().trim();
        if (productId.isEmpty()) return;

        db.collection("product").document(productId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        editTextProductName.setText(doc.getString("name"));
                        editTextCategory.setText(doc.getString("category"));
                        editTextDescription.setText(doc.getString("description"));
                        editTextPrice.setText(String.valueOf(doc.getDouble("price")));
                        Long qty = doc.getLong("stock");
                        currentQuantity = (qty != null) ? qty.intValue() : 0;
                        updateQuantityText();
                    } else {
                        Toast.makeText(this, "מוצר לא נמצא", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "שגיאה בטעינת נתונים", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "loadProduct failed", e);
                });
    }

    private void saveData(String imageUrl) {
        String productId = editTextProductId.getText().toString().trim();
        String name = editTextProductName.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();

        if (productId.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "שדות חובה חסרים", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("category", category);
        data.put("description", description);
        data.put("price", price);
        data.put("stock", currentQuantity);
        if (imageUrl != null) data.put("imageUrl", imageUrl);

        db.collection("product").document(productId).set(data)
                .addOnSuccessListener(aVoid -> {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser == null) return;
                    String uid = currentUser.getUid();

                    db.collection("users").document(uid).get()
                            .addOnSuccessListener(documentSnapshot -> {
                                String updatedBy = documentSnapshot.contains("username")
                                        ? documentSnapshot.getString("username")
                                        : (currentUser.getEmail() != null ? currentUser.getEmail() : "לא ידוע");

                                Map<String, Object> log = new HashMap<>();
                                log.put("productId", productId);
                                log.put("productName", name);
                                log.put("action", lastStockAction);
                                log.put("quantityBefore", 0);
                                log.put("quantityAfter", currentQuantity);
                                log.put("updatedBy", updatedBy);
                                log.put("timestamp", FieldValue.serverTimestamp());

                                db.collection("stock_logs").add(log)
                                        .addOnSuccessListener(r -> Log.d("Log", "Stock log added"))
                                        .addOnFailureListener(e -> Log.e("Log", "Failed to add log", e));

                                lastStockAction = "עדכון";
                                Toast.makeText(this, "המלאי עודכן בהצלחה!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "שגיאה בשליפת משתמש", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "שגיאה בעדכון", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "saveData failed", e);
                });
    }

    private int getEnteredQuantity() {
        try {
            return Integer.parseInt(editTextQuantity.getText().toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateQuantityText() {
        textViewCurrentQuantity.setText("כמות במלאי לאחר עדכון: " + currentQuantity);
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        imagePickerLauncher.launch(Intent.createChooser(intent, "בחר תמונה"));
    }

    private void uploadImageAndSaveData() {
        if (selectedImageUri == null) return;

        String productId = editTextProductId.getText().toString().trim();
        StorageReference fileRef = storageRef.child(productId + ".jpg");

        fileRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot ->
                        fileRef.getDownloadUrl().addOnSuccessListener(uri ->
                                saveData(uri.toString())))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "שגיאה בהעלאת תמונה", Toast.LENGTH_SHORT).show();
                    Log.e("Firebase", "Image upload failed", e);
                });
    }
}
