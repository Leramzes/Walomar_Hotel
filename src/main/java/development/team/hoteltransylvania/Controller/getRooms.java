package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionClient;
import development.team.hoteltransylvania.Business.GestionReservation;
import development.team.hoteltransylvania.Business.GestionRoom;
import development.team.hoteltransylvania.DTO.TableReservationDTO;
import development.team.hoteltransylvania.Model.Client;
import development.team.hoteltransylvania.Model.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "getRooms", urlPatterns = {"/getRooms"})
public class getRooms extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            int filter = Integer.parseInt(request.getParameter("filter"));

            List<Room> rooms = GestionRoom.getRoomByTypeRoom(filter);
            String msjEscapado = "";

            // Construcción de HTML con los datos del cliente
            out.println("<label for='habitacion'>Habitación</label>");
            out.println("<select class='form-select' id='habitacion' name='habitacion' required onchange='updateTotal()'>");
            for (Room room : rooms) {
                List<TableReservationDTO> reservaAsociate = GestionReservation.getRoomAsociateReservationPendiete(room.getId());
                StringBuilder msjBuilder = new StringBuilder();

                if (reservaAsociate != null && !reservaAsociate.isEmpty()) {
                    msjBuilder.append(reservaAsociate.size()).append(" reserva");
                    if (reservaAsociate.size() > 1) {
                        msjBuilder.append("s");
                    }
                    msjBuilder.append(":\n");
                    for (TableReservationDTO reserva : reservaAsociate) {
                        String fechaInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(reserva.getCheckInDate());
                        String fechaFin = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(reserva.getCheckOutDate());
                        msjBuilder.append("- Del ").append(fechaInicio).append(" al ").append(fechaFin).append("\n");
                    }
                }

                // Escapar para HTML y atributo (remplazar salto de línea por \n o espacios)
                msjEscapado = msjBuilder.toString()
                        .replace("\\", "\\\\")       // escapa backslashes
                        .replace("\"", "&quot;")     // escapa comillas dobles
                        .replace("'", "\\'")         // escapa comillas simples
                        .replace("\n", "\\n");
                out.println("<option value='"+room.getId()+"' data-precio='"+room.getPrice()+"' data-msj='"+msjEscapado+"'>"+room.getNumber()+"</option>");
            }
            out.println("</select>");

            out.println("<div class=\"form-text text-danger small\" id=\"msjRoom\">");
            out.println("</div>");

        } catch (Exception e) {
            e.printStackTrace(); // Para depuración en el servidor
        }
    }
}
