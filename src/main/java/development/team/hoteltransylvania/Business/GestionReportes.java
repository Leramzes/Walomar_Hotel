package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.DTO.*;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GestionReportes {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionVentas.class);

    /*public static List<AllInfoReporteVenta> getAllReporteVentaServicioHabitacion(){
        List<AllInfoReporteVenta> reporteVentas = new ArrayList<>();

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
    }*/
    /*public static List<AllInfoReporteVenta> getAllReporteVentaServicioHabitacionVD(){
        List<AllInfoReporteVenta> reporteVentas = new ArrayList<>();

        String sql = "select vd.id as id_Venta_Directa, p.id as id_producto, p.nombre as nombre_articulo,vd.cantidad,vd.precio_unit,vd.total,c.fecha_emision,\n" +
                "       e.nombre as nombre_empleado,e.id as id_empleado\n" +
                "from venta_directa vd\n" +
                "inner join productos p on p.id = vd.producto_id\n" +
                "inner join comprobantes c on c.id=vd.id_comprobante\n" +
                "inner join empleados e on e.id=vd.empleado_id";

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AllInfoReporteVenta reporte = new AllInfoReporteVenta();
                reporte.setId_venta(rs.getInt("id_Venta_Directa"));
                reporte.setId_habitacion(rs.getInt("id_producto"));
                reporte.setNombreArticulo(rs.getString("nombre_articulo"));
                reporte.setCantArticulo(rs.getInt("cantidad"));
                reporte.setPrecioUnitArticulo(rs.getDouble("precio_unit"));
                reporte.setPrecioTotalArticulo(rs.getDouble("total"));
                reporte.setFecha_hora_compra(rs.getTimestamp("fecha_emision"));
                reporte.setNombreEmpleado(rs.getString("nombre_empleado"));
                reporte.setIdEmpleado(rs.getInt("id_empleado"));

                reporteVentas.add(ats);
            }

        } catch (SQLException e) {
            LOGGER.warning("Error obteniendo ventas de productos: " + e.getMessage());
            e.printStackTrace();
        }

        return reporteVentas;
    }*/
    public static List<AllInfoReporteVenta> getAllReporteVentaProductoHabitacion(){
        List<AllInfoReporteVenta> reporteVentas = new ArrayList<>();

        String sql = "SELECT\n" +
                "    cp.id AS id_consumo,\n" +
                "    h.id AS id_habitacion,\n" +
                "    h.numero AS numero_habitacion,\n" +
                "    p.id AS id_producto,\n" +
                "    p.nombre AS nombre_articulo,\n" +
                "    cp.cantidad,\n" +
                "    cp.precio_unitario,\n" +
                "    cp.total,\n" +
                "    c.fecha_emision,\n" +
                "    e.nombre AS nombre_empleado,\n" +
                "    e.id AS id_empleado\n" +
                "FROM consumo_productos cp\n" +
                "         INNER JOIN productos p ON p.id = cp.producto_id\n" +
                "         INNER JOIN reservas r ON r.id = cp.reserva_id\n" +
                "         INNER JOIN empleados e ON e.id = r.empleado_id\n" +
                "\n" +
                "-- SUBCONSULTA para traer solo el comprobante más reciente por reserva\n" +
                "         INNER JOIN (\n" +
                "    SELECT c1.*\n" +
                "    FROM comprobantes c1\n" +
                "             INNER JOIN (\n" +
                "        SELECT reserva_id, MAX(fecha_emision) AS fecha_max\n" +
                "        FROM comprobantes\n" +
                "        GROUP BY reserva_id\n" +
                "    ) c2 ON c1.reserva_id = c2.reserva_id AND c1.fecha_emision = c2.fecha_max\n" +
                ") c ON c.reserva_id = r.id\n" +
                "\n" +
                "         INNER JOIN tipo_comprobante t ON t.id = c.tipo_comprobante_id\n" +
                "         INNER JOIN metodo_pago m ON m.id = c.metodo_pago_id\n" +
                "         INNER JOIN detalle_habitacion dh ON dh.reserva_id = r.id\n" +
                "         INNER JOIN habitaciones h ON h.id = dh.habitacion_id;";

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AllInfoReporteVenta reporte = new AllInfoReporteVenta();
                reporte.setId_venta(rs.getInt("id_consumo"));
                reporte.setId_habitacion(rs.getInt("id_habitacion"));
                reporte.setNumeroHabitacion(rs.getString("numero_habitacion"));
                reporte.setNombreArticulo(rs.getString("nombre_articulo"));
                reporte.setCantArticulo(rs.getInt("cantidad"));
                reporte.setPrecioUnitArticulo(rs.getDouble("precio_unitario"));
                reporte.setPrecioTotalArticulo(rs.getDouble("total"));
                reporte.setFecha_hora_compra(rs.getTimestamp("fecha_emision"));
                reporte.setNombreEmpleado(rs.getString("nombre_empleado"));
                reporte.setIdEmpleado(rs.getInt("id_empleado"));

                reporteVentas.add(reporte);
            }

        } catch (SQLException e) {
            LOGGER.warning("Error obteniendo ventas de productos: " + e.getMessage());
            e.printStackTrace();
        }

        return reporteVentas;
    }
    public static List<AllInfoReporteVenta> getAllReporteVentaServicioHabitacion(){
        List<AllInfoReporteVenta> reporteVentas = new ArrayList<>();

        String sql = "SELECT \n" +
                "    cs.id AS id_consumo,\n" +
                "    h.id AS id_habitacion,\n" +
                "    h.numero AS numero_habitacion,\n" +
                "    s.id AS id_servicio,\n" +
                "    s.nombre AS nombre_articulo,\n" +
                "    cs.total,\n" +
                "    c.fecha_emision,\n" +
                "    e.nombre AS nombre_empleado,\n" +
                "    e.id AS id_empleado\n" +
                "FROM consumo_servicios cs\n" +
                "INNER JOIN servicios s ON s.id = cs.servicio_id\n" +
                "INNER JOIN reservas r ON r.id = cs.reserva_id\n" +
                "INNER JOIN empleados e ON e.id = r.empleado_id\n" +
                "\n" +
                "-- Subconsulta para seleccionar solo el comprobante más reciente por reserva\n" +
                "INNER JOIN (\n" +
                "    SELECT c1.*\n" +
                "    FROM comprobantes c1\n" +
                "    INNER JOIN (\n" +
                "        SELECT reserva_id, MAX(fecha_emision) AS fecha_max\n" +
                "        FROM comprobantes\n" +
                "        GROUP BY reserva_id\n" +
                "    ) c2 ON c1.reserva_id = c2.reserva_id AND c1.fecha_emision = c2.fecha_max\n" +
                ") c ON c.reserva_id = r.id\n" +
                "\n" +
                "INNER JOIN tipo_comprobante t ON t.id = c.tipo_comprobante_id\n" +
                "INNER JOIN metodo_pago m ON m.id = c.metodo_pago_id\n" +
                "INNER JOIN detalle_habitacion dh ON dh.reserva_id = r.id\n" +
                "INNER JOIN habitaciones h ON h.id = dh.habitacion_id;";

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AllInfoReporteVenta reporte = new AllInfoReporteVenta();
                reporte.setId_venta(rs.getInt("id_consumo"));
                reporte.setId_habitacion(rs.getInt("id_habitacion"));
                reporte.setNumeroHabitacion(rs.getString("numero_habitacion"));
                reporte.setNombreArticulo(rs.getString("nombre_articulo"));
                reporte.setPrecioTotalArticulo(rs.getDouble("total"));
                reporte.setFecha_hora_compra(rs.getTimestamp("fecha_emision"));
                reporte.setNombreEmpleado(rs.getString("nombre_empleado"));
                reporte.setIdEmpleado(rs.getInt("id_empleado"));

                reporteVentas.add(reporte);
            }

        } catch (SQLException e) {
            LOGGER.warning("Error obteniendo ventas de productos: " + e.getMessage());
            e.printStackTrace();
        }

        return reporteVentas;
    }
    public static List<AllInfoReporteVenta> filterReportesHabitacion(int empleadoId, Date fechaFiltrada, String articulo) {
        List<AllInfoReporteVenta> combinados = new ArrayList<>();

        // 1. Fuente: Venta Directa
        for (AllInfoVentasDirecta vd : GestionVentas.getAllVentasDirecta()) {
            AllInfoReporteVenta r = new AllInfoReporteVenta();

            r.setId_venta(vd.getId_venta_directa());
            r.setNumeroHabitacion("--");
            r.setNombreArticulo(vd.getProducto());
            r.setCantArticulo(vd.getCantidad());
            r.setPrecioUnitArticulo(vd.getPrecio_unitario());
            r.setPrecioTotalArticulo(vd.getPrecio_total());
            r.setFecha_hora_compra(vd.getFecha_hora());
            r.setNombreEmpleado(vd.getEmpleado());
            r.setIdEmpleado(vd.getId_empleado());
            r.setTipoVenta("Venta Directa");

            combinados.add(r);
        }

        // 2. Fuente: Productos en habitación
        for (AllInfoReporteVenta ph : GestionReportes.getAllReporteVentaProductoHabitacion()) {
            AllInfoReporteVenta copia = new AllInfoReporteVenta();
            copia.setId_venta(ph.getId_venta());
            copia.setNumeroHabitacion(ph.getNumeroHabitacion());
            copia.setNombreArticulo(ph.getNombreArticulo());
            copia.setCantArticulo(ph.getCantArticulo());
            copia.setPrecioUnitArticulo(ph.getPrecioUnitArticulo());
            copia.setPrecioTotalArticulo(ph.getPrecioTotalArticulo());
            copia.setFecha_hora_compra(ph.getFecha_hora_compra());
            copia.setNombreEmpleado(ph.getNombreEmpleado());
            copia.setIdEmpleado(ph.getIdEmpleado());
            copia.setTipoVenta("Recepción");
            combinados.add(copia);
        }

        // 3. Fuente: Servicios en habitación
        for (AllInfoReporteVenta sh : GestionReportes.getAllReporteVentaServicioHabitacion()) {
            AllInfoReporteVenta copia = new AllInfoReporteVenta();
            copia.setId_venta(sh.getId_venta());
            copia.setNumeroHabitacion(sh.getNumeroHabitacion() + " - 24H");
            copia.setNombreArticulo(sh.getNombreArticulo());
            copia.setCantArticulo(0); // sin cantidad
            copia.setPrecioUnitArticulo(0.0); // sin PU
            copia.setPrecioTotalArticulo(sh.getPrecioTotalArticulo());
            copia.setFecha_hora_compra(sh.getFecha_hora_compra());
            copia.setNombreEmpleado(sh.getNombreEmpleado());
            copia.setIdEmpleado(sh.getIdEmpleado());
            copia.setTipoVenta("Recepción");
            combinados.add(copia);
        }

        // 4. Aplicar filtros
        List<AllInfoReporteVenta> filtrados = combinados.stream()
                .filter(rep -> {
                    boolean porEmpleado = empleadoId == -1 || rep.getIdEmpleado() == empleadoId;

                    boolean porFecha = true;
                    if (fechaFiltrada != null) {
                        LocalDate fechaCompra = rep.getFecha_hora_compra().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        porFecha = fechaCompra.equals(fechaFiltrada.toLocalDate());
                    }

                    boolean porProducto = true;
                    if (articulo != null && !articulo.trim().isEmpty()) {
                        porProducto = rep.getNombreArticulo() != null &&
                                rep.getNombreArticulo().toLowerCase().contains(articulo.toLowerCase());
                    }

                    return porEmpleado && porFecha && porProducto;
                })
                .collect(Collectors.toList());

        return filtrados;
    }
    public static List<AllInfoReporteVenta> filterReportesHabitacion(int empleadoId, Integer anio, Integer mes, String articulo) {
        List<AllInfoReporteVenta> combinados = new ArrayList<>();

        // 1. Fuente: Venta Directa
        for (AllInfoVentasDirecta vd : GestionVentas.getAllVentasDirecta()) {
            AllInfoReporteVenta r = new AllInfoReporteVenta();

            r.setId_venta(vd.getId_venta_directa());
            r.setNumeroHabitacion("--");
            r.setNombreArticulo(vd.getProducto());
            r.setCantArticulo(vd.getCantidad());
            r.setPrecioUnitArticulo(vd.getPrecio_unitario());
            r.setPrecioTotalArticulo(vd.getPrecio_total());
            r.setFecha_hora_compra(vd.getFecha_hora());
            r.setNombreEmpleado(vd.getEmpleado());
            r.setIdEmpleado(vd.getId_empleado());
            r.setTipoVenta("Venta Directa");

            combinados.add(r);
        }

        // 2. Productos en habitación
        for (AllInfoReporteVenta ph : GestionReportes.getAllReporteVentaProductoHabitacion()) {
            AllInfoReporteVenta copia = new AllInfoReporteVenta();
            copia.setId_venta(ph.getId_venta());
            copia.setNumeroHabitacion(ph.getNumeroHabitacion());
            copia.setNombreArticulo(ph.getNombreArticulo());
            copia.setCantArticulo(ph.getCantArticulo());
            copia.setPrecioUnitArticulo(ph.getPrecioUnitArticulo());
            copia.setPrecioTotalArticulo(ph.getPrecioTotalArticulo());
            copia.setFecha_hora_compra(ph.getFecha_hora_compra());
            copia.setNombreEmpleado(ph.getNombreEmpleado());
            copia.setIdEmpleado(ph.getIdEmpleado());
            copia.setTipoVenta("Recepción");
            combinados.add(copia);
        }

        // 3. Servicios en habitación
        for (AllInfoReporteVenta sh : GestionReportes.getAllReporteVentaServicioHabitacion()) {
            AllInfoReporteVenta copia = new AllInfoReporteVenta();
            copia.setId_venta(sh.getId_venta());
            copia.setNumeroHabitacion(sh.getNumeroHabitacion() + " - 24H");
            copia.setNombreArticulo(sh.getNombreArticulo());
            copia.setCantArticulo(0); // sin cantidad
            copia.setPrecioUnitArticulo(0.0); // sin PU
            copia.setPrecioTotalArticulo(sh.getPrecioTotalArticulo());
            copia.setFecha_hora_compra(sh.getFecha_hora_compra());
            copia.setNombreEmpleado(sh.getNombreEmpleado());
            copia.setIdEmpleado(sh.getIdEmpleado());
            copia.setTipoVenta("Recepción");
            combinados.add(copia);
        }

        // 4. Aplicar filtros
        List<AllInfoReporteVenta> filtrados = combinados.stream()
                .filter(rep -> {
                    boolean porEmpleado = empleadoId == -1 || rep.getIdEmpleado() == empleadoId;

                    boolean porMesAnio = true;
                    if (anio != null && mes != null) {
                        LocalDate fechaCompra = rep.getFecha_hora_compra().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        porMesAnio = (fechaCompra.getYear() == anio && fechaCompra.getMonthValue() == mes);
                    }

                    boolean porProducto = true;
                    if (articulo != null && !articulo.trim().isEmpty()) {
                        porProducto = rep.getNombreArticulo() != null &&
                                rep.getNombreArticulo().toLowerCase().contains(articulo.toLowerCase());
                    }

                    return porEmpleado && porMesAnio && porProducto;
                })
                .collect(Collectors.toList());

        return filtrados;
    }

    public static List<AllInfoReporteAlquiler> getReporteReservation() {
        String sql = "SELECT * FROM obtener_reporte_alquiler()";
        List<AllInfoReporteAlquiler> reservationList = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AllInfoReporteAlquiler alquiler = new AllInfoReporteAlquiler();
                alquiler.setIdReservation(rs.getInt("id_reserva"));
                alquiler.setIdClient(rs.getInt("id_cliente"));
                alquiler.setClientName(rs.getString("nombre_cliente"));
                alquiler.setClientApellidos(rs.getString("apellidos"));
                alquiler.setDocumentType(rs.getString("tipo_documento"));
                alquiler.setDocumentNumber(rs.getString("numero_documento"));
                alquiler.setEmail(rs.getString("email"));
                alquiler.setPhone(rs.getString("telefono"));
                alquiler.setEmpleadoId(rs.getInt("id_empleado"));
                alquiler.setIdRoom(rs.getInt("id_habitacion"));
                alquiler.setNumberRoom(rs.getString("numero_habitacion"));
                alquiler.setRoomType(rs.getString("tipo_habitacion"));
                alquiler.setCheckInDate(rs.getTimestamp("fecha_inicio"));
                alquiler.setCheckOutDate(rs.getTimestamp("fecha_fin"));
                alquiler.setFecha_ingreso(rs.getTimestamp("fecha_ingreso"));
                alquiler.setFecha_desalojo(rs.getTimestamp("fecha_desalojo"));
                alquiler.setCantDays(rs.getInt("cant_dias"));
                alquiler.setDsct(rs.getInt("descuento"));
                alquiler.setCobro_extra(rs.getDouble("cobro_extra"));
                alquiler.setAdelanto(rs.getDouble("adelanto"));
                alquiler.setPago_total_reserva(rs.getDouble("pago_total_reserva"));
                alquiler.setTotal_consumo_productos(rs.getDouble("total_consumo_productos"));
                alquiler.setTotal_consumo_servicios(rs.getDouble("total_consumo_servicios"));
                alquiler.setTotal_penalidad(rs.getDouble("total_penalidad"));
                alquiler.setReservationStatus(rs.getString("estado"));
                alquiler.setRoomTypeId(rs.getInt("tipo_id"));

                Timestamp fechaInicio = rs.getTimestamp("fecha_inicio");
                Timestamp fechaIngreso = rs.getTimestamp("fecha_ingreso");

                alquiler.setCheckInDate(fechaInicio);
                alquiler.setFecha_ingreso(fechaIngreso);

                // Determinar tipo de alquiler
                if (fechaInicio != null && fechaIngreso != null) {
                    LocalDate inicio = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate ingreso = fechaIngreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    if (inicio.equals(ingreso)) {
                        alquiler.setTipoAlquiler("Recepción");
                    } else {
                        alquiler.setTipoAlquiler("Reservación");
                    }
                } else {
                    alquiler.setTipoAlquiler("Cancelada *"); // Por si alguna de las fechas es nula
                }

                reservationList.add(alquiler);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving reservation report: " + e.getMessage());
        }

        return reservationList;
    }
    public static List<AllInfoReporteAlquiler> filterReportesReservation(int empleadoId, Date fechaFiltrada, String roomSearch,
                                                                         int page, int size) {
        List<AllInfoReporteAlquiler> allAlquileres = getReporteReservation();

        List<AllInfoReporteAlquiler> filtrados = allAlquileres.stream()
                .filter(alquiler -> {
                    boolean porEmpleado = empleadoId == -1 || alquiler.getEmpleadoId() == empleadoId;

                    boolean porFecha = true;
                    if (fechaFiltrada != null) {
                        LocalDate fechaVenta = alquiler.getCheckInDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        LocalDate fechaFiltro = fechaFiltrada.toLocalDate();
                        porFecha = fechaVenta.equals(fechaFiltro);
                    }

                    boolean porHabitacion = true;
                    if (roomSearch != null && !roomSearch.trim().isEmpty()) {
                        String searchLower = roomSearch.toLowerCase();
                        porHabitacion =
                                (alquiler.getNumberRoom() != null &&
                                        alquiler.getNumberRoom().toLowerCase().contains(searchLower)) ||

                                        (alquiler.getRoomType() != null &&
                                                alquiler.getRoomType().toLowerCase().contains(searchLower));
                    }

                    return porEmpleado && porFecha && porHabitacion;
                })
                .collect(Collectors.toList());

        // Paginación
        int from = Math.max((page - 1) * size, 0);
        int to = Math.min(from + size, filtrados.size());

        return filtrados.subList(from, to);
    }
    public static List<AllInfoReporteAlquiler> filterReportesReservation(int empleadoId, Integer anio, Integer mes,
                                                                         String roomSearch, int page, int size) {
        List<AllInfoReporteAlquiler> allAlquileres = getReporteReservation();

        List<AllInfoReporteAlquiler> filtrados = allAlquileres.stream()
                .filter(alquiler -> {
                    boolean porEmpleado = empleadoId == -1 || alquiler.getEmpleadoId() == empleadoId;

                    boolean porMesAnio = true;
                    if (anio != null && mes != null) {
                        LocalDate fechaVenta = alquiler.getCheckInDate().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        porMesAnio = (fechaVenta.getYear() == anio && fechaVenta.getMonthValue() == mes);
                    }

                    boolean porHabitacion = true;
                    if (roomSearch != null && !roomSearch.trim().isEmpty()) {
                        String searchLower = roomSearch.toLowerCase();
                        porHabitacion =
                                (alquiler.getNumberRoom() != null &&
                                        alquiler.getNumberRoom().toLowerCase().contains(searchLower)) ||

                                        (alquiler.getRoomType() != null &&
                                                alquiler.getRoomType().toLowerCase().contains(searchLower));
                    }

                    return porEmpleado && porMesAnio && porHabitacion;
                })
                .collect(Collectors.toList());

        // Paginación
        int from = Math.max((page - 1) * size, 0);
        int to = Math.min(from + size, filtrados.size());

        return filtrados.subList(from, to);
    }
}
