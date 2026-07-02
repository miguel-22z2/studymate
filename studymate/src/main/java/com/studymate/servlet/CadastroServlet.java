package com.studymate.servlet;

import com.studymate.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet responsável pelo cadastro de novos usuários.
 * GET  /cadastro → exibe o formulário de cadastro
 * POST /cadastro → processa e salva o novo usuário
 */
@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/views/auth/cadastro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nome            = req.getParameter("nome");
        String email           = req.getParameter("email");
        String senha           = req.getParameter("senha");
        String confirmarSenha  = req.getParameter("confirmarSenha");

        // Validação dos campos
        if (nome == null || nome.isBlank() ||
            email == null || email.isBlank() ||
            senha == null || senha.isBlank()) {

            req.setAttribute("erro", "Preencha todos os campos.");
            req.getRequestDispatcher("/WEB-INF/views/auth/cadastro.jsp").forward(req, resp);
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            req.setAttribute("erro", "As senhas não coincidem.");
            req.setAttribute("nome", nome);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/auth/cadastro.jsp").forward(req, resp);
            return;
        }

        if (senha.length() < 6) {
            req.setAttribute("erro", "A senha deve ter pelo menos 6 caracteres.");
            req.setAttribute("nome", nome);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/auth/cadastro.jsp").forward(req, resp);
            return;
        }

        try {
            boolean sucesso = usuarioDAO.cadastrar(nome.trim(), email.trim().toLowerCase(), senha);

            if (sucesso) {
                // Redireciona pro login com mensagem de sucesso
                resp.sendRedirect(req.getContextPath() + "/login?cadastro=sucesso");
            } else {
                req.setAttribute("erro", "Este e-mail já está cadastrado.");
                req.setAttribute("nome", nome);
                req.getRequestDispatcher("/WEB-INF/views/auth/cadastro.jsp").forward(req, resp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("erro", "Erro interno. Tente novamente.");
            req.getRequestDispatcher("/WEB-INF/views/auth/cadastro.jsp").forward(req, resp);
        }
    }
}
