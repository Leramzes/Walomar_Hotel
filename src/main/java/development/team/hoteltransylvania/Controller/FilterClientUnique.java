package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionClient;
import development.team.hoteltransylvania.Model.Client;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "filterClientUniq", urlPatterns = {"/filterClientUniq"})
public class FilterClientUnique extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            String filter = req.getParameter("filter");

            if (filter == null || filter.trim().isEmpty()) {
                out.println("<p style='color:red;' class='mt-2'>⚠️ Debes ingresar un documento</p>");
                return;
            }

            Client client = GestionClient.getClient(filter);
            if (client == null) {
                out.println("<p style='color:red;' class='mt-2'>❌ Cliente no encontrado</p>");
                return;
            }

            // Construcción de HTML con los datos del cliente
            out.println("<div class=\"mb-3\">");
            out.println("    <label for=\"nombre\">Nombre Completo</label>");
            out.println("    <input type=\"hidden\" class=\"form-control\" id=\"idCLiente\" name=\"idCLiente\" value=\"" + client.getId() + "\" required>");
            out.println("    <input type=\"text\" class=\"form-control\" id=\"nombre\" name=\"nombre\" value=\"" + client.getName() + "\" required readonly>");
            out.println("</div>");

            out.println("<div class=\"mb-3\">");
            out.println("    <label for=\"tipoDocumento\">Tipo de Documento</label>");
            out.println("    <input type=\"text\" class=\"form-control\" id=\"tipoDocumento\" name=\"tipoDocumento\" value=\"" +
                    client.getTypeDocument().name() + "\" required readonly>");
            out.println("</div>");

            out.println("<div class=\"mb-3\">");
            /*out.println("    <label for=\"documento\">Documento</label>");*/
            out.println("    <input type=\"hidden\" class=\"form-control\" id=\"documento\" name=\"documento\" value=\"" +
                    client.getNumberDocument() + "\" required readonly>");
            out.println("</div>");

            out.println("<div class=\"mb-3\">");
            out.println("    <label for=\"correo\">Correo</label>");
            out.println("    <input type=\"email\" class=\"form-control\" id=\"correo\" name=\"correo\" value=\"" +
                    client.getEmail() + "\" pattern=\"[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$\" required readonly>");
            out.println("</div>");

        } catch (Exception e) {
            e.printStackTrace(); // Para depuración en el servidor
        }
    }
}
