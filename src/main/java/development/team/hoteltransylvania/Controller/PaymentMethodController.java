package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionMetodosPago;
import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.Model.PaymentMethod;
import development.team.hoteltransylvania.Model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "metodoController", urlPatterns = {"/metodoController"})
public class PaymentMethodController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("actionMetodo");

        switch (action) {
            case "add":
                String metodoName = req.getParameter("metodo");
                GestionMetodosPago.registerMetodo(new PaymentMethod(metodoName,1));
                resp.sendRedirect("menu.jsp?view=metodosPago");
                break;
            case "disponible":
                int idMethod = Integer.parseInt(req.getParameter("idMethod"));
                int availability = Integer.parseInt(req.getParameter("availability"));
                GestionMetodosPago.updateAvailability(idMethod, availability);
                resp.sendRedirect("menu.jsp?view=metodosPago");
                break;
        }
    }
}
