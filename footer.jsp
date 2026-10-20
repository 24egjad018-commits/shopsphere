<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>
<div class="container">
    <footer class="border-top mt-5 py-3 text-center text-muted small">
        <p class="mb-0">&copy; ShopSphere - <fmt:message key="footer.text"/> |
           <fmt:message key="footer.date"/>: <fmt:formatDate value="${now}" dateStyle="long"/></p>
    </footer>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>