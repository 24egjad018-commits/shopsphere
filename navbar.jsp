<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/products">
            <c:out value="${applicationScope.appName}" default="ShopSphere"/>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#mainNav" aria-controls="mainNav" aria-expanded="false"
                aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/products"><fmt:message key="nav.products"/></a>
                </li>
                <c:if test="${not empty sessionScope.user}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/cart"><fmt:message key="nav.cart"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/orders"><fmt:message key="nav.orders"/></a>
                    </li>
                    <c:if test="${sessionScope.role == 'ADMIN'}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard.jsp"><fmt:message key="nav.admin"/></a>
                        </li>
                    </c:if>
                </c:if>
            </ul>

            <ul class="navbar-nav align-items-lg-center">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item">
                            <span class="navbar-text me-lg-3">
                                <fmt:message key="nav.welcome"/>, <c:out value="${sessionScope.user.name}"/>
                            </span>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/logout"><fmt:message key="nav.logout"/></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login"><fmt:message key="nav.login"/></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/register"><fmt:message key="nav.register"/></a>
                        </li>
                    </c:otherwise>
                </c:choose>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/lang?lang=en"><fmt:message key="lang.english"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/lang?lang=hi"><fmt:message key="lang.hindi"/></a>
                </li>
            </ul>
        </div>
    </div>
</nav>