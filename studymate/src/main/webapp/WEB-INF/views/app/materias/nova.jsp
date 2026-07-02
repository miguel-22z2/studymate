<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova Matéria — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>

<%@ include file="../_navbar.jsp" %>

<div class="container">

    <div class="page-header">
        <div>
            <h2>➕ Nova Matéria</h2>
            <p class="subtitulo">Adicione uma nova matéria para estudar</p>
        </div>
        <a href="${pageContext.request.contextPath}/app/materias" class="btn-secundario">
            ← Voltar
        </a>
    </div>

    <% if (request.getAttribute("erro") != null) { %>
        <div class="alert alert-erro"><%= request.getAttribute("erro") %></div>
    <% } %>

    <div class="form-card">
        <form action="${pageContext.request.contextPath}/app/materias" method="post">
            <input type="hidden" name="acao" value="criar">

            <div class="campo">
                <label for="nome">Nome da matéria *</label>
                <input type="text" id="nome" name="nome"
                       placeholder="Ex: Programação Orientada a Objetos"
                       value="${param.nome}" required autofocus>
            </div>

            <div class="campo">
                <label for="descricao">Descrição <span class="opcional">(opcional)</span></label>
                <textarea id="descricao" name="descricao" rows="4"
                          placeholder="Descreva brevemente o conteúdo desta matéria...">${param.descricao}</textarea>
            </div>

            <div class="form-acoes">
                <a href="${pageContext.request.contextPath}/app/materias" class="btn-secundario">Cancelar</a>
                <button type="submit" class="btn-primario">Salvar Matéria</button>
            </div>

        </form>
    </div>

</div>

</body>
</html>
