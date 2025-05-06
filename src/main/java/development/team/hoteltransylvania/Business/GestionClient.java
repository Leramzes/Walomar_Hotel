package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.DTO.usersEmployeeDTO;
import development.team.hoteltransylvania.Model.Client;
import development.team.hoteltransylvania.Model.Product;
import development.team.hoteltransylvania.Model.Room;
import development.team.hoteltransylvania.Model.TypeDocument;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GestionClient {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionClient.class);

    public static List<Client> getAllClients() {
        String sql = "SELECT * FROM clientes";
        List<Client> allClients = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id_cliente = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String numDocu = rs.getString("numero_documento");
                String tipoDoc = rs.getString("tipo_documento");
                String telefono = rs.getString("telefono");

                allClients.add(new Client(id_cliente,nombre,telefono,email, TypeDocument.valueOf(tipoDoc),numDocu));
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving clients: " + e.getMessage());
        }

        return allClients;
    }
    public static List<Client> getAllClientsPaginated(int page, int pageSize) {
        String sql = "SELECT * FROM clientes "+
                "LIMIT ? OFFSET ?";
        List<Client> allClients = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id_cliente = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String ap_paterno = rs.getString("ap_paterno");
                String ap_materno = rs.getString("ap_materno");
                String email = rs.getString("email");
                String numDocu = rs.getString("numero_documento");
                String tipoDoc = rs.getString("tipo_documento");
                String telefono = rs.getString("telefono");

                allClients.add(new Client(id_cliente,nombre, ap_materno, ap_paterno, telefono,email,
                        TypeDocument.valueOf(tipoDoc), numDocu));
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving clients: " + e.getMessage());
        }

        return allClients;
    }
    public static boolean deleteClient(int clientId) {
        String checkSql = "SELECT COUNT(*) FROM clientes WHERE id = ?";
        String deleteSql = "DELETE FROM clientes WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement checkPs = cnn.prepareStatement(checkSql)) {

            checkPs.setInt(1, clientId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                try (PreparedStatement deletePs = cnn.prepareStatement(deleteSql)) {
                    deletePs.setInt(1, clientId);

                    int rowsAffected = deletePs.executeUpdate();
                    if (rowsAffected > 0) {
                        LOGGER.info("Client with ID " + clientId + " deleted successfully.");
                        result = true;
                    }
                }
            } else {
                LOGGER.warning("Error deleting client. No client found with ID: " + clientId);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error deleting client " + clientId + ": " + e.getMessage());
        }

        return result;
    }
    public static boolean registerClient(Client client) {
        String sql = "INSERT INTO clientes (nombre, email, numero_documento, tipo_documento, telefono, ap_paterno, ap_materno, razon_social, direccion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Permite obtener el ID generado

            ps.setString(1, client.getName());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getNumberDocument());
            ps.setString(4, client.getTypeDocument().toString());
            ps.setString(5,client.getTelephone());
            ps.setString(6,client.getApPaterno());
            ps.setString(7,client.getApMaterno());
            ps.setString(8,client.getRazonSocial());
            ps.setString(9,client.getDireccion());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Client " + client.getName() + " registered successfully");
                result = true;
            }
        } catch (SQLException e) {
            LOGGER.warning("Error when registering the client: " + e.getMessage());
            System.out.println(e.getMessage());
        }
        return result;
    }
    public static Client getClientById(int clientId) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        Client client = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id_cliente = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String numDocu = rs.getString("numero_documento");
                String tipoDoc = rs.getString("tipo_documento");
                String telefono = rs.getString("telefono");


                client = new Client(id_cliente,nombre,telefono,email, TypeDocument.valueOf(tipoDoc),numDocu);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error retrieving client with ID " + client + ": " + e.getMessage());
        }

        return client;
    }
    public static boolean updateClient(Client client) {
        String sql = "UPDATE clientes " +
                "SET nombre = ?,\n" +
                "    email = ?,\n" +
                "    numero_documento = ?,\n" +
                "    tipo_documento = ?,\n" +
                "    telefono = ? WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, client.getName());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getNumberDocument());
            ps.setString(4, client.getTypeDocument().toString());
            ps.setString(5, client.getTelephone());
            ps.setInt(6, client.getId());// Se asume que `id` es un atributo de `Room`

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Client " + client.getId() + " updated successfully.");
                result = true;
            } else {
                LOGGER.warning("Error updating client. No client found with ID: " + client.getId());
            }
        } catch (SQLException e) {
            LOGGER.severe("Error updating client " + client.getId() + ": " + e.getMessage());
        }

        return result;
    }
    public static List<Client> filterClients(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return getAllClients(); // Devuelve todos los productos si no hay búsqueda.
        }

        return getAllClients().stream()
                .filter(client -> client.getName().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
    public static Client getClient(String numeroDocumento) {
        if (numeroDocumento == null || numeroDocumento.trim().isEmpty()) {
            return null;
        }

        return getAllClients().stream()
                .filter(client -> client.getNumberDocument().equalsIgnoreCase(numeroDocumento.trim()))
                .findFirst()
                .orElse(null);
    }
}
