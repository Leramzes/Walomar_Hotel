package development.team.hoteltransylvania.Model;

public class ConsumeService {
    private int id;
    private Reservation reservation;
    private Room room;
    private Service service;
    private double totalPrice;
    private String estado_pago;

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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getEstado_pago() {
        return estado_pago;
    }

    public void setEstado_pago(String estado_pago) {
        this.estado_pago = estado_pago;
    }

    @Override
    public String toString() {
        return "ConsumeService{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", room=" + room +
                ", service=" + service +
                ", totalPrice=" + totalPrice +
                ", estado_pago='" + estado_pago + '\'' +
                '}';
    }
}
