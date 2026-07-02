<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body>

<div class="auth-container">

    <div class="auth-card">

        <div class="auth-logo">
            <h1>📚 StudyMate</h1>
            <p>Crie sua conta gratuitamente</p>
        </div>

        <%-- Mensagem de erro --%>
        <% if (request.getAttribute("erro") != null) { %>
            <div class="alert alert-erro">
                <%= request.getAttribute("erro") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/cadastro" method="post">

            <div class="campo">
                <label for="nome">Nome completo</label>
                <input type="text" id="nome" name="nome"
                       placeholder="Seu nome"
                       value="<%= request.getAttribute("nome") != null ? request.getAttribute("nome") : "" %>"
                       required autofocus>
            </div>

            <div class="campo">
                <label for="email">E-mail</label>
                <input type="email" id="email" name="email"
                       placeholder="seu@email.com"
                       value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>"
                       required>
            </div>

            <div class="campo">
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha"
                       placeholder="Mínimo 6 caracteres" required>
            </div>

            <div class="campo">
                <label for="confirmarSenha">Confirmar senha</label>
                <input type="password" id="confirmarSenha" name="confirmarSenha"
                       placeholder="Repita a senha" required>
            </div>

            <button type="submit" class="btn-primario">Criar conta</button>

        </form>

        <div class="auth-rodape">
            <p>Já tem conta? <a href="${pageContext.request.contextPath}/login">Faça login</a></p>
        </div>

    </div>

</div>

</body>
</html>
