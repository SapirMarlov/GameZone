package com.example.firstapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GamePartnerAdapter extends RecyclerView.Adapter<GamePartnerAdapter.ViewHolder> {

    private List<PlayerPost> playerPosts;
    private String currentUserUid;

    public GamePartnerAdapter(List<PlayerPost> playerPosts) {
        this.playerPosts = playerPosts;

        // הוספת בדיקה למשתמש מחובר
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            this.currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            this.currentUserUid = null;
        }
    }

    public void updateList(List<PlayerPost> newList) {
        this.playerPosts = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayerPost post = playerPosts.get(position);
        Context context = holder.itemView.getContext();

        holder.playerName.setText(post.getGamerName());
        holder.playerGame.setText("מחפש שותפים למשחק: " + post.getFavoriteGame());
        holder.playerPlatform.setText("פלטפורמה: " + post.getPlatform());

        // הצג כפתור מחיקה רק אם המשתמש הנוכחי הוא היוצר
        if (post.getUid() != null && currentUserUid != null && post.getUid().equals(currentUserUid)) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("מחיקת מודעה")
                        .setMessage("האם אתה בטוח שברצונך למחוק את המודעה?")
                        .setPositiveButton("מחק", (dialog, which) -> {
                            FirebaseFirestore.getInstance()
                                    .collection("playerPosts")
                                    .document(post.getDocumentId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "המודעה נמחקה", Toast.LENGTH_SHORT).show();
                                        playerPosts.remove(position);
                                        notifyItemRemoved(position);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "שגיאה במחיקה", Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("בטל", null)
                        .show();
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return playerPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView playerAvatar, deleteButton;
        TextView playerName, playerGame, playerPlatform;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerAvatar = itemView.findViewById(R.id.player_avatar);
            playerName = itemView.findViewById(R.id.player_name);
            playerGame = itemView.findViewById(R.id.player_game);
            playerPlatform = itemView.findViewById(R.id.player_platform);
            deleteButton = itemView.findViewById(R.id.delete_post_button);
        }
    }
}
