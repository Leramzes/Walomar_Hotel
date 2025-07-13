package development.team.hoteltransylvania.DTO;

public class AllInfoTableServSalida {
    private int id_consumo;
    private int id_reserva;
    private int id_habitacion;
    private int id_servicio;
    private String nombreServicio;
    private double total;
    private String estadoServicio;

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

    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstadoServicio() {
        return estadoServicio;
    }

    public void setEstadoServicio(String estadoServicio) {
        this.estadoServicio = estadoServicio;
    }

    @Override
    public String toString() {
        return "AllInfoTableServSalida{" +
                "id_consumo=" + id_consumo +
                ", id_reserva=" + id_reserva +
                ", id_habitacion=" + id_habitacion +
                ", id_servicio=" + id_servicio +
                ", nombreServicio='" + nombreServicio + '\'' +
                ", total=" + total +
                ", estadoServicio='" + estadoServicio + '\'' +
                '}';
    }
}
