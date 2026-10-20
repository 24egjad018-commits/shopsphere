<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="ShopSphere - Login"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-4">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h2 class="h4 mb-3"><fmt:message key="nav.login"/></h2>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger"><c:out value="${error}"/></div>
                    </c:if>

                    <form method="post" action="${pageContext.request.contextPath}/login">
                        <div class="mb-3">
                            <label class="form-label" for="email">Email</label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="<c:out value='${email}'/>" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="password">Password</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100"><fmt:message key="nav.login"/></button>
                    </form>

                    <p class="mt-3 mb-0 text-center">
                        <a href="${pageContext.request.contextPath}/register"><fmt:message key="nav.register"/></a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="fragments/footer.jsp" %>