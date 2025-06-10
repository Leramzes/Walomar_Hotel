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

                String contenidoTD = "";

                if ("Cancelada".equalsIgnoreCase(estado)) {
                    contenidoTD = "----";
                } else if (fechaIngreso == null) {
                    if ("Pendiente".equalsIgnoreCase(estado) && fechaInicio != null) {
                        LocalDateTime inicio = fechaInicio.toLocalDateTime();
                        LocalDateTime ahora = LocalDateTime.now();
                        long minutosPasados = Duration.between(inicio, ahora).toMinutes();

                        if (minutosPasados > 20) {
                            contenidoTD = "<span style='color: red;'>Fuera del tiempo de tolerancia</span>";
                        } else {
                            contenidoTD = "Aún no ingresó";
                        }
                    } else {
                        contenidoTD = "Aún no ingresó";
                    }
                } else {
                    // Ya tiene ingreso
                    LocalDateTime ingreso = fechaIngreso.toLocalDateTime();
                    LocalDateTime ahora = LocalDateTime.now();
                    long minutosPasados = Duration.between(ingreso, ahora).toMinutes();

                    if ("Pendiente".equalsIgnoreCase(estado) && minutosPasados > 20) {
                        contenidoTD = "<span style='color: red;'>Fuera del tiempo de tolerancia</span>";
                    } else {
                        contenidoTD = fechaIngreso.toString(); // puedes formatear si lo deseas
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
                out.println("      <button class='btn btn-warning btn-sm' data-bs-toggle='modal' data-bs-target='#modalEditarReserva'>✏️</button>");
                out.println("      <button class='btn btn-danger btn-sm'>❌</button>");
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
