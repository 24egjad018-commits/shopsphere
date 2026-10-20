package com.shopsphere.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private CartDAO cartDAO;
    private ProductDAO productDAO;

    @Override
    public void init() {
        cartDAO = new CartDAO();
        productDAO = new ProductDAO();
    }

    // Show the cart
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        try {
            List<CartItem> items = cartDAO.getCartItems(user.getUserId());

            double total = 0;
            for (CartItem item : items) {
                total += item.getLineTotal();   // total is calculated on the server
            }

            request.setAttribute("items", items);
            request.setAttribute("total", total);
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load cart", e);
        }
    }

    // Add, update or remove an item
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        String action = request.getParameter("action");
        int productId = toInt(request.getParameter("productId"), 0);
        int quantity = toInt(request.getParameter("quantity"), 1);

        try {
            Product product = productDAO.getProductById(productId);

            if (product != null && action != null) {
                switch (action) {
                    case "add":
                         if (quantity >= 1 && product.getStock() > 0 && product.isStatus()) {
                            cartDAO.addToCart(user.getUserId(), productId, 1);
                        }
                        break;
                    case "update":
                        if (quantity >= 1 && quantity <= product.getStock()) {
                            cartDAO.updateQuantity(user.getUserId(), productId, quantity);
                        }
                        break;
                    case "remove":
                        cartDAO.removeFromCart(user.getUserId(), productId);
                        break;
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Cart action failed", e);
        }

        // Redirect so a page refresh does not repeat the action
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private int toInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}