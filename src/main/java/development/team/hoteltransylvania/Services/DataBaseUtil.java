package development.team.hoteltransylvania.Services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import development.team.hoteltransylvania.Exceptions.ConectionNotBDException;

import javax.sql.DataSource;

public class DataBaseUtil {
    // Variables de configuración
    private static final String DB_HOST = "tramway.proxy.rlwy.net";
    private static final String DB_PORT = "54575";
    private static final String DB_NAME = "railway"; // Ajusta si tu BD tiene otro nombre
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "pGuQqZmxvhmQfroUPxhfzdENsnEhEEUc";
    private static final String DB_DRIVER = "org.postgresql.Driver";

    // Construimos la URL de PostgreSQL
    private static final String DB_URL = String.format(
            "jdbc:postgresql://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME
    );

    private static final int MAX_POOL_SIZE = 10;
    private static final int MIN_IDLE_CONNECTIONS = 2;
    private static final long IDLE_TIMEOUT = 120_000; // 2 minutos
    private static final long CONNECTION_TIMEOUT = 30_000; // 30 segundos
    private static final long VALIDATION_TIMEOUT = 10_000; // 10 segundos
    private static final long MAX_LIFETIME = 1_800_000; // 30 minutos
    private static final String TEST_QUERY = "SELECT 1";

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        try {
            // Configuración del pool de conexiones
            config.setJdbcUrl(DB_URL);
            config.setUsername(DB_USERNAME);
            config.setPassword(DB_PASSWORD);
            config.setDriverClassName(DB_DRIVER);

            // Configuración del tamaño del pool
            config.setMaximumPoolSize(MAX_POOL_SIZE);
            config.setMinimumIdle(MIN_IDLE_CONNECTIONS);

            // Configuración de tiempos
            config.setIdleTimeout(IDLE_TIMEOUT);
            config.setConnectionTimeout(CONNECTION_TIMEOUT);
            config.setValidationTimeout(VALIDATION_TIMEOUT);
            config.setMaxLifetime(MAX_LIFETIME);

            // Prueba de conectividad
            config.setConnectionTestQuery(TEST_QUERY);

            // Inicializar DataSource
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new ConectionNotBDException(
                    "Error al inicializar la conexión a la base de datos: " + e.getMessage()
            );
        }
    }

    // Método para obtener el DataSource
    public static DataSource getDataSource() {
        return dataSource;
    }

    // Método para cerrar el DataSource al finalizar la aplicación
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}