package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionFloor;
import development.team.hoteltransylvania.Business.GestionTypeRoom;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "checkreservationbyroom", urlPatterns = {"/checkreservationbyroom"})
public class CheckReservationByFloor extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int typeRoomId = Integer.parseInt(req.getParameter("id"));
        boolean result = GestionFloor.hasUpcomingReservations(typeRoomId);
        resp.setContentType("application/json");
        resp.getWriter().write("{\"hasReservations\": " + result + "}");
    }
}
