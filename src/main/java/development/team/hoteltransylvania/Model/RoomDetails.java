package development.team.hoteltransylvania.Model;

public class RoomDetails {
    private int id;
    private Reservation reservation;
    private Room room;
    private double total;

    public RoomDetails(int id, Reservation reservation, Room room, double total) {
        this.id = id;
        this.reservation = reservation;
        this.room = room;
        this.total = total;
    }

    public RoomDetails(Reservation reservation, Room room, double total) {
        this.reservation = reservation;
        this.room = room;
        this.total = total;
    }

    public RoomDetails() {
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "RoomDetails{" +
                "id=" + id +
                ", reservation=" + reservation +
                ", room=" + room +
                ", total=" + total +
                '}';
    }
}
