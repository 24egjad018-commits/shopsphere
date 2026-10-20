<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="ShopSphere - Order ${order.orderId}"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<c:choose>
    <c:when test="${order.orderStatus == 'DELIVERED'}">
        <c:set var="pct" value="100"/>
        <c:set var="barClass" value="bg-success"/>
    </c:when>
    <c:when test="${order.orderStatus == 'SHIPPED'}">
        <c:set var="pct" value="66"/>
        <c:set var="barClass" value="bg-warning"/>
    </c:when>
    <c:otherwise>
        <c:set var="pct" value="33"/>
        <c:set var="barClass" value="bg-primary"/>
    </c:otherwise>
</c:choose>

<div class="container my-4">
    <a href="${pageContext.request.contextPath}/orders" class="btn btn-link ps-0">&larr; My Orders</a>
    <h1 class="h3 mb-3">Order #${order.orderId}</h1>

    <div class="card shadow-sm mb-4">
        <div class="card-body">
            <div class="progress" role="progressbar" aria-valuenow="${pct}" aria-valuemin="0" aria-valuemax="100">
                <div class="progress-bar ${barClass}" style="width: ${pct}%">
                    <c:out value="${order.orderStatus}"/>
                </div>
            </div>
            <div class="d-flex justify-content-between small text-muted mt-1">
                <span>PLACED</span>
                <span>SHIPPED</span>
                <span>DELIVERED</span>
            </div>
        </div>
    </div>

    <div class="row g-4">
        <div class="col-lg-7">
            <div class="card shadow-sm">
                <div class="card-header">Items</div>
                <div class="table-responsive">
                    <table class="table mb-0 align-middle">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${items}">
                                <tr>
                                    <td><c:out value="${item.productName}"/></td>
                                    <td><fmt:formatNumber value="${item.price}" type="currency" currencyCode="INR"/></td>
                                    <td>${item.quantity}</td>
                                    <td><fmt:formatNumber value="${item.lineTotal}" type="currency" currencyCode="INR"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot>
                            <tr class="fw-bold">
                                <td colspan="3" class="text-end">Order total</td>
                                <td><fmt:formatNumber value="${order.totalAmount}" type="currency" currencyCode="INR"/></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>

        <div class="col-lg-5">
            <div class="card shadow-sm">
                <div class="card-header">Details</div>
                <ul class="list-group list-group-flush">
                    <li class="list-group-item">
                        <strong>Date:</strong>
                        <fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy, hh:mm a"/>
                    </li>
                    <li class="list-group-item">
                        <strong>Payment:</strong>
                        <c:out value="${order.paymentMethod}"/> (<c:out value="${order.paymentStatus}"/>)
                    </li>
                    <li class="list-group-item">
                        <strong>Shipping address:</strong><br>
                        <c:out value="${order.shippingAddress}"/>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<%@ include file="fragments/footer.jsp" %>