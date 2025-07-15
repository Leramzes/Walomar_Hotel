package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionEmployee;
import development.team.hoteltransylvania.Business.GestionReportes;
import development.team.hoteltransylvania.Business.GestionVentas;
import development.team.hoteltransylvania.DTO.AllInfoReporteVenta;
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
            int pestania = Integer.parseInt(req.getParameter("pst"));

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

            int totalVentas = 0;
            int count = 1;

            switch (pestania) {
                case 1:

                    break;


                case 2:
                    // Obtener lista filtrada
                    List<AllInfoReporteVenta> ventasHabFiltradas = GestionReportes.filterReportesHabitacion(idEmpleado, fechaFiltrada, page, size);

                    totalVentas = ventasHabFiltradas.size();
                    double totalPorEmpleado = 0.0;

                    count = 1;
                    for (AllInfoReporteVenta venta : ventasHabFiltradas) {
                        out.println("<tr>");
                        out.println("<td>" + count + "</td>");
                        out.println("<td>" + venta.getTipoVenta() + "</td>");
                        out.println("<td>" + venta.getNumeroHabitacion() + "</td>");
                        out.println("<td>" + venta.getNombreArticulo() + "</td>");
                        out.println("<td>" + (venta.getCantArticulo() > 0 ? venta.getCantArticulo() : "-") + "</td>");
                        out.println("<td>" + (venta.getPrecioUnitArticulo() > 0 ? venta.getPrecioUnitArticulo() : "-") + "</td>");
                        out.println("<td>" + venta.getPrecioTotalArticulo() + "</td>");
                        out.println("<td>" + venta.getFecha_hora_compra() + "</td>");
                        out.println("<td>" + venta.getNombreEmpleado() + "</td>");
                        out.println("</tr>");
                        count++;
                    }
                    out.println("<!--COUNT:" + totalVentas + "-->");
                    out.println("<!--TOTAL_EMPLEADO:" + totalPorEmpleado + "-->");

                    break;


                case 3:
                    // Obtener lista filtrada
                    List<AllInfoVentasDirecta> ventasFiltradas = GestionVentas.filterVentaDirecta(idEmpleado, fechaFiltrada, page, size);

                    totalVentas = ventasFiltradas.size();
                    totalPorEmpleado = 0.0;
                    if (fechaFiltrada != null) {
                        if (idEmpleado == -1) {
                            totalPorEmpleado = GestionVentas.getMontoTotalVentaPorFecha(fechaFiltrada); // 🔁 todos los empleados
                        } else {
                            totalPorEmpleado = GestionVentas.getMontoTotalVentaPorEmpleadoYFecha(idEmpleado, fechaFiltrada); // 👤 específico
                        }
                    }
                    count = 1;
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
                    out.println("<!--TOTAL_EMPLEADO:" + totalPorEmpleado + "-->");
                    break;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
