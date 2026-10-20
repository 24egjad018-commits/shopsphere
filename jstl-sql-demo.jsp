<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Syllabus demo - JSTL SQL</title>
</head>
<body>
    <h1>JSTL SQL (syllabus demo)</h1>
    <p>Admin only. The real ShopSphere pages use Servlet, DAO and JDBC instead of this.</p>
    <p><a href="${pageContext.request.contextPath}/admin/dashboard.jsp">Dashboard</a></p>

    <%-- 1. Connection settings (demo only) --%>
    <sql:setDataSource var="db"
        driver="com.mysql.cj.jdbc.Driver"
        url="jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata"
        user="YOUR_MYSQL_PASSWORD"
        password="YOUR_MYSQL_PASSWORD"/>

    <%-- 2. A query with a join: all products with their category name --%>
    <sql:query var="all" dataSource="${db}">
        SELECT p.product_id, p.name, c.name AS category_name, p.price, p.stock
        FROM products p JOIN categories c ON p.category_id = c.category_id
        ORDER BY p.product_id
    </sql:query>

    <h2>All products (${all.rowCount} rows)</h2>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
        </tr>
        <c:forEach var="row" items="${all.rows}">
            <tr>
                <td>${row.product_id}</td>
                <td><c:out value="${row.name}"/></td>
                <td><c:out value="${row.category_name}"/></td>
                <td>₹<fmt:formatNumber value="${row.price}" minFractionDigits="2" maxFractionDigits="2"/></td>
                <td>${row.stock}</td>
            </tr>
        </c:forEach>
    </table>

    <%-- 3. A search with a parameter: the ? is filled in safely by sql:param --%>
    <h2>Search by name</h2>
    <form method="get" action="${pageContext.request.contextPath}/admin/jstl-sql-demo.jsp">
        <input type="text" name="q" value="<c:out value='${param.q}'/>">
        <button type="submit">Search</button>
    </form>

    <c:if test="${not empty param.q}">
        <c:set var="pattern" value="%${param.q}%"/>
        <sql:query var="found" dataSource="${db}">
            SELECT product_id, name, price FROM products WHERE name LIKE ?
            <sql:param value="${pattern}"/>
        </sql:query>

        <p>Found ${found.rowCount} product(s) for "<c:out value="${param.q}"/>".</p>
        <ul>
            <c:forEach var="row" items="${found.rows}">
                <li><c:out value="${row.name}"/> - ₹<fmt:formatNumber value="${row.price}" minFractionDigits="2" maxFractionDigits="2"/></li>
            </c:forEach>
        </ul>
    </c:if>
</body>
</html>