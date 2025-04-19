package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.Model.Service;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GestionService  {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionService.class);

    public static boolean registerservice(Service service) {
        String sql = "INSERT INTO servicios (nombre, precio) VALUES (?, ?)";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, service.getName());
            ps.setDouble(2, service.getPrice());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Service " + service.getName() + " registered successfully");
                result = true;
            }
        } catch (SQLException e) {
            LOGGER.warning("Error when registering the service: " + e.getMessage());
        }
        return result;
    }
    public static boolean updateservice(Service service) {
        String sql = "UPDATE servicios SET nombre = ?, precio = ? WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, service.getName());
            ps.setDouble(2, service.getPrice());
            ps.setInt(3, service.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Service " + service.getId() + " updated successfully.");
                result = true;
            } else {
                LOGGER.warning("Error updating service. No Service found with ID: " + service.getId());
            }
        } catch (SQLException e) {
            LOGGER.severe("Error updating Service " + service.getId() + ": " + e.getMessage());
        }

        return result;
    }
    public static boolean deleteservice(int serviceId) {
        String checkSql = "SELECT COUNT(*) FROM servicios WHERE id = ?";
        String deleteSql = "DELETE FROM servicios WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement checkPs = cnn.prepareStatement(checkSql)) {

            checkPs.setInt(1, serviceId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                try (PreparedStatement deletePs = cnn.prepareStatement(deleteSql)) {
                    deletePs.setInt(1, serviceId);

                    int rowsAffected = deletePs.executeUpdate();
                    if (rowsAffected > 0) {
                        LOGGER.info("Service with ID " + serviceId + " deleted successfully.");
                        result = true;
                    }
                }
            } else {
                LOGGER.warning("Error deleting service. No Service found with ID: " + serviceId);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error deleting Service " + serviceId + ": " + e.getMessage());
        }

        return result;
    }
    public static List<Service> getAllservices() {
        String sql = "SELECT * FROM servicios";
        List<Service> services = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nombre");
                Double price = rs.getDouble("precio");

                services.add(new Service(id, name, price));
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving services: " + e.getMessage());
        }

        return services;
    }
    public static Service getserviceById(int serviceId) {
        String sql = "SELECT id, nombre, precio FROM servicios WHERE id = ?";
        Service Service = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, serviceId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nombre");
                double price = rs.getDouble("precio");

                Service = new Service(id, name, price);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error retrieving Service with ID " + serviceId + ": " + e.getMessage());
        }

        return Service;
    }

}
