package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import development.team.hoteltransylvania.Business.GestionClient;
import development.team.hoteltransylvania.Business.GestionInformationHotel;
import development.team.hoteltransylvania.Business.GestionReservation;
import development.team.hoteltransylvania.Business.GestionVentas;
import development.team.hoteltransylvania.DTO.AllInfoTableProdSalida;
import development.team.hoteltransylvania.DTO.AllInfoTableServSalida;
import development.team.hoteltransylvania.DTO.TableReservationDTO;
import development.team.hoteltransylvania.Model.Client;
import development.team.hoteltransylvania.Model.InformationHotel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "proccheckout", urlPatterns = {"/proccheckout"})
public class ProcessCheckout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idReserva = Integer.parseInt(req.getParameter("idReserva"));
        int idClient = Integer.parseInt(req.getParameter("idClient"));

        TableReservationDTO reservation = GestionReservation.getReservationById(idReserva);
        List<AllInfoTableProdSalida> ventasByReserva = GestionVentas.obtenerVentasProdPorReserva(idReserva);
        List<AllInfoTableServSalida> ventasSByReserva = GestionVentas.obtenerVentasServPorReserva(idReserva);
        Client client = GestionClient.getClientById(idClient);
        InformationHotel informacion = GestionInformationHotel.getInformationHotel();

        // Prepara JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Usa tu librería favorita para serializar. Ejemplo con Gson:
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.add("reserva", gson.toJsonTree(reservation));
        json.add("cliente", gson.toJsonTree(client));
        json.add("ventasProd", gson.toJsonTree(ventasByReserva));
        json.add("ventasServ", gson.toJsonTree(ventasSByReserva));
        json.add("hotel", gson.toJsonTree(informacion));

        resp.getWriter().write(json.toString());
    }
}