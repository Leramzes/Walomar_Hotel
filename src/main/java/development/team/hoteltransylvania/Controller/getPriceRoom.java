package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionRoom;
import development.team.hoteltransylvania.Model.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "getPriceRooms", urlPatterns = {"/getPriceRooms"})
public class getPriceRoom extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            int filter = Integer.parseInt(request.getParameter("filter"));

            Room rooms = GestionRoom.getRoomById(filter);

            // Construcción de HTML con los datos del cliente
            out.println("<label for=\"totalPagar\">Total a Pagar</label>");
            out.println("<input type=\"number\" class=\"form-control\" id=\"totalPagar\" value='"+rooms.getPrice()+"' required>");


        } catch (Exception e) {
            e.printStackTrace(); // Para depuración en el servidor
        }
    }
}
