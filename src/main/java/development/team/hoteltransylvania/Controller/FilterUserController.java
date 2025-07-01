package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionEmployee;
import development.team.hoteltransylvania.DTO.usersEmployeeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "filterUserServlet", urlPatterns = {"/filterUserServlet"})
public class FilterUserController extends HttpServlet {
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
            List<usersEmployeeDTO> employees = GestionEmployee.filterEmployees(filter, estate, page, size);
            int totalEmployee = GestionEmployee.countFilteredEmployee(filter, estate);

            int count = 1;
            for (usersEmployeeDTO employee : employees) {
                out.println("<tr>");
                out.println("<td>" + count + "</td>");
                out.println("<td>" + employee.getName_employee() + "</td>");
                out.println("<td>" + employee.getName_user() + "</td>");
                out.println("<td>" + employee.getEmail_user() + "</td>");
                out.println("<td>" + employee.getTipo_user() + "</td>");
                out.println("<td>" + employee.getFecha_caducidad() + "</td>");
                out.println("<td>" + employee.getEstado_user() + "</td>");
                out.println("<td class='d-flex justify-content-center gap-1'>");
                out.println("  <button class='btn btn-warning btn-sm' id='btn-editar' " +
                        "data-bs-toggle='modal' " +
                        "data-bs-target='#modalEditarUsuario' " +
                        "onclick='editarUser(" + employee.getId_employee() + ")'>✏️</button>");
                out.println("  <form action='user' method='post'>");
                out.println("    <input type='hidden' name='idUser' value='" + employee.getId_user() + "'>");
                out.println("    <input type='hidden' name='accion' value='restartPass'>");
                out.println("    <button class='btn btn-secondary btn-sm'>🔑</button>");
                out.println("  </form>");

                if (employee.getEstado_user().equals("Activo")) {
                    out.println("  <form action='user' method='post'>");
                    out.println("    <input type='hidden' name='idUser' value='" + employee.getId_user() + "'>");
                    out.println("    <input type='hidden' name='accion' value='delete'>");
                    out.println("    <button class='btn btn-danger btn-sm'>❌</button>");
                    out.println("  </form>");
                } else {
                    out.println("  <form action='user' method='post'>");
                    out.println("    <input type='hidden' name='idUser' value='" + employee.getId_user() + "'>");
                    out.println("    <input type='hidden' name='accion' value='activate'>");
                    out.println("    <button class='btn btn-success btn-sm'>✅</button>");
                    out.println("  </form>");
                }
                out.println("</td>");
                out.println("</tr>");

                count++;
            }
            out.println("<!--COUNT:" + totalEmployee + "-->");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
