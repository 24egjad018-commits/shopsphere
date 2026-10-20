<%@ tag description="Shows one product in a card" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="shop" uri="https://shopsphere.local/tags" %>
<%@ attribute name="product" required="true" type="com.shopsphere.model.Product" %>

<c:set var="finalPrice" value="${product.price - (product.price * product.discount / 100)}"/>

<div class="col">
    <div class="card h-100 shadow-sm">
        <div class="card-body d-flex flex-column">
             <h5 class="card-title">
                <a href="${pageContext.request.contextPath}/product?id=${product.productId}"
                   class="text-decoration-none text-dark"><c:out value="${product.name}"/></a>
            </h5>
            <h6 class="card-subtitle mb-3 text-muted">
                <fmt:message key="product.brand"/>: <c:out value="${product.brand}"/>
            </h6>

            <p class="mb-3">
                <span class="fs-5 fw-bold">
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
            <p class="mb-2"><shop:stockBadge stock="${product.stock}"/></p>
            <c:choose>
                <c:when test="${product.stock > 0}">
                    <form method="post" action="${pageContext.request.contextPath}/cart" class="mt-auto">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <button type="submit" class="btn btn-primary w-100">
                            <fmt:message key="product.add"/>
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                    <button type="button" class="btn btn-secondary w-100 mt-auto" disabled>
                        <fmt:message key="product.outofstock"/>
                    </button>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>