package development.team.hoteltransylvania.Business;

import development.team.hoteltransylvania.DTO.usersEmployeeDTO;
import development.team.hoteltransylvania.Model.Employee;
import development.team.hoteltransylvania.Model.Product;
import development.team.hoteltransylvania.Services.DataBaseUtil;
import development.team.hoteltransylvania.Util.LoggerConfifg;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GestionProduct {
    private static final DataSource dataSource = DataBaseUtil.getDataSource();
    private static final Logger LOGGER = LoggerConfifg.getLogger(GestionProduct.class);

    public static boolean registerProduct(Product product) {
        String sql = "INSERT INTO productos (nombre, precio,status,cantidad) VALUES (?, ?, ?, ?)";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getStatus());
            ps.setInt(4,product.getQuantity());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Product " + product.getName() + " registered successfully");
                result = true;
            }
        } catch (SQLException e) {
            LOGGER.warning("Error when registering the product: " + e.getMessage());
        }
        return result;
    }
    public static boolean updateProduct(Product product) {
        String sql = "UPDATE productos SET nombre = ?, precio = ? WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("Product " + product.getId() + " updated successfully.");
                result = true;
            } else {
                LOGGER.warning("Error updating product. No product found with ID: " + product.getId());
            }
        } catch (SQLException e) {
            LOGGER.severe("Error updating product " + product.getId() + ": " + e.getMessage());
        }

        return result;
    }
    public static boolean deleteProduct(int productId) {
        String checkSql = "SELECT COUNT(*) FROM productos WHERE id = ?";
        String deleteSql = "DELETE FROM productos WHERE id = ?";

        boolean result = false;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement checkPs = cnn.prepareStatement(checkSql)) {

            checkPs.setInt(1, productId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                try (PreparedStatement deletePs = cnn.prepareStatement(deleteSql)) {
                    deletePs.setInt(1, productId);

                    int rowsAffected = deletePs.executeUpdate();
                    if (rowsAffected > 0) {
                        LOGGER.info("Product with ID " + productId + " deleted successfully.");
                        result = true;
                    }
                }
            } else {
                LOGGER.warning("Error deleting product. No product found with ID: " + productId);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error deleting product " + productId + ": " + e.getMessage());
        }

        return result;
    }
    public static List<Product> getAllProductsPaginated(int page, int pageSize) {
        String sql = "SELECT * FROM productos LIMIT ? OFFSET ?";
        List<Product> products = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("nombre"));
                product.setPrice(rs.getDouble("precio"));
                product.setStatus(rs.getInt("status"));
                product.setQuantity(rs.getInt("cantidad"));
                products.add(product);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving products: " + e.getMessage());
        }

        return products;
    }
    public static List<Product> getAllProducts() {
        String sql = "SELECT * FROM productos";
        List<Product> products = new ArrayList<>();

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {


            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("nombre"));
                product.setPrice(rs.getDouble("precio"));
                product.setStatus(rs.getInt("status"));
                product.setQuantity(rs.getInt("cantidad"));
                products.add(product);
            }

        } catch (SQLException e) {
            LOGGER.severe("Error retrieving products: " + e.getMessage());
        }

        return products;
    }
    public static Product getProductById(int productId) {
        String sql = "SELECT id, nombre, precio FROM productos WHERE id = ?";
        Product product = null;

        try (Connection cnn = dataSource.getConnection();
             PreparedStatement ps = cnn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nombre");
                double price = rs.getDouble("precio");

                product = new Product(id, name, price);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error retrieving product with ID " + productId + ": " + e.getMessage());
        }

        return product;
    }
    public static List<Product> filterProducts(String nombre, Integer estado, int page, int size) {
        List<Product> allProducts = getAllProducts(); // Obtiene todos los registros

        String nombreLower = nombre.toLowerCase().trim();

        List<Product> filteredProducts = allProducts.stream()
                .filter(product ->
                        (nombreLower.isEmpty() || product.getName().toLowerCase().contains(nombreLower)) &&
                                (estado == null || product.getStatus() == estado)
                )
                .collect(Collectors.toList());

        // Paginación: calcular desde qué índice empezar y hasta dónde llegar
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, filteredProducts.size());

        return filteredProducts.subList(fromIndex, toIndex);
    }
    public static int countFilteredProduct(String nombre, Integer estado) {
        List<Product> allProducts = getAllProducts();

        String nombreLower = nombre.toLowerCase().trim();

        return (int) allProducts.stream()
                .filter(product ->
                        (nombreLower.isEmpty() || product.getName().toLowerCase().contains(nombreLower)) &&
                                (estado == null || product.getStatus() == estado)
                )
                .count();
    }

}
