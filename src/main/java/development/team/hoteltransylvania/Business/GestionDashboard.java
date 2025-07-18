package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.DTO.TopHabitacion;
import development.team.hoteltransylvania.DTO.TopVendedor;
import development.team.hoteltransylvania.DTO.usersEmployeeDTO;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class GestionDashboard {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionDashboard.class);

    public static TopVendedor getTopVendedor() {
        String sql = "SELECT\n" +
                "    e.id AS empleado_id,\n" +
                "    e.nombre AS nombre_empleado,\n" +
                "    COUNT(DISTINCT r.id) AS cantidad_ventas,\n" +
                "    COALESCE(SUM(cp.total), 0) + COALESCE(SUM(cs.total), 0) AS total_ventas\n" +
                "FROM reservas r\n" +
                "         INNER JOIN empleados e ON r.empleado_id = e.id\n" +
                "         LEFT JOIN consumo_productos cp ON cp.reserva_id = r.id\n" +
                "         LEFT JOIN consumo_servicios cs ON cs.reserva_id = r.id\n" +
                "GROUP BY e.id, e.nombre\n" +
                "ORDER BY total_ventas DESC\n" +
                "LIMIT 1;";

        TopVendedor vendedor = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {


            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vendedor = new TopVendedor();
                    vendedor.setNombre_empleado(rs.getString("nombre_empleado"));
                    vendedor.setTotal_ventas(rs.getInt("cantidad_ventas"));
                    vendedor.setTotal_ventas(rs.getDouble("total_ventas"));
                    LOGGER.info("Top vendedor obtenido correctamente: " + vendedor.getNombre_empleado());
                }
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving employee by ID: " + e.getMessage());
        }

        return vendedor;// Retorna null si no se encuentra el empleado
    }

    public static TopHabitacion getTopHabitacion() {
        String sql = "SELECT h.numero AS numero_habitacion, SUM(r.pago_total_reserva) AS total_ingresos\n" +
                "        FROM reservas r\n" +
                "        INNER JOIN habitaciones h ON r.habitacion_id = h.id\n" +
                "        GROUP BY h.numero\n" +
                "        ORDER BY total_ingresos DESC\n" +
                "        LIMIT 1";

        TopHabitacion habitacion = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {


            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    habitacion = new TopHabitacion();
                    habitacion.setNumero_habitacion(rs.getString("numero_habitacion"));
                    habitacion.setTotal_ingresos(rs.getDouble("total_ingresos"));
                    LOGGER.info("Top habitación obtenida correctamente: " + habitacion.getNumero_habitacion());
                }
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving employee by ID: " + e.getMessage());
        }

        return habitacion;// Retorna null si no se encuentra el empleado
    }


}
