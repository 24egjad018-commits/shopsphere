package com.shopsphere.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.shopsphere.dao.OrderDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {

    private static final List<String> ALLOWED_STATUSES = List.of("PLACED", "SHIPPED", "DELIVERED");

    private OrderDAO orderDAO;

    @Override
    public void init() {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("orders", orderDAO.getAllOrders());
            request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load orders", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String status = request.getParameter("status");
        int orderId;
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }

        // Only the statuses we know are accepted
        if (status != null && ALLOWED_STATUSES.contains(status)) {
            try {
                orderDAO.updateOrderStatus(orderId, status);
            } catch (SQLException e) {
                throw new ServletException("Could not update order", e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders?updated=1");
    }
}