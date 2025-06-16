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

                resp.sendRedirect("menu.jsp?view=informacionHotelera");
                break;
            case 2:
                Part filePart = req.getPart("nuevaImagen");
                /*if (filePart != null && filePart.getSize() > 0) {
                    // Nombre del archivo con timestamp
                    String fileName = "hotel_logo_" + System.currentTimeMillis() + ".png";

                    // Ruta absoluta real hacia la carpeta 'img' dentro de 'webapp'
                    String imgFolderPath = getServletContext().getRealPath("/img");

                    // Asegurarse de que el directorio existe
                    File uploadDir = new File(imgFolderPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    // Ruta completa donde se guardará el archivo
                    String fullImagePath = imgFolderPath + File.separator + fileName;

                    // Guardar archivo en ruta
                    filePart.write(fullImagePath);
                    //Guardar ruta de archivo en BD
                    GestionInformationHotel.updateImgHotel(fullImagePath,1);

                    // Ruta relativa para acceder a la imagen desde el navegador (útil si la guardas en BD)
                    *//*String imageUrl = req.getContextPath() + "/img/" + fileName;
                    System.out.println("Imagen guardada correctamente en: " + imageUrl);*//*

                    // Redirección (puedes pasar imageUrl como parámetro si quieres mostrar la imagen luego)
                    resp.sendRedirect("menu.jsp?view=informacionHotelera");
                }*/
                break;
        }
    }
}
