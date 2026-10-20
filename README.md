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
<img width="869" height="466" alt="Screenshot 2026-10-18 180634" src="https://github.com/user-attachments/assets/fa2a5aeb-925a-4e8d-b8bd-3f9c7e5f95e7" />
<img width="933" height="441" alt="Screenshot 2026-10-18 180251" src="https://github.com/user-attachments/assets/938ec9d6-26bf-4ee3-9f23-b6eccbfa5d4e" />
<img width="555" height="284" alt="Screenshot 2026-10-18 181536" src="https://github.com/user-attachments/assets/85c2fe8c-e66f-4001-a3d3-04a95b76e894" />
<img width="430" height="392" alt="Screenshot 2026-10-18 182643" src="https://github.com/user-attachments/assets/c95e59a6-4499-4def-b021-a33d26a3b911" />
<img width="934" height="311" alt="Screenshot 2026-10-18 182913" src="https://github.com/user-attachments/assets/f2b39f38-6a5b-419f-987f-5a70007395c5" />
<img width="910" height="422" alt="Screenshot 2026-10-18 183105" src="https://github.com/user-attachments/assets/2f92be7b-90e0-40be-8551-e5dec74fb996" />
<img width="917" height="252" alt="Screenshot 2026-10-18 183236" src="https://github.com/user-attachments/assets/230e8ec7-3eb8-4a8b-a8bc-20dca32a8573" />
<img width="910" height="272" alt="Screenshot 2026-10-18 183152" src="https://github.com/user-attachments/assets/f067ad74-6910-46c3-b07a-c543587cb9ef" />
<img width="943" height="290" alt="Screenshot 2026-10-18 182933" src="https://github.com/user-attachments/assets/7155f717-0562-49bd-b7c9-cdc772dc13bc" />
<img width="928" height="431" alt="Screenshot 2026-10-18 183304" src="https://github.com/user-attachments/assets/387dbe74-f4f1-4156-9cd6-994de85211a7" />
<img width="794" height="277" alt="Screenshot 2026-10-18 184105" src="https://github.com/user-attachments/assets/6a613b0b-653a-4ff3-85d8-3168bfd2262c" />
<img width="912" height="253" alt="Screenshot 2026-10-18 184955" src="https://github.com/user-attachments/assets/4c30b0e0-314d-4946-9dfa-29dbe41c450a" />
<img width="656" height="324" alt="Screenshot 2026-10-18 185847" src="https://github.com/user-attachments/assets/d802421c-273f-4ff8-bae7-7af2917d2320" />
<img width="905" height="362" alt="Screenshot 2026-10-18 194827" src="https://github.com/user-attachments/assets/6b9a6897-afed-4c09-ac9a-49a017fe7191" />
<img width="933" height="250" alt="Screenshot 2026-10-18 195244" src="https://github.com/user-attachments/assets/c9964fd8-19cf-4068-92b6-c46bc7cfd57e" />



