package com.example.firstapp;

public class CartCountHelper {
    private int cartCount;

    public CartCountHelper() {} // נדרש לפיירבייס

    public CartCountHelper(int cartCount) {
        this.cartCount = cartCount;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }
}
