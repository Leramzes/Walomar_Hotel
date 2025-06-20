package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionEmployee;
import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.DTO.usersEmployeeDTO;
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
            String estateParam = req.getParameter("estate");
            Integer estate = null;

            if (estateParam != null && !estateParam.trim().isEmpty()) {
                estate = Integer.parseInt(estateParam);
            }

            // Obtener parámetros de paginación (si no existen, se asignan valores por defecto)
            int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
            int size = req.getParameter("size") != null ? Integer.parseInt(req.getParameter("size")) : 10;

            // Obtener lista paginada
            List<Product> products = GestionProduct.filterProducts(filter, estate, page, size);
            int totalProducts = GestionProduct.countFilteredProduct(filter, estate);

            int count = 1;
            for (Product product : products) {
                out.println("<tr>");
                out.println("<td>" + count + "</td>");
                out.println("<td>" + product.getName() + "</td>");
                out.println("<td>S/. " + product.getPrice() + "</td>");
                out.println("<td>S/. " + product.getQuantity() + "</td>");
                out.println("<td class='align-middle text-center'>");
                out.println("<div class='d-flex justify-content-center align-items-center gap-1'>");
                out.println("<button class='btn btn-warning' id='btn-editar' data-bs-toggle='modal' data-bs-target='#modalEditarCatalogoProducto' onclick='abrirModalEditar(" + product.getId() + ")'>✏️</button>");
                boolean disponible = product.getStatus() == 1;
                String btnClass = disponible ? "btn-danger" : "btn-success";
                String btnText = disponible ? "❌" : "✅";
                out.println("<form action='productcontrol' method='post'>");
                out.println("<input type='hidden' name='idproduct' value='" + product.getId() + "'>");
                out.println("<input type='hidden' name='actionproduct' value='inactive'>");
                out.println("<input type='hidden' name='availability' value='" + product.getStatus() + "'>");
                out.println("  <button class='btn " + btnClass + " btn-sm'>" + btnText + "</button>");
                out.println("</form>");
                out.println("</div>");
                out.println("</td>");
                out.println("</tr>");
                count++;
            }
            out.println("<!--COUNT:" + totalProducts + "-->");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
