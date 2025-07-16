package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionEmployee;
import development.team.hoteltransylvania.Business.GestionReportes;
import development.team.hoteltransylvania.Business.GestionVentas;
import development.team.hoteltransylvania.DTO.AllInfoReporteAlquiler;
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
            double totalPorEmpleado = 0.0;

            switch (pestania) {
                case 1:
                    // Obtener lista filtrada
                    List<AllInfoReporteAlquiler> alquilerFiltradas = GestionReportes.filterReportesReservation(idEmpleado, fechaFiltrada, page, size);

                    int totalAlquileres = alquilerFiltradas.size();
                    totalPorEmpleado = 0.0;

                    count = 1;
                    for (AllInfoReporteAlquiler alquiler : alquilerFiltradas) {
                        boolean esCancelada = "Cancelada *".equals(alquiler.getTipoAlquiler());

                        String clasePagoTotal = esCancelada ? "" : "text-success-emphasis fw-bold";
                        String pagoTotalTexto = esCancelada
                                ? alquiler.getPago_total_reserva() + " *"
                                : String.valueOf(alquiler.getPago_total_reserva());

                        String claseAdelanto = (esCancelada && alquiler.getAdelanto() > 0)
                                ? "text-success-emphasis fw-bold"
                                : "";

                        out.println("<tr>");
                        out.println("<td>" + count + "</td>");
                        out.println("<td>" + alquiler.getIdReservation() + "</td>");
                        out.println("<td>" + alquiler.getTipoAlquiler() + "</td>");
                        out.println("<td>" + alquiler.getNumberRoom() + "-" + alquiler.getRoomType() + "</td>");
                        out.println("<td>" + alquiler.getDsct() + "</td>");
                        out.println("<td>" + alquiler.getCobro_extra() + "</td>");
                        out.println("<td class='" + claseAdelanto + "'>" + alquiler.getAdelanto() + "</td>");
                        out.println("<td>" + (alquiler.getTotal_consumo_productos() + alquiler.getTotal_consumo_servicios()) + "</td>");
                        out.println("<td>" + alquiler.getTotal_penalidad() + "</td>");
                        out.println("<td class='" + clasePagoTotal + "'>" + pagoTotalTexto + "</td>");
                        out.println("<td class='text-danger'>Sí</td>");
                        out.println("<td class='d-flex justify-content-center gap-1'>");
                        out.println("    <button class='btn btn-info btn-sm' data-bs-toggle='modal' data-bs-target='#modalVerDetalle'>");
                        out.println("        👁️");
                        out.println("    </button>");
                        out.println("</td>");
                        out.println("</tr>");

                        count++;
                    }
                    out.println("<!--COUNT:" + totalAlquileres + "-->");
                    out.println("<!--TOTAL_EMPLEADO:" + totalPorEmpleado + "-->");

                    break;


                case 2:
                    // Obtener lista filtrada
                    List<AllInfoReporteVenta> ventasHabFiltradas = GestionReportes.filterReportesHabitacion(idEmpleado, fechaFiltrada);

                    totalVentas = ventasHabFiltradas.size();
                    totalPorEmpleado = 0.0;

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
