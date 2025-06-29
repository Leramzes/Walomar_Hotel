package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.Model.Client;
import development.team.hoteltransylvania.Model.ConsumeProduct;
import development.team.hoteltransylvania.Model.Product;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.sql.ResultSet;

public class GestionVentas {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionClient.class);

    public static boolean registerVentaDirecta(int reservaid, int roomid, ConsumeProduct productConsumer) {
        String selectStockSql = "SELECT cantidad FROM productos WHERE id = ?";
        String insertSql = "INSERT INTO consumo_productos (reserva_id, habitacion_id, producto_id, cantidad, precio_unitario, total) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String updateStockSql = "UPDATE productos SET cantidad = cantidad - ? WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection()) {
            cnn.setAutoCommit(false); // transacción manual

            // 1. Verificar stock disponible
            try (PreparedStatement psSelect = cnn.prepareStatement(selectStockSql)) {
                psSelect.setInt(1, productConsumer.getProduct().getId());
                ResultSet rs = psSelect.executeQuery();

                if (rs.next()) {
                    int stockDisponible = rs.getInt("cantidad");

                    if (productConsumer.getQuantity() > stockDisponible) {
                        System.out.println("Stock insuficiente para el producto ID: " + productConsumer.getProduct().getId());
                        return false;
                    }

                    // 2. Insertar consumo
                    try (PreparedStatement psInsert = cnn.prepareStatement(insertSql)) {
                        psInsert.setInt(1, reservaid);
                        psInsert.setInt(2, roomid);
                        psInsert.setInt(3, productConsumer.getProduct().getId());
                        psInsert.setInt(4, productConsumer.getQuantity());
                        psInsert.setDouble(5, productConsumer.getPriceUnit());
                        psInsert.setDouble(6, productConsumer.getPriceTotal());

                        int rowsAffected = psInsert.executeUpdate();
                        if (rowsAffected > 0) {
                            // 3. Actualizar stock
                            try (PreparedStatement psUpdate = cnn.prepareStatement(updateStockSql)) {
                                psUpdate.setInt(1, productConsumer.getQuantity());
                                psUpdate.setInt(2, productConsumer.getProduct().getId());
                                psUpdate.executeUpdate();
                            }

                            cnn.commit(); // todo correcto
                            LOGGER.info("Product Consumer " + productConsumer.getProduct().getId() + " registered successfully");
                            result = true;
                        }
                    }
                } else {
                    System.out.println("Producto no encontrado.");
                }
            }

        } catch (SQLException e) {
            LOGGER.warning("Error en la venta directa: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

}
