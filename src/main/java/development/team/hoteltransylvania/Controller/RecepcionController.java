package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Business.GestionClient;
import development.team.hoteltransylvania.Business.GestionReservation;
import development.team.hoteltransylvania.Business.GestionRoom;
import development.team.hoteltransylvania.Business.GestionUser;
import development.team.hoteltransylvania.DTO.TableReservationDTO;
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
import java.util.List;

@WebServlet(name = "recepController", urlPatterns = {"/recepController"})
public class RecepcionController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String accion = req.getParameter("accion");
        String vista = req.getParameter("vista");
        String habitacion = req.getParameter("roomSelect");//habitacion seleccionada
        int idReserva = Integer.parseInt(req.getParameter("idReserva"));
        Timestamp fechaEntradaReal = parseFecha(req.getParameter("fechaEntradaRealRecep"));

        if ("habilitar".equalsIgnoreCase(accion)) {
            GestionRoom.updateStatusRoom(Integer.parseInt(habitacion), 1);
            resp.sendRedirect("menu.jsp?view=recepcion");
            return;
        }
        if ("cancelar".equalsIgnoreCase(accion)) {
            GestionReservation.updateStatusReservation(idReserva,3);
            GestionRoom.updateStatusRoom(Integer.parseInt(habitacion), 1);
            if("reserva".equalsIgnoreCase(vista)){
                resp.sendRedirect("menu.jsp?view=reserva&success=reserva_cancelada");
            }else{
                resp.sendRedirect("menu.jsp?view=recepcion");
            }
            return;
        }
        if ("finalizar".equalsIgnoreCase(accion)) {
            /*
            * la habitacion pasa a estado 1 libre
            * el estado reserva pasa a 5 finalizada*/
            GestionReservation.updateStatusReservation(idReserva,5);
            GestionRoom.updateStatusRoom(Integer.parseInt(habitacion), 1);
            resp.sendRedirect("menu.jsp?view=recepcion");
            return;
        }
        if ("mantenimiento".equalsIgnoreCase(accion)) {
            GestionRoom.updateStatusRoom(Integer.parseInt(habitacion), 3);
            resp.sendRedirect("menu.jsp?view=recepcion");
            return;
        }
        if ("ocuparReservada".equalsIgnoreCase(accion)) {
            /*logica para ocupar haitacion reservada*/
            /*
                - obtener la reserva y cambiarle el estaod a ocupada
                - obtener la reserva y colocarle la fehca real de ingreso
                - obetenr la habiatcion y ponerle estado ocupada
            */
            GestionReservation.updateStatusReservation(idReserva,4);
            GestionReservation.updateFechaIngresoReserva(idReserva,fechaEntradaReal);
            GestionRoom.updateStatusRoom(Integer.parseInt(habitacion), 2);
            resp.sendRedirect("menu.jsp?view=recepcion");
            return;
        }

        User user = (User) session.getAttribute("usuario");
        Employee employee1 = GestionUser.getEmployeeIdByUserId(user.getId()); //empleado en sesion
        String idCLiente = req.getParameter("idClienteProcesar");//cliente
        String fecEntrada = req.getParameter("fechaEntradaRecep");//fecha de entrada reservada
        String fecSalida = req.getParameter("fechaSalidaRecep");//fecga de salida (ingresada)
        String val4 = req.getParameter("descuentoRecep");//dsct
        int descuento = (val4 == null || val4.isEmpty()) ? 0 : Integer.parseInt(val4);
        Double cobroExtra = Double.parseDouble(req.getParameter("cobroExtraRecep"));//cobro extra
        Double adelanto = Double.parseDouble(req.getParameter("adelantoRecep"));//adelanto
        Double totalPagar = Double.parseDouble(req.getParameter("totalPagarRecep"));//total a pagar
        Client cliente = GestionClient.getClientById(Integer.parseInt(idCLiente));//obtener cliente
        List<TableReservationDTO> reservaAsociate = GestionReservation.
                getRoomAsociateReservationPendiete(Integer.parseInt(habitacion)); //verificar reservas asociadas a la habitacion




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

            if (fechaEntradaLocal.isAfter(fechaSalidaLocal)) {
                // Puedes redirigir a una página de error o mostrar un mensaje
                resp.sendRedirect("menu.jsp?view=recepcion&error=fechas_invalidas"); //aqui manejar alerta
                return; // Evita que continúe con el proceso
            }

            boolean puedeReservar = validarReserva(fechaEntrada, fechaSalida, reservaAsociate);

            if (!puedeReservar) {
                System.out.println("Error: No se puede reservar porque hay un choque con otra reserva.");
                resp.sendRedirect("menu.jsp?view=reserva&error=choque_fechas"); //aqui manejar alerta
                return;
            }

            // Calcular días correctamente
            cant_dias = (int) ChronoUnit.DAYS.between(fechaEntradaLocal.toLocalDate(), fechaSalidaLocal.toLocalDate());
        } else {
            System.out.println("Error: Alguna de las fechas es nula.");
        }

        // Crear reserva con ststus 4 (ocupada)
        Reservation reservation = new Reservation(cliente, employee1, fechaEntrada, fechaSalida,
                StatusReservation.fromId(4), descuento, cobroExtra, adelanto, cant_dias, fechaEntrada);
        // Obtener habitación y calcular pago

        Room room = GestionRoom.getRoomById(Integer.parseInt(habitacion));
        double payment = totalPagar + adelanto;

        // Crear Checkout (Asegúrate de manejar bien el tiempo extra)
        Checkout checkout = new Checkout(reservation, fechaSalida, null, 0);

        int reservationResgitered = GestionReservation.registerReservation(reservation,room,payment, checkout);

        //cambio de estado de habitacion: cuando es recepcion siempre será status 2 ocupada
        GestionRoom.updateStatusRoom(room.getId(),2);

        System.out.println("se registro: "+reservationResgitered);
        resp.sendRedirect("menu.jsp?view=recepcion");



    }

    public static boolean validarReserva(Timestamp fechaEntrada, Timestamp fechaSalida, List<TableReservationDTO> reservasExistentes) {

        // 90 minutos en milisegundos
        long margen = 90 * 60 * 1000;

        for (TableReservationDTO reserva : reservasExistentes) {
            Timestamp inicioExistente = reserva.getCheckInDate();
            Timestamp finExistente = reserva.getCheckOutDate();

            // Calculamos los tiempos con margen
            long finExistenteMasMargen = finExistente.getTime() + margen;
            long inicioExistenteMenosMargen = inicioExistente.getTime() - margen;

            long nuevaEntrada = fechaEntrada.getTime();
            long nuevaSalida = fechaSalida.getTime();

            // Verificamos si no hay suficiente margen
            boolean choque = !(nuevaSalida + margen <= inicioExistente.getTime() || finExistente.getTime() + margen <= nuevaEntrada);

            if (choque) {
                return false; // Hay choque
            }
        }
        return true; // No hay choque con ninguna reserva
    }
    private static Timestamp parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.isEmpty()) return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(fechaStr, formatter);
        return Timestamp.valueOf(localDateTime);
    }
}
