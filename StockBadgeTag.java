package com.shopsphere.tags;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;

public class StockBadgeTag extends SimpleTagSupport {

    private int stock;

    // Called by Tomcat with the value of the "stock" attribute
    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public void doTag() throws JspException, IOException {
        String css;
        String text;

        if (stock <= 0) {
            css = "bg-danger";
            text = "Out of stock";
        } else if (stock <= 10) {
            css = "bg-warning text-dark";
            text = "Only " + stock + " left";
        } else {
            css = "bg-success";
            text = "In stock";
        }

        JspWriter out = getJspContext().getOut();
        out.print("<span class=\"badge " + css + "\">" + text + "</span>");
    }
}