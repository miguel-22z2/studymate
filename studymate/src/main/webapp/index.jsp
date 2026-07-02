<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Se já estiver logado, vai direto pro dashboard
    if (session.getAttribute("usuarioLogado") != null) {
        response.sendRedirect(request.getContextPath() + "/app/dashboard");
    } else {
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>
