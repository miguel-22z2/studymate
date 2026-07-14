<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.studymate.model.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Minhas Matérias — StudyMate</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>

<%@ include file="../_navbar.jsp" %>

<div class="container">

    <div class="page-header">
        <div>
            <h2>Matérias registradas</h2>
            <p class="subtitulo">Organize os conteúdos que você está estudando</p>
        </div>
        <a href="${pageContext.request.contextPath}/app/materias?acao=nova" class="btn-primario">
            + Nova Matéria
        </a>
    </div>

    <%-- Alertas de sucesso --%>
    <c:if test="${param.sucesso == 'criada'}">
        <div class="alert alert-sucesso">Matéria criada com sucesso!</div>
    </c:if>
    <c:if test="${param.sucesso == 'editada'}">
        <div class="alert alert-sucesso">Matéria atualizada com sucesso!</div>
    </c:if>
    <c:if test="${param.sucesso == 'deletada'}">
        <div class="alert alert-info">Matéria removida com sucesso.</div>
    </c:if>
    <c:if test="${param.erro == 'deletar'}">
        <div class="alert alert-erro">Erro ao remover a matéria. Tente novamente.</div>
    </c:if>
    <c:if test="${not empty erro}">
        <div class="alert alert-erro">${erro}</div>
    </c:if>

    <%-- Lista de matérias --%>
    <c:choose>
        <c:when test="${empty materias}">
            <div class="empty-state">
                <span class="empty-icon">📖</span>
                <h3>Nenhuma matéria cadastrada ainda</h3>
                <p>Crie sua primeira matéria para começar a estudar com IA!</p>
                <a href="${pageContext.request.contextPath}/app/materias?acao=nova" class="btn-primario">
                    + Criar primeira matéria
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="grid-cards">
                <c:forEach var="m" items="${materias}">
                    <div class="card-materia">
                        <div class="card-materia-topo">
                            <h3>${m.nome}</h3>
                            <div class="card-acoes">
                                <a href="${pageContext.request.contextPath}/app/materias?acao=editar&id=${m.id}"
                                   class="btn-icone" title="Editar">✏️</a>

                                <form method="post"
                                      action="${pageContext.request.contextPath}/app/materias"
                                      onsubmit="return confirm('Tem certeza que deseja excluir a matéria \'${m.nome}\'? Todos os exercícios e resumos serão apagados.')">
                                    <input type="hidden" name="acao" value="deletar">
                                    <input type="hidden" name="id" value="${m.id}">
                                    <button type="submit" class="btn-icone btn-perigo" title="Excluir">🗑️</button>
                                </form>
                            </div>
                        </div>

                        <c:if test="${not empty m.descricao}">
                            <p class="card-materia-desc">${m.descricao}</p>
                        </c:if>

                        <div class="card-materia-rodape">
                            <a href="${pageContext.request.contextPath}/app/exercicios?materiaId=${m.id}"
                               class="btn-secundario btn-sm">Exercícios</a>
                            <a href="${pageContext.request.contextPath}/app/resumos?materiaId=${m.id}"
                               class="btn-secundario btn-sm">Resumo</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</div>

</body>
</html>
