<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="shop" uri="https://shopsphere.local/tags" %>
<c:set var="pageTitle" value="${product.name} - ShopSphere"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<c:set var="finalPrice" value="${product.price - (product.price * product.discount / 100)}"/>

<div class="container my-4">
    <a href="${pageContext.request.contextPath}/products" class="btn btn-link ps-0">
        &larr; <fmt:message key="product.back"/>
    </a>

    <div class="card shadow-sm mt-2">
        <div class="card-body p-4">
            <h1 class="h3"><c:out value="${product.name}"/></h1>
            <p class="text-muted">
                <fmt:message key="product.brand"/>: <c:out value="${product.brand}"/>
            </p>

            <p class="mb-3">
                <span class="fs-3 fw-bold">
                    <fmt:formatNumber value="${finalPrice}" type="currency" currencyCode="INR"/>
                </span>
                <c:if test="${product.discount > 0}">
                    <s class="text-muted ms-2">
                        <fmt:formatNumber value="${product.price}" type="currency" currencyCode="INR"/>
                    </s>
                    <span class="badge bg-success ms-2">
                        <fmt:formatNumber value="${product.discount}" maxFractionDigits="1"/>% <fmt:message key="product.discount"/>
                    </span>
                </c:if>
            </p>

            <p><shop:stockBadge stock="${product.stock}"/></p>

            <h2 class="h6 mt-4"><fmt:message key="product.description"/></h2>
            <p><c:out value="${product.description}"/></p>

            <c:choose>
                <c:when test="${product.stock > 0}">
                    <form method="post" action="${pageContext.request.contextPath}/cart">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <button type="submit" class="btn btn-primary">
                            <fmt:message key="product.add"/>
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                    <button type="button" class="btn btn-secondary" disabled>
                        <fmt:message key="product.outofstock"/>
                    </button>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="fragments/footer.jsp" %>