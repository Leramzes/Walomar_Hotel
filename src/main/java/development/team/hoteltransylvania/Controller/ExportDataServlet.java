package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionClient;
import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.Business.GestionUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "export-data", urlPatterns = {"/export-data"})
public class ExportDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tipo = req.getParameter("tipo");

        List<?> dataList;

        switch (tipo) {
            case "clientes":
                dataList = GestionClient.getAllClients();
                break;
            case "usuarios":
                dataList = GestionUser.getAllUsers();
                break;
            // Agrega más tipos según necesidad
            default:
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Tipo no válido\"}");
                return;
        }

        // Aquí pones los datos del hotel desde tu base de datos o estáticos por ahora
        Map<String, Object> empresa = new HashMap<>();
        empresa.put("nombre", "Walomar Hotel");
        empresa.put("ruc", "RUC: 10428703575");
        empresa.put("direccion", "Calle Diego Ferre N°102");
        empresa.put("contacto", "Puerto Eten, Chiclayo - Tel: 948036274");
        empresa.put("logo", "img/imagenWalomar.jpg"); // Debe estar accesible por el navegador

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("empresa", empresa);
        respuesta.put("datos", dataList);

        Gson gson = new Gson();
        String json = gson.toJson(respuesta);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }
}
