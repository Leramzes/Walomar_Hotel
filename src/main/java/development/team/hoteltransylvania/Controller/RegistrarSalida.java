package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.*;
import development.team.hoteltransylvania.DTO.AllInfoTableProdSalida;
import development.team.hoteltransylvania.DTO.AllInfoTableServSalida;
import development.team.hoteltransylvania.DTO.TableReservationDTO;
import development.team.hoteltransylvania.Model.Client;
import development.team.hoteltransylvania.Model.InformationHotel;
import development.team.hoteltransylvania.Model.TypeVoucher;
import development.team.hoteltransylvania.Model.Voucher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@WebServlet(name = "registroSalida", urlPatterns = {"/registroSalida"})
public class RegistrarSalida extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idReserva = Integer.parseInt(req.getParameter("idReserva"));
        int idClient = Integer.parseInt(req.getParameter("idClient"));
        int tipoComprobante = Integer.parseInt(req.getParameter("tipoComprobante"));
        int enviarCorreo = req.getParameter("enviarCorreo") != null ? Integer.parseInt(req.getParameter("enviarCorreo")) : 0;
        double inputPenalidad = Double.parseDouble(req.getParameter("inputPenalidad"));
        int metodoPago = Integer.parseInt(req.getParameter("metodoPago"));

        //Obtengo toda la data necesaria para finalizar alquiler
        TableReservationDTO reservation = GestionReservation.getReservationById(idReserva);
        System.out.println(reservation);

        //Actualizo todos los consumos de servicios y productos que estan pendientes a pagados
        GestionVentas.actualizarProductosPagadosPorReserva(idReserva);
        GestionVentas.actualizarServiciosPagadosPorReserva(idReserva);

        //Libero la habitacion y la pongo en mantenimiento
        boolean sepudo1= GestionRoom.updateStatusRoom(reservation.getIdRoom(),3);
        System.out.println("se libero al habiatcion = "+sepudo1);

        //Actualizo el checkout
        Duration tiempoextra = TableReservationDTO.parseTiempoRebasado(reservation.getTiempoRebasado());
        boolean sepudo2 = GestionReservation.updateCheckoutReservation(idReserva,tiempoextra,inputPenalidad);
        System.out.println("se actualizo el checkout = "+sepudo2);

        //Actualizo el estado de la reserva
        boolean sepudo3 = GestionReservation.updateStatusReservation(idReserva, 5);
        System.out.println("se actualizo la resrva = "+sepudo3);

        //*Genero mi comprobante*//*
        //Sumo totales de productos y servicios asociados a la resevra
        List<AllInfoTableProdSalida> ventasByReserva = GestionVentas.obtenerVentasProdPorReserva(idReserva);
        double totalProducto = ventasByReserva.stream()
                .filter(v -> "Pagado al salir".equalsIgnoreCase(v.getEstadoProducto()))
                .mapToDouble(AllInfoTableProdSalida::getTotal)
                .sum();
        List<AllInfoTableServSalida> ventasSByReserva = GestionVentas.obtenerVentasServPorReserva(idReserva);
        double totalServicio = ventasSByReserva.stream()
                .filter(v -> "Pagado al salir".equalsIgnoreCase(v.getEstadoServicio()))
                .mapToDouble(AllInfoTableServSalida::getTotal)
                .sum();

        String nombreComprobante = tipoComprobante == 1 ? "Boleta" : "Factura";
        Voucher voucher = new Voucher();
        voucher.setTypeVoucher(new TypeVoucher(tipoComprobante, nombreComprobante));
        voucher.setPaymentMethod(GestionMetodosPago.getMethodPaymentById(metodoPago));
        voucher.setSubtotalProducts(totalProducto);
        voucher.setSubtotalServices(totalServicio);
        voucher.setSubtotalPenalidad(inputPenalidad);
        double totalAmount = totalProducto + totalServicio + inputPenalidad;
        voucher.setTotalAmount(totalAmount);

        int sepudo4 = GestionVentas.registrarComprobante(idReserva, voucher);
        System.out.println("se registro el comprobante = "+sepudo4);

        // Devuelve algo al frontend
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"status\":\"ok\"}");

    }
}
