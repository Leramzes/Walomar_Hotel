package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.Model.Client;
import development.team.hoteltransylvania.Model.ConsumeProduct;
import development.team.hoteltransylvania.Model.Product;
import development.team.hoteltransylvania.Model.Voucher;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class GestionVentas {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionClient.class);

    /*public static boolean registerVentaDirecta(int reservaid, int roomid, ConsumeProduct productConsumer) {
        String selectStockSql = "SELECT cantidad FROM productos WHERE id = ?";
        String insertSql = "INSERT INTO venta_directa (id_comprobante, producto_id, empleado_id, cantidad, precio_unit, total) " +
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
    }*/
    /*public static boolean registerVentaDirecta(int reservaid, int empleadoId, ConsumeProduct productConsumer, Voucher voucherConsumer) {
        String selectStockSql = "SELECT cantidad FROM productos WHERE id = ?";
        String insertComprobanteSql = "INSERT INTO comprobantes (reserva_id, tipo_comprobante_id, metodo_pago_id, total, subtotal_productos, " +
                "subtotal_servicios, subtotal_penalidad, fecha_emision) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        String insertVentaSql = "INSERT INTO venta_directa (id_comprobante, producto_id, empleado_id, cantidad, precio_unit, total) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String updateStockSql = "UPDATE productos SET cantidad = cantidad - ? WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection()) {
            cnn.setAutoCommit(false);

            // Verificar stock
            try (PreparedStatement psSelect = cnn.prepareStatement(selectStockSql)) {
                psSelect.setInt(1, productConsumer.getProduct().getId());
                ResultSet rs = psSelect.executeQuery();

                if (!rs.next()) {
                    System.out.println("Producto no encontrado.");
                    return false;
                }

                int stockDisponible = rs.getInt("cantidad");
                if (productConsumer.getQuantity() > stockDisponible) {
                    System.out.println("Stock insuficiente para el producto ID: " + productConsumer.getProduct().getId());
                    return false;
                }
            }

            // Insertar comprobante y obtener ID generado
            int comprobanteId = -1;
            try (PreparedStatement psComprobante = cnn.prepareStatement(insertComprobanteSql)) {
                psComprobante.setInt(1, reservaid);
                psComprobante.setInt(2, voucherConsumer.getTypeVoucher().getId());
                psComprobante.setInt(3, voucherConsumer.getPaymentMethod().getId());
                psComprobante.setDouble(4, voucherConsumer.getTotalAmount());
                psComprobante.setDouble(5, voucherConsumer.getSubtotalProducts());
                psComprobante.setDouble(6, voucherConsumer.getSubtotalServices());
                psComprobante.setDouble(7, voucherConsumer.getSubtotalPenalidad());
                psComprobante.setTimestamp(8, new Timestamp(System.currentTimeMillis()));

                ResultSet rs = psComprobante.executeQuery();
                if (rs.next()) {
                    comprobanteId = rs.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID del comprobante.");
                }
            }

            // Insertar venta directa
            try (PreparedStatement psInsertVenta = cnn.prepareStatement(insertVentaSql)) {
                psInsertVenta.setInt(1, comprobanteId);
                psInsertVenta.setInt(2, productConsumer.getProduct().getId());
                psInsertVenta.setInt(3, empleadoId);
                psInsertVenta.setInt(4, productConsumer.getQuantity());
                psInsertVenta.setDouble(5, productConsumer.getPriceUnit());
                psInsertVenta.setDouble(6, productConsumer.getPriceTotal());
                psInsertVenta.executeUpdate();
            }

            // Actualizar stock
            try (PreparedStatement psUpdate = cnn.prepareStatement(updateStockSql)) {
                psUpdate.setInt(1, productConsumer.getQuantity());
                psUpdate.setInt(2, productConsumer.getProduct().getId());
                psUpdate.executeUpdate();
            }

            cnn.commit();
            LOGGER.info("Venta directa registrada exitosamente con comprobante ID " + comprobanteId);
            result = true;

        } catch (SQLException e) {
            LOGGER.warning("Error en la venta directa: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }*/
    public static boolean registrarLineaVentaDirecta(int comprobanteId, int empleadoId, ConsumeProduct cp) {
        String selectStockSql = "SELECT cantidad FROM productos WHERE id = ?";
        String insertVentaSql = "INSERT INTO venta_directa (id_comprobante, producto_id, empleado_id, cantidad, precio_unit, total) VALUES (?, ?, ?, ?, ?, ?)";
        String updateStockSql = "UPDATE productos SET cantidad = cantidad - ? WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection()) {
            cnn.setAutoCommit(false);

            // Validar stock
            try (PreparedStatement psSelect = cnn.prepareStatement(selectStockSql)) {
                psSelect.setInt(1, cp.getProduct().getId());
                ResultSet rs = psSelect.executeQuery();

                if (!rs.next()) {
                    System.out.println("Producto no encontrado.");
                    return false;
                }

                int stockDisponible = rs.getInt("cantidad");
                if (cp.getQuantity() > stockDisponible) {
                    System.out.println("Stock insuficiente para el producto ID: " + cp.getProduct().getId());
                    return false;
                }
            }

            // Insertar venta
            try (PreparedStatement psInsertVenta = cnn.prepareStatement(insertVentaSql)) {
                psInsertVenta.setInt(1, comprobanteId);
                psInsertVenta.setInt(2, cp.getProduct().getId());
                psInsertVenta.setInt(3, empleadoId);
                psInsertVenta.setInt(4, cp.getQuantity());
                psInsertVenta.setDouble(5, cp.getPriceUnit());
                psInsertVenta.setDouble(6, cp.getPriceTotal());
                psInsertVenta.executeUpdate();
            }

            // Actualizar stock
            try (PreparedStatement psUpdate = cnn.prepareStatement(updateStockSql)) {
                psUpdate.setInt(1, cp.getQuantity());
                psUpdate.setInt(2, cp.getProduct().getId());
                psUpdate.executeUpdate();
            }

            cnn.commit();
            result = true;

        } catch (SQLException e) {
            LOGGER.warning("Error registrando línea de venta directa: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
    public static int registrarComprobante(int reservaId, Voucher voucher) throws SQLException {
        String sql = "INSERT INTO comprobantes (reserva_id, tipo_comprobante_id, metodo_pago_id, total, subtotal_productos, subtotal_servicios, subtotal_penalidad, fecha_emision) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, reservaId);
            ps.setInt(2, voucher.getTypeVoucher().getId());
            ps.setInt(3, voucher.getPaymentMethod().getId());
            ps.setDouble(4, voucher.getTotalAmount());
            ps.setDouble(5, voucher.getSubtotalProducts());
            ps.setDouble(6, voucher.getSubtotalServices());
            ps.setDouble(7, voucher.getSubtotalPenalidad());
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("No se pudo registrar el comprobante.");
            }
        }
    }
}
