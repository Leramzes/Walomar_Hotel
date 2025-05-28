package development.team.hoteltransylvania.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "recepController", urlPatterns = {"/recepController"})
public class RecepcionController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String val1 = req.getParameter("idClienteProcesar");
        System.out.println(val1);
        String val2 = req.getParameter("fechaEntradaRecep");
        System.out.println(val2);
        String val3 = req.getParameter("fechaSalidaRecep");
        System.out.println(val3);
        String val4 = req.getParameter("descuentoRecep");
        System.out.println(val4);
        String val5 = req.getParameter("cobroExtraRecep");
        System.out.println(val5);
        String val6 = req.getParameter("adelantoRecep");
        System.out.println(val6);
        String val7 = req.getParameter("totalPagarRecep");
        System.out.println(val7);

    }
}
