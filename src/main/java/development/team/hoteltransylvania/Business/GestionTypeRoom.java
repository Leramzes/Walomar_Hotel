package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.Model.*;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GestionTypeRoom {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionTypeRoom.class);

    public static boolean registerTypeRoom(TypeRoom typeRoom) {
        if (typeRoomExists(typeRoom.getName())) {
            LOGGER.warning("Type Room name " + typeRoom.getName() + " already exists.");
            return false;
        }

        String sql = "INSERT INTO tipo_habitacion (nombre, estatus) VALUES (?, ?)";
        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, typeRoom.getName());
            ps.setString(2, typeRoom.getStatus());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("TypeRoom " + typeRoom.getId() + " registered successfully.");
                result = true;
            }
        } catch (SQLException e) {
            LOGGER.warning("Error when registering the TypeRoom: " + e.getMessage());
        }

        return result;
    }

    public static boolean updateTypeRoom(TypeRoom TypeRoomUpdate) {
        if (typeRoomExists(TypeRoomUpdate.getName())) {
            LOGGER.warning("Cannot update. Type Room name " + TypeRoomUpdate.getName() + " already exists.");
            return false;
        }

        String sql = "UPDATE tipo_habitacion SET nombre = ? WHERE id = ?";
        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, TypeRoomUpdate.getName());
            ps.setInt(2, TypeRoomUpdate.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Room " + TypeRoomUpdate.getId() + " updated successfully.");
                result = true;
            } else {
                LOGGER.warning("No TypeRoom found with ID: " + TypeRoomUpdate.getId());
            }
        } catch (SQLException e) {
            LOGGER.severe("Error updating Room " + TypeRoomUpdate.getId() + ": " + e.getMessage());
        }

        return result;
    }


    // Método para verificar si el tipo de habitación ya existe en la tabla
    private static boolean typeRoomExists(String typeRoomName) {
        String sql = "SELECT 1 FROM tipo_habitacion WHERE nombre = ?";

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setString(1, typeRoomName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Si encuentra un resultado, el tipo de habitación ya existe
            }
        } catch (SQLException e) {
            LOGGER.severe("Error checking if TypeRoom exists in the table: " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteTypeRoom(int TypeRoomId) {
        String checkSql = "SELECT COUNT(*) FROM tipo_habitacion WHERE id = ?";
        String deleteSql = "DELETE FROM tipo_habitacion WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement checkPs = cnn.prepareStatement(checkSql)) {

            checkPs.setInt(1, TypeRoomId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                try (PreparedStatement deletePs = cnn.prepareStatement(deleteSql)) {
                    deletePs.setInt(1, TypeRoomId);

                    int rowsAffected = deletePs.executeUpdate();
                    if (rowsAffected > 0) {
                        LOGGER.info("TypeRoomId with ID " + TypeRoomId + " deleted successfully.");
                        result = true;
                    }
                }
            } else {
                LOGGER.warning("Error deleting Room. No Room found with ID: " + TypeRoomId);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error deleting Room " + TypeRoomId + ": " + e.getMessage());
        }

        return result;
    }
    public static List<TypeRoom> getAllTypeRooms() {
        String sql = "SELECT id, nombre, estatus FROM tipo_habitacion";

        List<TypeRoom> typeRooms = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String estatus = rs.getString("estatus");

                typeRooms.add(new TypeRoom(id, nombre,estatus));
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving TypeRooms: " + e.getMessage());
            System.out.println("Error retrieving TypeRooms: " + e.getMessage());
        }
        return typeRooms.stream()
                .sorted(Comparator.comparingInt(TypeRoom::getId))
                .collect(Collectors.toList());
    }
    public static List<TypeRoom> getAllTypeRoomsActive() {
        String sql = "SELECT id, nombre, estatus FROM tipo_habitacion WHERE estatus = 'Activo'";

        List<TypeRoom> typeRooms = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String estatus = rs.getString("estatus");

                typeRooms.add(new TypeRoom(id, nombre,estatus));
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving TypeRooms: " + e.getMessage());
            System.out.println("Error retrieving TypeRooms: " + e.getMessage());
        }
        return typeRooms.stream()
                .sorted(Comparator.comparingInt(TypeRoom::getId))
                .collect(Collectors.toList());
    }
    public static List<TypeRoom> getAllTypeRoomsPaginated(int page, int pageSize) {
        String sql = "SELECT id, nombre, estatus FROM tipo_habitacion " +
                "LIMIT ? OFFSET ?";;

        List<TypeRoom> typeRooms = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String estatus = rs.getString("estatus");

                typeRooms.add(new TypeRoom(id, nombre,estatus));
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving TypeRooms: " + e.getMessage());
            System.out.println("Error retrieving TypeRooms: " + e.getMessage());
        }
        return typeRooms.stream()
                .sorted(Comparator.comparingInt(TypeRoom::getId))
                .collect(Collectors.toList());
    }
    public static List<TypeRoom> filterTypeRooms(String nombre, String estado, int page, int size) {
        List<TypeRoom> allTypes = getAllTypeRooms(); // Obtiene todos los registros

        String estadoLower = estado.toLowerCase().trim();
        String nombreLower = nombre.toLowerCase().trim();

        List<TypeRoom> filteredRoomsType = allTypes.stream()
                .filter(room ->
                        (nombreLower.isEmpty() || room.getName().toLowerCase().contains(nombreLower)) &&
                                (estadoLower.isEmpty() || room.getStatus().equalsIgnoreCase(estadoLower))
                )
                .collect(Collectors.toList());

        // Paginación: calcular desde qué índice empezar y hasta dónde llegar
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, filteredRoomsType.size());

        return filteredRoomsType.subList(fromIndex, toIndex);
    }
    public static int countFilteredTypeRooms(String nombre, String estado) {
        List<TypeRoom> allTypes = getAllTypeRooms();

        String estadoLower = estado.toLowerCase().trim();
        String nombreLower = nombre.toLowerCase().trim();

        return (int) allTypes.stream()
                .filter(room ->
                        (nombreLower.isEmpty() || room.getName().toLowerCase().contains(nombreLower)) &&
                                (estadoLower.isEmpty() || room.getStatus().equalsIgnoreCase(estadoLower))
                ).count();
    }

    public static int getTotalTypeRooms() {
        String sql = "SELECT COUNT(*) FROM tipo_habitacion";
        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error counting TypeRooms: " + e.getMessage());
        }
        return 0;
    }

    public static List<TypeRoom> getTypeRoomsPaginated(int page, int pageSize) {
        String sql = "SELECT th.id, th.nombre, th.estatus " +
                "FROM tipo_habitacion th " +
                "ORDER BY th.id ASC " +
                "LIMIT ? OFFSET ?";

        List<TypeRoom> typeRooms = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize); // OFFSET = (página - 1) * tamaño

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nombre");
                String estatus = rs.getString("estatus");

                typeRooms.add(new TypeRoom(id, name,estatus));
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving TypeRooms: " + e.getMessage());
            System.out.println("Error retrieving TypeRooms: " + e.getMessage());
        }

        return typeRooms;
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

    public static void updateStatus(int idType, String estado) {
        // SQL para verificar si hay habitaciones de este tipo que no estén libres
        String checkSql = "SELECT COUNT(*) FROM habitaciones h " +
                "JOIN estado_habitacion eh ON h.estado_id = eh.id " +
                "WHERE h.tipo_id = ? AND eh.id != 1";

        // SQL para actualizar el estado del tipo de habitación
        String updateSql = "UPDATE tipo_habitacion SET estatus = ? WHERE id = ?";

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement checkPs = cnn.prepareStatement(checkSql)) {

            checkPs.setInt(1, idType);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                LOGGER.warning("No se puede actualizar el estado del tipo de habitación " + idType +
                        " porque hay habitaciones de este tipo que no están libres.");
                return; // Salimos del método si hay habitaciones ocupadas
            }

            // Si todas las habitaciones de este tipo están libres, procedemos con la actualización
            try (PreparedStatement updatePs = cnn.prepareStatement(updateSql)) {
                updatePs.setString(1, estado);
                updatePs.setInt(2, idType);

                int rowsAffected = updatePs.executeUpdate();
                if (rowsAffected > 0) {
                    LOGGER.info("Tipo de habitación " + idType + " actualizado exitosamente.");
                } else {
                    LOGGER.warning("No se encontró el tipo de habitación con ID: " + idType);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error actualizando el tipo de habitación " + idType + ": " + e.getMessage());
        }
    }

    public static List<TypeRoom> filterTypeRoom(String nombre) {
        List<TypeRoom> allTypeRooms = getAllTypeRooms(); // Obtiene todos los empleados una sola vez

        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Lista completa de Tipo Habitaciones retornada: " + allTypeRooms);
            return allTypeRooms;
        }

        String nombreLower = nombre.toLowerCase(); // Convertir el criterio de búsqueda a minúsculas

        List<TypeRoom> filteredTypeRooms = allTypeRooms.stream()
                .filter(typeRoom -> typeRoom.getName().toLowerCase().contains(nombreLower))
                .collect(Collectors.toList());

        System.out.println("Empleados filtrados: " + filteredTypeRooms);
        return filteredTypeRooms;
    }

    public static boolean hasUpcomingReservations(int idTypeRoom) {
        String sql = "SELECT count(*)\n" +
                "FROM habitaciones h\n" +
                "         JOIN estado_habitacion e ON h.estado_id = e.id\n" +
                "         JOIN detalle_habitacion dh ON h.id = dh.habitacion_id\n" +
                "         JOIN reservas r ON dh.reserva_id = r.id\n" +
                "WHERE h.tipo_id = ?\n" +
                "  AND r.estado_id IN (1, 4);";
        boolean result = false;

        try(Connection cnn = dataSource.getConnection();
            PreparedStatement ps = cnn.prepareStatement(sql)){
            ps.setInt(1, idTypeRoom);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(1) > 0;
                }
            }

        }catch (SQLException e){
            LOGGER.severe("Error type room not found " + idTypeRoom + ": " + e.getMessage());
        }
        return result;
    }
}


