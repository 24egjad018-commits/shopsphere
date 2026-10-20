package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Product;
import com.shopsphere.util.DBConnection;

public class ProductDAO {

    // Get all active products
    public List<Product> getAllProducts() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE status = TRUE";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }
    // Get ALL products, including hidden ones (for the admin page)
    public List<Product> getAllProductsForAdmin() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY product_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }
    // Get one product by its id (returns null if not found)
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }
    // Save a new product (used by the admin page)
    public void addProduct(Product p) throws SQLException {
        String sql = "INSERT INTO products (category_id, name, brand, description, price, discount, stock) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.executeUpdate();
        }
    }
        // Update an existing product (used by the admin edit page)
    public void updateProduct(Product p) throws SQLException {
        String sql = "UPDATE products SET category_id = ?, name = ?, brand = ?, description = ?, "
                   + "price = ?, discount = ?, stock = ? WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.setInt(8, p.getProductId());
            ps.executeUpdate();
        }
    }

    // Show or hide a product (hidden products are not shown to customers)
    public void setStatus(int productId, boolean status) throws SQLException {
        String sql = "UPDATE products SET status = ? WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }
        // Search active products by name or brand
    public List<Product> searchProducts(String keyword) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE status = TRUE AND (name LIKE ? OR brand LIKE ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }
     // Set the stock of a product to a new value. Returns false if the product
    // does not exist or the value is negative.
    public boolean updateStock(int productId, int stock) throws SQLException {
        if (stock < 0) {
            return false;
        }
        String sql = "UPDATE products SET stock = ? WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, stock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }
        // Search + category filter + sort for the customer products page
    public List<Product> findProducts(String keyword, int categoryId, String sort) throws SQLException {
        List<Product> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE status = TRUE");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (name LIKE ? OR brand LIKE ?)");
            String pattern = "%" + keyword + "%";
            params.add(pattern);
            params.add(pattern);
        }
        if (categoryId > 0) {
            sql.append(" AND category_id = ?");
            params.add(categoryId);
        }

        // The ORDER BY part comes from this fixed list, never from user text
        switch (sort == null ? "" : sort) {
            case "price_asc":
                sql.append(" ORDER BY price - (price * discount / 100) ASC");
                break;
            case "price_desc":
                sql.append(" ORDER BY price - (price * discount / 100) DESC");
                break;
            case "name":
                sql.append(" ORDER BY name ASC");
                break;
            case "newest":
                sql.append(" ORDER BY created_at DESC, product_id DESC");
                break;
            default:
                sql.append(" ORDER BY product_id ASC");
                break;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }
    // Convert one database row into a Product object
    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setDiscount(rs.getDouble("discount"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setStatus(rs.getBoolean("status"));
        return p;
    }

    // Small test: run this file to check the DAO
    public static void main(String[] args) throws SQLException {
        ProductDAO dao = new ProductDAO();

        for (Product p : dao.getAllProducts()) {
            System.out.println(p.getProductId() + " | " + p.getName() + " | Rs." + p.getPrice());
        }

        Product one = dao.getProductById(3);
        System.out.println("Product 3 is: " + (one != null ? one.getName() : "not found"));
    }
}