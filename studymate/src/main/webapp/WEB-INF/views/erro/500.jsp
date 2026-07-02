<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erro interno — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/erro.css">
</head>
<body>
<div class="erro-container">
    <div class="erro-card">
        <div class="erro-codigo erro-500">500</div>
        <h2>Erro interno do servidor</h2>
        <p>Algo deu errado no servidor. Tente novamente em alguns instantes.</p>
        <div class="erro-acoes">
            <a href="${pageContext.request.contextPath}/app/dashboard" class="btn-primario">🏠 Ir para o Dashboard</a>
            <a href="javascript:history.back()" class="btn-secundario">← Voltar</a>
        </div>
    </div>
</div>
</body>
</html>
