package development.team.hoteltransylvania.Test;

import development.team.hoteltransylvania.Business.*;
import development.team.hoteltransylvania.DTO.AllInfoReporteAlquiler;
import development.team.hoteltransylvania.DTO.TableReservationDTO;
import development.team.hoteltransylvania.Model.Product;
import development.team.hoteltransylvania.Model.Room;
import development.team.hoteltransylvania.Model.User;
import development.team.hoteltransylvania.Services.Emails;
import development.team.hoteltransylvania.Util.LoggerConfifg;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class testGeneral {
    public static void main(String[] args) {
        /*File archivo = new File("src/main/webapp/img/evidencia.jpg");

        Emails.enviarCorreoConAdjunto(
                "U21216255@utp.edu.pe",
                "DEJESE DE WEBADAS",
                "<p>Su equipo de trabajo esta esperando su avance. ¿Por qué se fue el Mall?" +
                        "Adjuntamos evidencia.</p>",
                true,
                archivo
        );*/
        List<AllInfoReporteAlquiler> alquileres = GestionReportes.getReporteReservation();
        alquileres.forEach(System.out::println);
    }

}
