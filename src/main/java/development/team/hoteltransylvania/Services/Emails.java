package development.team.hoteltransylvania.Services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Properties;

public class Emails {
    private static final String REMITENTE = "walomarhotel@gmail.com";
    private static final String CLAVE = "diofzdewbozbemit"; // ¡No uses la clave normal!

    public enum TipoCorreo {
        CORREO_BOLETA,
        CORREO_FACTURA
    }

    public static void enviarCorreo(String destinatario, String asunto, String contenido, boolean esHtml) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Crear sesión autenticada
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMITENTE, CLAVE);
            }
        });

        try {
            // Crear mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(REMITENTE, "Walomar Hotel"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            // Contenido
            if (esHtml) {
                message.setContent(contenido, "text/html; charset=utf-8");
            } else {
                message.setText(contenido);
            }

            // Enviar mensaje
            Transport.send(message);
            System.out.println("Correo enviado exitosamente a " + destinatario);

        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void enviarCorreoConAdjunto(String destinatario, String asunto, String contenido, boolean esHtml, File archivoAdjunto) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMITENTE, CLAVE);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(REMITENTE, "Walomar Hotel"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            // Parte 1: cuerpo del mensaje
            MimeBodyPart cuerpo = new MimeBodyPart();
            if (esHtml) {
                cuerpo.setContent(contenido, "text/html; charset=utf-8");
            } else {
                cuerpo.setText(contenido);
            }

            // Parte 2: archivo adjunto
            MimeBodyPart adjunto = new MimeBodyPart();
            adjunto.attachFile(archivoAdjunto); // puede ser un .pdf

            // Unir ambas partes
            Multipart multiparte = new MimeMultipart();
            multiparte.addBodyPart(cuerpo);
            multiparte.addBodyPart(adjunto);

            message.setContent(multiparte);

            // Enviar correo
            Transport.send(message);
            System.out.println("Correo con adjunto enviado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al enviar correo con adjunto: " + e.getMessage());
            e.printStackTrace();
        }
    }
}