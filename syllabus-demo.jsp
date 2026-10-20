<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.Date" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- JSP comment: the browser never receives this line --%>
<!-- HTML comment: this line IS visible in "View page source" -->

<%!
    // Declaration: becomes part of the servlet class, shared by ALL requests
    private int visitCount = 0;

    private synchronized int nextVisit() {
        return ++visitCount;
    }

    private String greet(String name) {
        return "Hello, " + name + "!";
    }
%>

<%
    // Scriptlet: Java code that runs on every request
    int visit = nextVisit();
    String student = "Student";
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Syllabus demo - JSP elements and JSTL functions</title>
</head>
<body>
    <h1>Traditional JSP elements</h1>

    <p>Expression: <%= greet(student) %></p>
    <p>This page has been opened <%= visit %> time(s) since the server started.</p>
    <p>Server time: <%= new Date() %></p>

    <p>Scriptlet mixed with HTML:</p>
    <ul>
        <% for (int i = 1; i <= 3; i++) { %>
            <li>Item number <%= i %></li>
        <% } %>
    </ul>

    <% out.println("<p>This line was written with out.println.</p>"); %>

    <h1>JSTL functions</h1>

    <c:set var="text" value="  Java Advanced ShopSphere Project  "/>
    <c:set var="trimmed" value="${fn:trim(text)}"/>
    <c:set var="words" value="${fn:split(trimmed, ' ')}"/>

    <p>Trimmed text: ${trimmed}</p>
    <p>Length: ${fn:length(trimmed)}</p>
    <p>Upper case: ${fn:toUpperCase(trimmed)}</p>
    <p>Lower case: ${fn:toLowerCase(trimmed)}</p>
    <p>Contains "shop" (ignoring case): ${fn:containsIgnoreCase(trimmed, 'shop')}</p>
    <p>First 4 letters: ${fn:substring(trimmed, 0, 4)}</p>
    <p>Replace: ${fn:replace(trimmed, 'Project', 'Application')}</p>
    <p>Number of words: ${fn:length(words)}</p>
    <p>Words joined with dashes: ${fn:join(words, '-')}</p>
    <p>Escaped HTML: ${fn:escapeXml('<b>bold</b>')}</p>
</body>
</html>