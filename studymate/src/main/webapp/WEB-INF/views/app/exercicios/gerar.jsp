<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.studymate.model.Materia, com.studymate.model.Exercicio, java.util.List" %>
<%
    Materia materia = (Materia) request.getAttribute("materia");
    List<Exercicio> exercicios = (List<Exercicio>) request.getAttribute("exercicios");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Exercícios — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/exercicios.css">
</head>
<body>

<%@ include file="../_navbar.jsp" %>

<div class="container">

    <div class="page-header">
        <div>
            <h2>🤖 Exercícios com IA</h2>
            <p class="subtitulo">Matéria: <strong><%= materia != null ? materia.getNome() : "" %></strong></p>
        </div>
        <a href="${pageContext.request.contextPath}/app/materias" class="btn-secundario">← Voltar</a>
    </div>

    <%-- Alertas --%>
    <% if ("gerados".equals(request.getParameter("sucesso"))) { %>
        <div class="alert alert-sucesso">
            ✅ <%= request.getParameter("qtd") %> exercício(s) gerado(s) com sucesso pela IA!
        </div>
    <% } %>
    <% if (request.getAttribute("erro") != null) { %>
        <div class="alert alert-erro"><%= request.getAttribute("erro") %></div>
    <% } %>

    <%-- Card de geração --%>
    <div class="gerar-card">
        <h3>✨ Gerar novos exercícios</h3>
        <p>A IA vai criar exercícios dissertativos sobre <strong><%= materia != null ? materia.getNome() : "" %></strong>.</p>

        <form action="${pageContext.request.contextPath}/app/exercicios" method="post" class="gerar-form">
            <input type="hidden" name="acao" value="gerar">
            <input type="hidden" name="materiaId" value="<%= materia != null ? materia.getId() : 0 %>">

            <div class="gerar-controles">
                <label for="quantidade">Quantos exercícios?</label>
                <div class="quantidade-selector">
                    <c:forEach var="n" begin="1" end="10">
                        <button type="button" class="qtd-btn" onclick="selecionarQtd(${n})">${n}</button>
                    </c:forEach>
                </div>
                <input type="hidden" id="quantidade" name="quantidade" value="5">
                <p class="qtd-selecionada">Selecionado: <strong id="qtdTexto">5</strong></p>
            </div>

            <button type="submit" class="btn-primario btn-gerar" id="btnGerar">
                🤖 Gerar com IA
            </button>
        </form>
    </div>

    <%-- Lista de exercícios existentes --%>
    <div class="secao-exercicios">
        <h3>📋 Exercícios disponíveis (<%= exercicios != null ? exercicios.size() : 0 %>)</h3>

        <% if (exercicios == null || exercicios.isEmpty()) { %>
            <div class="empty-state">
                <span class="empty-icon">🤖</span>
                <h3>Nenhum exercício ainda</h3>
                <p>Clique em "Gerar com IA" para criar seus primeiros exercícios!</p>
            </div>
        <% } else { %>
            <div class="lista-exercicios">
                <% int index = 1;
                   for (Exercicio ex : exercicios) { %>
                    <div class="exercicio-item">
                        <div class="exercicio-numero">Q<%= index %></div>
                        <div class="exercicio-conteudo">
                            <p class="exercicio-enunciado"><%= ex.getEnunciado() %></p>
                            <div class="exercicio-acoes">
                                <a href="${pageContext.request.contextPath}/app/exercicios?acao=quiz&id=<%= ex.getId() %>&materiaId=<%= ex.getMateriaId() %>"
                                   class="btn-primario btn-sm">▶ Responder</a>
                                <button class="btn-secundario btn-sm"
                                        onclick="toggleGabarito('gab-<%= ex.getId() %>')">
                                    👁 Ver gabarito
                                </button>
                            </div>
                            <div id="gab-<%= ex.getId() %>" class="gabarito-box" style="display:none">
                                <strong>Gabarito:</strong> <%= ex.getGabaritoIa() %>
                            </div>
                        </div>
                    </div>
                <% index++; } %>
            </div>
        <% } %>
    </div>

</div>

<script>
    // Seletor visual de quantidade
    let qtdSelecionada = 5;

    function selecionarQtd(n) {
        qtdSelecionada = n;
        document.getElementById('quantidade').value = n;
        document.getElementById('qtdTexto').textContent = n;

        document.querySelectorAll('.qtd-btn').forEach(function(btn) {
            btn.classList.remove('ativo');
        });
        event.target.classList.add('ativo');
    }

    // Marca o botão 5 como ativo por padrão
    document.addEventListener('DOMContentLoaded', function() {
        var botoes = document.querySelectorAll('.qtd-btn');
        if (botoes.length >= 5) botoes[4].classList.add('ativo');
    });

    // Toggle do gabarito
    function toggleGabarito(id) {
        var el = document.getElementById(id);
        el.style.display = el.style.display === 'none' ? 'block' : 'none';
    }

    // Feedback visual ao clicar em Gerar
    document.querySelector('.gerar-form').addEventListener('submit', function() {
        var btn = document.getElementById('btnGerar');
        btn.textContent = '⏳ Gerando...';
        btn.disabled = true;
    });
</script>

</body>
</html>
