package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionEmployee;
import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.Model.Product;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "productcontrol", urlPatterns = {"/productcontrol"})
public class ProductController extends HttpServlet {
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
        String action = req.getParameter("actionproduct");

        switch (action) {
            case "add":
                String productName = req.getParameter("nameproduct");
                double price = Double.parseDouble(req.getParameter("priceproduct"));
                int quantity = Integer.parseInt(req.getParameter("cantidadproduct"));
                GestionProduct.registerProduct(new Product(productName, price, 1, quantity));
                resp.sendRedirect("menu.jsp?view=catalogoProductos");
                break;
            case "inactive":
                int productId = Integer.parseInt(req.getParameter("idproduct"));
                int availability = Integer.parseInt(req.getParameter("availability"));
                GestionProduct.updateAvailability(productId,availability);
                resp.sendRedirect("menu.jsp?view=catalogoProductos");
                break;
            case "update":
                int id = Integer.parseInt(req.getParameter("idproduct"));
                Product product = GestionProduct.getProductById(id);
                String name = req.getParameter("nameproduct");
                String priceString = req.getParameter("priceproduct");
                String quantityString = req.getParameter("cantidadproduct");
                product.setName(name); product.setPrice(Double.parseDouble(priceString)); product.setQuantity(Integer.parseInt(quantityString));
                GestionProduct.updateProduct(product);
                resp.sendRedirect("menu.jsp?view=catalogoProductos");
                break;
        }

    }

}
