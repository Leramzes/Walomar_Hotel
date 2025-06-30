package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.Model.PaymentMethod;
import development.team.hoteltransylvania.Model.Product;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class GestionMetodosPago {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionMetodosPago.class);

    public static boolean registerMetodo(PaymentMethod method) {
        String sql = "INSERT INTO metodo_pago (metodo, status) VALUES (?, ?)";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, method.getNameMethod());
            ps.setInt(2, method.getStatus());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Method " + method.getNameMethod() + " registered successfully");
                result = true;
            }
        } catch (SQLException e) {
            LOGGER.warning("Error when registering the method: " + e.getMessage());
        }
        return result;
    }
    public static boolean updateAvailability(int idMethod, int availability) {
        String checkSql = "SELECT COUNT(*) FROM metodo_pago WHERE id = ?";
        String updateSql = "UPDATE metodo_pago SET status = ? WHERE id = ?";
        boolean result = false;

        // Invertir el valor: si es 1 pasa a 0, si es 0 pasa a 1
        int newAvailability = (availability == 1) ? 0 : 1;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement checkPs = cnn.prepareStatement(checkSql)) {

            checkPs.setInt(1, idMethod);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    try (PreparedStatement updatePs = cnn.prepareStatement(updateSql)) {
                        updatePs.setInt(1, newAvailability);
                        updatePs.setInt(2, idMethod);

                        int rowsAffected = updatePs.executeUpdate();
                        if (rowsAffected > 0) {
                            LOGGER.info("Method with ID " + idMethod + " availability updated to " + newAvailability);
                            result = true;
                        } else {
                            LOGGER.warning("No rows updated. Method ID may not exist.");
                        }
                    }
                } else {
                    LOGGER.warning("No Method found with ID: " + idMethod);
                }
            }

        } catch (SQLException e) {
            LOGGER.severe("Error updating availability for Method " + idMethod + ": " + e.getMessage());
        }

        return result;
    }
    public static PaymentMethod getMethodPaymentById(int metodoPagoId) {
        String sql = "SELECT * FROM metodo_pago WHERE id = ?";
        PaymentMethod paymentMethod = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, metodoPagoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                paymentMethod = new PaymentMethod();
                paymentMethod.setId(rs.getInt("id"));
                paymentMethod.setNameMethod(rs.getString("metodo"));
                paymentMethod.setStatus(rs.getInt("status"));
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving payment method with ID " + metodoPagoId + ": " + e.getMessage());
        }

        return paymentMethod;
    }
    public static List<PaymentMethod> getAllMethodPayments() {
        String sql = "SELECT * FROM metodo_pago";
        List<PaymentMethod> paymentMethods = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {


            while (rs.next()) {
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setId(rs.getInt("id"));
                paymentMethod.setNameMethod(rs.getString("metodo"));
                paymentMethod.setStatus(rs.getInt("status"));
                paymentMethods.add(paymentMethod);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving methods: " + e.getMessage());
        }

        return paymentMethods;
    }
    public static List<PaymentMethod> filterPaymethMethods(String nombre, Integer estado) {
        List<PaymentMethod> allMethods = getAllMethodPayments(); // Obtiene todos los registros

        String nombreLower = nombre.toLowerCase().trim();

        List<PaymentMethod> filteredMetods = allMethods.stream()
                .filter(method ->
                        (nombreLower.isEmpty() || method.getNameMethod().toLowerCase().contains(nombreLower)) &&
                                (estado == null || method.getStatus() == estado)
                )
                .collect(Collectors.toList());

        return filteredMetods;
    }
    public static int countFilteredMethod(String nombre, Integer estado) {
        List<PaymentMethod> allMethods = getAllMethodPayments();

        String nombreLower = nombre.toLowerCase().trim();

        return (int) allMethods.stream()
                .filter(method ->
                        (nombreLower.isEmpty() || method.getNameMethod().toLowerCase().contains(nombreLower)) &&
                                (estado == null || method.getStatus() == estado)
                )
                .count();
    }
}
