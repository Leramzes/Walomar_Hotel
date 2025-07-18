package development.team.hoteltransylvania.DTO;

public class TopHabitacion {
    private String numero_habitacion;
    private double total_ingresos;

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

    @Override
    public String toString() {
        return "TopHabitacion{" +
                "numero_habitacion='" + numero_habitacion + '\'' +
                ", total_ingresos=" + total_ingresos +
                '}';
    }
}
