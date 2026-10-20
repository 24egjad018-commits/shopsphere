package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.util.DBConnection;

public class OrderDAO {

    // Turn the user's cart into an order. Returns the new order id,
    // or -1 if the cart is empty. Everything happens in ONE transaction.
    public int placeOrder(int userId, String address) throws SQLException {
        Connection con = DBConnection.getConnection();
        try {
            con.setAutoCommit(false);   // start the transaction

            // 1. Read the cart with CURRENT prices and stock from the database
            List<CartItem> items = new ArrayList<>();
            Map<Integer, Integer> stockMap = new HashMap<>();

            String cartSql = "SELECT c.product_id, c.quantity, p.name, p.price, p.discount, p.stock "
                           + "FROM cart c JOIN products p ON c.product_id = p.product_id "
                           + "WHERE c.user_id = ? AND p.status = TRUE FOR UPDATE";
            try (PreparedStatement ps = con.prepareStatement(cartSql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        CartItem item = new CartItem();
                        item.setProductId(rs.getInt("product_id"));
                        item.setQuantity(rs.getInt("quantity"));
                        item.setProductName(rs.getString("name"));
                        item.setPrice(rs.getDouble("price"));
                        item.setDiscount(rs.getDouble("discount"));
                        items.add(item);
                        stockMap.put(item.getProductId(), rs.getInt("stock"));
                    }
                }
            }

            if (items.isEmpty()) {
                con.rollback();
                return -1;
            }

            // 2. Check stock and calculate the total on the server
            double total = 0;
            for (CartItem item : items) {
                if (item.getQuantity() > stockMap.get(item.getProductId())) {
                    throw new IllegalStateException("Not enough stock for " + item.getProductName());
                }
                total += item.getLineTotal();
            }
            total = Math.round(total * 100) / 100.0;

            // 3. Create the order row
            int orderId;
            String orderSql = "INSERT INTO orders (user_id, shipping_address, total_amount) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setString(2, address);
                ps.setDouble(3, total);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    orderId = keys.getInt(1);
                }
            }

            // 4. Create the order items and reduce the stock
            String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            String stockSql = "UPDATE products SET stock = stock - ? WHERE product_id = ?";
            try (PreparedStatement itemPs = con.prepareStatement(itemSql);
                 PreparedStatement stockPs = con.prepareStatement(stockSql)) {

                for (CartItem item : items) {
                    itemPs.setInt(1, orderId);
                    itemPs.setInt(2, item.getProductId());
                    itemPs.setInt(3, item.getQuantity());
                    itemPs.setDouble(4, Math.round(item.getFinalPrice() * 100) / 100.0);
                    itemPs.executeUpdate();

                    stockPs.setInt(1, item.getQuantity());
                    stockPs.setInt(2, item.getProductId());
                    stockPs.executeUpdate();
                }
            }

            // 5. Empty the cart
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM cart WHERE user_id = ?")) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            }

            con.commit();   // everything worked: save it all
            return orderId;

        } catch (SQLException | RuntimeException e) {
            con.rollback(); // something failed: undo everything
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    // All orders of one user, newest first
    public List<Order> getOrdersByUser(int userId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC, order_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order();
                    o.setOrderId(rs.getInt("order_id"));
                    o.setUserId(rs.getInt("user_id"));
                    o.setShippingAddress(rs.getString("shipping_address"));
                    o.setTotalAmount(rs.getDouble("total_amount"));
                    o.setPaymentMethod(rs.getString("payment_method"));
                    o.setPaymentStatus(rs.getString("payment_status"));
                    o.setOrderStatus(rs.getString("order_status"));
                    o.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(o);
                }
            }
        }
        return list;
    }
    // All orders of all customers, newest first (for the admin page)
    public List<Order> getAllOrders() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS customer_name FROM orders o "
                   + "JOIN users u ON o.user_id = u.user_id "
                   + "ORDER BY o.created_at DESC, o.order_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setUserId(rs.getInt("user_id"));
                o.setCustomerName(rs.getString("customer_name"));
                o.setShippingAddress(rs.getString("shipping_address"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                o.setPaymentMethod(rs.getString("payment_method"));
                o.setPaymentStatus(rs.getString("payment_status"));
                o.setOrderStatus(rs.getString("order_status"));
                o.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(o);
            }
        }
        return list;
    }

    // Change the status of an order. Cash on Delivery orders become PAID when delivered.
    public void updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET order_status = ?, "
                   + "payment_status = CASE WHEN ? = 'DELIVERED' THEN 'PAID' ELSE payment_status END "
                   + "WHERE order_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, status);
            ps.setInt(3, orderId);
            ps.executeUpdate();
        }
    }
        // One order, but only if it belongs to the given user (otherwise null)
    public Order getOrderForUser(int orderId, int userId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order o = new Order();
                    o.setOrderId(rs.getInt("order_id"));
                    o.setUserId(rs.getInt("user_id"));
                    o.setShippingAddress(rs.getString("shipping_address"));
                    o.setTotalAmount(rs.getDouble("total_amount"));
                    o.setPaymentMethod(rs.getString("payment_method"));
                    o.setPaymentStatus(rs.getString("payment_status"));
                    o.setOrderStatus(rs.getString("order_status"));
                    o.setCreatedAt(rs.getTimestamp("created_at"));
                    return o;
                }
            }
        }
        return null;
    }

    // The items of one order, with the product names
    public List<OrderItem> getOrderItems(int orderId) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.price, p.name "
                   + "FROM order_items oi JOIN products p ON oi.product_id = p.product_id "
                   + "WHERE oi.order_id = ? ORDER BY oi.order_item_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderItemId(rs.getInt("order_item_id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getDouble("price"));
                    list.add(item);
                }
            }
        }
        return list;
    }
    // Small test: run this file ONCE. It creates a real test order for user 2.
    public static void main(String[] args) throws SQLException {
        CartDAO cartDAO = new CartDAO();
        OrderDAO orderDAO = new OrderDAO();

        cartDAO.addToCart(2, 1, 2);   // 2 x Galaxy A15
        cartDAO.addToCart(2, 4, 1);   // 1 x Cotton T-Shirt

        int orderId = orderDAO.placeOrder(2, "Test address");
        System.out.println("Order placed, id = " + orderId);
        System.out.println("Cart items left: " + cartDAO.getCartItems(2).size());

        for (Order o : orderDAO.getOrdersByUser(2)) {
            System.out.println("Order " + o.getOrderId() + " | Rs." + o.getTotalAmount() + " | " + o.getOrderStatus());
        }
    }
}