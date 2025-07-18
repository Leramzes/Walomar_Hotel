package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionDashboard;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "dashboardController", urlPatterns = {"/dashboardController"})
public class DashboardController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        switch (action) {
            case "topVendedor":
                out.print(new Gson().toJson(GestionDashboard.getTopVendedor()));
                break;
            case "topHabitacion":
                out.print(new Gson().toJson(GestionDashboard.getTopHabitacion()));
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Acción no válida\"}");
                break;
        }
    }
}
