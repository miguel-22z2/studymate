<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.studymate.model.Exercicio" %>
<%
    Exercicio exercicio   = (Exercicio) request.getAttribute("exercicio");
    String respostaAluno  = (String)  request.getAttribute("respostaAluno");
    String feedback       = (String)  request.getAttribute("feedback");
    boolean acertou       = (boolean) request.getAttribute("acertou");
    int materiaId         = (int)     request.getAttribute("materiaId");

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
    <title>Feedback da IA — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/exercicios.css">
</head>
<body>

<%@ include file="../_navbar.jsp" %>

<div class="container">

    <div class="page-header">
        <div>
            <h2>🤖 Feedback da IA</h2>
            <p class="subtitulo">Respondidas: <%= totalQuestoes %> | Acertos: <%= totalAcertos %></p>
        </div>
    </div>

    <%-- Resultado --%>
    <div class="feedback-resultado <%= acertou ? "feedback-acerto" : "feedback-erro" %>">
        <span class="feedback-icone"><%= acertou ? "✅" : "❌" %></span>
        <span><%= acertou ? "Resposta Correta!" : "Resposta Incorreta" %></span>
    </div>

    <%-- Pergunta --%>
    <div class="feedback-card">
        <h4>📋 Pergunta</h4>
        <p><%= exercicio.getEnunciado() %></p>
    </div>

    <%-- Resposta do aluno --%>
    <div class="feedback-card">
        <h4>✏️ Sua resposta</h4>
        <p><%= respostaAluno %></p>
    </div>

    <%-- Feedback da IA --%>
    <div class="feedback-card feedback-ia-card">
        <h4>🤖 Análise da IA</h4>
        <div class="feedback-texto">
            <%
                // Formata o feedback quebrando linhas
                String feedbackFormatado = feedback
                    .replace("Resultado:", "<strong>Resultado:</strong>")
                    .replace("Feedback:", "<br><strong>Feedback:</strong>")
                    .replace("Dica:", "<br><strong>Dica:</strong>")
                    .replace("\n", "<br>");
            %>
            <%= feedbackFormatado %>
        </div>
    </div>

    <%-- Ações --%>
    <div class="feedback-acoes">
        <a href="${pageContext.request.contextPath}/app/exercicios?materiaId=<%= materiaId %>"
           class="btn-secundario">← Ver todos os exercícios</a>

        <a href="${pageContext.request.contextPath}/app/exercicios?acao=resultado"
           class="btn-secundario"
           onclick="return confirm('Encerrar a sessão e ver o resultado final?')">
            🏁 Encerrar sessão
        </a>
    </div>

</div>

</body>
</html>
