package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionTypeRoom;
import development.team.hoteltransylvania.Model.TypeRoom;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "typeroomcontroller", urlPatterns = {"/typeroomcontroller"})
public class TypeRoomController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("get".equals(action)) {
            int idTypeRoom = Integer.parseInt(req.getParameter("idtyperoom"));
            System.out.println(idTypeRoom);

            TypeRoom typeroom = GestionTypeRoom.getTypeRoomById(idTypeRoom);
            System.out.println(typeroom.toString());
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(new Gson().toJson(typeroom));
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("actionTypeRoom");

        switch (action) {
            case "add":
                String nombre = req.getParameter("nombreType");
                GestionTypeRoom.registerTypeRoom(new TypeRoom(1,nombre,"Activo"));
                resp.sendRedirect("menu.jsp?view=habitacionesTipo");
                break;
            case "update":
                int id = Integer.parseInt(req.getParameter("idTypeRoom"));
                String nombreUpdate = req.getParameter("nombreEditar");
                GestionTypeRoom.updateTypeRoom(new TypeRoom(id,nombreUpdate,"Activo"));
                resp.sendRedirect("menu.jsp?view=habitacionesTipo");
                break;
            case "inactivate":
                int idType = Integer.parseInt(req.getParameter("idType"));
                GestionTypeRoom.updateStatus(idType, "Inactivo");
                resp.sendRedirect("menu.jsp?view=habitacionesTipo");
                break;
            case "activate":
                int idTipoH = Integer.parseInt(req.getParameter("idType"));
                GestionTypeRoom.updateStatus(idTipoH, "Activo");
                resp.sendRedirect("menu.jsp?view=habitacionesTipo");
                break;
        }
    }
}

