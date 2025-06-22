package com.example.firstapp;

public class PlayerPost {
    private String documentId;
    private String gamerName;
    private String favoriteGame;
    private String description;
    private String platform;
    private String uid;

    public PlayerPost() {}

    public PlayerPost(String documentId, String gamerName, String favoriteGame, String description, String platform, String uid) {
        this.documentId = documentId;
        this.gamerName = gamerName;
        this.favoriteGame = favoriteGame;
        this.description = description;
        this.platform = platform;
        this.uid = uid;
    }

    public String getDocumentId() { return documentId; }
    public String getGamerName() { return gamerName; }
    public String getFavoriteGame() { return favoriteGame; }
    public String getDescription() { return description; }
    public String getPlatform() { return platform; }
    public String getUid() { return uid; }
}

