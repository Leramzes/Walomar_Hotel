package development.team.hoteltransylvania.DTO;

import java.sql.Timestamp;

public class TableReservationDTO {
    private int idReservation;
    private int idClient;
    private String clientName;
    private String documentType;
    private String documentNumber;
    private int idRoom;
    private String numberRoom;
    private String roomType;
    private Timestamp checkInDate;
    private Timestamp checkOutDate;
    private String reservationStatus;
    private String email;
    private String phone;
    private String address;

    public TableReservationDTO() {
    }

    public TableReservationDTO(int idReservation, int idClient, String clientName, String documentType, String documentNumber, int idRoom,
                               String numberRoom, String roomType, Timestamp checkInDate, Timestamp checkOutDate, String reservationStatus) {
        this.idReservation = idReservation;
        this.idClient = idClient;
        this.clientName = clientName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.idRoom = idRoom;
        this.numberRoom = numberRoom;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.reservationStatus = reservationStatus;
    }

    public TableReservationDTO(int idClient, String clientName, String documentType, String documentNumber, int idRoom, String numberRoom,
                               String roomType, Timestamp checkInDate, Timestamp checkOutDate, String reservationStatus) {
        this.idClient = idClient;
        this.clientName = clientName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.idRoom = idRoom;
        this.numberRoom = numberRoom;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.reservationStatus = reservationStatus;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
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

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    public String getNumberRoom() {
        return numberRoom;
    }

    public void setNumberRoom(String numberRoom) {
        this.numberRoom = numberRoom;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Timestamp getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Timestamp checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Timestamp getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Timestamp checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString() {
        return "TableReservationDTO{" +
                "idReservation=" + idReservation +
                ", idClient=" + idClient +
                ", clientName='" + clientName + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", idRoom=" + idRoom +
                ", numberRoom='" + numberRoom + '\'' +
                ", roomType='" + roomType + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", reservationStatus='" + reservationStatus + '\'' +
                '}';
    }
}
