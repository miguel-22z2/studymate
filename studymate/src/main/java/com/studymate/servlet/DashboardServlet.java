package com.studymate.servlet;

import com.studymate.dao.ExercicioDAO;
import com.studymate.dao.MateriaDAO;
import com.studymate.dao.ResumoDAO;
import com.studymate.dao.RespostaDAO;
import com.studymate.dao.SessaoEstudoDAO;
import com.studymate.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet do Dashboard — carrega todas as estatísticas do aluno logado.
 * GET /app/dashboard
 */
@WebServlet("/app/dashboard")
public class DashboardServlet extends HttpServlet {

    private final MateriaDAO      materiaDAO      = new MateriaDAO();
    private final ExercicioDAO    exercicioDAO    = new ExercicioDAO();
    private final ResumoDAO       resumoDAO       = new ResumoDAO();
    private final SessaoEstudoDAO sessaoDAO       = new SessaoEstudoDAO();
    private final RespostaDAO     respostaDAO     = new RespostaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogado");

        try {
            int totalMaterias  = materiaDAO.contarPorUsuario(usuario.getId());
            int totalExercicios = exercicioDAO.contarTotalPorUsuario(usuario.getId());
            int totalResumos   = resumoDAO.contarTotalPorUsuario(usuario.getId());
            int totalSessoes   = sessaoDAO.contarPorUsuario(usuario.getId());
            int totalAcertos   = respostaDAO.contarAcertosPorUsuario(usuario.getId());

            req.setAttribute("totalMaterias",   totalMaterias);
            req.setAttribute("totalExercicios", totalExercicios);
            req.setAttribute("totalResumos",    totalResumos);
            req.setAttribute("totalSessoes",    totalSessoes);
            req.setAttribute("totalAcertos",    totalAcertos);

            req.getRequestDispatcher("/WEB-INF/views/app/dashboard.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao carregar o dashboard.");
            req.getRequestDispatcher("/WEB-INF/views/app/dashboard.jsp").forward(req, resp);
        }
    }
}
