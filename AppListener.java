package com.shopsphere.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        context.setAttribute("appName", "ShopSphere");
        System.out.println("ShopSphere started. appName saved in ServletContext.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("ShopSphere is shutting down.");
    }
}