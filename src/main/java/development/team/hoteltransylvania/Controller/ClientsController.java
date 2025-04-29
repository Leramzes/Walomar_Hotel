package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionClient;
import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.Model.Client;
import development.team.hoteltransylvania.Model.Employee;
import development.team.hoteltransylvania.Model.Product;
import development.team.hoteltransylvania.Model.TypeDocument;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "clientcontrol", urlPatterns = {"/clientcontrol"})
public class ClientsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("get".equals(action)) {
            String idClient = req.getParameter("idclient");

            Client client = GestionClient.getClientById(Integer.parseInt(idClient));
            System.out.println(client.toString());


            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(new Gson().toJson(client));
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("actionclient");
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        switch (action) {
            case "add":
                String clientName = req.getParameter("nombre");
                String clientEmail = req.getParameter("clientemail");
                String typeDocument = req.getParameter("typedocumentHidden");
                String document = req.getParameter("numberdocumentHidden");
                String telephone = req.getParameter("telephone");
                //search number document replicated
                boolean isDuplicated = GestionClient.getAllClients()
                        .stream()
                        .anyMatch(c -> c.getNumberDocument().equals(document));

                if(clientName == null || clientName.equals("Error al consultar documento")) {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Número de documento incorrecto');");
                    out.println("history.back();");
                    out.println("</script>");
                }
                if(typeDocument.isEmpty() || document.isEmpty()) {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Debe ingresar el documento del cliente');");
                    out.println("history.back();");
                    out.println("</script>");
                }
                if (isDuplicated) {
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('El cliente con DNI: "+document+" ya se encuentra registrado.');");
                    out.println("history.back();");
                    out.println("</script>");
                }else{
                    GestionClient.registerClient(new Client(clientName,telephone,clientEmail, TypeDocument.valueOf(typeDocument),document));
                    resp.sendRedirect("menu.jsp?view=clientes");
                }
                break;
            case "delete":
                int idClient = Integer.parseInt(req.getParameter("idClient"));
                GestionClient.deleteClient(idClient);
                resp.sendRedirect("menu.jsp?view=clientes");
                break;
            case "update":
                int id = Integer.parseInt(req.getParameter("idclient"));
                Client client = GestionClient.getClientById(id);
                String name = req.getParameter("nombreEditar");
                String email = req.getParameter("correoEditar");
                String typeDoc = req.getParameter("tipoDocumentoEditar");
                String documentedit = req.getParameter("documentoEditar");
                String telephoneedit = req.getParameter("telefonoEditar");
                client.setName(name);client.setEmail(email);client.setTypeDocument(TypeDocument.valueOf(typeDoc)); client.setNumberDocument(documentedit);
                client.setTelephone(telephoneedit);
                GestionClient.updateClient(client);
                resp.sendRedirect("menu.jsp?view=clientes");
                break;
        }
    }

}
