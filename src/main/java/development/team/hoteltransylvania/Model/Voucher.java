package development.team.hoteltransylvania.Model;

import java.sql.Timestamp;

public class Voucher {
    private int id;
    private Reservation reservation;
    private TypeVoucher typeVoucher;
    private PaymentMethod paymentMethod;
    private double totalAmount;
    private double subtotalProducts;
    private double subtotalServices;
    private double subtotalPenalidad;
    private Timestamp fecha_emision;

    public Voucher(int id, Reservation reservation, TypeVoucher typeVoucher, PaymentMethod paymentMethod, double totalAmount) {
        this.id = id;
        this.reservation = reservation;
        this.typeVoucher = typeVoucher;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
    }

    public Voucher() {
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

    public TypeVoucher getTypeVoucher() {
        return typeVoucher;
    }

    public void setTypeVoucher(TypeVoucher typeVoucher) {
        this.typeVoucher = typeVoucher;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getSubtotalProducts() {
        return subtotalProducts;
    }

    public void setSubtotalProducts(double subtotalProducts) {
        this.subtotalProducts = subtotalProducts;
    }

    public double getSubtotalServices() {
        return subtotalServices;
    }

    public void setSubtotalServices(double subtotalServices) {
        this.subtotalServices = subtotalServices;
    }

    public double getSubtotalPenalidad() {
        return subtotalPenalidad;
    }

    public void setSubtotalPenalidad(double subtotalPenalidad) {
        this.subtotalPenalidad = subtotalPenalidad;
    }

    public Timestamp getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(Timestamp fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public double calcularTotal() { //para verificacion de salidas
        this.totalAmount = this.subtotalProducts + this.subtotalServices + this.subtotalPenalidad;
        return this.totalAmount;
    }
    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", typeVoucher=" + typeVoucher +
                ", paymentMethod=" + paymentMethod +
                ", totalAmount=" + totalAmount +
                ", subtotalProducts=" + subtotalProducts +
                ", subtotalServices=" + subtotalServices +
                ", subtotalPenalidad=" + subtotalPenalidad +
                ", fecha_emision=" + fecha_emision +
                '}';
    }
}
