<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>ShopSphere - Edit Product</title>
</head>
<body>
    <h1>Edit Product #${product.productId}</h1>
    <p><a href="${pageContext.request.contextPath}/admin/products">Back to products</a></p>

    <c:if test="${not empty error}">
        <p style="color:red;"><c:out value="${error}"/></p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/admin/product-edit">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="id" value="${product.productId}">
        <p>
            Name:<br>
            <input type="text" name="name" size="40" value="<c:out value='${product.name}'/>" required>
        </p>
        <p>
            Brand:<br>
            <input type="text" name="brand" size="40" value="<c:out value='${product.brand}'/>">
        </p>
        <p>
            Description:<br>
            <textarea name="description" rows="3" cols="40"><c:out value="${product.description}"/></textarea>
        </p>
        <p>
            Category:<br>
            <select name="categoryId" required>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat.categoryId}" ${cat.categoryId == product.categoryId ? 'selected' : ''}>
                        <c:out value="${cat.name}"/>
                    </option>
                </c:forEach>
            </select>
        </p>
        <p>
            Price (₹):<br>
            <input type="number" name="price" step="0.01" min="0.01" value="${product.price}" required>
        </p>
        <p>
            Discount (%):<br>
            <input type="number" name="discount" step="0.01" min="0" max="100" value="${product.discount}" required>
        </p>
        <p>
            Stock:<br>
            <input type="number" name="stock" min="0" value="${product.stock}" required>
        </p>
        <button type="submit">Save Changes</button>
    </form>
</body>
</html>