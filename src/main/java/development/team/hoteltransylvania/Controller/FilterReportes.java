package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionEmployee;
import development.team.hoteltransylvania.Business.GestionVentas;
import development.team.hoteltransylvania.DTO.AllInfoVentasDirecta;
import development.team.hoteltransylvania.DTO.usersEmployeeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.sql.Date;

@WebServlet(name = "filterReportes", urlPatterns = {"/filterReportes"})
public class FilterReportes extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            String fechaParam = req.getParameter("fecha");
            String empleadoIdParam = req.getParameter("empleadoId");

            // Convertir fecha
            Date fechaFiltrada = null;
            if (fechaParam != null && !fechaParam.trim().isEmpty()) {
                fechaFiltrada = Date.valueOf(fechaParam); // <-- convierte yyyy-MM-dd a java.sql.Date
            }

            // Convertir ID de empleado
            int idEmpleado = -1;
            if (empleadoIdParam != null && !empleadoIdParam.equalsIgnoreCase("todos")) {
                idEmpleado = Integer.parseInt(empleadoIdParam);
            }


            // Obtener parámetros de paginación (si no existen, se asignan valores por defecto)
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
            int size = req.getParameter("size") != null ? Integer.parseInt(req.getParameter("size")) : 10;

            // Obtener lista filtrada
            List<AllInfoVentasDirecta> ventasFiltradas = GestionVentas.filterVentaDirecta(idEmpleado, fechaFiltrada, page, size);

            int totalVentas = GestionVentas.countFilteredVentaDirecta(idEmpleado, fechaFiltrada);

            int count = 1;
            for (AllInfoVentasDirecta ventaFiltrada : ventasFiltradas) {
                out.println("<tr>");
                out.println("<td>" + count + "</td>");
                out.println("<td>" + ventaFiltrada.getProducto() + "</td>");
                out.println("<td>" + ventaFiltrada.getCantidad() + "</td>");
                out.println("<td>" + ventaFiltrada.getPrecio_unitario() + "</td>");
                out.println("<td>" + ventaFiltrada.getPrecio_total() + "</td>");
                out.println("<td>" + ventaFiltrada.getFecha_hora() + "</td>");
                out.println("<td>" + ventaFiltrada.getEmpleado() + "</td>");
                out.println("</tr>");
                count++;
            }
            out.println("<!--COUNT:" + totalVentas + "-->");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
