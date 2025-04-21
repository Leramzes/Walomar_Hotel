package development.team.hoteltransylvania.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet(name = "apireniec", urlPatterns = {"/apireniec"})
public class apiConsultaReniec extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tipo_documento = req.getParameter("typeDoc");
        String numDoc = req.getParameter("numDoc");
        try {
            String url="";
            if(tipo_documento.equalsIgnoreCase("DNI")){
                url= "https://api.apis.net.pe/v1/dni?numero=" + numDoc;
            }else if(tipo_documento.equalsIgnoreCase("RUC")){
                url= "https://api.apis.net.pe/v1/ruc?numero=" + numDoc;
            }


            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(),"UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                PrintWriter out = resp.getWriter();
                out.println(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}