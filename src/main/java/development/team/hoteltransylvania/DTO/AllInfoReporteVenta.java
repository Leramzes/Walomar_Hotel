package development.team.hoteltransylvania.DTO;
import java.sql.Timestamp;

public class AllInfoReporteVenta {
    private int id_venta;
    private int id_habitacion;
    private String tipoHabitacion;
    private String numeroHabitacion;
    private int id_articulo;
    private String nombreArticulo;
    private int cantArticulo;
    private double precioUnitArticulo;
    private double precioTotalArticulo;
    private Timestamp fecha_hora_compra;
    private int idEmpleado;
    private String nombreEmpleado;
    private String tipoVenta;

    public int getId_venta() {
        return id_venta;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public int getId_habitacion() {
        return id_habitacion;
    }

    public void setId_habitacion(int id_habitacion) {
        this.id_habitacion = id_habitacion;
    }

    public String getNombreHabitacion() {
        return tipoHabitacion;
    }

    public void setNombreHabitacion(String nombreHabitacion) {
        this.tipoHabitacion = nombreHabitacion;
    }

    public String getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(String numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public int getCantArticulo() {
        return cantArticulo;
    }

    public void setCantArticulo(int cantArticulo) {
        this.cantArticulo = cantArticulo;
    }

    public double getPrecioUnitArticulo() {
        return precioUnitArticulo;
    }

    public void setPrecioUnitArticulo(double precioUnitArticulo) {
        this.precioUnitArticulo = precioUnitArticulo;
    }

    public double getPrecioTotalArticulo() {
        return precioTotalArticulo;
    }

    public void setPrecioTotalArticulo(double precioTotalArticulo) {
        this.precioTotalArticulo = precioTotalArticulo;
    }

    public Timestamp getFecha_hora_compra() {
        return fecha_hora_compra;
    }

    public void setFecha_hora_compra(Timestamp fecha_hora_compra) {
        this.fecha_hora_compra = fecha_hora_compra;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public int getId_articulo() {
        return id_articulo;
    }

    public void setId_articulo(int id_articulo) {
        this.id_articulo = id_articulo;
    }

    @Override
    public String toString() {
        return "AllInfoReporteVenta{" +
                "id_venta=" + id_venta +
                ", id_habitacion=" + id_habitacion +
                ", tipoHabitacion='" + tipoHabitacion + '\'' +
                ", numeroHabitacion='" + numeroHabitacion + '\'' +
                ", id_articulo=" + id_articulo +
                ", nombreArticulo='" + nombreArticulo + '\'' +
                ", cantArticulo=" + cantArticulo +
                ", precioUnitArticulo=" + precioUnitArticulo +
                ", precioTotalArticulo=" + precioTotalArticulo +
                ", fecha_hora_compra=" + fecha_hora_compra +
                ", idEmpleado=" + idEmpleado +
                ", nombreEmpleado='" + nombreEmpleado + '\'' +
                '}';
    }
}
