package development.team.hoteltransylvania.Model;

public class ConsumeProduct {
    private int id;
    private Reservation reservation;
    private Room room;
    private Product product;
    private int quantity;
    private double priceUnit;
    private double priceTotal;

    public ConsumeProduct(int id, Reservation reservation, Product product, int quantity) {
        this.id = id;
        this.reservation = reservation;
        this.product = product;
        this.quantity = quantity;
    }

    public ConsumeProduct() {
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(double priceUnit) {
        this.priceUnit = priceUnit;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "ConsumeProduct{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
