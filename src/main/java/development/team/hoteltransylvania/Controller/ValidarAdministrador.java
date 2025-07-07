package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionReservation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "validarAdmin", urlPatterns = {"/validarAdmin"})
public class ValidarAdministrador extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usuario = req.getParameter("usuario");
        String clave = req.getParameter("clave");

        boolean esValido = GestionReservation.validarAdmin(usuario, clave);

        resp.setContentType("text/plain");
        resp.getWriter().write(esValido ? "OK" : "ERROR");
    }
}
