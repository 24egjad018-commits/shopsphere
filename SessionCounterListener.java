package com.shopsphere.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionCounterListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        ServletContext context = event.getSession().getServletContext();
        synchronized (context) {
            Integer count = (Integer) context.getAttribute("activeSessions");
            context.setAttribute("activeSessions", (count == null ? 0 : count) + 1);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        ServletContext context = event.getSession().getServletContext();
        synchronized (context) {
            Integer count = (Integer) context.getAttribute("activeSessions");
            context.setAttribute("activeSessions", (count == null || count <= 0) ? 0 : count - 1);
        }
    }
}