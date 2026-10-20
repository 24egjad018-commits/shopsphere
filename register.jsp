<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="ShopSphere - Register"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-5">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h2 class="h4 mb-3"><fmt:message key="nav.register"/></h2>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger"><c:out value="${error}"/></div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="alert alert-success">
                            <c:out value="${success}"/>
                            <a href="${pageContext.request.contextPath}/login" class="alert-link"><fmt:message key="nav.login"/></a>
                        </div>
                    </c:if>

                    <form method="post" action="${pageContext.request.contextPath}/register">
                        <div class="mb-3">
                            <label class="form-label" for="name">Name</label>
                            <input type="text" class="form-control" id="name" name="name"
                                   value="<c:out value='${name}'/>" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="email">Email</label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="<c:out value='${email}'/>" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="mobile">Mobile (10 digits)</label>
                            <input type="text" class="form-control" id="mobile" name="mobile"
                                   value="<c:out value='${mobile}'/>">
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="password">Password (min 6 characters)</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100"><fmt:message key="nav.register"/></button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="fragments/footer.jsp" %>