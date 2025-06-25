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
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "filterMetodoServlet", urlPatterns = {"/filterMetodoServlet"})
public class FilterMetodoServlet extends HttpServlet {
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

            // Obtener lista paginada
            List<PaymentMethod> methods = GestionMetodosPago.filterPaymethMethods(filter, estate);
            int totalMethods = GestionMetodosPago.countFilteredMethod(filter, estate);

            int count = 1;
            for (PaymentMethod paymentMethod : methods) {
                out.println("<tr>");
                out.println("<td>" + count + "</td>");
                out.println("<td>" + paymentMethod.getNameMethod() + "</td>");
                out.println("<td>S/. " + (paymentMethod.getStatus()==1 ? "Activo" : "Inactivo") + "</td>");
                out.println("<td class='align-middle text-center'>");
                out.println("<div class='d-flex justify-content-center align-items-center gap-1'>");
                boolean disponible = paymentMethod.getStatus() == 1;
                String btnClass = disponible ? "btn-danger" : "btn-success";
                String btnText = disponible ? "❌" : "✅";
                out.println("<form action='metodoController' method='post'>");
                out.println("<input type='hidden' name='idMethod' value='" + paymentMethod.getId() + "'>");
                out.println("<input type='hidden' name='actionMetodo' value='disponible'>");
                out.println("<input type='hidden' name='availability' value='" + paymentMethod.getStatus() + "'>");
                out.println("  <button class='btn " + btnClass + " btn-sm'>" + btnText + "</button>");
                out.println("</form>");
                out.println("</div>");
                out.println("</td>");
                out.println("</tr>");
                count++;
            }
            out.println("<!--COUNT:" + totalMethods + "-->");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
