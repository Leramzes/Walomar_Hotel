package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Services.Auth;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private final Auth authService = new Auth();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (authService.logout(request)) {
            response.sendRedirect("index.jsp");
        } else {
            response.sendRedirect("index.jsp"); //MENSAJE ERROR: No hay sesión activa
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response); // Redirigir GET a POST para mayor seguridad
    }
}

