<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>ShopSphere - Manage Products</title>
</head>
<body>
    <h1>Manage Products</h1>
    <p><a href="${pageContext.request.contextPath}/admin/dashboard.jsp">Dashboard</a> |
       <a href="${pageContext.request.contextPath}/logout">Logout</a></p>

    <c:if test="${param.added == '1'}">
        <p style="color:green;">Product added successfully.</p>
    </c:if>
     <c:if test="${param.updated == '1'}">
        <p style="color:green;">Product updated successfully.</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color:red;"><c:out value="${error}"/></p>
    </c:if>

    <h3>Add a new product</h3>
    <form method="post" action="${pageContext.request.contextPath}/admin/products">
        <p>
            Name:<br>
            <input type="text" name="name" size="40" value="<c:out value='${fName}'/>" required>
        </p>
        <p>
            Brand:<br>
            <input type="text" name="brand" size="40" value="<c:out value='${fBrand}'/>">
        </p>
        <p>
            Description:<br>
            <textarea name="description" rows="3" cols="40"><c:out value="${fDescription}"/></textarea>
        </p>
        <p>
            Category:<br>
            <select name="categoryId" required>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat.categoryId}" ${cat.categoryId == fCategoryId ? 'selected' : ''}>
                        <c:out value="${cat.name}"/>
                    </option>
                </c:forEach>
            </select>
        </p>
        <p>
            Price (₹):<br>
            <input type="number" name="price" step="0.01" min="0.01" value="<c:out value='${fPrice}'/>" required>
        </p>
        <p>
            Discount (%):<br>
            <input type="number" name="discount" step="0.01" min="0" max="100" value="<c:out value='${empty fDiscount ? 0 : fDiscount}'/>" required>
        </p>
        <p>
            Stock:<br>
            <input type="number" name="stock" min="0" value="<c:out value='${empty fStock ? 0 : fStock}'/>" required>
        </p>
        <button type="submit">Add Product</button>
    </form>

    <h3>All products</h3>
       <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Brand</th>
            <th>Price</th>
            <th>Discount</th>
            <th>Stock</th>
            <th>Visible</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="p" items="${products}">
            <tr>
                <td>${p.productId}</td>
                <td><c:out value="${p.name}"/></td>
                <td><c:out value="${p.brand}"/></td>
                <td>₹<fmt:formatNumber value="${p.price}" minFractionDigits="2" maxFractionDigits="2"/></td>
                <td>${p.discount}%</td>
                <td>${p.stock}</td>
                <td>${p.status ? 'Yes' : 'No'}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/product-edit?id=${p.productId}">Edit</a>
                    <form method="post" action="${pageContext.request.contextPath}/admin/product-edit" style="display:inline;">
                        <input type="hidden" name="action" value="toggle">
                        <input type="hidden" name="id" value="${p.productId}">
                        <button type="submit">${p.status ? 'Hide' : 'Show'}</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>