package development.team.hoteltransylvania.DTO;

public class TopVendedor {
    private String nombre_empleado;
    private int cantidad_ventas;
    private double total_ventas;

    // Getters y Setters
    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public void setNombre_empleado(String nombre_empleado) {
        this.nombre_empleado = nombre_empleado;
    }

    public int getCantidad_ventas() {
        return cantidad_ventas;
    }

    public void setCantidad_ventas(int cantidad_ventas) {
        this.cantidad_ventas = cantidad_ventas;
    }

    public double getTotal_ventas() {
        return total_ventas;
    }

    public void setTotal_ventas(double total_ventas) {
        this.total_ventas = total_ventas;
    }

    @Override
    public String toString() {
        return "TopVendedor{" +
                "nombre_empleado='" + nombre_empleado + '\'' +
                ", cantidad_ventas=" + cantidad_ventas +
                ", total_ventas=" + total_ventas +
                '}';
    }
}
