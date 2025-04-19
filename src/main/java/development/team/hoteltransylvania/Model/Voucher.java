package development.team.hoteltransylvania.Model;

public class Voucher {
    private int id;
    private Reservation reservation;
    private TypeVoucher typeVoucher;
    private PaymentMethod paymentMethod;
    private double totalAmount;

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

    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", typeVoucher=" + typeVoucher +
                ", paymentMethod=" + paymentMethod +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
