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

@WebServlet(name = "filterClientServlet", urlPatterns = {"/filterClientServlet"})
public class FilterClientController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            String filter = req.getParameter("filter");
            List<Client> clients = GestionClient.filterClients(filter);
            int count = 1;
            for (Client client : clients) {
                out.println("<tr>");
                out.println("    <td>" + count + "</td>");
                out.println("    <td>" + client.getName() + " " +client.getApPaterno() + " " +client.getApMaterno() + "</td>");
                out.println("    <td>" + client.getTypeDocument() + "</td>");
                out.println("    <td>" + client.getNumberDocument() + "</td>");
                out.println("    <td>" + client.getEmail() + "</td>");
                out.println("    <td>" + client.getTelephone() + "</td>");
                out.println("    <td class='d-flex justify-content-center gap-1'>");
                out.println("        <button class='btn btn-warning btn-sm' id='btn-editar' ");
                out.println("                data-bs-toggle='modal' ");
                out.println("                data-bs-target='#modalEditarCliente' ");
                out.println("                onclick='editarClient(" + client.getId() + ")'>");
                out.println("            ✏️");
                out.println("        </button>");
                out.println("        <form action='clientcontrol' method='post'>");
                out.println("            <input type='hidden' name='idClient' value='" + client.getId() + "'>");
                out.println("            <input type='hidden' name='actionclient' value='delete'>");
                out.println("            <button class='btn btn-danger btn-sm'>❌</button>");
                out.println("        </form>");
                out.println("    </td>");
                out.println("</tr>");
                count++;
            }
            out.println("<!--COUNT:" + clients.size() + "-->");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
