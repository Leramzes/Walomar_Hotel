package development.team.hoteltransylvania.Model;

public class ConsumeService {
    private int id;
    private Reservation reservation;
    private Service service;
    private int quantity;

    public ConsumeService(int id, Reservation reservation, Service service, int quantity) {
        this.id = id;
        this.reservation = reservation;
        this.service = service;
        this.quantity = quantity;
    }

    public ConsumeService() {
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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ConsumeService{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", service=" + service +
                ", quantity=" + quantity +
                '}';
    }
}
