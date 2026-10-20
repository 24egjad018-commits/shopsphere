package com.shopsphere.dao;

import com.shopsphere.model.User;
import com.shopsphere.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Save a new customer. The password is hashed before saving.
    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password, mobile, role) VALUES (?, ?, ?, ?, 'CUSTOMER')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            ps.setString(4, user.getMobile());
            return ps.executeUpdate() > 0;
        }
    }

    // Find a user by email (returns null if not found)
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setName(rs.getString("name"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setMobile(rs.getString("mobile"));
                    u.setRole(rs.getString("role"));
                    u.setStatus(rs.getBoolean("status"));
                    return u;
                }
            }
        }
        return null;
    }

    // Check email + password. Returns the user if correct, otherwise null.
    public User login(String email, String plainPassword) throws SQLException {
        User u = findByEmail(email);
        if (u != null && u.isStatus() && BCrypt.checkpw(plainPassword, u.getPassword())) {
            return u;
        }
        return null;
    }

    // Small test: run this file once to check the DAO
    public static void main(String[] args) throws SQLException {
        UserDAO dao = new UserDAO();

        if (dao.findByEmail("test@shopsphere.com") == null) {
            User u = new User();
            u.setName("Test User");
            u.setEmail("test@shopsphere.com");
            u.setPassword("test123");
            u.setMobile("9999999999");
            System.out.println("Registered: " + dao.registerUser(u));
        }

        System.out.println("Correct password: " + (dao.login("test@shopsphere.com", "test123") != null));
        System.out.println("Wrong password: " + (dao.login("test@shopsphere.com", "wrong") != null));
    }
}