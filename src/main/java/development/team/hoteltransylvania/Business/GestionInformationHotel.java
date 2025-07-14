package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.Model.*;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class GestionInformationHotel {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionRoom.class);

    public static InformationHotel getInformationHotel() {
        String sql = "SELECT id, nombre, telefono, email, ubicacion, ruta_img, ruc FROM informacion_hotel LIMIT 1";
        InformationHotel hotel = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                hotel = new InformationHotel();
                hotel.setId(rs.getInt("id"));
                hotel.setName(rs.getString("nombre"));
                hotel.setPhone(rs.getString("telefono"));
                hotel.setEmail(rs.getString("email"));
                hotel.setAddress(rs.getString("ubicacion"));
                hotel.setImg(rs.getString("ruta_img"));
                hotel.setRuc(rs.getString("ruc"));
            } else {
                LOGGER.warning("No se encontró ningún registro en la tabla tipo_habitacion.");
            }
        } catch (SQLException e) {
            LOGGER.severe("Error al obtener la información del hotel: " + e.getMessage());
        }
        return hotel;
    }

    public static boolean updateInformationHotel(InformationHotel Hotel) {
        String sql = "UPDATE informacion_hotel SET nombre = ?, " +
                        "telefono = ?, email = ?, ubicacion = ? " +
                        "WHERE id = ?";
        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, Hotel.getName());
            ps.setString(2, Hotel.getPhone());
            ps.setString(3, Hotel.getEmail());
            ps.setString(4, Hotel.getAddress());
            ps.setInt(5, Hotel.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("InformationHotel " + Hotel.getId() + " updated successfully.");
                result = true;
            } else {
                LOGGER.warning("Error updating InformationHotel found with ID: " + Hotel.getId());
            }
        } catch (SQLException e) {
            LOGGER.severe("Error updating InformationHotel " + Hotel.getId() + ": " + e.getMessage());
        }
        return result;
    }

    public static boolean updateImgHotel(String img, int hotelId) {
        String sql = "UPDATE informacion_hotel SET ruta_img = ? " +
                "WHERE id = ?";
        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, img);
            ps.setInt(2, hotelId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("InformationHotel " + hotelId + " img update successfully.");
                result = true;
            } else {
                LOGGER.warning("Error updating InformationHotel found with ID: " + hotelId);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error updating InformationHotel " + hotelId + ": " + e.getMessage());
        }
        return result;
    }
}


