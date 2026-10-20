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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    // Show the empty registration form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    // Handle the submitted form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");

        // Server-side validation
        String error = null;
        if (name == null || name.trim().isEmpty()) {
            error = "Name is required.";
        } else if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            error = "Please enter a valid email address.";
        } else if (mobile != null && !mobile.isEmpty() && !mobile.matches("\\d{10}")) {
            error = "Mobile number must be 10 digits.";
        } else if (password == null || password.length() < 6) {
            error = "Password must be at least 6 characters.";
        }

        try {
            if (error == null && userDAO.findByEmail(email.trim()) != null) {
                error = "This email is already registered.";
            }

            if (error == null) {
                User user = new User();
                user.setName(name.trim());
                user.setEmail(email.trim());
                user.setMobile(mobile);
                user.setPassword(password);
                userDAO.registerUser(user);
                request.setAttribute("success", "Registration successful!");
            }
        } catch (SQLException e) {
            throw new ServletException("Registration failed", e);
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("mobile", mobile);
        }
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}