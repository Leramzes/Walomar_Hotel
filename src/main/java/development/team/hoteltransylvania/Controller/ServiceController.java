package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.Business.GestionService;
import development.team.hoteltransylvania.Model.Product;
import development.team.hoteltransylvania.Model.Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "serviciocontrol", urlPatterns = {"/serviciocontrol"})
public class ServiceController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("get".equals(action)) {
            String idProduct = req.getParameter("idproduct");

            Product product = GestionProduct.getProductById(Integer.parseInt(idProduct));

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(new Gson().toJson(product));
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("actionservice");

        switch (action) {
            case "add":
                String productName = req.getParameter("nombreService");
                double price = Double.parseDouble(req.getParameter("precioService"));
                Service service = new Service();
                service.setName(productName);
                service.setPrice(price);
                service.setStatus(1);
                System.out.println(service);
                GestionService.registerservice(service);
                resp.sendRedirect("menu.jsp?view=catalogoServicios");
                break;
            case "inactive":
                int idservice = Integer.parseInt(req.getParameter("idservice"));
                int availability = Integer.parseInt(req.getParameter("availability"));
                GestionService.updateAvailability(idservice,availability);
                resp.sendRedirect("menu.jsp?view=catalogoServicios");
                break;
            case "update":
                int id = Integer.parseInt(req.getParameter("idServicio"));
                Service service1 = GestionService.getserviceById(id);
                String name = req.getParameter("nombreEditar");
                String priceString = req.getParameter("precioServicioEditar");
                service1.setName(name); service1.setPrice(Double.parseDouble(priceString));
                GestionService.updateservice(service1);
                resp.sendRedirect("menu.jsp?view=catalogoServicios");
                break;
        }
    }
}
