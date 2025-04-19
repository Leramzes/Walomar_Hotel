package development.team.hoteltransylvania.Util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfifg {
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());

        if (logger.getHandlers().length == 0) {  // Evita agregar múltiples handlers
            try {
                String logsPath = new File("src/main/webapp/logs").getAbsolutePath();
                File logDir = new File(logsPath);

                if (!logDir.exists()) {
                    logDir.mkdirs();
                }

                FileHandler fileHandler = new FileHandler(logsPath + "/Logs.txt", true);
                fileHandler.setFormatter(new SimpleFormatter());
                logger.setUseParentHandlers(false);
                logger.addHandler(fileHandler);

            } catch (IOException e) {
                System.err.println("No se pudo configurar el archivo de logs: " + e.getMessage());
            }
        }
        return logger;
    }
}