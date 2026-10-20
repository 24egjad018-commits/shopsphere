<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="ShopSphere - Order placed"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card shadow-sm text-center">
                <div class="card-body p-5">
                    <h1 class="h3 text-success mb-3">Thank you!</h1>
                    <p class="lead">Your order #<c:out value="${param.id}"/> has been placed.</p>
                    <a href="${pageContext.request.contextPath}/order?id=<c:out value='${param.id}'/>" class="btn btn-primary me-2">View order details</a>
                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-primary me-2">My orders</a>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary">Continue shopping</a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="fragments/footer.jsp" %>