package development.team.hoteltransylvania.DTO;

public class TableReservationDTO {
    private int idClient;
    private int idReservation;
    private String clientName;
    private String documentType;
    private String documentNumber;
    private String room;
    private String checkInDate;
    private String checkOutDate;
    private String reservationStatus;

    public TableReservationDTO(int idClient, int idReservation, String clientName, String documentType, String documentNumber, String room,
                               String checkInDate, String checkOutDate, String reservationStatus) {
        this.idClient = idClient;
        this.idReservation = idReservation;
        this.clientName = clientName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.reservationStatus = reservationStatus;
    }

    public TableReservationDTO() {
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
