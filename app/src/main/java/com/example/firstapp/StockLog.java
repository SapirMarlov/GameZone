package com.example.firstapp; // שנה לפי השם שלך

public class StockLog {
    private String productName;
    private String action; // "הוספה", "הסרה", "עדכון"
    private int quantityBefore;
    private int quantityAfter;
    private String updatedBy;
    private String timestamp;

    public StockLog() {}

    // getters
    public String getProductName() { return productName; }
    public String getAction() { return action; }
    public int getQuantityBefore() { return quantityBefore; }
    public int getQuantityAfter() { return quantityAfter; }
    public String getUpdatedBy() { return updatedBy; }
    public String getTimestamp() { return timestamp; }
}

