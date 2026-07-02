<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.studymate.model.Usuario" %>
<%
    Usuario _usuario = (Usuario) session.getAttribute("usuarioLogado");
    String _nomeUsuario = (_usuario != null) ? _usuario.getNome() : "Usuário";
    String _ctx = request.getContextPath();
    String _uri = request.getRequestURI();
%>

<nav class="navbar">
    <div class="navbar-marca">
        <a href="<%= _ctx %>/app/dashboard">📚 StudyMate</a>
    </div>

    <ul class="navbar-links">
        <li>
            <a href="<%= _ctx %>/app/dashboard"
               class="<%= _uri.contains("/dashboard") ? "ativo" : "" %>">
               🏠 Dashboard
            </a>
        </li>
        <li>
            <a href="<%= _ctx %>/app/materias"
               class="<%= _uri.contains("/materias") ? "ativo" : "" %>">
               📚 Matérias
            </a>
        </li>
        <li>
            <a href="<%= _ctx %>/app/exercicios"
               class="<%= _uri.contains("/exercicios") ? "ativo" : "" %>">
               🤖 Exercícios
            </a>
        </li>
        <li>
            <a href="<%= _ctx %>/app/resumos"
               class="<%= _uri.contains("/resumos") ? "ativo" : "" %>">
               📝 Resumos
            </a>
        </li>
    </ul>

    <div class="navbar-usuario">
        <button id="btnToggleTema" class="btn-toggle-tema" title="Alternar tema" onclick="alternarTema()">🌙</button>
        <span class="navbar-nome">👤 <%= _nomeUsuario %></span>
        <a href="<%= _ctx %>/logout" class="btn-logout">Sair</a>
    </div>
</nav>

<script>
    // Aplica o tema salvo assim que a navbar carrega (evita "flash" de tema errado)
    (function() {
        var temaSalvo = localStorage.getItem('studymate-tema') || 'light';
        document.documentElement.setAttribute('data-theme', temaSalvo);
        atualizarIconeToggle(temaSalvo);
    })();

    function alternarTema() {
        var temaAtual = document.documentElement.getAttribute('data-theme') || 'light';
        var novoTema = temaAtual === 'dark' ? 'light' : 'dark';

        document.documentElement.setAttribute('data-theme', novoTema);
        localStorage.setItem('studymate-tema', novoTema);
        atualizarIconeToggle(novoTema);
    }

    function atualizarIconeToggle(tema) {
        var btn = document.getElementById('btnToggleTema');
        if (btn) {
            btn.textContent = tema === 'dark' ? '☀️' : '🌙';
        }
    }
</script>