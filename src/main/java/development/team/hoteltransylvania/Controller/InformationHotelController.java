package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionInformationHotel;
import development.team.hoteltransylvania.Model.InformationHotel;
import development.team.hoteltransylvania.Model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "hotelcontrol", urlPatterns = {"/hotelcontrol"})
@MultipartConfig
public class InformationHotelController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int action = Integer.parseInt(req.getParameter("accion"));

        switch (action) {
            case 1:
                String nombreHotel = req.getParameter("nombreHotel");
                String telefonoHotel = req.getParameter("telefonoHotel");
                String correoHotel = req.getParameter("correoHotel");
                String ubicacionHotel = req.getParameter("ubicacionHotel");
                InformationHotel Hotel = new InformationHotel(1, nombreHotel, ubicacionHotel, telefonoHotel, correoHotel);
                GestionInformationHotel.updateInformationHotel(Hotel);

                resp.sendRedirect("menu.jsp");
                break;
            case 2:
                Part filePart = req.getPart("nuevaImagen");
                System.out.println(filePart.getSubmittedFileName());

                /*if (filePart != null && filePart.getSize() > 0) {
                    String fileName = "hotel_logo_" + System.currentTimeMillis() + ".png"; // Nombre único
                    String relativePath = "img/" + fileName;
                    String absolutePath = getServletContext().getRealPath("/") + relativePath;

                    // Crear carpeta si no existe
                    File uploadDir = new File(getServletContext().getRealPath("/") + "img/");
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    // Guardar archivo
                    filePart.write(absolutePath);


                    // Ejemplo:
                    //HotelDAO dao = new HotelDAO();
                    //dao.actualizarLogo(relativePath); // Método que actualiza en BD

                    resp.sendRedirect("menu.jsp");
                }*/
                break;
        }
    }
}
