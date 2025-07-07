package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.DTO.TableReservationDTO;
import development.team.hoteltransylvania.Model.*;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                "    CONCAT(cl.ap_paterno, ' ', cl.ap_materno) AS apellidos,\n" +
                "    cl.tipo_documento,\n" +
                "    cl.numero_documento,\n" +
                "    cl.email,\n" +
                "    cl.telefono,\n" +
                "    em.id AS id_empleado,\n" +
                "    h.id AS id_habitacion,\n" +
                "    h.numero,\n" +
                "    h.tipo_id,\n" +
                "    th.nombre AS tipo_habitacion,\n" +
                "    r.fecha_inicio,\n" +
                "    r.fecha_fin,\n" +
                "    r.fecha_ingreso,\n" +
                "    r.cant_dias,\n" +
                "    r.descuento,\n" +
                "    r.cobro_extra,\n" +
                "    r.adelanto,\n" +
                "    dh.pago_total,\n" +
                "    er.estado\n" +
                "FROM reservas r\n" +
                "         INNER JOIN empleados em ON em.id = r.empleado_id\n" +
                "         INNER JOIN estado_reserva er ON er.id = r.estado_id\n" +
                "         INNER JOIN clientes cl ON r.cliente_id = cl.id\n" +
                "         INNER JOIN detalle_habitacion dh ON dh.reserva_id = r.id\n" +
                "         INNER JOIN habitaciones h ON h.id = dh.habitacion_id\n" +
                "         INNER JOIN tipo_habitacion th ON th.id = h.tipo_id\n" +
                "WHERE r.id = ?\n" +
                "ORDER BY r.id DESC;";

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
                reservation.setClientApellidos(rs.getString("apellidos"));
                reservation.setDocumentType(rs.getString("tipo_documento"));
                reservation.setDocumentNumber(rs.getString("numero_documento"));
                reservation.setEmail(rs.getString("email"));
                reservation.setPhone(rs.getString("telefono"));
                reservation.setEmpleadoId(rs.getInt("id_empleado"));
                reservation.setIdRoom(rs.getInt("id_habitacion"));
                reservation.setNumberRoom(rs.getString("numero"));
                reservation.setRoomType(rs.getString("tipo_habitacion"));
                reservation.setCheckInDate(rs.getTimestamp("fecha_inicio"));
                reservation.setCheckOutDate(rs.getTimestamp("fecha_fin"));
                reservation.setFecha_ingreso(rs.getTimestamp("fecha_ingreso"));
                reservation.setCantDays(rs.getInt("cant_dias"));
                reservation.setDsct(rs.getInt("descuento"));
                reservation.setCobro_extra(rs.getDouble("cobro_extra"));
                reservation.setAdelanto(rs.getDouble("adelanto"));
                reservation.setPago_total(rs.getDouble("pago_total"));
                reservation.setReservationStatus(rs.getString("estado"));
                reservation.setRoomTypeId(rs.getInt("tipo_id"));

            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Reservations: " + e.getMessage());
        }

        return reservation;
    }

    public static List<TableReservationDTO> getRoomAsociateReservationPendienteExcluyendoActual(int idHabitacion, int idReservaEditar) {
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
                "  AND r.estado_id IN (1, 4)\n" +
                "  AND r.id <> ? \n" + // excluir la reserva que se está editando
                "ORDER BY r.fecha_inicio ASC;";

        List<TableReservationDTO> reservations = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, idHabitacion);
            ps.setInt(2, idReservaEditar); // Excluir la reserva actual

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TableReservationDTO reservation = new TableReservationDTO();
                reservation.setIdReservation(rs.getInt("id_reserva"));
                reservation.setIdClient(rs.getInt("id_cliente"));
                reservation.setCheckInDate(rs.getTimestamp("fecha_inicio"));
                reservation.setCheckOutDate(rs.getTimestamp("fecha_fin"));
                reservation.setReservationStatus(rs.getString("estado"));
                reservations.add(reservation);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Reservations: " + e.getMessage());
        }

        return reservations;
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
                dto.setFecha_ingreso(rs.getTimestamp("fecha_ingreso"));
                dto.setReservationStatus(rs.getString("estado"));
                dto.setReservationStatusId(rs.getInt("id_statusreserva"));

                reservationDTOS.add(dto);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Reservations: " + e.getMessage());
        }

        return reservationDTOS;
    }
    public static List<TableReservationDTO> getAllReservations() {
        String sql = "SELECT\n" +
                "            r.id AS id_reserva,\n" +
                "            cl.id AS id_cliente,\n" +
                "            cl.nombre,\n" +
                "            cl.tipo_documento,\n" +
                "            cl.numero_documento,\n" +
                "            h.id AS id_habitacion,\n" +
                "            h.numero,\n" +
                "            th.nombre AS tipo_habitacion,\n" +
                "            r.fecha_inicio,\n" +
                "            r.fecha_fin,\n" +
                "            r.fecha_ingreso,\n" +
                "            er.estado,\n" +
                "            er.id AS id_statusreserva\n" +
                "        FROM reservas r\n" +
                "                 INNER JOIN estado_reserva er ON er.id = r.estado_id\n" +
                "                 INNER JOIN clientes cl ON r.cliente_id = cl.id\n" +
                "                 INNER JOIN detalle_habitacion dh ON dh.reserva_id = r.id\n" +
                "                 INNER JOIN habitaciones h ON h.id = dh.habitacion_id\n" +
                "                 INNER JOIN tipo_habitacion th ON th.id = h.tipo_id\n" +
                "        ORDER BY r.id DESC";

        List<TableReservationDTO> reservationDTOS = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {


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
                dto.setFecha_ingreso(rs.getTimestamp("fecha_ingreso"));
                dto.setReservationStatus(rs.getString("estado"));
                dto.setReservationStatusId(rs.getInt("id_statusreserva"));

                reservationDTOS.add(dto);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Reservations: " + e.getMessage());
        }

        return reservationDTOS;
    }

    public static int registerReservation(Reservation reservation, Room room, double payment, Checkout checkout) {
        String sql = "INSERT INTO reservas (cliente_id, empleado_id, fecha_inicio, fecha_fin, estado_id, descuento, cobro_extra, adelanto,fecha_ingreso) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                ps.setTimestamp(9, reservation.getFechaIngreso());
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

    public static List<TableReservationDTO> filterReservation(String client, String documento, String fecDesde, String fecHasta,
                                                              String estado, int page, int size) {
        List<TableReservationDTO> allReservations = getAllReservations(); // Obtiene todos los registros

        String clientLower = client.toLowerCase().trim();
        String documentoLower = documento.toLowerCase().trim();
        java.sql.Timestamp fecDesdeLower = parseFecha(fecDesde);
        java.sql.Timestamp fecHastaLower = parseFecha(fecHasta);
        String estadoLower = estado.toLowerCase().trim();

        List<TableReservationDTO> filteredRooms = allReservations.stream()
                .filter(reservation ->
                                (clientLower.isEmpty() || reservation.getClientName().toLowerCase().contains(clientLower)) &&
                                (documentoLower.isEmpty() || reservation.getDocumentNumber().contains(documentoLower)) &&
                                (fecDesdeLower == null || !reservation.getCheckOutDate().before(fecDesdeLower)) &&
                                (fecHastaLower == null || !reservation.getCheckInDate().after(fecHastaLower)) &&
                                (estadoLower.isEmpty() || reservation.getReservationStatus().toLowerCase().equalsIgnoreCase(estadoLower))
                )
                .collect(Collectors.toList());

        // Paginación: calcular desde qué índice empezar y hasta dónde llegar
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, filteredRooms.size());

        return filteredRooms.subList(fromIndex, toIndex);
    }
    public static int countFilteredReservations(String client, String documento, String fecDesde, String fecHasta,
                                                String estado) {
        List<TableReservationDTO> allReservations = getAllReservations();

        String clientLower = client.toLowerCase().trim();
        String documentoLower = documento.toLowerCase().trim();
        java.sql.Timestamp fecDesdeLower = parseFecha(fecDesde);
        java.sql.Timestamp fecHastaLower = parseFecha(fecHasta);
        String estadoLower = estado.toLowerCase().trim();

        return (int) allReservations.stream()
                .filter(reservation ->
                        (clientLower.isEmpty() || reservation.getClientName().toLowerCase().contains(clientLower)) &&
                                (documentoLower.isEmpty() || reservation.getDocumentNumber().contains(documentoLower)) &&
                                (fecDesdeLower == null || !reservation.getCheckOutDate().before(fecDesdeLower)) &&
                                (fecHastaLower == null || !reservation.getCheckInDate().after(fecHastaLower)) &&
                                (estadoLower.isEmpty() || reservation.getReservationStatus().toLowerCase().equalsIgnoreCase(estadoLower))
                ).count();
    }
    private static Timestamp parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.isEmpty()) return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(fechaStr, formatter);
        return Timestamp.valueOf(localDateTime);
    }

    public static boolean actualizarFechaSalidaReserva(int idReservaEditar, Timestamp nuevaFechaFin) {
        String obtenerDatosSql = """
        SELECT r.fecha_inicio, h.precio
        FROM reservas r
        JOIN detalle_habitacion dh ON r.id = dh.reserva_id
        JOIN habitaciones h ON dh.habitacion_id = h.id
        WHERE r.id = ?
    """;

        String sqlReserva = "UPDATE reservas SET fecha_fin = ? WHERE id = ?";
        String sqlCheckout = "UPDATE checkout SET fecha_checkout = ? WHERE reserva_id = ?";
        String sqlDetalle = "UPDATE detalle_habitacion SET pago_total = ? WHERE reserva_id = ?";
        boolean success = false;

        try (Connection cnn = dataSource.getConnection()) {
            cnn.setAutoCommit(false);

            try (
                    PreparedStatement psDatos = cnn.prepareStatement(obtenerDatosSql);
                    PreparedStatement psReserva = cnn.prepareStatement(sqlReserva);
                    PreparedStatement psCheckout = cnn.prepareStatement(sqlCheckout);
                    PreparedStatement psDetalle = cnn.prepareStatement(sqlDetalle)
            ) {
                psDatos.setInt(1, idReservaEditar);
                ResultSet rs = psDatos.executeQuery();

                if (rs.next()) {
                    Timestamp fechaInicio = rs.getTimestamp("fecha_inicio");
                    BigDecimal precioHabitacion = rs.getBigDecimal("precio");

                    // Calcular cantidad de días (al menos 1)
                    long millisDiferencia = nuevaFechaFin.getTime() - fechaInicio.getTime();
                    long dias = Math.max(1, millisDiferencia / (1000 * 60 * 60 * 24));

                    BigDecimal nuevoTotal = precioHabitacion.multiply(BigDecimal.valueOf(dias));

                    // Actualizar reservas
                    psReserva.setTimestamp(1, nuevaFechaFin);
                    psReserva.setInt(2, idReservaEditar);
                    psReserva.executeUpdate();

                    // Actualizar checkout
                    psCheckout.setTimestamp(1, nuevaFechaFin);
                    psCheckout.setInt(2, idReservaEditar);
                    psCheckout.executeUpdate();

                    // Actualizar detalle_habitacion con nuevo pago_total
                    psDetalle.setBigDecimal(1, nuevoTotal);
                    psDetalle.setInt(2, idReservaEditar);
                    psDetalle.executeUpdate();

                    cnn.commit();
                    success = true;
                } else {
                    System.out.println("No se encontró la reserva o la habitación.");
                }
            } catch (SQLException e) {
                cnn.rollback();
                System.out.println("Error durante la actualización: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }

        return success;
    }

    public static boolean validarAdmin(String usuario, String clavePlano) {
        boolean esValido = false;

        String sql = "SELECT u.password " +  // Aquí traes el hash desde BD
                "FROM usuarios u " +
                "INNER JOIN empleados e ON u.empleado_id = e.id " +
                "INNER JOIN roles r ON r.id = e.rol_id " +
                "WHERE u.username = ? " +
                "AND r.id = 1 " + // Verificas que sea admin
                "AND u.estado = 'Activo'";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario); // Solo pasas el usuario

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String claveHasheada = rs.getString("password"); // Hash almacenado en BD

                    // Comparas la clave ingresada (plana) con la clave hasheada
                    if (BCrypt.checkpw(clavePlano, claveHasheada)) {
                        esValido = true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return esValido;
    }
}
