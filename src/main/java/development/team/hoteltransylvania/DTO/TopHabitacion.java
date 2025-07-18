package development.team.hoteltransylvania.DTO;

public class TopHabitacion {
    private String numero_habitacion;
    private double total_ingresos;
    private int cantidad_reservas;

    public String getNumero_habitacion() {
        return numero_habitacion;
    }

    public void setNumero_habitacion(String numero_habitacion) {
        this.numero_habitacion = numero_habitacion;
    }

    public double getTotal_ingresos() {
        return total_ingresos;
    }

    public void setTotal_ingresos(double total_ingresos) {
        this.total_ingresos = total_ingresos;
    }

    public int getCantidad_reservas() {
        return cantidad_reservas;
    }

    public void setCantidad_reservas(int cantidad_reservas) {
        this.cantidad_reservas = cantidad_reservas;
    }

    @Override
    public String toString() {
        return "TopHabitacion{" +
                "numero_habitacion='" + numero_habitacion + '\'' +
                ", total_ingresos=" + total_ingresos +
                ", cantidad_reservas=" + cantidad_reservas +
                '}';
    }
}
