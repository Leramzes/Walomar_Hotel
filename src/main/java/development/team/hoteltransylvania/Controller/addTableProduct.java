package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionClient;
import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.Model.Client;
import development.team.hoteltransylvania.Model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "addTableProduct", urlPatterns = {"/addTableProduct"})
public class addTableProduct extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            Product product = GestionProduct.getProductById(Integer.parseInt(req.getParameter("filter")));
            out.println("<tr data-id='" + product.getId() + "'>");
            out.println("    <td>" + product.getName() + "</td>");
            out.println("    <td>");
            out.println("        <input type='number' name='cantProduct[]' class='form-control cantidad-producto' value='1' min='1' " +
                    "data-precio='" + product.getPrice() + "'" +
                    "data-stock='" + product.getQuantity() + "'" +
                    "data-nombre='" + product.getName() + "'>");
            out.println("        <input type='hidden' name='idProduct[]' value='" + product.getId() + "'>");
            out.println("        <input type='hidden' name='precioUnitProduct[]' value='" + product.getPrice() + "'>");
            out.println("        <input type='hidden' name='precioTotalProduct[]' class='input-precio-total' value='" + product.getPrice() + "'>");
            out.println("    </td>");
            out.println("    <td class='precio-unit'>S/. " + product.getPrice() + "</td>");
            out.println("    <td class='precio-total'>S/. " + product.getPrice() + "</td>");
            out.println("    <td class='align-middle text-center'>");
            out.println("        <button class='btn btn-danger btn-eliminar-producto'><i class='fas fa-trash'></i></button>");
            out.println("    </td>");
            out.println("</tr>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
