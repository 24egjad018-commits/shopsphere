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

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
    }

    // Show the product list and the "add product" form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            loadPageData(request);
            request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load products", e);
        }
    }

    // Save a new product
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = trim(request.getParameter("name"));
        String brand = trim(request.getParameter("brand"));
        String description = trim(request.getParameter("description"));
        int categoryId = toInt(request.getParameter("categoryId"), -1);
        double price = toDouble(request.getParameter("price"), -1);
        double discount = toDouble(request.getParameter("discount"), -1);
        int stock = toInt(request.getParameter("stock"), -1);

        try {
            List<Category> categories = categoryDAO.getAllCategories();

            // Server-side validation
            String error = null;
            if (name.isEmpty() || name.length() > 150) {
                error = "Product name is required (maximum 150 characters).";
            } else if (brand.length() > 100) {
                error = "Brand can be at most 100 characters.";
            } else if (!categoryExists(categories, categoryId)) {
                error = "Please choose a valid category.";
            } else if (price <= 0 || price > 10000000) {
                error = "Price must be greater than 0.";
            } else if (discount < 0 || discount > 100) {
                error = "Discount must be between 0 and 100.";
            } else if (stock < 0) {
                error = "Stock cannot be negative.";
            }

            if (error == null) {
                Product p = new Product();
                p.setCategoryId(categoryId);
                p.setName(name);
                p.setBrand(brand);
                p.setDescription(description);
                p.setPrice(price);
                p.setDiscount(discount);
                p.setStock(stock);
                productDAO.addProduct(p);

                // Redirect so a page refresh does not add the product again
                response.sendRedirect(request.getContextPath() + "/admin/products?added=1");
                return;
            }

            // Something was wrong: show the page again with the message
            loadPageData(request);
            request.setAttribute("error", error);
            request.setAttribute("fName", name);
            request.setAttribute("fBrand", brand);
            request.setAttribute("fDescription", description);
            request.setAttribute("fCategoryId", categoryId);
            request.setAttribute("fPrice", request.getParameter("price"));
            request.setAttribute("fDiscount", request.getParameter("discount"));
            request.setAttribute("fStock", request.getParameter("stock"));
            request.getRequestDispatcher("/admin/products.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Could not save product", e);
        }
    }

    private void loadPageData(HttpServletRequest request) throws SQLException {
        request.setAttribute("products", productDAO.getAllProductsForAdmin());
        request.setAttribute("categories", categoryDAO.getAllCategories());
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