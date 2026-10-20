<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>ShopSphere - Admin Dashboard</title>
</head>
<body>
    <h1>Admin Dashboard</h1>
    <p>Welcome, <c:out value="${sessionScope.user.name}"/> (<c:out value="${sessionScope.role}"/>)</p>
       <p>Active sessions: ${applicationScope.activeSessions}</p>
     <p><a href="${pageContext.request.contextPath}/admin/products">Manage Products</a> |
       <a href="${pageContext.request.contextPath}/admin/orders">Manage Orders</a></p>
    <p><a href="${pageContext.request.contextPath}/products">View store</a> |
       <a href="${pageContext.request.contextPath}/logout">Logout</a></p>
</body>
</html>