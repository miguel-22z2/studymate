<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.studymate.model.Usuario" %>
<%
    Usuario usuario       = (Usuario) session.getAttribute("usuarioLogado");
    int totalMaterias     = request.getAttribute("totalMaterias")   != null ? (int) request.getAttribute("totalMaterias")   : 0;
    int totalExercicios   = request.getAttribute("totalExercicios") != null ? (int) request.getAttribute("totalExercicios") : 0;
    int totalResumos      = request.getAttribute("totalResumos")    != null ? (int) request.getAttribute("totalResumos")    : 0;
    int totalSessoes      = request.getAttribute("totalSessoes")    != null ? (int) request.getAttribute("totalSessoes")    : 0;
    int totalAcertos      = request.getAttribute("totalAcertos")    != null ? (int) request.getAttribute("totalAcertos")    : 0;

    // Saudação por hora do dia
    int hora = java.time.LocalTime.now().getHour();
    String saudacao = hora < 12 ? "Bom dia" : hora < 18 ? "Boa tarde" : "Boa noite";
    String primeiroNome = usuario.getNome().split(" ")[0];
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>

<%@ include file="_navbar.jsp" %>

<div class="container">

    <%-- Cabeçalho de boas-vindas --%>
    <div class="dashboard-hero">
        <div class="dashboard-hero-texto">
            <h2><%= saudacao %>, <%= primeiroNome %>! 👋</h2>
            <p>Acompanhe seu progresso e continue estudando!</p>
        </div>
        <a href="${pageContext.request.contextPath}/app/materias?acao=nova"
           class="btn-primario btn-hero">+ Nova Matéria</a>
    </div>

    <% if (request.getAttribute("erro") != null) { %>
        <div class="alert alert-erro"><%= request.getAttribute("erro") %></div>
    <% } %>

    <%-- Cards de estatísticas --%>
    <div class="stats-grid">

        <div class="stat-card stat-azul">
            <div class="stat-card-icone"></div>
            <div class="stat-card-info">
                <span class="stat-card-numero"><%= totalMaterias %></span>
                <span class="stat-card-label">Matérias cadastradas</span>
            </div>
        </div>

        <div class="stat-card stat-roxo">
            <div class="stat-card-icone"></div>
            <div class="stat-card-info">
                <span class="stat-card-numero"><%= totalExercicios %></span>
                <span class="stat-card-label">Exercícios gerados</span>
            </div>
        </div>

        <div class="stat-card stat-verde">
            <div class="stat-card-icone"></div>
            <div class="stat-card-info">
                <span class="stat-card-numero"><%= totalAcertos %></span>
                <span class="stat-card-label">Questões acertadas</span>
            </div>
        </div>

        <div class="stat-card stat-amarelo">
            <div class="stat-card-icone"></div>
            <div class="stat-card-info">
                <span class="stat-card-numero"><%= totalResumos %></span>
                <span class="stat-card-label">Resumos gerados</span>
            </div>
        </div>

        <div class="stat-card stat-cinza">
            <div class="stat-card-icone"></div>
            <div class="stat-card-info">
                <span class="stat-card-numero"><%= totalSessoes %></span>
                <span class="stat-card-label">Sessões de estudo</span>
            </div>
        </div>

    </div>

    <%-- Acesso rápido --%>
    <div class="acesso-rapido">
        <div class="acesso-grid">

            <a href="${pageContext.request.contextPath}/app/materias" class="acesso-card">
                <span class="acesso-icone"></span>
                <div>
                    <strong>Minhas Matérias</strong>
                    <p>Gerencie e organize suas matérias</p>
                </div>
            </a>

            <a href="${pageContext.request.contextPath}/app/materias" class="acesso-card">
                <span class="acesso-icone"></span>
                <div>
                    <strong>Gerar Exercícios</strong>
                    <p>Pratique com questões criadas por IA</p>
                </div>
            </a>

            <a href="${pageContext.request.contextPath}/app/materias" class="acesso-card">
                <span class="acesso-icone"></span>
                <div>
                    <strong>Criar Resumo</strong>
                    <p>Resuma conteúdos com IA em segundos</p>
                </div>
            </a>

        </div>
    </div>

    <%-- Estado vazio — primeiro acesso --%>
    <% if (totalMaterias == 0) { %>
    <div class="primeiros-passos">
        <h3>🚀 Por onde começar?</h3>
        <div class="passos-lista">
            <div class="passo">
                <div class="passo-numero">1</div>
                <div class="passo-texto">
                    <strong>Cadastre uma matéria</strong>
                    <p>Ex: "Programação Orientada a Objetos", "Banco de Dados"...</p>
                </div>
            </div>
            <div class="passo-seta">→</div>
            <div class="passo">
                <div class="passo-numero">2</div>
                <div class="passo-texto">
                    <strong>Gere exercícios com IA</strong>
                    <p>Escolha a quantidade e o Gemini cria questões automaticamente</p>
                </div>
            </div>
            <div class="passo-seta">→</div>
            <div class="passo">
                <div class="passo-numero">3</div>
                <div class="passo-texto">
                    <strong>Responda e receba feedback</strong>
                    <p>A IA corrige sua resposta e dá dicas personalizadas</p>
                </div>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/app/materias?acao=nova"
           class="btn-primario" style="margin-top: 24px; display: inline-block;">
            Começar agora →
        </a>
    </div>
    <% } %>

</div>

</body>
</html>
