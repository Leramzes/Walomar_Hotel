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
                System.out.println("entrando al execute");
                int rowsAffected = ps.executeUpdate();
                System.out.println("saliendo del  execute");
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
