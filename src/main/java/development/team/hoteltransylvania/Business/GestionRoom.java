package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.DTO.usersEmployeeDTO;
import development.team.hoteltransylvania.Model.*;
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

public class GestionRoom {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionRoom.class);

    public static boolean registerRoom(Room Room) {
        String sql = "INSERT INTO habitaciones (numero, tipo_id, estado_id, precio, piso_id) VALUES (?,?,?,?,?)";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, Room.getNumber());
            ps.setInt(2, Room.getTypeRoom().getId());
            ps.setInt(3, Room.getStatusRoom().getValue());
            ps.setDouble(4, Room.getPrice());
            ps.setInt(5, Room.getFloor());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Room " + Room.getNumber() + " registered successfully");
                result = true;
            }
        } catch (SQLException e) {
            LOGGER.warning("Error when registering the Room: " + e.getMessage());
        }
        return result;
    }
    public static boolean updateRoom(Room roomUpdate) {
        String sql = "UPDATE habitaciones SET numero = ?, tipo_id = ?, estado_id = ?, precio = ? WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, roomUpdate.getNumber());
            ps.setDouble(2, roomUpdate.getTypeRoom().getId());
            ps.setInt(3, roomUpdate.getStatusRoom().getValue());
            ps.setDouble(4, roomUpdate.getPrice());
            ps.setInt(5, roomUpdate.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Room " + roomUpdate.getId() + " updated successfully.");
                result = true;
            } else {
                LOGGER.warning("Error updating Room. No Room found with ID: " + roomUpdate.getId());
            }
        } catch (SQLException e) {
            LOGGER.severe("Error updating Room " + roomUpdate.getId() + ": " + e.getMessage());
        }

        return result;
    }
    public static boolean updateStatusRoom(int idRoom, int status) {
        String sql = "UPDATE habitaciones SET estado_id = ? WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, status);
            ps.setDouble(2, idRoom);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Room " + idRoom + " updated successfully.");
                result = true;
            } else {
                LOGGER.warning("Error updating Room. No Room found with ID: " + idRoom);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error updating Room " + idRoom + ": " + e.getMessage());
        }

        return result;
    }
    public static boolean hasUpcomingReservations(int idRoom) {
        String sql = "SELECT\n" +
                "    count(*)\n" +
                "FROM habitaciones h\n" +
                "         JOIN detalle_habitacion dh ON h.id = dh.habitacion_id\n" +
                "         JOIN reservas r ON r.id = dh.reserva_id\n" +
                "         JOIN estado_habitacion e ON h.estado_id = e.id\n" +
                "WHERE dh.habitacion_id=? and r.estado_id in (1,4);";
        boolean result = false;

        try(Connection cnn = dataSource.getConnection();
            PreparedStatement ps = cnn.prepareStatement(sql)){
            ps.setInt(1, idRoom);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(1) > 0;
                }
            }

        }catch (SQLException e){
            LOGGER.severe("Error room not found. " + idRoom + ": " + e.getMessage());
        }
        return result;
    }
    public static boolean updateAvailability(int roomId, int availability) {
        String checkSql = "SELECT COUNT(*) FROM habitaciones WHERE id = ?";
        String updateSql = "UPDATE habitaciones SET disponible = ? WHERE id = ?";
        boolean result = false;

        // Invertir el valor: si es 1 pasa a 0, si es 0 pasa a 1
        int newAvailability = (availability == 1) ? 0 : 1;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement checkPs = cnn.prepareStatement(checkSql)) {

            checkPs.setInt(1, roomId);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    try (PreparedStatement updatePs = cnn.prepareStatement(updateSql)) {
                        updatePs.setInt(1, newAvailability);
                        updatePs.setInt(2, roomId);

                        int rowsAffected = updatePs.executeUpdate();
                        if (rowsAffected > 0) {
                            LOGGER.info("Room with ID " + roomId + " availability updated to " + newAvailability);
                            result = true;
                        } else {
                            LOGGER.warning("No rows updated. Room ID may not exist.");
                        }
                    }
                } else {
                    LOGGER.warning("No room found with ID: " + roomId);
                }
            }

        } catch (SQLException e) {
            LOGGER.severe("Error updating availability for room " + roomId + ": " + e.getMessage());
        }

        return result;
    }
    public static List<Room> getAllRoomsReservation() {

        String actualizarPendientes =
                "UPDATE habitaciones " +
                        "SET estado_id = 4 " +
                        "WHERE id IN ( " +
                        "  SELECT h.id " +
                        "  FROM habitaciones h " +
                        "  JOIN detalle_habitacion dh ON h.id = dh.habitacion_id " +
                        "  JOIN reservas r ON r.id = dh.reserva_id " +
                        "  WHERE r.estado_id = 1 " +
                        "    AND r.fecha_inicio BETWEEN " +
                        "      now() - interval '20 minutes' AND " +
                        "      now() + interval '1 hour' " +
                        ")";

        String actualizarPasadas = "UPDATE habitaciones \n" +
                "SET estado_id = 1\n" +
                "WHERE id IN (\n" +
                "    SELECT h.id\n" +
                "    FROM habitaciones h\n" +
                "    JOIN detalle_habitacion dh ON h.id = dh.habitacion_id\n" +
                "    JOIN reservas r ON r.id = dh.reserva_id\n" +
                "    WHERE r.estado_id = 1\n" +
                "      AND r.fecha_inicio < now() - interval '20 minutes'\n" +
                ");";

        String actualizarReservasPasTolerancia = "UPDATE reservas\n" +
                "SET estado_id = 3\n" +
                "WHERE id IN (\n" +
                "    SELECT r.id\n" +
                "    FROM reservas r\n" +
                "    JOIN detalle_habitacion dh ON r.id = dh.reserva_id\n" +
                "    JOIN habitaciones h ON dh.habitacion_id = h.id\n" +
                "    WHERE r.estado_id = 1\n" +
                "      AND r.fecha_inicio < now() - interval '20 minutes'\n" +
                ");";

        String sql = "SELECT " +
                "    h.id, " +
                "    h.numero, " +
                "    h.piso_id, " +
                "    p.nombre AS nombre_piso, " +
                "    h.tipo_id, " +
                "    t.nombre AS tipo_nombre, t.estatus AS estado_tipo, " +
                "    h.precio, " +
                "    h.estado_id, " +
                "    e.estado AS estado_nombre, " +
                "    h.disponible " +
                "FROM habitaciones h " +
                "JOIN tipo_habitacion t ON h.tipo_id = t.id " +
                "JOIN estado_habitacion e ON h.estado_id = e.id " +
                "JOIN pisos p ON p.id = h.piso_id " +
                "WHERE p.estatus = 'Activo' and t.estatus = 'Activo' and h.disponible=1";  // Paginación aplicada

        List<Room> rooms = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection()) {

            // Forzar la zona horaria en cada conexión del pool
            try (PreparedStatement setTZ = cnn.prepareStatement("SET TIME ZONE 'America/Lima'")) {
                setTZ.execute();
            }

            // Ejecutar la actualización primero
            try (PreparedStatement actualizarPs = cnn.prepareStatement(actualizarPendientes)) {
                actualizarPs.executeUpdate();
            }
            try (PreparedStatement actualizarPas = cnn.prepareStatement(actualizarPasadas)) {
                actualizarPas.executeUpdate();
            }
            try (PreparedStatement actualizarPasTol = cnn.prepareStatement(actualizarReservasPasTolerancia)) {
                actualizarPasTol.executeUpdate();
            }

            // Ahora sí, obtenemos las habitaciones ya actualizadas
            try (PreparedStatement ps = cnn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String number = rs.getString("numero");
                    int typeId = rs.getInt("tipo_id");
                    String typeName = rs.getString("tipo_nombre");
                    String statusTypeRoom = rs.getString("estado_tipo");
                    int statusId = rs.getInt("estado_id");
                    double price = rs.getDouble("precio");
                    int floorId = rs.getInt("piso_id");
                    int disponible = rs.getInt("disponible");

                    StatusRoom statusRoom = StatusRoom.fromId(statusId);
                    TypeRoom typeRoom = new TypeRoom(typeId, typeName, statusTypeRoom);

                    rooms.add(new Room(id, number, typeRoom, statusRoom, price, floorId, disponible));
                }
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Rooms: " + e.getMessage());
        }

        return rooms.stream()
                .sorted(Comparator.comparing((Room room) -> Integer.parseInt(room.getNumber())))
                .collect(Collectors.toList());
    }
    public static List<Room> getAllRooms() {
        String sql = "SELECT " +
                "    h.id, " +
                "    h.numero, " +
                "    h.piso_id, " +
                "    p.nombre AS nombre_piso, " +
                "    h.tipo_id, " +
                "    t.nombre AS tipo_nombre, t.estatus AS estado_tipo, " +
                "    h.precio, " +
                "    h.estado_id, " +
                "    e.estado AS estado_nombre, " +
                "    h.disponible " +
                "FROM habitaciones h " +
                "JOIN tipo_habitacion t ON h.tipo_id = t.id " +
                "JOIN estado_habitacion e ON h.estado_id = e.id " +
                "JOIN pisos p ON p.id = h.piso_id ";  // Paginación aplicada

        List<Room> rooms = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("numero");
                int typeId = rs.getInt("tipo_id");
                String typeName = rs.getString("tipo_nombre");
                String statusTypeRoom = rs.getString("estado_tipo");
                int statusId = rs.getInt("estado_id");
                double price = rs.getDouble("precio");
                int floorId = rs.getInt("piso_id");
                int disponible = rs.getInt("disponible");

                // Se obtiene el enum directamente por ID
                StatusRoom statusRoom = StatusRoom.fromId(statusId);

                // Se crea el objeto TypeRoom directamente sin consulta extra
                TypeRoom typeRoom = new TypeRoom(typeId, typeName, statusTypeRoom);

                rooms.add(new Room(id, number, typeRoom, statusRoom, price, floorId, disponible));
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Rooms: " + e.getMessage());
        }

        return rooms.stream()
                .sorted(Comparator.comparing((Room room) -> Integer.parseInt(room.getNumber())))
                .collect(Collectors.toList());
    }
    public static List<Room> getRoomsPaginated(int page, int pageSize) {
        String sql = "SELECT " +
                "    h.id, " +
                "    h.numero, " +
                "    h.piso_id, " +
                "    p.nombre AS nombre_piso, " +
                "    h.tipo_id, " +
                "    t.nombre AS tipo_nombre, t.estatus AS estado_tipo, " +
                "    h.precio, " +
                "    h.estado_id, " +
                "    e.estado AS estado_nombre, " +
                "    h.disponible " +
                "FROM habitaciones h " +
                "JOIN tipo_habitacion t ON h.tipo_id = t.id " +
                "JOIN estado_habitacion e ON h.estado_id = e.id " +
                "JOIN pisos p ON p.id = h.piso_id " +
                "ORDER BY h.numero ASC " +
                "LIMIT ? OFFSET ?";

        List<Room> rooms = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize); // OFFSET = (página - 1) * tamaño

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("numero");
                int typeId = rs.getInt("tipo_id");
                String typeName = rs.getString("tipo_nombre");
                String statusTypeR = rs.getString("estado_tipo");
                int statusId = rs.getInt("estado_id");
                double price = rs.getDouble("precio");
                int floorId = rs.getInt("piso_id");
                int disponible = rs.getInt("disponible");

                StatusRoom statusRoom = StatusRoom.fromId(statusId);
                TypeRoom typeRoom = new TypeRoom(typeId, typeName, statusTypeR);

                rooms.add(new Room(id, number, typeRoom, statusRoom, price, floorId, disponible));
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Rooms: " + e.getMessage());
        }

        return rooms;
    }
    public static List<Room> filterRooms(String nombre, String estado, int page, int size) {
        List<Room> allRooms = getAllRooms(); // Obtiene todos los registros

        String estadoLower = estado.toLowerCase().trim();
        String nombreLower = nombre.toLowerCase().trim();

        List<Room> filteredRooms = allRooms.stream()
                .filter(room ->
                        (nombreLower.isEmpty() || room.getNumber().contains(nombreLower)) &&
                                (estadoLower.isEmpty() || room.getStatusRoom().getName().equalsIgnoreCase(estadoLower))
                )
                .collect(Collectors.toList());

        // Paginación: calcular desde qué índice empezar y hasta dónde llegar
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, filteredRooms.size());

        return filteredRooms.subList(fromIndex, toIndex);
    }
    public static int countFilteredRooms(String nombre, String estado) {
        List<Room> allRooms = getAllRooms();

        String estadoLower = estado.toLowerCase().trim();
        String nombreLower = nombre.toLowerCase().trim();

        return (int) allRooms.stream()
                .filter(room ->
                        (nombreLower.isEmpty() || room.getNumber().contains(nombreLower)) &&
                                (estadoLower.isEmpty() || room.getStatusRoom().getName().equalsIgnoreCase(estadoLower))
                ).count();
    }
    public static int getTotalRooms() {
        String sql = "SELECT COUNT(*) FROM habitaciones";
        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error counting Rooms: " + e.getMessage());
            System.out.println("Error counting Rooms: " + e.getMessage());
        }
        return 0;
    }
    public static StatusRoom getStatusRoom(String nombre) {
        try {
            return StatusRoom.valueOf(nombre.toLowerCase());
        } catch (IllegalArgumentException e) {
            LOGGER.severe("Error: StatusRoom no válido -> '" + nombre + "'");
            return null;
        }
    }
    public static TypeRoom getTypeRoomById(int idType) {
        String sql = "SELECT * FROM tipo_habitacion WHERE id = ?";
        TypeRoom typeRoom = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, idType);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int tipoId = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String estatus = rs.getString("estatus");
                    typeRoom = new TypeRoom(tipoId, nombre, estatus);
                }
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving TypeRoom: " + e.getMessage());
        }

        return typeRoom;
    }
    public static Room getRoomById(int roomId) {
        String sql = "SELECT h.id, h.numero, h.tipo_id, h.piso_id, h.precio, h.estado_id, " +
                "t.nombre AS tipo_nombre, t.estatus AS estado_tipo " +
                "FROM habitaciones h " +
                "JOIN tipo_habitacion t ON h.tipo_id = t.id " +
                "WHERE h.id = ?;";
        Room room = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("numero");
                int typeId = rs.getInt("tipo_id");
                String typeName = rs.getString("tipo_nombre");
                String estadoType = rs.getString("estado_tipo");
                int statusId = rs.getInt("estado_id");
                double price = rs.getDouble("precio");
                int floorId = rs.getInt("piso_id");

                StatusRoom statusRoom = StatusRoom.fromId(statusId);
                TypeRoom typeRoom = new TypeRoom(typeId, typeName, estadoType);

                room = new Room(id, number, typeRoom, statusRoom, price, floorId);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error retrieving product with ID " + roomId + ": " + e.getMessage());
        }

        return room;
    }
    public static int quantityFloors(){
        String sql = "SELECT COUNT(*) FROM pisos WHERE estatus = 'Activo'";
        int count = 0;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1); // Obtener el resultado del conteo
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
    public static List<Floor> quantityFloorsEnabled(){
        String sql = "SELECT * FROM pisos WHERE estatus = 'Activo'";
        List<Floor> floors = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                floors.add(new Floor(rs.getInt("id"), rs.getString("nombre"), rs.getString("estatus")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return floors;
    }
    public static List<Room> getRoomByTypeRoom(int typeRoomId) {
        String sql = "SELECT * FROM habitaciones h " +
                "INNER JOIN tipo_habitacion th ON h.tipo_id=th.id " +
                "WHERE th.id = ? and h.disponible=1";
        List<Room> rooms = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, typeRoomId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("numero");
                double price = rs.getDouble("precio");
                StatusRoom statusRoom = StatusRoom.fromId(rs.getInt("estado_id"));
                rooms.add(new Room(id, number,price,statusRoom));
            }
        } catch (SQLException e) {
            LOGGER.severe("No rooms of type could be found " + typeRoomId + ": " + e.getMessage());
        }

        return rooms;
    }


}

