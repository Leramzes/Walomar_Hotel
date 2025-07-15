package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionClient;
import development.team.hoteltransylvania.Model.Client;
import development.team.hoteltransylvania.Services.Emails;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "envioCorreo", urlPatterns = {"/envioCorreo"})
@MultipartConfig
public class EnvioCorreoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Obtener parámetros
            String idReserva = request.getParameter("idReserva");
            String idClient = request.getParameter("idClient");
            String tipo = request.getParameter("tipo"); // "boleta" o "factura"

            // 2. Obtener archivo PDF
            Part archivoPart = request.getPart("archivo");
            if (archivoPart == null || archivoPart.getSize() == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se envió el archivo.");
                return;
            }

            // 3. Obtener datos del cliente desde el backend
            Client clienteEnvio = GestionClient.getClientById(Integer.parseInt(idClient));
            String destinatario = clienteEnvio.getEmail();
            String nombreCliente = clienteEnvio.getName() + " " + clienteEnvio.getApPaterno() + " " + clienteEnvio.getApMaterno();

            // 4. Guardar el archivo temporalmente
            File archivoTemp = File.createTempFile("comprobante_temp", ".pdf");
            try (InputStream input = archivoPart.getInputStream();
                 FileOutputStream fos = new FileOutputStream(archivoTemp)) {
                byte[] buffer = new byte[1024];
                int bytesLeidos;
                while ((bytesLeidos = input.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesLeidos);
                }
            }

            // 5. Preparar asunto y mensaje
            String asunto = tipo.equals("factura") ? "Su FACTURA de hospedaje" : "Su BOLETA de hospedaje";
            String mensaje = "Estimado(a) " + nombreCliente + ",<br><br>"
                    + "Adjunto encontrará su " + tipo + " correspondiente a su estadía en nuestro hotel.<br><br>"
                    + "Gracias por preferirnos.<br><br><strong>Walomar Hotel</strong>";

            // 6. Enviar correo con archivo adjunto
            Emails.enviarCorreoConAdjunto(destinatario, asunto, mensaje, true, archivoTemp);

            // 7. Eliminar archivo temporal
            archivoTemp.delete();

            // 8. Responder OK
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al enviar correo.");
        }
    }
}
