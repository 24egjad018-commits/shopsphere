<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="uiLang" value="${cookie.lang.value == 'hi' ? 'hi' : 'en'}" scope="request"/>
<fmt:setLocale value="${uiLang}_IN" scope="request"/>
<fmt:setBundle basename="messages" scope="request"/>
<!DOCTYPE html>
<html lang="${uiLang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${pageTitle}" default="ShopSphere"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>