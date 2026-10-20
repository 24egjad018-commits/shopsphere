# ShopSphere - Java Advanced E-Commerce Project

ShopSphere is a small online shop built with Java Servlets, JSP, JDBC and MySQL.
Customers can browse and search products, keep a cart, place orders and see their
order history. Admins can manage products and update order status. The project also
contains small demonstrations of Socket programming, RMI, JNDI, Swing,
URL/URLConnection, internationalization and the JSTL XML and SQL tag libraries.

## Features

**Customer**
- Registration and login (passwords stored as BCrypt hashes, session based login)
- Product list with search by name or brand
- Shopping cart (add, change quantity, remove)
- Checkout with Cash on Delivery (one database transaction, stock check on the server)
- Order history with status badges
- English and Hindi interface (language kept in a cookie)

**Admin**
- Dashboard with active session counter
- Add, edit, hide and show products
- View all orders and change their status (PLACED, SHIPPED, DELIVERED)
- Admin pages are protected by a filter

**Extra modules**
- Socket client and server (inventory queries, object serialization)
- RMI inventory service and JNDI lookup
- Swing desktop admin (login and product table, MVC)
- URL / URLConnection demo, internationalization demo
- JSP demo pages: traditional JSP elements, JSTL functions, JSTL XML, JSTL SQL

## Technology

| Part | Version |
|---|---|
| Java | 21 or newer |
| Servlet / JSP container | Apache Tomcat 11 (Servlet 6.1) |
| JSTL | Jakarta Standard Tag Library 3.0 |
| Database | MySQL 8 with Connector/J 9.1.0 |
| Password hashing | jBCrypt 0.4 |
| Build tool | Apache Maven |
| Page styling | Bootstrap 5.3 (loaded from a CDN) |

## Requirements

- JDK 21 or newer (`java -version`)
- Apache Maven 3.9 or newer (`mvn -v`)
- MySQL Server 8 and MySQL Workbench
- Apache Tomcat 11
- Internet connection (Maven downloads libraries, Bootstrap is loaded from a CDN)

## How to run

1. **Create the database.** Open `database/shopsphere.sql` in MySQL Workbench and run
   the whole script once. It creates the `shopsphere` database, six tables and four
   sample products.

2. **Set the database password.** Open
   `web/src/main/java/com/shopsphere/util/DBConnection.java` and write your MySQL
   user and password in `USER` and `PASSWORD`. If you want to use the JSTL SQL demo,
   do the same in `web/src/main/webapp/admin/jstl-sql-demo.jsp`.

3. **Build the project.**
   ```
   cd web
   mvn clean package
   ```
   This creates `web/target/web.war`.

4. **Deploy on Tomcat.** Copy `web/target/web.war` into the `webapps` folder of your
   Tomcat 11 and start Tomcat (`bin\startup.bat`). `JAVA_HOME` must be set. In VS Code
   you can also use the Community Server Connectors extension and add the war file.

5. **Open the site:** http://localhost:8080/web/

6. **Create an admin account.** Register a normal account on the website, then run
   this in MySQL Workbench with your own email:
   ```sql
   USE shopsphere;
   UPDATE users SET role = 'ADMIN' WHERE email = 'your-email@example.com';
   ```
   Log out and log in again, because the role is saved in the session at login.

## Main pages

| Address (after `http://localhost:8080/web`) | Who can open it |
|---|---|
| `/products` , `/products?q=word` | everyone |
| `/register` , `/login` , `/logout` | everyone |
| `/cart` , `/checkout` , `/orders` | logged in users |
| `/admin/dashboard.jsp` | admin |
| `/admin/products` , `/admin/product-edit?id=1` | admin |
| `/admin/orders` | admin |
| `/lang?lang=en` , `/lang?lang=hi` | everyone |

Demo pages: `/syllabus-demo.jsp`, `/jstl-xml-demo.jsp`, `/jndi-demo`,
`/admin/jstl-sql-demo.jsp` (admin only).

## Running the extra modules

These are normal Java programs. In VS Code, open the file and click **Run** above
its `main` method. Start the server first, then the client.

| Module | Server | Client |
|---|---|---|
| Socket (port 5000) | `com.shopsphere.network.InventoryServer` | `com.shopsphere.network.InventoryClient` |
| RMI (port 1099) | `com.shopsphere.rmi.RmiInventoryServer` | `com.shopsphere.rmi.RmiInventoryClient` |
| JNDI over RMI | `com.shopsphere.rmi.RmiInventoryServer` | `com.shopsphere.rmi.JndiRmiDemo` |
| Swing admin | - | `com.shopsphere.swing.SwingAdminApp` |
| URL / URLConnection | Tomcat running | `com.shopsphere.util.UrlDemo` |
| Internationalization | - | `com.shopsphere.util.I18nDemo` |

The Socket and RMI servers have no login. They are for local demonstration only, so
stop them after use.

## Project structure

```
ShopSphere/
  README.md
  database/shopsphere.sql          database script
  diagrams/                        ER diagram and other diagrams
  web/
    pom.xml
    src/main/java/com/shopsphere/
      controller/                  servlets
      dao/                         database access (JDBC)
      filter/                      AuthenticationFilter, AdminFilter
      listener/                    AppListener, SessionCounterListener
      model/                       Product, User, CartItem, Order, Category
      network/                     Socket server and client
      rmi/                         RMI service, server, client, JNDI demo
      swing/                       Swing admin (view and controller)
      util/                        DBConnection and small demos
    src/main/resources/            messages_en / messages_hi (text for both languages)
    src/main/webapp/
      *.jsp                        customer pages
      admin/                       admin pages
      fragments/                   header, navbar, footer
      WEB-INF/tags/                productCard.tag
      WEB-INF/web.xml
```

## Security notes

- All SQL uses `PreparedStatement`, so user input cannot change a query.
- Passwords are hashed with BCrypt and never shown again.
- Prices, totals and stock are calculated and checked on the server, not in the browser.
- User input shown on a page goes through `<c:out>`.
- `AuthenticationFilter` protects cart, checkout and orders. `AdminFilter` protects
  everything under `/admin/`.
- The server creates a new session at login.

## Known limitations

- Only Cash on Delivery, there is no online payment.
- Products have no images yet.
- The language switch covers the navbar, product pages and the search box. Other
  pages are in English.
- Before using this on a real server, remove the demo pages and servers, and keep
  the database password in a configuration file outside the source code.
