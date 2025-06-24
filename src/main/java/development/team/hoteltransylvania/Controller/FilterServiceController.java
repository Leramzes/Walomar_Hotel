package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionService;
import development.team.hoteltransylvania.Model.Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "filterServiceServlet", urlPatterns = {"/filterServiceServlet"})
public class FilterServiceController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            String filter = req.getParameter("filter");
            String estateParam = req.getParameter("estate");
            Integer estate = null;

            if (estateParam != null && !estateParam.trim().isEmpty()) {
                estate = Integer.parseInt(estateParam);
            }

            // Obtener parámetros de paginación (si no existen, se asignan valores por defecto)
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
            int size = req.getParameter("size") != null ? Integer.parseInt(req.getParameter("size")) : 10;

            // Obtener lista paginada
            List<Service> services = GestionService.filterServices(filter, estate, page, size);
            int totalServices = GestionService.countFilteredService(filter, estate);

            int count = 1;
            for (Service service : services) {
                boolean disponible = service.getStatus() == 1;
                String btnClass = disponible ? "btn-danger" : "btn-success";
                String btnText = disponible ? "❌" : "✅";

                out.println("<tr>");
                out.println("    <td>" + count + "</td>");
                out.println("    <td>" + service.getName() + "</td>");
                out.println("    <td>S/. " + service.getPrice() + "</td>");
                out.println("    <td class='align-middle text-center'>");
                out.println("        <div class='d-flex justify-content-center align-items-center gap-1'>");
                out.println("            <button class='btn btn-warning btn-sm' id='btn-editar'");
                out.println("                    data-bs-toggle='modal'");
                out.println("                    data-bs-target='#modalEditarCatalogoServicio'");
                out.println("                    onclick='abrirModalEditarServicio(" + service.getId() + ")'>");
                out.println("                ✏️");
                out.println("            </button>");
                out.println("            <form action='serviciocontrol' method='post'>");
                out.println("                <input type='hidden' name='idservice' value='" + service.getId() + "'>");
                out.println("                <input type='hidden' name='actionservice' value='inactive'>");
                out.println("                <input type='hidden' name='availability' value='" + service.getStatus() + "'>");
                out.println("                <button class='btn " + btnClass + " btn-sm'>" + btnText + "</button>");
                out.println("            </form>");
                out.println("        </div>");
                out.println("    </td>");
                out.println("</tr>");
                count++;
            }
            out.println("<!--COUNT:" + totalServices + "-->");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
