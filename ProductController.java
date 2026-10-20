package com.shopsphere.swing.controller;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import com.shopsphere.swing.SwingAdminApp;
import com.shopsphere.swing.view.ProductFrame;

import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductController {

    private final ProductFrame view;
    private final ProductDAO productDAO = new ProductDAO();

    public ProductController(ProductFrame view) {
        this.view = view;
        view.addRefreshListener(e -> loadProducts());
        view.addShowHiddenListener(e -> loadProducts());
        view.addLogoutListener(e -> logout());
        loadProducts();
    }

    private void loadProducts() {
        try {
            List<Product> shown = new ArrayList<>();
            for (Product p : productDAO.getAllProductsForAdmin()) {
                if (p.isStatus() || view.isShowHidden()) {
                    shown.add(p);
                }
            }
            view.showProducts(shown);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view,
                    "Could not load products:\n" + e.getMessage(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(view, "Do you want to log out?",
                "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            SwingAdminApp.showLogin();
            view.dispose();
        }
    }
}