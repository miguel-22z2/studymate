package com.studymate.servlet;

import com.studymate.dao.UsuarioDAO;
import com.studymate.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet responsável pelo login do usuário.
 * GET  /login → exibe a tela de login
 * POST /login → processa as credenciais
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Se já estiver logado, redireciona pro dashboard
        HttpSession sessao = req.getSession(false);
        if (sessao != null && sessao.getAttribute("usuarioLogado") != null) {
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        // Validação básica
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            req.setAttribute("erro", "Preencha todos os campos.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(email.trim(), senha);

            if (usuario != null) {
                // Cria sessão e armazena o usuário logado
                HttpSession sessao = req.getSession(true);
                sessao.setAttribute("usuarioLogado", usuario);
                sessao.setMaxInactiveInterval(30 * 60); // 30 minutos

                resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            } else {
                req.setAttribute("erro", "E-mail ou senha incorretos.");
                req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro interno. Tente novamente.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        }
    }
}
