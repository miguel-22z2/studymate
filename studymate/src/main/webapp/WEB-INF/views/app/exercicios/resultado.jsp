<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.studymate.model.Resposta, java.util.List" %>
<%
    int totalQuestoes = request.getAttribute("totalQuestoes") != null ? (int) request.getAttribute("totalQuestoes") : 0;
    int totalAcertos  = request.getAttribute("totalAcertos")  != null ? (int) request.getAttribute("totalAcertos") : 0;
    List<Resposta> respostas = (List<Resposta>) request.getAttribute("respostas");
    int percentual    = totalQuestoes > 0 ? (totalAcertos * 100) / totalQuestoes : 0;
    String emoji      = percentual >= 80 ? "🏆" : percentual >= 50 ? "👍" : "📚";
    String mensagem   = percentual >= 80 ? "Excelente desempenho!" : percentual >= 50 ? "Bom trabalho! Continue estudando." : "Continue praticando, você vai melhorar!";
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resultado — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/exercicios.css">
</head>
<body>

<%@ include file="../_navbar.jsp" %>

<div class="container">

    <h2>🏁 Resultado da Sessão</h2>

    <%-- Placar --%>
    <div class="resultado-placar">
        <div class="resultado-emoji"><%= emoji %></div>
        <h3><%= mensagem %></h3>

        <div class="resultado-stats">
            <div class="stat-item">
                <span class="stat-numero"><%= totalAcertos %></span>
                <span class="stat-label">Acertos</span>
            </div>
            <div class="stat-item">
                <span class="stat-numero"><%= totalQuestoes - totalAcertos %></span>
                <span class="stat-label">Erros</span>
            </div>
            <div class="stat-item">
                <span class="stat-numero"><%= percentual %>%</span>
                <span class="stat-label">Aproveitamento</span>
            </div>
        </div>

        <%-- Barra de progresso --%>
        <div class="barra-progresso-container">
            <div class="barra-progresso" style="width: <%= percentual %>%"></div>
        </div>
    </div>

    <%-- Histórico de respostas --%>
    <% if (respostas != null && !respostas.isEmpty()) { %>
        <div class="historico-sessao">
            <h3>📋 Histórico da sessão</h3>

            <% int st = 1;
               for (Resposta r : respostas) { %>
                <div class="historico-item <%= r.isAcertou() ? "historico-acerto" : "historico-erro" %>">
                    <div class="historico-header">
                        <span><%= r.isAcertou() ? "✅" : "❌" %> Questão <%= st %></span>
                    </div>
                    <p class="historico-enunciado"><strong>Pergunta:</strong> <%= r.getEnunciadoExercicio() %></p>
                    <p><strong>Sua resposta:</strong> <%= r.getRespostaAluno() %></p>
                </div>
            <% st++; } %>
        </div>
    <% } %>

    <%-- Ações --%>
    <div class="feedback-acoes">
        <a href="${pageContext.request.contextPath}/app/materias" class="btn-secundario">← Minhas Matérias</a>
        <a href="${pageContext.request.contextPath}/app/dashboard" class="btn-primario">🏠 Dashboard</a>
    </div>

</div>

</body>
</html>
