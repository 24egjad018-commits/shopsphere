package com.shopsphere.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private CartDAO cartDAO;
    private OrderDAO orderDAO;

    @Override
    public void init() {
        cartDAO = new CartDAO();
        orderDAO = new OrderDAO();
    }

    // Show the checkout page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        try {
            if (!loadCart(request, user)) {
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load checkout", e);
        }
    }

    // Place the order
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("user");

        String address = request.getParameter("address");
        address = (address == null) ? "" : address.trim();

        try {
            String error = null;
            if (address.length() < 10 || address.length() > 255) {
                error = "Please enter a full address (10 to 255 characters).";
            }

            if (error == null) {
                try {
                    int orderId = orderDAO.placeOrder(user.getUserId(), address);
                    if (orderId == -1) {
                        response.sendRedirect(request.getContextPath() + "/cart");
                        return;
                    }
                    response.sendRedirect(request.getContextPath() + "/order-success.jsp?id=" + orderId);
                    return;
                } catch (IllegalStateException e) {
                    error = e.getMessage();   // for example: not enough stock
                }
            }

            // Something was wrong: show the checkout page again with the message
            loadCart(request, user);
            request.setAttribute("error", error);
            request.setAttribute("address", address);
            request.getRequestDispatcher("/checkout.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Checkout failed", e);
        }
    }

    // Puts the cart items and the total into the request. Returns false if the cart is empty.
    private boolean loadCart(HttpServletRequest request, User user) throws SQLException {
        List<CartItem> items = cartDAO.getCartItems(user.getUserId());
        double total = 0;
        for (CartItem item : items) {
            total += item.getLineTotal();
        }
        request.setAttribute("items", items);
        request.setAttribute("total", total);
        return !items.isEmpty();
    }
}