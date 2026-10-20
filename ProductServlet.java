package com.shopsphere.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/products",
        initParams = @WebInitParam(name = "tagline", value = "Best prices, fast delivery"))
public class ProductServlet extends HttpServlet {

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private String tagline;

    @Override
    public void init() {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        // ServletConfig: a value given only to this servlet
        tagline = getServletConfig().getInitParameter("tagline");
        System.out.println("ProductServlet started. tagline = " + tagline);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Search word, category and sort order chosen on the page
        String q = request.getParameter("q");
        q = (q == null) ? "" : q.trim();
        if (q.length() > 100) {
            q = q.substring(0, 100);
        }

        int categoryId = toInt(request.getParameter("category"), 0);

        String sort = request.getParameter("sort");
        sort = (sort == null) ? "" : sort;

        try {
            List<Product> products = productDAO.findProducts(q, categoryId, sort);

            request.setAttribute("products", products);
            request.setAttribute("categories", categoryDAO.getAllCategories());
            request.setAttribute("q", q);
            request.setAttribute("categoryId", categoryId);
            request.setAttribute("sort", sort);
            request.setAttribute("tagline", tagline);
            request.getRequestDispatcher("/products.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Could not load products", e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("ProductServlet is being destroyed.");
    }

    private int toInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}