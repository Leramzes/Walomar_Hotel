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

@WebServlet(name = "addTableService", urlPatterns = {"/addTableService"})
public class addTableService extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            Service service = GestionService.getserviceById(Integer.parseInt(req.getParameter("filter")));
            out.println("<tr data-id='" + service.getId() + "'>");
            out.println("    <td>" + service.getName() + "</td>");
            out.println("    <td class='precio-total'>S/. " + service.getPrice() + "</td>");
            out.println("    <td class='align-middle text-center'>");
            out.println("        <div class='d-flex justify-content-center align-items-center gap-1'>");
            out.println("            <button class='btn btn-danger btn-eliminar-servicio'><i class='fas fa-trash'></i></button>");
            out.println("        </div>");
            out.println("        <input type='hidden' name='serviceId[]' value='" + service.getId() + "'>");
            out.println("        <input type='hidden' name='precioTotal[]' value='" + service.getPrice() + "'>");
            out.println("    </td>");
            out.println("</tr>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
