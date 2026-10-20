package com.shopsphere.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = null;
            if (email != null && password != null) {
                user = userDAO.login(email.trim(), password);
            }

            if (user == null) {
                request.setAttribute("error", "Invalid email or password.");
                request.setAttribute("email", email);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            // Start a fresh session (the old one is discarded for safety)
            HttpSession old = request.getSession(false);
            if (old != null) {
                old.invalidate();
            }
            HttpSession session = request.getSession(true);

            user.setPassword(null);   // never keep the password in the session
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());

            response.sendRedirect(request.getContextPath() + "/products");

        } catch (SQLException e) {
            throw new ServletException("Login failed", e);
        }
    }
}