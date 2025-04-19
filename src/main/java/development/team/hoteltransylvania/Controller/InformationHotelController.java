package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionInformationHotel;
import development.team.hoteltransylvania.Model.InformationHotel;
import development.team.hoteltransylvania.Model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "hotelcontrol", urlPatterns = {"/hotelcontrol"})
public class InformationHotelController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("accion");

        switch (action) {
            case "update":
                String nombreHotel = req.getParameter("nombreHotel");
                String telefonoHotel = req.getParameter("telefonoHotel");
                String correoHotel = req.getParameter("correoHotel");
                String ubicacionHotel = req.getParameter("ubicacionHotel");
                InformationHotel Hotel = new InformationHotel(1, nombreHotel, ubicacionHotel, telefonoHotel, correoHotel);
                GestionInformationHotel.updateInformationHotel(Hotel);

                resp.sendRedirect("menu.jsp");
                break;
        }
    }
}
