package com.shopsphere.swing.controller;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.swing.view.LoginFrame;
import com.shopsphere.swing.view.ProductFrame;

import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.Arrays;

public class LoginController {

    private final LoginFrame view;
    private final UserDAO userDAO = new UserDAO();

    public LoginController(LoginFrame view) {
        this.view = view;
        view.addLoginListener(e -> login());
    }

    private void login() {
        String email = view.getEmail();

        // Take the password out of the field and wipe the char array afterwards
        char[] chars = view.getPassword();
        String password = new String(chars);
        Arrays.fill(chars, ' ');
        view.clearPassword();

        if (email.isEmpty() || password.isEmpty()) {
            view.setMessage("Enter email and password.");
            return;
        }

        try {
            User user = userDAO.login(email, password);

            if (user == null) {
                view.setMessage("Invalid email or password.");
                return;
            }
            if (!"ADMIN".equals(user.getRole())) {
                view.setMessage("Admins only.");
                return;
            }

            // Open the products window, then close the login window
            ProductFrame productFrame = new ProductFrame(user.getName());
            new ProductController(productFrame);
            productFrame.setVisible(true);
            view.dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                    "Could not connect to the database:\n" + e.getMessage(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
        }
    }
}