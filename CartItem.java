package com.shopsphere.model;

import java.io.Serializable;

public class CartItem implements Serializable {

    private int cartId;
    private int userId;
    private int productId;
    private int quantity;

    // Extra fields filled from the products table (for showing in the cart page)
    private String productName;
    private double price;
    private double discount;

    public CartItem() {
    }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    // Price of one item after discount
    public double getFinalPrice() {
        return price - (price * discount / 100);
    }

    // Total for this row = final price x quantity
    public double getLineTotal() {
        return getFinalPrice() * quantity;
    }
}