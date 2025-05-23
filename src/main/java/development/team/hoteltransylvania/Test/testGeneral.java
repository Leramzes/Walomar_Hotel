package development.team.hoteltransylvania.Test;

import development.team.hoteltransylvania.Business.GestionProduct;
import development.team.hoteltransylvania.Business.GestionReservation;
import development.team.hoteltransylvania.Business.GestionRoom;
import development.team.hoteltransylvania.Model.Product;
import development.team.hoteltransylvania.Model.Room;
import development.team.hoteltransylvania.Util.LoggerConfifg;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class testGeneral {
    public static void main(String[] args) {

        System.out.println(GestionRoom.updateStatusRoom(23, 4));


    }
}
