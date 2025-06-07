package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.DTO.TableReservationDTO;
import development.team.hoteltransylvania.Model.Room;
import development.team.hoteltransylvania.Model.StatusRoom;
import development.team.hoteltransylvania.Model.TypeRoom;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GestionRecepcion {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionRecepcion.class);

    public static TableReservationDTO getReservationPendiente(String roomNumber) {
        String sql = "SELECT\n" +
                "    h.id AS habitacion_id,\n" +
                "    h.numero,\n" +
                "    h.tipo_id,\n" +
                "    r.id AS reserva_id,\n" +
                "    r.cliente_id,\n" +
                "    r.estado_id,\n" +
                "    c.nombre AS nombre_cliente,\n" +
                "    r.fecha_inicio,\n" +
                "    r.fecha_fin,\n" +
                "    r.fecha_ingreso,\n" +
                "    c.tipo_documento,\n" +
                "    c.numero_documento,\n" +
                "    c.telefono,\n" +
                "    c.email\n" +
                "FROM habitaciones h\n" +
                "         JOIN detalle_habitacion dh ON h.id = dh.habitacion_id\n" +
                "         JOIN reservas r ON dh.reserva_id = r.id\n" +
                "         JOIN clientes c ON r.cliente_id = c.id\n" +
                "WHERE h.estado_id = 4  -- Estado \"pendiente\" de la habitación\n" +
                "  AND r.estado_id = 1  -- Estado \"pendiente\" de la reserva\n" +
                "  AND h.numero = ?\n" +
                "ORDER BY r.fecha_inicio ASC\n" +
                "LIMIT 1;";

        TableReservationDTO dto = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto = new TableReservationDTO();
                    dto.setIdRoom(rs.getInt("habitacion_id"));
                    dto.setNumberRoom(rs.getString("numero"));
                    dto.setIdReservation(rs.getInt("reserva_id"));
                    dto.setIdClient(rs.getInt("cliente_id"));
                    dto.setClientName(rs.getString("nombre_cliente"));
                    dto.setCheckInDate(rs.getTimestamp("fecha_inicio"));
                    dto.setCheckOutDate(rs.getTimestamp("fecha_fin"));
                    dto.setReservationStatus(rs.getString("estado_id"));
                    dto.setRoomType(rs.getString("tipo_id"));
                    dto.setDocumentType(rs.getString("tipo_documento"));
                    dto.setPhone(rs.getString("telefono"));
                    dto.setEmail(rs.getString("email"));
                    dto.setDocumentNumber(rs.getString("numero_documento"));
                    dto.setFecha_ingreso(rs.getTimestamp("fecha_ingreso"));
                }
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Rooms: " + e.getMessage());
        }

        return dto;
    }
    public static TableReservationDTO getReservationOcupada(String roomNumber) {
        String sql = "SELECT\n" +
                "    h.id AS habitacion_id,\n" +
                "    h.numero,\n" +
                "    h.tipo_id,\n" +
                "    r.id AS reserva_id,\n" +
                "    r.cliente_id,\n" +
                "    r.estado_id,\n" +
                "    c.nombre AS nombre_cliente,\n" +
                "    r.fecha_inicio,\n" +
                "    r.fecha_fin,\n" +
                "    r.fecha_ingreso,\n" +
                "    c.tipo_documento,\n" +
                "    c.numero_documento,\n" +
                "    c.telefono,\n" +
                "    c.email\n" +
                "FROM habitaciones h\n" +
                "         JOIN detalle_habitacion dh ON h.id = dh.habitacion_id\n" +
                "         JOIN reservas r ON dh.reserva_id = r.id\n" +
                "         JOIN clientes c ON r.cliente_id = c.id\n" +
                "WHERE h.estado_id = 2  -- Estado \"ocupada\" de la habitación\n" +
                "  AND r.estado_id = 4  -- Estado \"ocupada\" de la reserva\n" +
                "  AND h.numero = ?\n" +
                "ORDER BY h.id\n" +
                "LIMIT 1;";

        TableReservationDTO dto = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto = new TableReservationDTO();
                    dto.setIdRoom(rs.getInt("habitacion_id"));
                    dto.setNumberRoom(rs.getString("numero"));
                    dto.setIdReservation(rs.getInt("reserva_id"));
                    dto.setIdClient(rs.getInt("cliente_id"));
                    dto.setClientName(rs.getString("nombre_cliente"));
                    dto.setCheckInDate(rs.getTimestamp("fecha_inicio"));
                    dto.setCheckOutDate(rs.getTimestamp("fecha_fin"));
                    dto.setReservationStatus(rs.getString("estado_id"));
                    dto.setRoomType(rs.getString("tipo_id"));
                    dto.setDocumentType(rs.getString("tipo_documento"));
                    dto.setPhone(rs.getString("telefono"));
                    dto.setEmail(rs.getString("email"));
                    dto.setDocumentNumber(rs.getString("numero_documento"));
                    dto.setFecha_ingreso(rs.getTimestamp("fecha_ingreso"));
                }
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Rooms: " + e.getMessage());
        }

        return dto;
    }
}
