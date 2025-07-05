package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionReservation;
import development.team.hoteltransylvania.Business.GestionRoom;
import development.team.hoteltransylvania.DTO.TableReservationDTO;
import development.team.hoteltransylvania.Model.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "filterReservations", urlPatterns = {"/filterReservations"})
public class FilterReservationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            String clientFilter = req.getParameter("clientFilter");
            String docFilter = req.getParameter("docFilter");
            String fecDesdeFilter = req.getParameter("fecDesdeFilter");
            String fecHastaFilter = req.getParameter("fecHastaFilter");
            String statusFilter = req.getParameter("statusFilter");

            // Obtener parámetros de paginación (si no existen, se asignan valores por defecto)
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
            int size = req.getParameter("size") != null ? Integer.parseInt(req.getParameter("size")) : 10;

            // Obtener lista paginada
            List<TableReservationDTO> reservations = GestionReservation.filterReservation(clientFilter, docFilter, fecDesdeFilter,
                    fecHastaFilter, statusFilter, page, size);
            int totalReservation = GestionReservation.countFilteredReservations(clientFilter, docFilter, fecDesdeFilter,
                    fecHastaFilter, statusFilter);

            int count = 1;
            for (TableReservationDTO reservation : reservations) {
                out.println("<tr>");
                out.println("  <td>" + count + "</td>");
                out.println("  <td>" + reservation.getClientName() + "</td>");
                out.println("  <td>" + reservation.getDocumentType() + "</td>");
                out.println("  <td>" + reservation.getDocumentNumber() + "</td>");
                out.println("  <td>" + reservation.getNumberRoom() + " - " + reservation.getRoomType() + "</td>");
                out.println("  <td>" + reservation.getCheckInDate() + "</td>");
                out.println("  <td>" + reservation.getCheckOutDate() + "</td>");
                String estado = reservation.getReservationStatus();
                Timestamp fechaIngreso = reservation.getFecha_ingreso();
                Timestamp fechaInicio = reservation.getCheckInDate();
                boolean permitCancel = false;

                ZoneId zonaPeru = ZoneId.of("America/Lima");
                LocalDateTime ahora = ZonedDateTime.now(zonaPeru).toLocalDateTime();

                String contenidoTD;

                if ("Cancelada".equalsIgnoreCase(estado)) {
                    contenidoTD = "----";

                } else if ("Pendiente".equalsIgnoreCase(estado)) {
                    if (fechaIngreso == null && fechaInicio != null) {
                        LocalDateTime inicio = fechaInicio.toLocalDateTime();
                        long minutosPasados = Duration.between(inicio, ahora).toMinutes();

                        if (minutosPasados > 20) {
                            permitCancel = true;
                            contenidoTD = "<span style='color: red;'>Fuera del tiempo de tolerancia</span>";
                        } else {
                            contenidoTD = "Aún no ingresó";
                        }
                    } else if (fechaIngreso != null) {
                        contenidoTD = fechaIngreso.toString(); // Formatear si quieres
                    } else {
                        contenidoTD = "Aún no ingresó";
                    }

                } else {
                    // Ocupada, Finalizada, etc.
                    if (fechaIngreso != null) {
                        contenidoTD = fechaIngreso.toString(); // ✅ Mostrar fecha real de ingreso
                    } else {
                        contenidoTD = "Aún no registrado"; // O algo más claro que "----"
                    }
                }

                out.println("<td>" + contenidoTD + "</td>");
                out.println("  <td>" + reservation.getReservationStatus() + "</td>");
                out.println("  <td class='align-middle text-center'>");
                out.println("    <div class='d-flex justify-content-center align-items-center gap-1'>");
                out.println("      <button class='btn btn-info btn-sm' data-bs-toggle='modal' data-bs-target='#modalVerDetalle'");
                out.println("              title='Ver Detalle'");
                out.println("              onclick='detalleReserva(" + reservation.getIdReservation() + ")'>");
                out.println("          👁️");
                out.println("      </button>");

                String estadoEdit = reservation.getReservationStatus();
                boolean puedeEditar = "Pendiente".equalsIgnoreCase(estadoEdit) || "Ocupada".equalsIgnoreCase(estadoEdit);
                if (puedeEditar) {
                    out.println("      <button class='btn btn-warning btn-sm' data-bs-toggle='modal' data-bs-target='#modalEditarReserva' " +
                            "title='Editar Reserva' onclick='editarReserva("+reservation.getIdReservation()+")'>✏️</button>");
                }

                if (permitCancel) {
                    out.println("<form action=\"recepController\" method=\"post\">");
                    out.println("    <input type=\"hidden\" name=\"vista\" value=\"reserva\">");
                    out.println("    <input type=\"hidden\" name=\"idReserva\" value=\"" + reservation.getIdReservation() + "\">");
                    out.println("    <input type=\"hidden\" name=\"roomSelect\" value=\"" + reservation.getIdRoom() + "\">");
                    out.println("<input type=\"hidden\" name=\"accion\" value=\"cancelar\">");
                    out.println("    <button type=\"button\" class=\"btn btn-danger btn-sm\" title=\"Cancelar Reserva\" onclick=\"validarCancelacionReserva()\">❌\n" +
                            "                            </button>");
                    out.println("</form>");
                }
                out.println("    </div>");
                out.println("  </td>");
                out.println("</tr>");

                count++;
            }
            out.println("<!--COUNT:" + totalReservation + "-->");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
