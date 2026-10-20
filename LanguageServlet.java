package com.shopsphere.controller;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/lang")
public class LanguageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String lang = request.getParameter("lang");

        // Only two languages are accepted
        if ("en".equals(lang) || "hi".equals(lang)) {
            Cookie cookie = new Cookie("lang", lang);
            cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
            cookie.setMaxAge(30 * 24 * 60 * 60);   // remember for 30 days
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }

        response.sendRedirect(request.getContextPath() + "/products");
    }
}