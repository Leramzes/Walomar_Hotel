package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionClient;
import development.team.hoteltransylvania.Model.Client;
import development.team.hoteltransylvania.Model.TypeDocument;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "filterClientRecp", urlPatterns = {"/filterClientRecp"})
public class filterClientRecepcion extends HttpServlet {
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
            out.println("<div class='row'>");
            out.println("<div class='col-md-6 mt-2'>");
            out.println("    <label for='tipoDocumento' class='form-label'><strong>Tipo de Documento:</strong></label>");
            out.println("    <input type='text' class='form-control' id='tipoDocumento' value='" + client.getTypeDocument() + "' readonly>");
            out.println("</div>");

            out.println("<div class='col-md-6 mt-2'>");
            out.println("    <label for='documento' class='form-label'><strong>Documento:</strong></label>");
            out.println("    <input type='text' class='form-control' id='documento' value='" + client.getNumberDocument() + "' readonly>");
            out.println("</div>");
            out.println("</div>");

            String typeName = client.getTypeDocument().equals(TypeDocument.RUC) ? "Razón Social:" : "Nombre Completo:";

            out.println("<div class='col-md-12 mt-2'>");
            out.println("    <label for='name' class='form-label'><strong>"+typeName+"</strong></label>");
            out.println("    <input type='text' class='form-control' id='name' value='" + client.getName() +" "+
                    client.getApPaterno() +" "+ client.getApMaterno() + "' readonly>");
            out.println("</div>");

            out.println("<div class='col-md-12 mt-2'>");
            out.println("    <label for='correo' class='form-label'><strong>Correo:</strong></label>");
            out.println("    <input type='email' class='form-control' id='correo' value='" + client.getEmail() + "' readonly>");
            out.println("</div>");

            out.println("<div class='form-check d-flex justify-content-center mt-2'>");
            out.println("    <input class='form-check-input me-2' type='checkbox' value='' id='enviarCorreo'>");
            out.println("    <label class='form-check-label' for='enviarCorreo'>");
            out.println("        <strong>Enviar estado de cuenta por <span class='text-primary'>correo</span>.</strong>");
            out.println("    </label>");
            out.println("</div>");

            out.println("<div class='col-md-12 mt-2'>");
            out.println("    <label for='telefono' class='form-label'><strong>Teléfono:</strong></label>");
            out.println("    <input type='text' class='form-control' value='" + client.getTelephone() + "' id='telefono' readonly>");
            out.println("</div>");

        } catch (Exception e) {
            e.printStackTrace(); // Para depuración en el servidor
        }
    }
}
