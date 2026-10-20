<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="x" uri="jakarta.tags.xml" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Syllabus demo - JSTL XML</title>
</head>
<body>
    <h1>JSTL XML: product feed</h1>

    <%-- 1. A small product feed, kept as text in a variable --%>
    <c:set var="feedText">
        <feed shop="ShopSphere">
            <product id="1" category="Mobiles">
                <name>Galaxy A15</name>
                <price>15999</price>
                <stock>23</stock>
            </product>
            <product id="2" category="Mobiles">
                <name>Redmi Note 13</name>
                <price>18999</price>
                <stock>40</stock>
            </product>
            <product id="3" category="Books">
                <name>Java: The Complete Reference</name>
                <price>899</price>
                <stock>60</stock>
            </product>
            <product id="4" category="Clothing">
                <name>Cotton T-Shirt</name>
                <price>499</price>
                <stock>98</stock>
            </product>
        </feed>
    </c:set>

    <%-- 2. Turn the text into an XML document --%>
    <x:parse var="feed" doc="${feedText}"/>

    <%-- 3. Pick values out of it with XPath --%>
    <h2>Shop: <x:out select="$feed/feed/@shop"/></h2>
    <p>Number of products: <x:out select="count($feed/feed/product)"/></p>
    <p>Total stock: <x:out select="sum($feed/feed/product/stock)"/></p>
    <p>Product with id 3: <x:out select="$feed/feed/product[@id='3']/name"/></p>

    <%-- 4. Loop over the products --%>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Type</th>
        </tr>
        <x:forEach select="$feed/feed/product">
            <tr>
                <td><x:out select="@id"/></td>
                <td><x:out select="name"/></td>
                <td><x:out select="@category"/></td>
                <td><x:out select="price"/></td>
                <td>
                    <x:out select="stock"/>
                    <x:if select="stock > 50"> (plenty)</x:if>
                </td>
                <td>
                    <x:choose>
                        <x:when select="price > 10000">Premium</x:when>
                        <x:otherwise>Budget</x:otherwise>
                    </x:choose>
                </td>
            </tr>
        </x:forEach>
    </table>
</body>
</html>