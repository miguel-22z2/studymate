<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body>

<div class="auth-container">

    <div class="auth-card">

        <div class="auth-logo">
            <h1>📚 StudyMate</h1>
            <p>Seu assistente de estudos com IA</p>
        </div>

        <%-- Mensagem de sucesso após cadastro --%>
        <% if ("sucesso".equals(request.getParameter("cadastro"))) { %>
            <div class="alert alert-sucesso">
                Cadastro realizado com sucesso! Faça login para continuar.
            </div>
        <% } %>

        <%-- Mensagem de logout --%>
        <% if ("true".equals(request.getParameter("logout"))) { %>
            <div class="alert alert-info">
                Você saiu da sua conta.
            </div>
        <% } %>

        <%-- Mensagem de erro --%>
        <% if (request.getAttribute("erro") != null) { %>
            <div class="alert alert-erro">
                <%= request.getAttribute("erro") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">

            <div class="campo">
                <label for="email">E-mail</label>
                <input type="email" id="email" name="email"
                       placeholder="seu@email.com" required autofocus>
            </div>

            <div class="campo">
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha"
                       placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn-primario">Entrar</button>

        </form>

        <div class="auth-rodape">
            <p>Não tem conta? <a href="${pageContext.request.contextPath}/cadastro">Cadastre-se</a></p>
        </div>

    </div>

</div>

</body>
</html>
