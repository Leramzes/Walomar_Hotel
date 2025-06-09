package development.team.hoteltransylvania.Model;

import java.sql.Date;
import java.sql.Timestamp;

public class Reservation {
    private int id;
    private Client client;
    private Employee employee;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;
    private Timestamp fechaIngreso;
    private StatusReservation statusReservation;
    private int dsct;
    private double cobro_extra;
    private double adelanto;
    private int cant_dias;

    public Reservation() {
    }

    public Reservation(Client client, Employee employee, Timestamp fechaInicio, Timestamp fechaFin, StatusReservation statusReservation, int dsct,
                       double cobro_extra, double adelanto, int cant_dias, Timestamp fechaIngreso) {
        this.client = client;
        this.employee = employee;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.statusReservation = statusReservation;
        this.dsct = dsct;
        this.cobro_extra = cobro_extra;
        this.adelanto = adelanto;
        this.cant_dias = cant_dias;
        this.fechaIngreso = fechaIngreso;
    }

    public Reservation(int id, Client client, Employee employee, Timestamp fechaInicio, Timestamp fechaFin, StatusReservation statusReservation,
                       int dsct, double cobro_extra, double adelanto, int cant_dias) {
        this.id = id;
        this.client = client;
        this.employee = employee;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.statusReservation = statusReservation;
        this.dsct = dsct;
        this.cobro_extra = cobro_extra;
        this.adelanto = adelanto;
        this.cant_dias = cant_dias;
    }

    public Timestamp getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Timestamp fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public int getCant_dias() {
        return cant_dias;
    }

    public void setCant_dias(int cant_dias) {
        this.cant_dias = cant_dias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public StatusReservation getStatusReservation() {
        return statusReservation;
    }

    public void setStatusReservation(StatusReservation statusReservation) {
        this.statusReservation = statusReservation;
    }

    public int getDsct() {
        return dsct;
    }

    public void setDsct(int dsct) {
        this.dsct = dsct;
    }

    public double getCobro_extra() {
        return cobro_extra;
    }

    public void setCobro_extra(double cobro_extra) {
        this.cobro_extra = cobro_extra;
    }

    public double getAdelanto() {
        return adelanto;
    }

    public void setAdelanto(double adelanto) {
        this.adelanto = adelanto;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", client=" + client +
                ", employee=" + employee +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", statusReservation=" + statusReservation +
                ", dsct=" + dsct +
                ", cobro_extra=" + cobro_extra +
                ", adelanto=" + adelanto +
                ", cant_dias=" + cant_dias +
                '}';
    }
}
