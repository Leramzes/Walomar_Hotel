package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.Business.GestionVentas;
import development.team.hoteltransylvania.Model.ConsumeProduct;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ventacontroller", urlPatterns = {"/ventacontroller"})
public class ventaController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("actionVenta");

        switch (action) {
            case "directa":
                String[] cantProducts = req.getParameterValues("cantProduct[]");
                String[] preciosUnit = req.getParameterValues("precioUnitProduct[]");
                String[] preciosTotal = req.getParameterValues("precioTotalProduct[]");
                String[] idsProductos = req.getParameterValues("idProduct[]");
                boolean ventaHecha = false;
                if (idsProductos != null) {
                    for (int i = 0; i < idsProductos.length; i++) {
                        ConsumeProduct cp = new ConsumeProduct();
                        cp.setProduct(GestionProduct.getProductById(Integer.parseInt(idsProductos[i]))); // ID del producto
                        cp.setQuantity(Integer.parseInt(cantProducts[i])); // Cantidad
                        cp.setPriceUnit(Float.parseFloat(preciosUnit[i]));
                        cp.setPriceTotal(Float.parseFloat(preciosTotal[i]));
                        ventaHecha = GestionVentas.registerVentaDirecta(1000000000,1000000000,cp);
                    }
                    if (ventaHecha) resp.sendRedirect("menu.jsp?view=ventaDirecta&succes=ventadirectarealizada");
                } else {
                    System.out.println("No se recibieron productos.");
                }

                break;
        }

    }
}
