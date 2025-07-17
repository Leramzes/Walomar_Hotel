package development.team.hoteltransylvania.Controller;

import development.team.hoteltransylvania.Services.Auth;
import development.team.hoteltransylvania.Util.ConfigUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.stream.Collectors;

import static javax.crypto.Cipher.SECRET_KEY;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private Auth authService = new Auth(); // Instanciamos la clase Auth

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String gRecaptchaResponse = req.getParameter("g-recaptcha-response");
        String secretKey = ConfigUtil.getProperty("recaptcha.secret_key");

        boolean captchaValidado = validarCaptcha(gRecaptchaResponse, secretKey);

        if (!captchaValidado) {
            req.setAttribute("errorCaptcha", true);
            req.setAttribute("siteKey", ConfigUtil.getProperty("recaptcha.site_key"));
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (authService.login(username, password, req)) {
            resp.sendRedirect("menu.jsp"); // Éxito: redirigir al dashboard
        } else {
            req.setAttribute("loginError", true);
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response); // Redirigir GET a POST para mayor seguridad
    }

    private boolean validarCaptcha(String responseToken, String secretKey) throws IOException {
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "secret=" + URLEncoder.encode(secretKey, "UTF-8") +
                "&response=" + URLEncoder.encode(responseToken, "UTF-8");

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(params);
        out.flush();
        out.close();

        InputStream is = con.getInputStream();
        String result = new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n"));

        JSONObject json = new JSONObject(result);
        return json.getBoolean("success");
    }
}

