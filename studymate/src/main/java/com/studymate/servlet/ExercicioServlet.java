package com.studymate.servlet;

import com.studymate.dao.*;
import com.studymate.model.*;
import com.studymate.service.ExercicioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet responsável pela geração e correção de exercícios com IA.
 *
 * Rotas:
 *   GET  /app/exercicios?materiaId=X       → exibe form de geração + lista de exercícios
 *   POST /app/exercicios?acao=gerar        → gera novos exercícios via IA e salva no banco
 *   GET  /app/exercicios?acao=quiz&id=X    → inicia quiz com um exercício
 *   POST /app/exercicios?acao=corrigir     → corrige resposta do aluno via IA
 *   GET  /app/exercicios?acao=resultado    → exibe resultado final da sessão
 */
@WebServlet("/app/exercicios")
public class ExercicioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
	private final ExercicioDAO     exercicioDAO     = new ExercicioDAO();
    private final MateriaDAO       materiaDAO       = new MateriaDAO();
    private final SessaoEstudoDAO  sessaoDAO        = new SessaoEstudoDAO();
    private final RespostaDAO      respostaDAO      = new RespostaDAO();
    private final ExercicioService exercicioService = new ExercicioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if ("quiz".equals(acao))       { iniciarQuiz(req, resp);    return; }
        if ("resultado".equals(acao))  { exibirResultado(req, resp); return; }

        exibirGerar(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if ("gerar".equals(acao))    { gerarExercicios(req, resp);  return; }
        if ("corrigir".equals(acao)) { corrigirResposta(req, resp); return; }

        resp.sendRedirect(req.getContextPath() + "/app/materias");
    }

    private void exibirGerar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int materiaId   = parseId(req.getParameter("materiaId"));
        Usuario usuario = getUsuario(req);

        if (materiaId <= 0) { resp.sendRedirect(req.getContextPath() + "/app/materias"); return; }

        try {
            Materia materia = materiaDAO.buscarPorId(materiaId, usuario.getId());
            if (materia == null) { resp.sendRedirect(req.getContextPath() + "/app/materias"); return; }

            List<Exercicio> exercicios = exercicioDAO.listarPorMateria(materiaId);
            req.setAttribute("materia", materia);
            req.setAttribute("exercicios", exercicios);
            req.getRequestDispatcher("/WEB-INF/views/app/exercicios/gerar.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao carregar exercícios.");
            req.getRequestDispatcher("/WEB-INF/views/app/exercicios/gerar.jsp").forward(req, resp);
        }
    }

    private void gerarExercicios(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int materiaId  = parseId(req.getParameter("materiaId"));
        int quantidade = parseId(req.getParameter("quantidade"));
        Usuario usuario = getUsuario(req);

        if (materiaId <= 0 || quantidade < 1 || quantidade > 10) {
            resp.sendRedirect(req.getContextPath() + "/app/materias"); return;
        }

        try {
            Materia materia = materiaDAO.buscarPorId(materiaId, usuario.getId());
            if (materia == null) { resp.sendRedirect(req.getContextPath() + "/app/materias"); return; }

            List<Exercicio> gerados = exercicioService.gerarExercicios(materiaId, materia.getNome(), quantidade);
            exercicioDAO.salvarLote(gerados);

            resp.sendRedirect(req.getContextPath() +
                "/app/exercicios?materiaId=" + materiaId + "&sucesso=gerados&qtd=" + gerados.size());

        } catch (IOException ie) {
            ie.printStackTrace();
            req.setAttribute("erro", "Erro ao conectar com a IA. Verifique sua chave do Gemini.");
            exibirGerar(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro ao salvar exercícios. Tente novamente.");
            exibirGerar(req, resp);
        }
    }

    private void iniciarQuiz(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int exercicioId = parseId(req.getParameter("id"));
        int materiaId   = parseId(req.getParameter("materiaId"));
        Usuario usuario = getUsuario(req);
        HttpSession httpSessao = req.getSession();

        try {
            Integer sessaoId = (Integer) httpSessao.getAttribute("sessaoEstudoId");
            if (sessaoId == null) {
                sessaoId = sessaoDAO.criar(usuario.getId(), materiaId);
                httpSessao.setAttribute("sessaoEstudoId", sessaoId);
                httpSessao.setAttribute("totalQuestoes", 0);
                httpSessao.setAttribute("totalAcertos", 0);
            }

            Exercicio exercicio = exercicioDAO.buscarPorId(exercicioId);
            if (exercicio == null) {
                resp.sendRedirect(req.getContextPath() + "/app/exercicios?materiaId=" + materiaId); return;
            }

            req.setAttribute("exercicio", exercicio);
            req.setAttribute("materiaId", materiaId);
            req.getRequestDispatcher("/WEB-INF/views/app/exercicios/quiz.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/app/exercicios?materiaId=" + materiaId);
        }
    }

    private void corrigirResposta(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int exercicioId      = parseId(req.getParameter("exercicioId"));
        int materiaId        = parseId(req.getParameter("materiaId"));
        String respostaAluno = req.getParameter("resposta");
        HttpSession httpSessao = req.getSession();

        if (respostaAluno == null || respostaAluno.isBlank()) {
            resp.sendRedirect(req.getContextPath() +
                "/app/exercicios?acao=quiz&id=" + exercicioId + "&materiaId=" + materiaId); return;
        }

        try {
            Exercicio exercicio = exercicioDAO.buscarPorId(exercicioId);
            if (exercicio == null) {
                resp.sendRedirect(req.getContextPath() + "/app/exercicios?materiaId=" + materiaId); return;
            }

            String[] resultado = exercicioService.corrigirResposta(
                exercicio.getEnunciado(), exercicio.getGabaritoIa(), respostaAluno);

            String  feedback = resultado[0];
            boolean acertou  = Boolean.parseBoolean(resultado[1]);

            int sessaoId      = (Integer) httpSessao.getAttribute("sessaoEstudoId");
            int totalQuestoes = (Integer) httpSessao.getAttribute("totalQuestoes") + 1;
            int totalAcertos  = (Integer) httpSessao.getAttribute("totalAcertos") + (acertou ? 1 : 0);

            httpSessao.setAttribute("totalQuestoes", totalQuestoes);
            httpSessao.setAttribute("totalAcertos", totalAcertos);

            Resposta resposta = new Resposta();
            resposta.setSessaoId(sessaoId);
            resposta.setExercicioId(exercicioId);
            resposta.setRespostaAluno(respostaAluno);
            resposta.setFeedbackIa(feedback);
            resposta.setAcertou(acertou);
            respostaDAO.salvar(resposta);

            req.setAttribute("exercicio", exercicio);
            req.setAttribute("respostaAluno", respostaAluno);
            req.setAttribute("feedback", feedback);
            req.setAttribute("acertou", acertou);
            req.setAttribute("materiaId", materiaId);
            req.getRequestDispatcher("/WEB-INF/views/app/exercicios/feedback.jsp").forward(req, resp);

        } catch (IOException ie) {
            ie.printStackTrace();
            resp.sendRedirect(req.getContextPath() +
                "/app/exercicios?acao=quiz&id=" + exercicioId + "&materiaId=" + materiaId);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/app/exercicios?materiaId=" + materiaId);
        }
    }

    private void exibirResultado(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession httpSessao = req.getSession();
        Integer sessaoId = (Integer) httpSessao.getAttribute("sessaoEstudoId");

        if (sessaoId == null) { resp.sendRedirect(req.getContextPath() + "/app/materias"); return; }

        try {
            int totalQuestoes = (Integer) httpSessao.getAttribute("totalQuestoes");
            int totalAcertos  = (Integer) httpSessao.getAttribute("totalAcertos");

            sessaoDAO.encerrar(sessaoId, totalQuestoes, totalAcertos);

            List<Resposta> respostas = respostaDAO.listarPorSessao(sessaoId);

            req.setAttribute("totalQuestoes", totalQuestoes);
            req.setAttribute("totalAcertos", totalAcertos);
            req.setAttribute("respostas", respostas);

            httpSessao.removeAttribute("sessaoEstudoId");
            httpSessao.removeAttribute("totalQuestoes");
            httpSessao.removeAttribute("totalAcertos");

            req.getRequestDispatcher("/WEB-INF/views/app/exercicios/resultado.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/app/materias");
        }
    }

    private Usuario getUsuario(HttpServletRequest req) {
        return (Usuario) req.getSession().getAttribute("usuarioLogado");
    }

    private int parseId(String valor) {
        try { return Integer.parseInt(valor); }
        catch (NumberFormatException e) { return -1; }
    }
}
