<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="ShopSphere - Cart"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<div class="container my-4">
    <h1 class="h3 mb-4">My Cart</h1>

    <c:choose>
        <c:when test="${empty items}">
            <div class="alert alert-info">
                Your cart is empty.
                <a href="${pageContext.request.contextPath}/products" class="alert-link">Continue shopping</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="table align-middle">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Discount</th>
                            <th>Quantity</th>
                            <th>Total</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${items}">
                            <tr>
                                <td><c:out value="${item.productName}"/></td>
                                <td><fmt:formatNumber value="${item.price}" type="currency" currencyCode="INR"/></td>
                                <td>${item.discount}%</td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/cart" class="d-flex gap-2">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="productId" value="${item.productId}">
                                        <input type="number" name="quantity" value="${item.quantity}" min="1"
                                               class="form-control form-control-sm" style="width:80px;">
                                        <button type="submit" class="btn btn-sm btn-outline-primary">Update</button>
                                    </form>
                                </td>
                                <td><fmt:formatNumber value="${item.lineTotal}" type="currency" currencyCode="INR"/></td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/cart">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="productId" value="${item.productId}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger">Remove</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="d-flex justify-content-between align-items-center mt-3">
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary">Continue shopping</a>
                <div class="text-end">
                    <div class="h5 mb-2">
                        Grand total: <fmt:formatNumber value="${total}" type="currency" currencyCode="INR"/>
                    </div>
                    <a href="${pageContext.request.contextPath}/checkout" class="btn btn-success">Proceed to Checkout</a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="fragments/footer.jsp" %>