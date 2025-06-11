package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionRoom;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "checkreservations", urlPatterns = {"/checkreservations"})
public class CheckReservationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int roomId = Integer.parseInt(req.getParameter("id"));
        boolean result = GestionRoom.hasUpcomingReservations(roomId);
        resp.setContentType("application/json");
        resp.getWriter().write("{\"hasReservations\": " + result + "}");
    }
}
