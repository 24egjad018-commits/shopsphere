package com.shopsphere.controller;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/jndi-demo")
public class JndiDemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        try {
            // Tomcat puts the web.xml values under the name java:comp/env
            InitialContext context = new InitialContext();
            String shopName = (String) context.lookup("java:comp/env/shopName");
            Integer maxQuantity = (Integer) context.lookup("java:comp/env/maxCartQuantity");

            response.getWriter().println("shopName = " + shopName);
            response.getWriter().println("maxCartQuantity = " + maxQuantity);
        } catch (NamingException e) {
            throw new ServletException("JNDI lookup failed", e);
        }
    }
}