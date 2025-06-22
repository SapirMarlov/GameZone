package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class GamePartnerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GamePartnerAdapter adapter;
    private FirebaseFirestore db;

    private List<PlayerPost> allPosts = new ArrayList<>();
    private List<PlayerPost> filteredPosts = new ArrayList<>();

    private String currentPlatformFilter = "";
    private EditText searchPlayers;

    private ListenerRegistration postsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_partners);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler_game_partners);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GamePartnerAdapter(filteredPosts);
        recyclerView.setAdapter(adapter);

        searchPlayers = findViewById(R.id.search_players);

        // חיפוש לפי טקסט
        searchPlayers.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPosts();
            }
        });

        // כפתורי סינון פלטפורמה
        setupPlatformFilter(R.id.filter_playstation, "PlayStation");
        setupPlatformFilter(R.id.filter_xbox, "Xbox");
        setupPlatformFilter(R.id.filter_nintendo, "Nintendo");
        setupPlatformFilter(R.id.filter_pc, "PC");

        // כפתור פתיחת יצירת מודעה
        Button createPostButton = findViewById(R.id.create_post_button);
        createPostButton.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(this, "עליך להתחבר כדי לפרסם מודעה", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(GamePartnerActivity.this, PlayerPostCreationActivity.class));
            }
        });

        // מאזין לשינויים בזמן אמת
        listenToRealtimePosts();
    }

    private void setupPlatformFilter(int buttonId, String platformName) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            currentPlatformFilter = platformName;
            filterPosts();
        });
    }

    private void listenToRealtimePosts() {
        postsListener = db.collection("playerPosts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null || querySnapshot == null) return;

                    allPosts.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String documentId = doc.getId();
                        String gamerName = doc.getString("gamerName");
                        String favoriteGame = doc.getString("favoriteGame");
                        String description = doc.getString("description");
                        String platform = doc.getString("platform");
                        String uid = doc.getString("uid");

                        PlayerPost post = new PlayerPost(documentId, gamerName, favoriteGame, description, platform, uid);
                        allPosts.add(post);
                    }
                    filterPosts();
                });
    }

    private void filterPosts() {
        String searchText = searchPlayers.getText().toString().toLowerCase();
        filteredPosts.clear();

        for (PlayerPost post : allPosts) {
            String platform = post.getPlatform();
            String game = post.getFavoriteGame();
            String name = post.getGamerName();

            boolean matchesPlatform = currentPlatformFilter.isEmpty() || platform.equalsIgnoreCase(currentPlatformFilter);
            boolean matchesSearch = searchText.isEmpty()
                    || (game != null && game.toLowerCase().contains(searchText))
                    || (name != null && name.toLowerCase().contains(searchText));

            if (matchesPlatform && matchesSearch) {
                filteredPosts.add(post);
            }
        }

        adapter.updateList(filteredPosts);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (postsListener != null) {
            postsListener.remove();
        }
    }
}
