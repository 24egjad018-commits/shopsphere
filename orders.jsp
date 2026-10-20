<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>ShopSphere - Manage Orders</title>
</head>
<body>
    <h1>Manage Orders</h1>
    <p><a href="${pageContext.request.contextPath}/admin/dashboard.jsp">Dashboard</a> |
       <a href="${pageContext.request.contextPath}/admin/products">Manage Products</a> |
       <a href="${pageContext.request.contextPath}/logout">Logout</a></p>

    <c:if test="${param.updated == '1'}">
        <p style="color:green;">Order status updated.</p>
    </c:if>

    <c:choose>
        <c:when test="${empty orders}">
            <p>No orders yet.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="8" cellspacing="0">
                <tr>
                    <th>Order #</th>
                    <th>Date</th>
                    <th>Customer</th>
                    <th>Address</th>
                    <th>Total</th>
                    <th>Payment</th>
                    <th>Status</th>
                </tr>
                <c:forEach var="o" items="${orders}">
                    <tr>
                        <td>${o.orderId}</td>
                        <td><fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></td>
                        <td><c:out value="${o.customerName}"/></td>
                        <td><c:out value="${o.shippingAddress}"/></td>
                        <td>₹<fmt:formatNumber value="${o.totalAmount}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td><c:out value="${o.paymentMethod}"/> (<c:out value="${o.paymentStatus}"/>)</td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/admin/orders">
                                <input type="hidden" name="orderId" value="${o.orderId}">
                                <select name="status">
                                    <option value="PLACED" ${o.orderStatus == 'PLACED' ? 'selected' : ''}>PLACED</option>
                                    <option value="SHIPPED" ${o.orderStatus == 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                    <option value="DELIVERED" ${o.orderStatus == 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                </select>
                                <button type="submit">Update</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>