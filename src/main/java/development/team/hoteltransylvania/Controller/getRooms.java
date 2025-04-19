package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionClient;
import development.team.hoteltransylvania.Business.GestionRoom;
import development.team.hoteltransylvania.Model.Client;
import development.team.hoteltransylvania.Model.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "getRooms", urlPatterns = {"/getRooms"})
public class getRooms extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            int filter = Integer.parseInt(request.getParameter("filter"));

            List<Room> rooms = GestionRoom.getRoomByTypeRoom(filter);

            // Construcción de HTML con los datos del cliente
            out.println("<label for='habitacion'>Habitación</label>");
            out.println("<select class='form-select' id='habitacion' name='habitacion' required onchange='updateTotal()'>");
            for (Room room : rooms) {
                out.println("<option value='"+room.getId()+"' data-precio='"+room.getPrice()+"'>"+room.getNumber()+"</option>");
            }
            out.println("</select>");

        } catch (Exception e) {
            e.printStackTrace(); // Para depuración en el servidor
        }
    }
}
