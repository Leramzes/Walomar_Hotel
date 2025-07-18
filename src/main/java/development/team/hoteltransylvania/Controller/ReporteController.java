package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionReportes;
import development.team.hoteltransylvania.Business.GestionReservation;
import development.team.hoteltransylvania.DTO.AllInfoReporteAlquiler;
import development.team.hoteltransylvania.DTO.TableReservationDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "reporteController", urlPatterns = {"/reporteController"})
public class ReporteController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("get".equals(action)) {
            int idReserva = Integer.parseInt(req.getParameter("idreserva"));

            AllInfoReporteAlquiler reservation = GestionReportes.getReporteReservationById(idReserva);


            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(new Gson().toJson(reservation));
            out.flush();
        }
    }

}
