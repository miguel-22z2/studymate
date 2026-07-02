package com.studymate.servlet;

import com.studymate.dao.MateriaDAO;
import com.studymate.dao.ResumoDAO;
import com.studymate.model.Materia;
import com.studymate.model.Resumo;
import com.studymate.model.Usuario;
import com.studymate.service.AIService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet responsável pela geração e visualização de resumos com IA.
 *
 * Rotas:
 *   GET  /app/resumos?materiaId=X          → exibe form + lista de resumos da matéria
 *   GET  /app/resumos?acao=ver&id=X        → exibe um resumo completo
 *   POST /app/resumos?acao=gerar           → gera resumo via IA e salva no banco
 *   POST /app/resumos?acao=deletar         → remove um resumo
 */
@WebServlet("/app/resumos")
public class ResumoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
	private final ResumoDAO  resumoDAO  = new ResumoDAO();
    private final MateriaDAO materiaDAO = new MateriaDAO();
    private final AIService  aiService  = new AIService();

    // ----------------------------------------------------------------
    // GET
    // ----------------------------------------------------------------

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if ("ver".equals(acao)) {
            verResumo(req, resp);
            return;
        }

        // Padrão: lista de resumos da matéria
        listar(req, resp);
    }

    // ----------------------------------------------------------------
    // POST
    // ----------------------------------------------------------------

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if ("gerar".equals(acao)) {
            gerarResumo(req, resp);
            return;
        }

        if ("deletar".equals(acao)) {
            deletarResumo(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/app/materias");
    }

    // ----------------------------------------------------------------
    // LISTAR resumos de uma matéria
    // ----------------------------------------------------------------

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int materiaId   = parseId(req.getParameter("materiaId"));
        Usuario usuario = getUsuario(req);

        if (materiaId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/app/materias");
            return;
        }

        try {
            Materia materia = materiaDAO.buscarPorId(materiaId, usuario.getId());
            if (materia == null) {
                resp.sendRedirect(req.getContextPath() + "/app/materias");
                return;
            }

            List<Resumo> resumos = resumoDAO.listarPorMateria(materiaId);

            req.setAttribute("materia", materia);
            req.setAttribute("resumos", resumos);
            req.getRequestDispatcher("/WEB-INF/views/app/resumos/lista.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao carregar resumos.");
            req.getRequestDispatcher("/WEB-INF/views/app/resumos/lista.jsp").forward(req, resp);
        }
    }

    // ----------------------------------------------------------------
    // VER resumo completo
    // ----------------------------------------------------------------

    private void verResumo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id          = parseId(req.getParameter("id"));
        int materiaId   = parseId(req.getParameter("materiaId"));
        Usuario usuario = getUsuario(req);

        try {
            Materia materia = materiaDAO.buscarPorId(materiaId, usuario.getId());
            Resumo  resumo  = resumoDAO.buscarPorId(id);

            if (materia == null || resumo == null || resumo.getMateriaId() != materiaId) {
                resp.sendRedirect(req.getContextPath() + "/app/materias");
                return;
            }

            req.setAttribute("materia", materia);
            req.setAttribute("resumo", resumo);
            req.getRequestDispatcher("/WEB-INF/views/app/resumos/ver.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/app/resumos?materiaId=" + materiaId);
        }
    }

    // ----------------------------------------------------------------
    // GERAR resumo via IA
    // ----------------------------------------------------------------

    private void gerarResumo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int materiaId    = parseId(req.getParameter("materiaId"));
        String conteudo  = req.getParameter("conteudo");
        Usuario usuario  = getUsuario(req);

        // Validações
        if (materiaId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/app/materias");
            return;
        }

        if (conteudo == null || conteudo.isBlank()) {
            req.setAttribute("erro", "Cole algum conteúdo antes de gerar o resumo.");
            listar(req, resp);
            return;
        }

        if (conteudo.trim().length() < 50) {
            req.setAttribute("erro", "O conteúdo é muito curto. Insira pelo menos 50 caracteres.");
            req.setAttribute("conteudoAnterior", conteudo);
            listar(req, resp);
            return;
        }

        try {
            Materia materia = materiaDAO.buscarPorId(materiaId, usuario.getId());
            if (materia == null) {
                resp.sendRedirect(req.getContextPath() + "/app/materias");
                return;
            }

            // Chama a IA para gerar o resumo
            String resumoGerado = aiService.gerarResumo(conteudo.trim());

            // Monta e salva o objeto
            Resumo resumo = new Resumo();
            resumo.setMateriaId(materiaId);
            resumo.setConteudoRaw(conteudo.trim());
            resumo.setResumoIa(resumoGerado);

            int novoId = resumoDAO.salvar(resumo);

            // Redireciona direto pro resumo gerado
            resp.sendRedirect(req.getContextPath() +
                "/app/resumos?acao=ver&id=" + novoId + "&materiaId=" + materiaId + "&novo=true");

        } catch (IOException ie) {
            ie.printStackTrace();
            req.setAttribute("erro", "Erro ao conectar com a IA. Verifique sua chave do Gemini.");
            req.setAttribute("conteudoAnterior", conteudo);
            listar(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao salvar o resumo. Tente novamente.");
            listar(req, resp);
        }
    }

    // ----------------------------------------------------------------
    // DELETAR resumo
    // ----------------------------------------------------------------

    private void deletarResumo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id        = parseId(req.getParameter("id"));
        int materiaId = parseId(req.getParameter("materiaId"));

        try {
            resumoDAO.deletar(id, materiaId);
            resp.sendRedirect(req.getContextPath() +
                "/app/resumos?materiaId=" + materiaId + "&sucesso=deletado");

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() +
                "/app/resumos?materiaId=" + materiaId + "&erro=deletar");
        }
    }

    // ----------------------------------------------------------------
    // Auxiliares
    // ----------------------------------------------------------------

    private Usuario getUsuario(HttpServletRequest req) {
        return (Usuario) req.getSession().getAttribute("usuarioLogado");
    }

    private int parseId(String valor) {
        try { return Integer.parseInt(valor); }
        catch (NumberFormatException e) { return -1; }
    }
}
