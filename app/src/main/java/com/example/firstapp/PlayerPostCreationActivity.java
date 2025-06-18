package com.example.firstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PlayerPostCreationActivity extends AppCompatActivity {

    private String selectedConsole = "PlayStation"; // ברירת מחדל
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_post_creation);

        db = FirebaseFirestore.getInstance();

        EditText gamerNameInput = findViewById(R.id.gamer_name_input);
        EditText favoriteGameInput = findViewById(R.id.favorite_game_input);
        EditText additionalDescription = findViewById(R.id.additional_description);

        // כפתורי בחירת קונסולה
        Button btnPlaystation = findViewById(R.id.console_playstation);
        Button btnXbox = findViewById(R.id.console_xbox);
        Button btnNintendo = findViewById(R.id.console_nintendo);
        Button btnPc = findViewById(R.id.console_pc);

        View.OnClickListener platformClickListener = v -> {
            int id = v.getId();
            if (id == R.id.console_playstation) {
                selectedConsole = "PlayStation";
            } else if (id == R.id.console_xbox) {
                selectedConsole = "Xbox";
            } else if (id == R.id.console_nintendo) {
                selectedConsole = "Nintendo";
            } else if (id == R.id.console_pc) {
                selectedConsole = "PC";
            }
            Toast.makeText(this, "נבחרה פלטפורמה: " + selectedConsole, Toast.LENGTH_SHORT).show();
        };

        btnPlaystation.setOnClickListener(platformClickListener);
        btnXbox.setOnClickListener(platformClickListener);
        btnNintendo.setOnClickListener(platformClickListener);
        btnPc.setOnClickListener(platformClickListener);

        Button publishProfileButton = findViewById(R.id.publish_profile_button);
        publishProfileButton.setOnClickListener(v -> {
            String gamerName = gamerNameInput.getText().toString().trim();
            String favoriteGame = favoriteGameInput.getText().toString().trim();
            String description = additionalDescription.getText().toString().trim();

            if (gamerName.isEmpty() || favoriteGame.isEmpty()) {
                Toast.makeText(this, "נא למלא את שם המשתמש והמשחק", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Map<String, Object> playerPost = new HashMap<>();
            playerPost.put("gamerName", gamerName);
            playerPost.put("favoriteGame", favoriteGame);
            playerPost.put("description", description);
            playerPost.put("platform", selectedConsole);
            playerPost.put("uid", uid);
            playerPost.put("timestamp", FieldValue.serverTimestamp()); // 🔁 מעודכן

            db.collection("playerPosts")
                    .add(playerPost)
                    .addOnSuccessListener(docRef -> {
                        Toast.makeText(this, "המודעה פורסמה!", Toast.LENGTH_SHORT).show();
                        finish(); // חזרה למסך הקודם
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "שגיאה בפרסום המודעה", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
