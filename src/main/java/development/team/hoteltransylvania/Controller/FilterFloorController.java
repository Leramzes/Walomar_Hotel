package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionFloor;
import development.team.hoteltransylvania.Model.Floor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "filterFloorServlet", urlPatterns = {"/filterFloorServlet"})
public class FilterFloorController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            String filter = req.getParameter("filter");
            String estate = req.getParameter("estate");

            // Obtener parámetros de paginación (si no existen, se asignan valores por defecto)
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
            int size = req.getParameter("size") != null ? Integer.parseInt(req.getParameter("size")) : 10;

            // Obtener lista paginada
            List<Floor> Floors = GestionFloor.filterFloors(filter, estate, page, size);
            int totalFloor = GestionFloor.countFilteredFloors(filter, estate);
            int count = 1;
            for (Floor floors : Floors) {
                out.println("<tr>");
                out.println("  <td>" + count + "</td>");
                out.println("  <td>" + floors.getName() + "</td>");
                out.println("  <td>" + floors.getStatus() + "</td>");
                out.println("  <td class='d-flex justify-content-center gap-1'>");
                out.println("    <button class='btn btn-warning btn-sm'");
                out.println("            data-bs-toggle='modal'");
                out.println("            data-bs-target='#modalEditarPiso'");
                out.println("            onclick='editarFloor(" + floors.getId() + ")'>✏️</button>");

                if (floors.getStatus().equals("Activo")) {
                    out.println("    <form action='floorcontroller' method='post'>");
                    out.println("      <input type='hidden' name='idFloor' value='" + floors.getId() + "'>");
                    out.println("      <input type='hidden' name='actionFloor' value='inactivate'>");
                    out.println("      <button class='btn btn-danger btn-sm'>❌</button>");
                    out.println("    </form>");
                } else {
                    out.println("    <form action='floorcontroller' method='post'>");
                    out.println("      <input type='hidden' name='idFloor' value='" + floors.getId() + "'>");
                    out.println("      <input type='hidden' name='actionFloor' value='activate'>");
                    out.println("      <button class='btn btn-success btn-sm'>✅</button>");
                    out.println("    </form>");
                }
                out.println("  </td>");
                out.println("</tr>");

                count++;
            }
            out.println("<!--COUNT:" + totalFloor + "-->");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

