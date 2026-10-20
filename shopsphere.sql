-- =====================================================================
-- ShopSphere database setup script (MySQL 8)
--
-- Run this ONCE on a fresh MySQL server. It creates:
--   1. the database "shopsphere"
--   2. the 6 tables used by the project
--   3. some sample categories and products
--
-- It does NOT create any user account. To get an admin account:
--   1. Start the website and register a normal account.
--   2. Run (with your own email):
--        UPDATE users SET role = 'ADMIN' WHERE email = 'your-email@example.com';
--   3. Log out and log in again.
-- =====================================================================

CREATE DATABASE IF NOT EXISTS shopsphere;
USE shopsphere;

-- ---------------------------------------------------------------------
-- Users (customers and admins). Passwords are stored as BCrypt hashes.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    mobile VARCHAR(15),
    role VARCHAR(10) NOT NULL DEFAULT 'CUSTOMER',
    status BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- Product categories
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    status BOOLEAN NOT NULL DEFAULT TRUE
);

-- ---------------------------------------------------------------------
-- Products. "discount" is a percentage. "status" FALSE means hidden.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    brand VARCHAR(100),
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    discount DECIMAL(5,2) NOT NULL DEFAULT 0,
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(255),
    status BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- ---------------------------------------------------------------------
-- Shopping cart: one row per user and product
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    UNIQUE (user_id, product_id)
);

-- ---------------------------------------------------------------------
-- Orders and their items
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    shipping_address VARCHAR(255) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL DEFAULT 'COD',
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    order_status VARCHAR(20) NOT NULL DEFAULT 'PLACED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- ---------------------------------------------------------------------
-- Sample data (run once on an empty database)
-- ---------------------------------------------------------------------
INSERT INTO categories (name, description) VALUES
('Mobiles', 'Smartphones and accessories'),
('Books', 'Study and story books'),
('Clothing', 'Men and women clothing');

INSERT INTO products (category_id, name, brand, description, price, discount, stock) VALUES
((SELECT category_id FROM categories WHERE name = 'Mobiles'),  'Galaxy A15', 'Samsung', '6GB RAM, 128GB storage', 15999.00, 10, 25),
((SELECT category_id FROM categories WHERE name = 'Mobiles'),  'Redmi Note 13', 'Xiaomi', '8GB RAM, 256GB storage', 18999.00, 5, 40),
((SELECT category_id FROM categories WHERE name = 'Books'),    'Java: The Complete Reference', 'McGraw Hill', 'Beginner to advanced Java', 899.00, 15, 60),
((SELECT category_id FROM categories WHERE name = 'Clothing'), 'Cotton T-Shirt', 'Roadster', 'Round neck, regular fit', 499.00, 20, 100);
