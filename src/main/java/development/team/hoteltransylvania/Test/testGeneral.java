package development.team.hoteltransylvania.Test;

import development.team.hoteltransylvania.Business.*;
import development.team.hoteltransylvania.Model.Product;
import development.team.hoteltransylvania.Model.Room;
import development.team.hoteltransylvania.Model.User;
import development.team.hoteltransylvania.Util.LoggerConfifg;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class testGeneral {
    public static void main(String[] args) {

    }
    private static Timestamp parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.isEmpty()) return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(fechaStr, formatter);
        return Timestamp.valueOf(localDateTime);
    }
}
