package development.team.hoteltransylvania.Model;

public class ConsumeProduct {
    private int id;
    private Reservation reservation;
    private Product product;
    private int quantity;

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
