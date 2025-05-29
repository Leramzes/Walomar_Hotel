package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionClient;
import development.team.hoteltransylvania.Business.GestionReservation;
import development.team.hoteltransylvania.Business.GestionRoom;
import development.team.hoteltransylvania.Business.GestionUser;
import development.team.hoteltransylvania.Model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@WebServlet(name = "recepController", urlPatterns = {"/recepController"})
public class RecepcionController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("usuario");
        Employee employee1 = GestionUser.obtenerEmpleadoPorId(user.getId()); //empleado en sesion
        String idCLiente = req.getParameter("idClienteProcesar");//cliente
        String habitacion = req.getParameter("roomSelect");//habitacion seleccionada
        String fecEntrada = req.getParameter("fechaEntradaRecep");//fecha de entrada (del sistema)
        String fecSalida = req.getParameter("fechaSalidaRecep");//fecga de salida (ingresada)
        String val4 = req.getParameter("descuentoRecep");//dsct
        int descuento = (val4 == null || val4.isEmpty()) ? 0 : Integer.parseInt(val4);
        Double cobroExtra = Double.parseDouble(req.getParameter("cobroExtraRecep"));//cobro extra
        Double adelanto = Double.parseDouble(req.getParameter("adelantoRecep"));//adelanto
        Double totalPagar = Double.parseDouble(req.getParameter("totalPagarRecep"));//total a pagar
        Client cliente = GestionClient.getClientById(Integer.parseInt(idCLiente));//obtener cliente

        Timestamp fechaEntrada = null;
        Timestamp  fechaSalida = null;
        int cant_dias=1;
        if (fecEntrada != null && fecSalida != null) {
            // Formato correcto de fecha con hora
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime fechaEntradaLocal = LocalDateTime.parse(fecEntrada, formatter);
            LocalDateTime fechaSalidaLocal = LocalDateTime.parse(fecSalida, formatter);

            // Convertimos a Timestamp
            fechaEntrada = Timestamp.valueOf(fechaEntradaLocal);
            fechaSalida = Timestamp.valueOf(fechaSalidaLocal);

            // Calcular días correctamente
            cant_dias = (int) ChronoUnit.DAYS.between(fechaEntradaLocal.toLocalDate(), fechaSalidaLocal.toLocalDate());
        } else {
            System.out.println("Error: Alguna de las fechas es nula.");
        }

        // Crear reserva con ststus 4 (ocupada)
        Reservation reservation = new Reservation(cliente, employee1, fechaEntrada, fechaSalida,
                StatusReservation.fromId(4), descuento, cobroExtra, adelanto, cant_dias);
        // Obtener habitación y calcular pago

        Room room = GestionRoom.getRoomById(Integer.parseInt(habitacion));
        double payment = totalPagar + adelanto;

        // Crear Checkout (Asegúrate de manejar bien el tiempo extra)
        Checkout checkout = new Checkout(reservation, fechaSalida, null, 0);

        int reservationResgitered = GestionReservation.registerReservation(reservation,room,payment, checkout);

        //cambio de estado de habitacion: cuando es recepcion inicial siempre será status 2 ocupada
        GestionRoom.updateStatusRoom(room.getId(),2);

        System.out.println("se registro: "+reservationResgitered);
        resp.sendRedirect("menu.jsp?view=recepcion");



    }
}
