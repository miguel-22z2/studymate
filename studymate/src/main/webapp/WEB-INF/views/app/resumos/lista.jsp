<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.studymate.model.Materia, com.studymate.model.Resumo, java.util.List, java.time.format.DateTimeFormatter" %>
<%
    Materia materia = (Materia) request.getAttribute("materia");
    List<Resumo> resumos = (List<Resumo>) request.getAttribute("resumos");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resumos — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/resumos.css">
</head>
<body>

<%@ include file="../_navbar.jsp" %>

<div class="container">

    <div class="page-header">
        <div>
            <h2>📝 Resumos com IA</h2>
            <p class="subtitulo">Matéria: <strong><%= materia != null ? materia.getNome() : "" %></strong></p>
        </div>
        <a href="${pageContext.request.contextPath}/app/materias" class="btn-secundario">← Voltar</a>
    </div>

    <%-- Alertas --%>
    <% if ("deletado".equals(request.getParameter("sucesso"))) { %>
        <div class="alert alert-info">Resumo removido com sucesso.</div>
    <% } %>
    <% if ("deletar".equals(request.getParameter("erro"))) { %>
        <div class="alert alert-erro">Erro ao remover o resumo. Tente novamente.</div>
    <% } %>
    <% if (request.getAttribute("erro") != null) { %>
        <div class="alert alert-erro"><%= request.getAttribute("erro") %></div>
    <% } %>

    <%-- Card de geração --%>
    <div class="resumo-gerar-card">
        <div class="resumo-gerar-header">
            <h3>✨ Gerar novo resumo</h3>
            <p>Cole o conteúdo que deseja resumir — slide, texto do livro, anotações — e a IA cria um resumo organizado.</p>
        </div>

        <form action="${pageContext.request.contextPath}/app/resumos" method="post" id="formResumo">
            <input type="hidden" name="acao" value="gerar">
            <input type="hidden" name="materiaId" value="<%= materia != null ? materia.getId() : 0 %>">

            <div class="campo">
                <label for="conteudo">
                    Conteúdo para resumir
                    <span class="contador-chars" id="contadorChars">0 caracteres</span>
                </label>
                <textarea id="conteudo" name="conteudo" rows="10"
                          placeholder="Cole aqui o texto que deseja resumir...&#10;&#10;Ex: capítulo do livro, slides, anotações de aula, etc."
                          required><% if (request.getAttribute("conteudoAnterior") != null) { %><%= request.getAttribute("conteudoAnterior") %><% } %></textarea>
            </div>

            <div class="form-acoes">
                <button type="button" class="btn-secundario" onclick="limparTexto()">🗑 Limpar</button>
                <button type="submit" class="btn-primario btn-resumir" id="btnResumir">
                    🤖 Resumir com IA
                </button>
            </div>
        </form>
    </div>

    <%-- Lista de resumos anteriores --%>
    <div class="secao-resumos">
        <h3>📋 Resumos anteriores (<%= resumos != null ? resumos.size() : 0 %>)</h3>

        <% if (resumos == null || resumos.isEmpty()) { %>
            <div class="empty-state">
                <span class="empty-icon">📝</span>
                <h3>Nenhum resumo gerado ainda</h3>
                <p>Cole um conteúdo acima e clique em "Resumir com IA"!</p>
            </div>
        <% } else { %>
            <div class="lista-resumos">
                <% for (Resumo r : resumos) { %>
                    <div class="resumo-card-item">
                        <div class="resumo-card-preview">
                            <%-- Exibe trecho do resumo --%>
                            <p><%= r.getResumoIa().length() > 200 ? r.getResumoIa().substring(0, 200).concat("...") : r.getResumoIa() %></p>
                        </div>
                        <div class="resumo-card-rodape">
                            <span class="resumo-data">
                                🕐 <%= r.getGeradoEm() != null ? r.getGeradoEm().format(fmt) : "" %>
                            </span>
                            <div class="resumo-acoes">
                                <a href="${pageContext.request.contextPath}/app/resumos?acao=ver&id=<%= r.getId() %>&materiaId=<%= r.getMateriaId() %>"
                                   class="btn-primario btn-sm">👁 Ver completo</a>

                                <form method="post" action="${pageContext.request.contextPath}/app/resumos"
                                      style="display:inline"
                                      onsubmit="return confirm('Tem certeza que deseja excluir este resumo?')">
                                    <input type="hidden" name="acao" value="deletar">
                                    <input type="hidden" name="id" value="<%= r.getId() %>">
                                    <input type="hidden" name="materiaId" value="<%= r.getMateriaId() %>">
                                    <button type="submit" class="btn-secundario btn-sm">🗑 Excluir</button>
                                </form>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
        <% } %>
    </div>

</div>

<script>
    // Contador de caracteres
    var textarea = document.getElementById('conteudo');
    var contador = document.getElementById('contadorChars');

    function atualizarContador() {
        var n = textarea.value.length;
        contador.textContent = n + ' caractere' + (n !== 1 ? 's' : '');
        contador.style.color = n < 50 ? '#ef4444' : '#6b7280';
    }

    textarea.addEventListener('input', atualizarContador);
    atualizarContador();

    // Limpar textarea
    function limparTexto() {
        if (textarea.value.length === 0 || confirm('Limpar o conteúdo digitado?')) {
            textarea.value = '';
            atualizarContador();
        }
    }

    // Feedback visual ao enviar
    document.getElementById('formResumo').addEventListener('submit', function() {
        var btn = document.getElementById('btnResumir');
        btn.textContent = '⏳ Resumindo...';
        btn.disabled = true;
    });
</script>

</body>
</html>
