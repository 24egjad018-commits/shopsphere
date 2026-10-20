package com.shopsphere.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {
        "/cart", "/cart.jsp",
        "/checkout", "/checkout.jsp", "/order-success.jsp",
        "/orders", "/orders.jsp", "/order", "/order-details.jsp",
        "/profile", "/profile.jsp",
        "/wishlist", "/wishlist.jsp"
})
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (loggedIn) {
            chain.doFilter(req, res);   // allow the request to continue
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}