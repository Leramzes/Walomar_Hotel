package development.team.hoteltransylvania.Model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;

public class Checkout {
    private int id;
    private Reservation reservation;
    private Timestamp date_checkout;
    private Duration dateExtra_checkout;
    private double total_price;

    public Checkout(Reservation reservation, Timestamp date_checkout, Duration dateExtra_checkout, double total_price) {
        this.reservation = reservation;
        this.date_checkout = date_checkout;
        this.dateExtra_checkout = dateExtra_checkout;
        this.total_price = total_price;
    }

    public Checkout() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Timestamp getDate_checkout() {
        return date_checkout;
    }

    public void setDate_checkout(Timestamp date_checkout) {
        this.date_checkout = date_checkout;
    }

    public Duration getDateExtra_checkout() {
        return dateExtra_checkout;
    }

    public void setDateExtra_checkout(Duration dateExtra_checkout) {
        this.dateExtra_checkout = dateExtra_checkout;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    @Override
    public String toString() {
        return "Checkout{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", date_checkout=" + date_checkout +
                ", dateExtra_checkout=" + dateExtra_checkout +
                ", total_price=" + total_price +
                '}';
    }
}

