package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.DTO.AllInfoTableProdSalida;
import development.team.hoteltransylvania.DTO.AllInfoTableServSalida;
import development.team.hoteltransylvania.DTO.AllInfoVentasDirecta;
import development.team.hoteltransylvania.DTO.usersEmployeeDTO;
import development.team.hoteltransylvania.Model.*;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.stream.Collectors;

public class GestionVentas {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionVentas.class);

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
    public static int registrarComprobante(int reservaId, Voucher voucher) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static double getAmuntTotalVentaDirecta(){
        String sql = "SELECT SUM(total) FROM comprobantes WHERE reserva_id = 1000000000";
        double total = 0.0;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error retrieving get amount total venta_directa with ID " + e.getMessage());
        }

        return total;
    }
    public static double getMontoTotalVentaPorEmpleado(int empleadoId) {
        String sql = "SELECT SUM(total) FROM venta_directa vd " +
                "WHERE empleado_id = ?";
        double total = 0.0;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, empleadoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble(1);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error al obtener el monto total de ventas por empleado ID " + empleadoId + ": " + e.getMessage());
        }

        return total;
    }
    public static double getMontoTotalVentaPorEmpleadoYFecha(int empleadoId, Date fecha) {
        String sql = "SELECT SUM(vd.total)\n" +
                "FROM venta_directa vd\n" +
                "INNER JOIN comprobantes co on vd.id_comprobante = co.id\n" +
                "WHERE empleado_id = ? AND DATE(co.fecha_emision) = ?";
        double total = 0.0;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, empleadoId);
            ps.setDate(2, fecha); // ← Aquí aplicamos el filtro por fecha

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble(1);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error al obtener el monto total de ventas por empleado y fecha: " + e.getMessage());
        }

        return total;
    }
    public static double getMontoTotalVentaPorFecha(Date fecha) {
        String sql = "SELECT SUM(vd.total) " +
                "FROM venta_directa vd " +
                "INNER JOIN comprobantes co ON vd.id_comprobante = co.id " +
                "WHERE DATE(co.fecha_emision) = ?";
        double total = 0.0;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setDate(1, fecha);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble(1);
                if (rs.wasNull()) total = 0.0;
            }

        } catch (SQLException e) {
            LOGGER.severe("Error al obtener el monto total de ventas por fecha: " + e.getMessage());
        }

        return total;
    }
    public static boolean registrarVenta(int idReserva, int idRoom, ConsumeProduct consumeProduct) {
        String selectStockSql = "SELECT cantidad FROM productos WHERE id = ?";
        String insertSql = "INSERT INTO consumo_productos " +
                "(reserva_id, habitacion_id, producto_id, cantidad, precio_unitario, total, estado_pago) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateStockSql = "UPDATE productos SET cantidad = cantidad - ? WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection()) {
            cnn.setAutoCommit(false); // Iniciar transacción

            // Validar stock
            try (PreparedStatement psSelect = cnn.prepareStatement(selectStockSql)) {
                psSelect.setInt(1, consumeProduct.getProduct().getId());
                ResultSet rs = psSelect.executeQuery();

                if (!rs.next()) {
                    System.out.println("Producto no encontrado.");
                    cnn.rollback();
                    return false;
                }

                int stockDisponible = rs.getInt("cantidad");
                if (consumeProduct.getQuantity() > stockDisponible) {
                    System.out.println("Stock insuficiente para el producto ID: " + consumeProduct.getProduct().getId());
                    cnn.rollback();
                    return false;
                }
            }

            // Registrar consumo del producto
            try (PreparedStatement psInsert = cnn.prepareStatement(insertSql)) {
                psInsert.setInt(1, idReserva);
                psInsert.setInt(2, idRoom);
                psInsert.setInt(3, consumeProduct.getProduct().getId());
                psInsert.setDouble(4, consumeProduct.getQuantity());
                psInsert.setDouble(5, consumeProduct.getPriceUnit());
                psInsert.setDouble(6, consumeProduct.getPriceTotal());
                psInsert.setString(7, consumeProduct.getEstado_pago());
                psInsert.executeUpdate();
            }

            // Actualizar stock
            try (PreparedStatement psUpdate = cnn.prepareStatement(updateStockSql)) {
                psUpdate.setDouble(1, consumeProduct.getQuantity());
                psUpdate.setInt(2, consumeProduct.getProduct().getId());
                psUpdate.executeUpdate();
            }

            cnn.commit(); // Confirmar transacción
            result = true;
        } catch (SQLException e) {
            LOGGER.warning("Error registrando venta y actualizando stock: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
    public static boolean registrarVentaService(int idReserva, int idRoom, ConsumeService consumeService) {
        String insertSql = "INSERT INTO consumo_servicios " +
                "(reserva_id, habitacion_id, servicio_id, total, estado_pago) " +
                "VALUES (?, ?, ?, ?, ?)";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement psInsert = cnn.prepareStatement(insertSql)) {

            psInsert.setInt(1, idReserva);
            psInsert.setInt(2, idRoom);
            psInsert.setInt(3, consumeService.getService().getId());
            psInsert.setDouble(4, consumeService.getTotalPrice());
            psInsert.setString(5, consumeService.getEstado_pago());

            int rows = psInsert.executeUpdate();
            result = rows > 0;

            if (result) {
                LOGGER.info("Servicio registrado correctamente");
            }
        } catch (SQLException e) {
            LOGGER.warning("Error al registrar servicio: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
    public static List<AllInfoTableProdSalida> obtenerVentasProdPorReserva(int idReserva) {
        List<AllInfoTableProdSalida> ventas = new ArrayList<>();

        String sql = "SELECT cp.id, cp.reserva_id, cp.habitacion_id, cp.producto_id, p.nombre, cp.cantidad, cp.precio_unitario, cp.total, cp.estado_pago\n" +
                "FROM consumo_productos cp\n" +
                "inner join productos p on p.id = cp.producto_id\n" +
                "WHERE cp.reserva_id = ?";

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AllInfoTableProdSalida ats = new AllInfoTableProdSalida();

                ats.setId_consumo(rs.getInt("id"));
                ats.setId_reserva(rs.getInt("reserva_id"));
                ats.setId_habitacion(rs.getInt("habitacion_id"));
                ats.setId_producto(rs.getInt("producto_id"));
                ats.setNombreProducto(rs.getString("nombre"));
                ats.setCantidad(rs.getInt("cantidad"));
                ats.setPrecioUnitProducto(rs.getDouble("precio_unitario"));
                ats.setTotal(rs.getDouble("total"));
                ats.setEstadoProducto(rs.getString("estado_pago"));

                ventas.add(ats);
            }

        } catch (SQLException e) {
            LOGGER.warning("Error obteniendo ventas de productos: " + e.getMessage());
            e.printStackTrace();
        }

        return ventas;
    }
    public static List<AllInfoTableServSalida> obtenerVentasServPorReserva(int idReserva) {
        List<AllInfoTableServSalida> ventas = new ArrayList<>();

        String sql = "SELECT cs.id, cs.reserva_id, cs.habitacion_id, s.nombre, cs.total, cs.estado_pago\n" +
                "FROM consumo_servicios cs\n" +
                "inner join servicios s on s.id = cs.servicio_id\n" +
                "WHERE cs.reserva_id = ?";

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AllInfoTableServSalida ats = new AllInfoTableServSalida();

                ats.setId_consumo(rs.getInt("id"));
                ats.setId_reserva(rs.getInt("reserva_id"));
                ats.setId_habitacion(rs.getInt("habitacion_id"));
                ats.setNombreServicio(rs.getString("nombre"));
                ats.setTotal(rs.getDouble("total"));
                ats.setEstadoServicio(rs.getString("estado_pago"));

                ventas.add(ats);
            }

        } catch (SQLException e) {
            LOGGER.warning("Error obteniendo ventas de productos: " + e.getMessage());
            e.printStackTrace();
        }

        return ventas;
    }
    public static boolean actualizarProductosPagadosPorReserva(int idReserva) {
        String sql = "UPDATE consumo_productos SET estado_pago = 'Pagado al salir' WHERE reserva_id = ? and estado_pago = 'Pendiente'";
        boolean success = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, idReserva);
            int rows = ps.executeUpdate();
            success = rows > 0;

        } catch (SQLException e) {
            System.out.println("Error actualizando estado de productos: " + e.getMessage());
            e.printStackTrace();
        }

        return success;
    }
    public static boolean actualizarServiciosPagadosPorReserva(int idReserva) {
        String sql = "UPDATE consumo_servicios SET estado_pago = 'Pagado al salir' WHERE reserva_id = ? and estado_pago = 'Pendiente'";
        boolean success = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, idReserva);
            int rows = ps.executeUpdate();
            success = rows > 0;

        } catch (SQLException e) {
            System.out.println("Error actualizando estado de productos: " + e.getMessage());
            e.printStackTrace();
        }

        return success;
    }
    public static List<AllInfoVentasDirecta> getAllVentasDirecta() {
        List<AllInfoVentasDirecta> ventas = new ArrayList<>();

        String sql = "select vd.id as idVentaDirecta, c.id as idComprobante, e.id as idEmpleado, p.id as idProducto, p.nombre, vd.cantidad, vd.precio_unit, vd.total,\n" +
                "       c.fecha_emision, e.nombre as empleadoNombre\n" +
                "from venta_directa vd\n" +
                "inner join comprobantes c on c.id=vd.id_comprobante\n" +
                "inner join productos p on p.id=vd.producto_id\n" +
                "inner join empleados e on e.id=vd.empleado_id\n" +
                "ORDER BY c.fecha_emision DESC";

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AllInfoVentasDirecta aivd = new AllInfoVentasDirecta();

                aivd.setId_venta_directa(rs.getInt("idVentaDirecta"));
                aivd.setId_comprobante(rs.getInt("idComprobante"));
                aivd.setId_empleado(rs.getInt("idEmpleado"));
                aivd.setId_producto(rs.getInt("idProducto"));
                aivd.setProducto(rs.getString("nombre"));
                aivd.setCantidad(rs.getInt("cantidad"));
                aivd.setPrecio_unitario(rs.getDouble("precio_unit"));
                aivd.setPrecio_total(rs.getDouble("total"));
                aivd.setFecha_hora(rs.getTimestamp("fecha_emision"));
                aivd.setEmpleado(rs.getString("empleadoNombre"));

                ventas.add(aivd);
            }

        } catch (SQLException e) {
            LOGGER.warning("Error obteniendo ventas de productos: " + e.getMessage());
            e.printStackTrace();
        }

        return ventas;
    }
    public static List<AllInfoVentasDirecta> filterVentaDirecta(int empleadoId, Date fechaFiltrada, int page, int size) {
        List<AllInfoVentasDirecta> todasVentas = getAllVentasDirecta();

        List<AllInfoVentasDirecta> filtradas = todasVentas.stream()
                .filter(venta -> {
                    boolean porEmpleado = empleadoId == -1 || venta.getId_empleado() == empleadoId;

                    boolean porFecha = true;
                    if (fechaFiltrada != null) {
                        LocalDate fechaVenta = venta.getFecha_hora().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        LocalDate fechaFiltro = fechaFiltrada.toLocalDate();
                        porFecha = fechaVenta.equals(fechaFiltro);
                    }

                    return porEmpleado && porFecha;
                })
                .collect(Collectors.toList());

        // Paginación
        int from = Math.max((page - 1) * size, 0);
        int to = Math.min(from + size, filtradas.size());

        return filtradas.subList(from, to);
    }
    public static int countFilteredVentaDirecta(int empleadoId, Date fechaFiltrada) {
        List<AllInfoVentasDirecta> todasVentas = getAllVentasDirecta();

        return (int) todasVentas.stream()
                .filter(venta -> {
                    boolean porEmpleado = empleadoId == -1 || venta.getId_empleado() == empleadoId;

                    boolean porFecha = true;
                    if (fechaFiltrada != null) {
                        LocalDate fechaVenta = venta.getFecha_hora().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        LocalDate fechaFiltro = fechaFiltrada.toLocalDate();
                        porFecha = fechaVenta.equals(fechaFiltro);
                    }

                    return porEmpleado && porFecha;
                })
                .count();
    }
}
