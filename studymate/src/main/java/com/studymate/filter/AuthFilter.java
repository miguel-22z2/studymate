package com.studymate.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Protege todas as rotas dentro de /app/*.
 * Se o usuário não estiver logado, redireciona para /login.
 */
@WebFilter("/app/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession sessao = req.getSession(false);
        boolean logado = (sessao != null && sessao.getAttribute("usuarioLogado") != null);

        if (logado) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
