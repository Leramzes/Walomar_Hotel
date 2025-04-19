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

import java.io.DataOutput;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@WebServlet(name = "reservatioController", urlPatterns = {"/reservatioController"})
public class ReservationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        //Que pasa cuando registro una reserva
        /*
        * Jalo el id del cliente para su insercion
        * Jalo el id del empleado para su insercion
        * Hago una resta de fechas de estadia para insertar en cant_dias
        * Si la resvera se hace por telefono y aun no hay pago adelantado, estado: pendiente
        * Si la reserva se hace por telefono y si hay pago adelantado, estado: confirmada
        * Jalo el id de la reserva insertada y jalo el id de la habiacion
        * Hago un calculo del total a pagar, eso ira en tabla detalle_habitacion
        * Una vez insertada la reserva la habuiatcion cambia de estado (update a reservada) //ver como insertar el nuevo campo porque es enum
        *
        * */
        String idCLiente = req.getParameter("idCLiente");
        User user = (User) session.getAttribute("usuario");
        Employee employee1 = GestionUser.obtenerEmpleadoPorId(user.getId());
        String nombre = req.getParameter("nombre");
        String tipoDocumento = req.getParameter("tipoDocumento");
        String fechaEntradaStr = (req.getParameter("fechaEntrada"));
        Double adelanto = Double.valueOf(req.getParameter("adelanto"));
        String documento = req.getParameter("documento");
        String correo = req.getParameter("correo");
        String tipoHabitacion = req.getParameter("tipoHabitacion");
        String habitacion = req.getParameter("habitacion");
        String fechaSalidaStr = (req.getParameter("fechaSalida"));
        int descuento = Integer.parseInt(req.getParameter("descuento"));
        Double cobroExtra = Double.valueOf(req.getParameter("cobroExtra"));
        Double totalPagar = Double.valueOf(req.getParameter("totalPagar"));
        String observacion = req.getParameter("observacion");

        Client cliente = GestionClient.getClientById(Integer.parseInt(idCLiente));


        Timestamp  fechaEntrada = null;
        Timestamp  fechaSalida = null;
        int cant_dias=1;
        if (fechaEntradaStr != null && fechaSalidaStr != null) {
            // Formato correcto de fecha con hora
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime fechaEntradaLocal = LocalDateTime.parse(fechaEntradaStr, formatter);
            LocalDateTime fechaSalidaLocal = LocalDateTime.parse(fechaSalidaStr, formatter);

            // Convertimos a Timestamp
            fechaEntrada = Timestamp.valueOf(fechaEntradaLocal);
            fechaSalida = Timestamp.valueOf(fechaSalidaLocal);

            // Calcular días correctamente
            cant_dias = (int) ChronoUnit.DAYS.between(fechaEntradaLocal.toLocalDate(), fechaSalidaLocal.toLocalDate());
        } else {
            System.out.println("Error: Alguna de las fechas es nula.");
        }

        // Crear reserva
        Reservation reservation = new Reservation(cliente, employee1, fechaEntrada, fechaSalida,
                StatusReservation.fromId(1), descuento, cobroExtra, adelanto, cant_dias);

        // Obtener habitación y calcular pago
        Room room = GestionRoom.getRoomById(Integer.parseInt(habitacion));
        double payment = totalPagar + adelanto;

        // Crear Checkout (Asegúrate de manejar bien el tiempo extra)
        Checkout checkout = new Checkout(reservation, fechaSalida, null, 0);
/*
        System.out.println(reservation);
        System.out.println(room);
        System.out.println(payment);
        System.out.println(checkout);*/

        int reservationResgitered = GestionReservation.registerReservation(reservation,room,payment, checkout);
        System.out.println("se registro: "+reservationResgitered);
        //aun falta pasr bien el employee

    }
}
