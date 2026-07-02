package com.studymate.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet responsável pelo logout.
 * GET /logout → invalida a sessão e redireciona para o login
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sessao = req.getSession(false);
        if (sessao != null) {
            sessao.invalidate();
        }

        resp.sendRedirect(req.getContextPath() + "/login?logout=true");
    }
}
