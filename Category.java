package com.shopsphere.model;

import java.io.Serializable;

public class Category implements Serializable {

    private int categoryId;
    private String name;

    public Category() {
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}