package com.example.firstapp;

import com.google.firebase.Timestamp;

public class StockLog {

    private String productId;

    private String productName;
    private String action;
    private int quantityBefore;
    private int quantityAfter;
    private String updatedBy;
    private Timestamp timestamp;

    public StockLog() {}

    // Getters
    public String getProductId() { return productId; }

    public String getProductName() { return productName; }
    public String getAction() { return action; }
    public int getQuantityBefore() { return quantityBefore; }
    public int getQuantityAfter() { return quantityAfter; }
    public String getUpdatedBy() { return updatedBy; }
    public Timestamp getTimestamp() { return timestamp; }
}

