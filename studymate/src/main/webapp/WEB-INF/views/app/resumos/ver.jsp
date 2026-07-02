<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.studymate.model.Resumo, com.studymate.model.Materia, java.time.format.DateTimeFormatter" %>
<%
    Resumo  resumo  = (Resumo)  request.getAttribute("resumo");
    Materia materia = (Materia) request.getAttribute("materia");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    // Formata o resumo convertendo markdown básico para HTML
    String resumoHtml = resumo.getResumoIa()
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        // Títulos com ##
        .replaceAll("(?m)^##\\s+(.+)$", "<h3>$1</h3>")
        .replaceAll("(?m)^#\\s+(.+)$",  "<h2>$1</h2>")
        // Negrito **texto**
        .replaceAll("\\*\\*(.+?)\\*\\*", "<strong>$1</strong>")
        // Tópicos com - ou *
        .replaceAll("(?m)^[\\-\\*]\\s+(.+)$", "<li>$1</li>")
        // Quebras de linha
        .replace("\n", "<br>");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resumo — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/resumos.css">
</head>
<body>

<%@ include file="../_navbar.jsp" %>

<div class="container">

    <div class="page-header">
        <div>
            <h2>📝 Resumo Gerado</h2>
            <p class="subtitulo">Matéria: <strong><%= materia.getNome() %></strong>
                <% if (resumo.getGeradoEm() != null) { %>
                    &nbsp;·&nbsp; 🕐 <%= resumo.getGeradoEm().format(fmt) %>
                <% } %>
            </p>
        </div>
        <div style="display:flex; gap:10px;">
            <a href="${pageContext.request.contextPath}/app/resumos?materiaId=<%= materia.getId() %>"
               class="btn-secundario">← Voltar</a>
            <button onclick="window.print()" class="btn-secundario">🖨 Imprimir</button>
        </div>
    </div>

    <% if ("true".equals(request.getParameter("novo"))) { %>
        <div class="alert alert-sucesso">✅ Resumo gerado com sucesso pela IA!</div>
    <% } %>

    <%-- Conteúdo original (colapsável) --%>
    <div class="conteudo-original-card">
        <button class="toggle-original" onclick="toggleOriginal()">
            📄 Ver conteúdo original enviado <span id="seta">▼</span>
        </button>
        <div id="conteudoOriginal" class="conteudo-original-texto" style="display:none">
            <%= resumo.getConteudoRaw().replace("\n", "<br>") %>
        </div>
    </div>

    <%-- Resumo gerado pela IA --%>
    <div class="resumo-completo-card">
        <div class="resumo-completo-header">
            <span class="resumo-badge">🤖 Resumo gerado por IA</span>
        </div>
        <div class="resumo-conteudo-ia">
            <%= resumoHtml %>
        </div>
    </div>

    <%-- Ações --%>
    <div class="feedback-acoes" style="margin-top: 24px;">
        <a href="${pageContext.request.contextPath}/app/resumos?materiaId=<%= materia.getId() %>"
           class="btn-secundario">← Voltar para resumos</a>

        <a href="${pageContext.request.contextPath}/app/exercicios?materiaId=<%= materia.getId() %>"
           class="btn-primario">🤖 Gerar exercícios sobre esta matéria</a>
    </div>

</div>

<script>
    function toggleOriginal() {
        var div  = document.getElementById('conteudoOriginal');
        var seta = document.getElementById('seta');
        if (div.style.display === 'none') {
            div.style.display = 'block';
            seta.textContent  = '▲';
        } else {
            div.style.display = 'none';
            seta.textContent  = '▼';
        }
    }
</script>

<style>
    @media print {
        .navbar, .page-header a, .conteudo-original-card,
        .feedback-acoes, .alert { display: none !important; }
        .resumo-completo-card   { box-shadow: none; border: none; }
    }
</style>

</body>
</html>
