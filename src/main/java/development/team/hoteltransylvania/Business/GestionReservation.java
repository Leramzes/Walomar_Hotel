package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.DTO.TableReservationDTO;
import development.team.hoteltransylvania.Model.*;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GestionReservation {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionReservation.class);

    public static TableReservationDTO getReservationById(int id) {
        String sql = "SELECT\n" +
                "    r.id,\n" +
                "    cl.id AS id_cliente,\n" +
                "    cl.nombre,\n" +
                "    cl.tipo_documento,\n" +
                "    cl.numero_documento,\n" +
                "    h.id AS id_habitacion,\n" +
                "    h.numero,\n" +
                "    th.nombre AS tipo_habitacion,\n" +
                "    r.fecha_inicio,\n" +
                "    r.fecha_fin,\n" +
                "    er.estado\n" +
                "FROM reservas r\n" +
                "    INNER JOIN estado_reserva er ON er.id = r.estado_id\n" +
                "    INNER JOIN clientes cl ON r.cliente_id = cl.id\n" +
                "    INNER JOIN detalle_habitacion dh ON dh.reserva_id = r.id\n" +
                "    INNER JOIN habitaciones h ON h.id = dh.habitacion_id\n" +
                "    INNER JOIN tipo_habitacion th ON th.id = h.tipo_id\n" +
                "WHERE r.id = ?\n" +
                "ORDER BY r.id DESC";

        TableReservationDTO reservation = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reservation = new TableReservationDTO();
                reservation.setIdReservation(rs.getInt("id"));
                reservation.setIdClient(rs.getInt("id_cliente"));
                reservation.setClientName(rs.getString("nombre"));
                reservation.setDocumentType(rs.getString("tipo_documento"));
                reservation.setDocumentNumber(rs.getString("numero_documento"));
                reservation.setIdRoom(rs.getInt("id_habitacion"));
                reservation.setNumberRoom(rs.getString("numero"));
                reservation.setRoomType(rs.getString("tipo_habitacion"));
                reservation.setCheckInDate(rs.getTimestamp("fecha_inicio"));
                reservation.setCheckOutDate(rs.getTimestamp("fecha_fin"));
                reservation.setReservationStatus(rs.getString("estado"));

            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Reservations: " + e.getMessage());
        }

        return reservation;
    }

    public static List<TableReservationDTO> getRoomAsociateReservationPendiete(int id) {
        String sql = "SELECT\n" +
                "    r.id AS id_reserva,\n" +
                "    cl.id AS id_cliente,\n" +
                "    cl.nombre,\n" +
                "    cl.tipo_documento,\n" +
                "    cl.numero_documento,\n" +
                "    h.id AS id_habitacion,\n" +
                "    h.numero,\n" +
                "    th.nombre AS tipo_habitacion,\n" +
                "    r.fecha_inicio,\n" +
                "    r.fecha_fin,\n" +
                "    er.estado\n" +
                "FROM reservas r\n" +
                "    INNER JOIN estado_reserva er ON er.id = r.estado_id\n" +
                "    INNER JOIN clientes cl ON cl.id = r.cliente_id\n" +
                "    INNER JOIN detalle_habitacion dh ON dh.reserva_id = r.id\n" +
                "    INNER JOIN habitaciones h ON h.id = dh.habitacion_id\n" +
                "    INNER JOIN tipo_habitacion th ON th.id = h.tipo_id\n" +
                "WHERE h.id = ? \n" +
                "  AND r.estado_id in (1,4) \n" +
                "ORDER BY r.fecha_inicio ASC;";

        List<TableReservationDTO> reservations = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TableReservationDTO reservation = new TableReservationDTO();
                reservation.setIdReservation(rs.getInt("id_reserva"));
                reservation.setIdClient(rs.getInt("id_cliente"));
                reservation.setCheckInDate(rs.getTimestamp("fecha_inicio"));
                reservation.setCheckOutDate(rs.getTimestamp("fecha_fin"));
                reservation.setReservationStatus(rs.getString("estado"));
                // Puedes completar otros campos si los necesitas
                reservations.add(reservation);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Reservations: " + e.getMessage());
        }

        return reservations;
    }

    public static List<TableReservationDTO> getReservationPaginated(int page, int pageSize) {
        String sql = "SELECT * FROM obtener_reservas_paginado(?,?)";

        List<TableReservationDTO> reservationDTOS = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize); // OFFSET = (página - 1) * tamaño

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TableReservationDTO dto = new TableReservationDTO();
                dto.setIdReservation(rs.getInt("id_reserva"));
                dto.setIdClient(rs.getInt("id_cliente"));
                dto.setClientName(rs.getString("nombre"));
                dto.setDocumentType(rs.getString("tipo_documento"));
                dto.setDocumentNumber(rs.getString("numero_documento"));
                dto.setIdRoom(rs.getInt("id_habitacion"));
                dto.setNumberRoom(rs.getString("numero"));
                dto.setRoomType(rs.getString("tipo_habitacion"));
                dto.setCheckInDate(rs.getTimestamp("fecha_inicio"));
                dto.setCheckOutDate(rs.getTimestamp("fecha_fin"));
                dto.setReservationStatus(rs.getString("estado"));

                reservationDTOS.add(dto);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Reservations: " + e.getMessage());
        }

        return reservationDTOS;
    }

    public static int registerReservation(Reservation reservation, Room room, double payment, Checkout checkout) {
        String sql = "INSERT INTO reservas (cliente_id, empleado_id, fecha_inicio, fecha_fin, estado_id, descuento, cobro_extra, adelanto) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection cnn = dataSource.getConnection()) {
            cnn.setAutoCommit(false);

            try (PreparedStatement ps = cnn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, reservation.getClient().getId());
                ps.setInt(2, reservation.getEmployee().getId());

                // Convertimos a Timestamp porque la BD espera TIMESTAMP
                ps.setTimestamp(3,  reservation.getFechaInicio());
                ps.setTimestamp(4,  reservation.getFechaFin());
                ps.setInt(5, reservation.getStatusReservation().getValue());
                ps.setInt(6, reservation.getDsct());
                ps.setDouble(7, reservation.getCobro_extra());
                ps.setDouble(8, reservation.getAdelanto());
                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected);
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            generatedId = generatedKeys.getInt(1);
                            System.out.println("generatedId "+generatedId);
                        }
                    }
                    LOGGER.info("Reserva registrada correctamente con ID: " + generatedId);
                }
            }

            if (generatedId != -1) {
                RoomDetails roomDetails = new RoomDetails(reservation, room, payment);

                boolean detailOk = registerDetailRoom(cnn, roomDetails, generatedId, payment);
                boolean checkoutOk = registerCheckout(cnn, roomDetails, generatedId, checkout);

                if (detailOk && checkoutOk) {
                    cnn.commit();
                } else {
                    cnn.rollback();
                    LOGGER.warning("Transacción revertida: error al insertar detalles o checkout.");
                    generatedId = -1;
                }
            }

        } catch (SQLException e) {
            LOGGER.warning("Error al registrar la reserva: " + e.getMessage());
        }

        return generatedId;
    }

    public static boolean updateStatusReservation(int idReservation, int statusReservation) {
        String sql = "UPDATE reservas SET estado_id = ? WHERE id = ?";
        boolean success = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setInt(1, statusReservation);
            ps.setInt(2, idReservation);

            int rows = ps.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return success;
    }
    public static boolean updateFechaIngresoReserva(int idReservation, Timestamp fechaIngreso) {
        String sql = "UPDATE reservas SET fecha_ingreso = ? WHERE id = ?";
        boolean success = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setTimestamp(1, fechaIngreso);
            ps.setInt(2, idReservation);

            int rows = ps.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return success;
    }

    private static boolean registerDetailRoom(Connection cnn, RoomDetails roomDetails, int idReservation, double payment) {
        String sql = "INSERT INTO detalle_habitacion (reserva_id, habitacion_id, pago_total) VALUES (?, ?, ?)";
        boolean success = false;

        try (PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setInt(1, idReservation);
            ps.setInt(2, roomDetails.getRoom().getId());
            ps.setDouble(3, payment);

            int rows = ps.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return success;
    }

    private static boolean registerCheckout(Connection cnn, RoomDetails roomDetails, int idReservation, Checkout checkout) {
        String sql = "INSERT INTO checkout (reserva_id, fecha_checkout, tiempo_extra, total) VALUES (?, ?, ?, ?)";
        boolean success = false;

        try (PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setInt(1, idReservation);
            ps.setTimestamp(2, roomDetails.getReservation().getFechaFin());
            ps.setObject(3, null);
            ps.setDouble(4, checkout.getTotal_price());

            int rows = ps.executeUpdate();
            success = rows > 0;
        } catch (SQLException e) {
            LOGGER.warning("Error al registrar el checkout: " + e.getMessage());
        }

        return success;
    }
}
