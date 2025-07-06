package development.team.hoteltransylvania.DTO;

import java.sql.Timestamp;

public class AllInfoVentasDirecta {
    private int id_venta_directa;
    private int id_comprobante;
    private int id_empleado;
    private int id_producto;
    private String producto;
    private int cantidad;
    private double precio_unitario;
    private double precio_total;
    private Timestamp fecha_hora;
    private String empleado;

    public int getId_venta_directa() {
        return id_venta_directa;
    }

    public void setId_venta_directa(int id_venta_directa) {
        this.id_venta_directa = id_venta_directa;
    }

    public int getId_comprobante() {
        return id_comprobante;
    }

    public void setId_comprobante(int id_comprobante) {
        this.id_comprobante = id_comprobante;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public double getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
    }

    public Timestamp getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(Timestamp fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    @Override
    public String toString() {
        return "AllInfoVentasDirecta{" +
                "id_venta_directa=" + id_venta_directa +
                ", id_comprobante=" + id_comprobante +
                ", id_empleado=" + id_empleado +
                ", id_producto=" + id_producto +
                ", producto='" + producto + '\'' +
                ", cantidad=" + cantidad +
                ", precio_unitario=" + precio_unitario +
                ", precio_total=" + precio_total +
                ", fecha_hora=" + fecha_hora +
                ", empleado='" + empleado + '\'' +
                '}';
    }
}
