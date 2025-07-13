package development.team.hoteltransylvania.DTO;

public class AllInfoTableProdSalida {
    private int id_consumo;
    private int id_reserva;
    private int id_habitacion;
    private int id_producto;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitProducto;
    private double total;
    private String estadoProducto;

    public int getId_consumo() {
        return id_consumo;
    }

    public void setId_consumo(int id_consumo) {
        this.id_consumo = id_consumo;
    }

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public int getId_habitacion() {
        return id_habitacion;
    }

    public void setId_habitacion(int id_habitacion) {
        this.id_habitacion = id_habitacion;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitProducto() {
        return precioUnitProducto;
    }

    public void setPrecioUnitProducto(double precioUnitProducto) {
        this.precioUnitProducto = precioUnitProducto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(String estadoProducto) {
        this.estadoProducto = estadoProducto;
    }

    @Override
    public String toString() {
        return "AllInfoTableProdSalida{" +
                "id_consumo=" + id_consumo +
                ", id_reserva=" + id_reserva +
                ", id_habitacion=" + id_habitacion +
                ", id_producto=" + id_producto +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", cantidad=" + cantidad +
                ", precioUnitProducto=" + precioUnitProducto +
                ", total=" + total +
                ", estadoProducto='" + estadoProducto + '\'' +
                '}';
    }
}
