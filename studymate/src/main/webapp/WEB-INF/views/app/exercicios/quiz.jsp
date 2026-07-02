<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.studymate.model.Exercicio" %>
<%
    Exercicio exercicio = (Exercicio) request.getAttribute("exercicio");
    int materiaId = (int) request.getAttribute("materiaId");
    int totalQuestoes = request.getSession().getAttribute("totalQuestoes") != null
        ? (int) request.getSession().getAttribute("totalQuestoes") : 0;
    int totalAcertos = request.getSession().getAttribute("totalAcertos") != null
        ? (int) request.getSession().getAttribute("totalAcertos") : 0;
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/exercicios.css">
</head>
<body>

<%@ include file="../_navbar.jsp" %>

<div class="container">

    <div class="page-header">
        <div>
            <h2>✏️ Responder Questão</h2>
            <p class="subtitulo">Respondidas: <%= totalQuestoes %> | Acertos: <%= totalAcertos %></p>
        </div>
        <a href="${pageContext.request.contextPath}/app/exercicios?acao=resultado"
           class="btn-secundario"
           onclick="return confirm('Encerrar a sessão e ver o resultado?')">
            🏁 Encerrar sessão
        </a>
    </div>

    <% if ("vazio".equals(request.getParameter("erro"))) { %>
        <div class="alert alert-erro">Digite uma resposta antes de enviar.</div>
    <% } %>

    <div class="quiz-card">
        <div class="quiz-enunciado">
            <span class="quiz-badge">Questão</span>
            <p><%= exercicio.getEnunciado() %></p>
        </div>

        <form action="${pageContext.request.contextPath}/app/exercicios" method="post">
            <input type="hidden" name="acao" value="corrigir">
            <input type="hidden" name="exercicioId" value="<%= exercicio.getId() %>">
            <input type="hidden" name="materiaId" value="<%= materiaId %>">

            <div class="campo">
                <label for="resposta">Sua resposta:</label>
                <textarea id="resposta" name="resposta" rows="6"
                          placeholder="Digite sua resposta aqui..." required autofocus></textarea>
            </div>

            <div class="form-acoes">
                <a href="${pageContext.request.contextPath}/app/exercicios?materiaId=<%= materiaId %>"
                   class="btn-secundario">← Voltar</a>
                <button type="submit" class="btn-primario" id="btnEnviar">
                    📤 Enviar para correção
                </button>
            </div>
        </form>
    </div>

</div>

<script>
    document.querySelector('form').addEventListener('submit', function() {
        var btn = document.getElementById('btnEnviar');
        btn.textContent = '⏳ Corrigindo...';
        btn.disabled = true;
    });
</script>

</body>
</html>
