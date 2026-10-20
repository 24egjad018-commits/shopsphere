package com.shopsphere.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/product-edit")
public class AdminProductEditServlet extends HttpServlet {

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
    }

    // Show the edit form for one product
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = toInt(request.getParameter("id"), -1);

        try {
            Product product = productDAO.getProductById(id);
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/admin/products");
                return;
            }
            request.setAttribute("product", product);
            request.setAttribute("categories", categoryDAO.getAllCategories());
            request.getRequestDispatcher("/admin/product-edit.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load product", e);
        }
    }

    // Save the changes, or hide/show the product
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        int id = toInt(request.getParameter("id"), -1);

        try {
            Product existing = productDAO.getProductById(id);
            if (existing == null || action == null) {
                response.sendRedirect(request.getContextPath() + "/admin/products");
                return;
            }

            if (action.equals("toggle")) {
                productDAO.setStatus(id, !existing.isStatus());
                response.sendRedirect(request.getContextPath() + "/admin/products");
                return;
            }

            if (action.equals("save")) {
                Product p = new Product();
                p.setProductId(id);
                p.setName(trim(request.getParameter("name")));
                p.setBrand(trim(request.getParameter("brand")));
                p.setDescription(trim(request.getParameter("description")));
                p.setCategoryId(toInt(request.getParameter("categoryId"), -1));
                p.setPrice(toDouble(request.getParameter("price"), -1));
                p.setDiscount(toDouble(request.getParameter("discount"), -1));
                p.setStock(toInt(request.getParameter("stock"), -1));

                List<Category> categories = categoryDAO.getAllCategories();

                // Server-side validation
                String error = null;
                if (p.getName().isEmpty() || p.getName().length() > 150) {
                    error = "Product name is required (maximum 150 characters).";
                } else if (p.getBrand().length() > 100) {
                    error = "Brand can be at most 100 characters.";
                } else if (!categoryExists(categories, p.getCategoryId())) {
                    error = "Please choose a valid category.";
                } else if (p.getPrice() <= 0 || p.getPrice() > 10000000) {
                    error = "Price must be greater than 0.";
                } else if (p.getDiscount() < 0 || p.getDiscount() > 100) {
                    error = "Discount must be between 0 and 100.";
                } else if (p.getStock() < 0) {
                    error = "Stock cannot be negative.";
                }

                if (error == null) {
                    productDAO.updateProduct(p);
                    response.sendRedirect(request.getContextPath() + "/admin/products?updated=1");
                    return;
                }

                // Something was wrong: show the form again with the typed values
                request.setAttribute("error", error);
                request.setAttribute("product", p);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/admin/product-edit.jsp").forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/admin/products");

        } catch (SQLException e) {
            throw new ServletException("Could not update product", e);
        }
    }

    private boolean categoryExists(List<Category> categories, int id) {
        for (Category c : categories) {
            if (c.getCategoryId() == id) {
                return true;
            }
        }
        return false;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private int toInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private double toDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}