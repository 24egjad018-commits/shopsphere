package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.CartItem;
import com.shopsphere.util.DBConnection;

public class CartDAO {

    // Add a product. If it is already in the cart, increase the quantity.
    public void addToCart(int userId, int productId, int quantity) throws SQLException {
        String sql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    // Get all cart items of one user, with product details
    public List<CartItem> getCartItems(int userId) throws SQLException {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT c.cart_id, c.user_id, c.product_id, c.quantity, "
                   + "p.name, p.price, p.discount "
                   + "FROM cart c JOIN products p ON c.product_id = p.product_id "
                   + "WHERE c.user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setCartId(rs.getInt("cart_id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setProductName(rs.getString("name"));
                    item.setPrice(rs.getDouble("price"));
                    item.setDiscount(rs.getDouble("discount"));
                    list.add(item);
                }
            }
        }
        return list;
    }

    // Change the quantity of one product in the user's cart
    public void updateQuantity(int userId, int productId, int quantity) throws SQLException {
        String sql = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        }
    }

    // Remove one product from the user's cart
    public void removeFromCart(int userId, int productId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    // Empty the whole cart (used after an order is placed)
    public void clearCart(int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    // Small test: run this file to check the DAO (uses user 2 = your admin account)
    public static void main(String[] args) throws SQLException {
        CartDAO dao = new CartDAO();

        dao.addToCart(2, 1, 1);
        dao.addToCart(2, 1, 2);   // same product again: quantity becomes 3
        dao.addToCart(2, 3, 1);

        for (CartItem item : dao.getCartItems(2)) {
            System.out.println(item.getProductName() + " | qty " + item.getQuantity()
                    + " | total Rs." + item.getLineTotal());
        }

        dao.clearCart(2);
        System.out.println("Items after clear: " + dao.getCartItems(2).size());
    }
}