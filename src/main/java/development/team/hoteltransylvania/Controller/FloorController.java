package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionFloor;
import development.team.hoteltransylvania.Model.Floor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "floorcontroller", urlPatterns = {"/floorcontroller"})
public class FloorController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("get".equals(action)) {
            int idFloor = Integer.parseInt(req.getParameter("idfloor"));
            System.out.println(idFloor);

            Floor floor = GestionFloor.getFloorById(idFloor);
            System.out.println(floor.toString());
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(new Gson().toJson(floor));
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("actionFloor");

        switch (action) {
            case "add":
                String nombre = req.getParameter("nombreFloor");

                boolean isregistred = GestionFloor.registerFloor(new Floor(1,nombre,"Activo"));

                if (!isregistred) {
                    // Si no se pudo registrar (porque ya existe), redirige con parámetro
                    resp.sendRedirect("menu.jsp?view=pisos&error=pisoexistente");
                } else {
                    // Si se registra bien, redirige normalmente
                    resp.sendRedirect("menu.jsp?view=pisos");
                }
                break;
            case "update":
                int id = Integer.parseInt(req.getParameter("idFloor"));
                String nombreUpdate = req.getParameter("nombreEditar");
                boolean isExisteName = GestionFloor.updateFloor(new Floor(id,nombreUpdate,"Activo"));

                if (!isExisteName) {
                    // Si no se pudo registrar (porque ya existe), redirige con parámetro
                    resp.sendRedirect("menu.jsp?view=pisos&error=nombrepisoexistente");
                } else {
                    // Si se registra bien, redirige normalmente
                    resp.sendRedirect("menu.jsp?view=pisos");
                }
                break;
            case "inactivate":
                int idFloor = Integer.parseInt(req.getParameter("idFloor"));
                GestionFloor.updateStatus(idFloor, "Inactivo");
                resp.sendRedirect("menu.jsp?view=pisos");
                break;
            case "activate":
                int idPiso = Integer.parseInt(req.getParameter("idFloor"));
                GestionFloor.updateStatus(idPiso, "Activo");
                resp.sendRedirect("menu.jsp?view=pisos");
                break;
        }
    }
}

