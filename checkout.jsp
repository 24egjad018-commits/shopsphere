<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="ShopSphere - Checkout"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<div class="container my-4">
    <h1 class="h3 mb-4">Checkout</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger"><c:out value="${error}"/></div>
    </c:if>

    <div class="row g-4">
        <div class="col-lg-7">
            <div class="card shadow-sm">
                <div class="card-header">Order summary</div>
                <ul class="list-group list-group-flush">
                    <c:forEach var="item" items="${items}">
                        <li class="list-group-item d-flex justify-content-between">
                            <span><c:out value="${item.productName}"/> &times; ${item.quantity}</span>
                            <span><fmt:formatNumber value="${item.lineTotal}" type="currency" currencyCode="INR"/></span>
                        </li>
                    </c:forEach>
                    <li class="list-group-item d-flex justify-content-between fw-bold">
                        <span>Grand total</span>
                        <span><fmt:formatNumber value="${total}" type="currency" currencyCode="INR"/></span>
                    </li>
                </ul>
            </div>
        </div>

        <div class="col-lg-5">
            <div class="card shadow-sm">
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/checkout">
                        <div class="mb-3">
                            <label class="form-label" for="address">Shipping address</label>
                            <textarea class="form-control" id="address" name="address" rows="4" required><c:out value="${address}"/></textarea>
                        </div>
                        <p class="text-muted">Payment: Cash on Delivery</p>
                        <button type="submit" class="btn btn-success w-100">Place Order</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-link w-100 mt-2">Back to cart</a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="fragments/footer.jsp" %>