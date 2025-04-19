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
                GestionProduct.registerProduct(new Product(productName, price));
                //req.getRequestDispatcher("jsp/catalagoProductos.jsp").forward(req, resp);
                resp.sendRedirect("menu.jsp?view=catalogoProductos");
                break;
            case "delete":
                int productId = Integer.parseInt(req.getParameter("idproduct"));
                GestionProduct.deleteProduct(productId);
                resp.sendRedirect("menu.jsp?view=catalogoProductos");
                break;
            case "update":
                int id = Integer.parseInt(req.getParameter("idproduct"));
                Product product = GestionProduct.getProductById(id);
                String name = req.getParameter("nameproduct");
                String priceString = req.getParameter("priceproduct");
                product.setName(name); product.setPrice(Double.parseDouble(priceString));
                GestionProduct.updateProduct(product);
                resp.sendRedirect("menu.jsp?view=catalogoProductos");
                break;
        }

    }

}
