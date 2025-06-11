package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.Business.GestionRoom;
import development.team.hoteltransylvania.Model.Product;
import development.team.hoteltransylvania.Model.Room;
import development.team.hoteltransylvania.Model.StatusRoom;
import development.team.hoteltransylvania.Model.TypeRoom;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "roomcontroller", urlPatterns = {"/roomcontroller"})
public class RoomController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("get".equals(action)) {
            int idroom = Integer.parseInt(req.getParameter("idroom"));
            System.out.println(idroom);

            Room room = GestionRoom.getRoomById(idroom);
            System.out.println(room.toString());
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(new Gson().toJson(room));
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("actionRoom");

        switch (action) {
            case "add":
                String name = req.getParameter("nombre");
                TypeRoom type = GestionRoom.getTypeRoomById(Integer.parseInt(req.getParameter("tipo")));
                double price = Double.parseDouble(req.getParameter("precio"));
                int floor = Integer.parseInt(req.getParameter("piso"));
                boolean isNumberExiste = GestionRoom.registerRoom(new Room(name,type, StatusRoom.libre,price,floor));
                if (!isNumberExiste) {
                    // Si no se pudo registrar (porque ya existe), redirige con parámetro
                    resp.sendRedirect("menu.jsp?view=habitaciones&errorHab=existNumber");
                } else {
                    // Si se registra bien, redirige normalmente
                    resp.sendRedirect("menu.jsp?view=habitaciones");
                }

                break;
            case "disponible":
                int roomid = Integer.parseInt(req.getParameter("idroom"));
                int availability = Integer.parseInt(req.getParameter("availability"));
                if(!GestionRoom.hasUpcomingReservations(roomid)) {
                    GestionRoom.updateAvailability(roomid,availability);

                }

                resp.sendRedirect("menu.jsp?view=habitaciones");
                break;
            case "update":
                int idroom = Integer.parseInt(req.getParameter("idroom"));
                Room room = GestionRoom.getRoomById(idroom);

                String nombredit = req.getParameter("nombredit");
                TypeRoom typedit = GestionRoom.getTypeRoomById(Integer.parseInt(req.getParameter("tipo")));
                double precioedit = Double.parseDouble(req.getParameter("precioedit"));
                String statusedit = req.getParameter("statusedit");

                room.setId(idroom); room.setNumber(nombredit); room.setTypeRoom(typedit); room.setPrice(precioedit);
                room.setStatusRoom(StatusRoom.valueOf(statusedit));

                boolean isNumberExisteUpdate = GestionRoom.updateRoom(room);
                if (!isNumberExisteUpdate) {
                    // Si no se pudo registrar (porque ya existe), redirige con parámetro
                    resp.sendRedirect("menu.jsp?view=habitaciones&errorHab=existNumber");
                } else {
                    // Si se registra bien, redirige normalmente
                    resp.sendRedirect("menu.jsp?view=habitaciones");
                }
                break;
        }
    }
}
