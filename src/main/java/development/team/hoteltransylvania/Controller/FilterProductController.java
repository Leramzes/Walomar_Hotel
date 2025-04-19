package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.Model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "filterProducServlet", urlPatterns = {"/filterProducServlet"})
public class FilterProductController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            String filter = req.getParameter("filter");
            List<Product> products = GestionProduct.filterProducts(filter);
            int count = 1;
            for (Product product : products) {
                out.println("<tr>");
                out.println("<td>" + count + "</td>");
                out.println("<td>Producto</td>");
                out.println("<td>" + product.getName() + "</td>");
                out.println("<td>S/. " + product.getPrice() + "</td>");
                out.println("<td class='align-middle text-center'>");
                out.println("<div class='d-flex justify-content-center align-items-center gap-1'>");
                out.println("<button class='btn btn-warning' id='btn-editar' data-bs-toggle='modal' data-bs-target='#modalEditarCatalogoProducto' onclick='abrirModalEditar(" + product.getId() + ")'>✏️</button>");
                out.println("<form action='productcontrol' method='post'>");
                out.println("<input type='hidden' name='idproduct' value='" + product.getId() + "'>");
                out.println("<input type='hidden' name='actionproduct' value='delete'>");
                out.println("<button class='btn btn-danger btn-sm'>❌</button>");
                out.println("</form>");
                out.println("</div>");
                out.println("</td>");
                out.println("</tr>");
                count++;
            }
            out.println("<!--COUNT:" + products.size() + "-->");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
