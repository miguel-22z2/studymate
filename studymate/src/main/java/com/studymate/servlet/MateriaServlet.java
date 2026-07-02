package com.studymate.servlet;

import com.studymate.dao.MateriaDAO;
import com.studymate.model.Materia;
import com.studymate.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet responsável pelo CRUD de matérias.
 *
 * Rotas:
 *   GET  /app/materias            → lista todas as matérias do usuário
 *   GET  /app/materias?acao=nova  → exibe formulário de criação
 *   GET  /app/materias?acao=editar&id=X → exibe formulário de edição
 *   POST /app/materias?acao=criar   → salva nova matéria
 *   POST /app/materias?acao=editar  → atualiza matéria existente
 *   POST /app/materias?acao=deletar → remove matéria
 */
@WebServlet("/app/materias")
public class MateriaServlet extends HttpServlet {

    private final MateriaDAO materiaDAO = new MateriaDAO();

    // ----------------------------------------------------------------
    // GET
    // ----------------------------------------------------------------

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if ("nova".equals(acao)) {
            req.getRequestDispatcher("/WEB-INF/views/app/materias/nova.jsp").forward(req, resp);
            return;
        }

        if ("editar".equals(acao)) {
            exibirFormEditar(req, resp);
            return;
        }

        // Padrão: lista as matérias
        listar(req, resp);
    }

    // ----------------------------------------------------------------
    // POST
    // ----------------------------------------------------------------

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if ("criar".equals(acao)) {
            criar(req, resp);
            return;
        }

        if ("editar".equals(acao)) {
            atualizar(req, resp);
            return;
        }

        if ("deletar".equals(acao)) {
            deletar(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/app/materias");
    }

    // ----------------------------------------------------------------
    // LISTAR
    // ----------------------------------------------------------------

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = getUsuarioLogado(req);

        try {
            List<Materia> materias = materiaDAO.listarPorUsuario(usuario.getId());
            req.setAttribute("materias", materias);
            req.getRequestDispatcher("/WEB-INF/views/app/materias/lista.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao carregar matérias.");
            req.getRequestDispatcher("/WEB-INF/views/app/materias/lista.jsp").forward(req, resp);
        }
    }

    // ----------------------------------------------------------------
    // CRIAR
    // ----------------------------------------------------------------

    private void criar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nome      = req.getParameter("nome");
        String descricao = req.getParameter("descricao");
        Usuario usuario  = getUsuarioLogado(req);

        if (nome == null || nome.isBlank()) {
            req.setAttribute("erro", "O nome da matéria é obrigatório.");
            req.getRequestDispatcher("/WEB-INF/views/app/materias/nova.jsp").forward(req, resp);
            return;
        }

        try {
            Materia materia = new Materia(usuario.getId(), nome.trim(), descricao);
            materiaDAO.criar(materia);
            resp.sendRedirect(req.getContextPath() + "/app/materias?sucesso=criada");

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao salvar matéria. Tente novamente.");
            req.getRequestDispatcher("/WEB-INF/views/app/materias/nova.jsp").forward(req, resp);
        }
    }

    // ----------------------------------------------------------------
    // EXIBIR FORMULÁRIO DE EDIÇÃO
    // ----------------------------------------------------------------

    private void exibirFormEditar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id           = parseId(req.getParameter("id"));
        Usuario usuario  = getUsuarioLogado(req);

        if (id <= 0) {
            resp.sendRedirect(req.getContextPath() + "/app/materias");
            return;
        }

        try {
            Materia materia = materiaDAO.buscarPorId(id, usuario.getId());

            if (materia == null) {
                resp.sendRedirect(req.getContextPath() + "/app/materias");
                return;
            }

            req.setAttribute("materia", materia);
            req.getRequestDispatcher("/WEB-INF/views/app/materias/editar.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/app/materias");
        }
    }

    // ----------------------------------------------------------------
    // ATUALIZAR
    // ----------------------------------------------------------------

    private void atualizar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id           = parseId(req.getParameter("id"));
        String nome      = req.getParameter("nome");
        String descricao = req.getParameter("descricao");
        Usuario usuario  = getUsuarioLogado(req);

        if (nome == null || nome.isBlank()) {
            req.setAttribute("erro", "O nome da matéria é obrigatório.");
            exibirFormEditar(req, resp);
            return;
        }

        try {
            Materia materia = new Materia();
            materia.setId(id);
            materia.setNome(nome.trim());
            materia.setDescricao(descricao);

            boolean ok = materiaDAO.atualizar(materia, usuario.getId());

            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/app/materias?sucesso=editada");
            } else {
                resp.sendRedirect(req.getContextPath() + "/app/materias");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao atualizar matéria. Tente novamente.");
            exibirFormEditar(req, resp);
        }
    }

    // ----------------------------------------------------------------
    // DELETAR
    // ----------------------------------------------------------------

    private void deletar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id          = parseId(req.getParameter("id"));
        Usuario usuario = getUsuarioLogado(req);

        try {
            materiaDAO.deletar(id, usuario.getId());
            resp.sendRedirect(req.getContextPath() + "/app/materias?sucesso=deletada");

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/app/materias?erro=deletar");
        }
    }

    // ----------------------------------------------------------------
    // Auxiliares
    // ----------------------------------------------------------------

    private Usuario getUsuarioLogado(HttpServletRequest req) {
        return (Usuario) req.getSession().getAttribute("usuarioLogado");
    }

    private int parseId(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
