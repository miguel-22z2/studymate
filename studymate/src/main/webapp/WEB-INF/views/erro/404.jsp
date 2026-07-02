<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Página não encontrada — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/erro.css">
</head>
<body>
<div class="erro-container">
    <div class="erro-card">
        <div class="erro-codigo">404</div>
        <h2>Página não encontrada</h2>
        <p>A página que você tentou acessar não existe ou foi movida.</p>
        <div class="erro-acoes">
            <a href="${pageContext.request.contextPath}/app/dashboard" class="btn-primario">🏠 Ir para o Dashboard</a>
            <a href="javascript:history.back()" class="btn-secundario">← Voltar</a>
        </div>
    </div>
</div>
</body>
</html>
