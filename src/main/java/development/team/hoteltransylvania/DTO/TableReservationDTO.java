package development.team.hoteltransylvania.DTO;

import java.sql.Timestamp;

public class TableReservationDTO {
    private int idReservation;
    private int idClient;
    private String clientName;
    private String clientApellidos;
    private String documentType;
    private String documentNumber;
    private int idRoom;
    private String numberRoom;
    private String roomType;
    private int roomTypeId;
    private Timestamp checkInDate;
    private Timestamp checkOutDate;
    private Timestamp fecha_ingreso;
    private int cantDays;
    private String reservationStatus;
    private int reservationStatusId;
    private String email;
    private String phone;
    private String address;
    private int dsct;
    private double cobro_extra;
    private double adelanto;
    private double pago_total;
    private int empleadoId;

    public TableReservationDTO() {
    }

    public TableReservationDTO(int idReservation, int idClient, String clientName, String documentType, String documentNumber, int idRoom,
                               Timestamp checkInDate, Timestamp checkOutDate, String email, String phone) {
        this.idReservation = idReservation;
        this.idClient = idClient;
        this.clientName = clientName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.idRoom = idRoom;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.email = email;
        this.phone = phone;
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

    public double getPago_total() {
        return pago_total;
    }

    public void setPago_total(double pago_total) {
        this.pago_total = pago_total;
    }

    public int getDsct() {
        return dsct;
    }

    public void setDsct(int dsct) {
        this.dsct = dsct;
    }

    public double getCobro_extra() {
        return cobro_extra;
    }

    public void setCobro_extra(double cobro_extra) {
        this.cobro_extra = cobro_extra;
    }

    public double getAdelanto() {
        return adelanto;
    }

    public void setAdelanto(double adelanto) {
        this.adelanto = adelanto;
    }

    public Timestamp getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(Timestamp fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public int getReservationStatusId() {
        return reservationStatusId;
    }

    public void setReservationStatusId(int reservationStatusId) {
        this.reservationStatusId = reservationStatusId;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getClientApellidos() {
        return clientApellidos;
    }

    public void setClientApellidos(String clientApellidos) {
        this.clientApellidos = clientApellidos;
    }

    public int getCantDays() {
        return cantDays;
    }

    public void setCantDays(int cantDays) {
        this.cantDays = cantDays;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getTiempoEstimado() {
        long milis = checkOutDate.getTime() - checkInDate.getTime();

        long minutos = milis / (1000 * 60);
        long dias = minutos / (60 * 24);
        long horas = (minutos % (60 * 24)) / 60;
        long mins = minutos % 60;

        return dias + " días " + horas + " horas " + mins + " minutos";
    }
    public String getTiempoRebasado() {
        if (fecha_ingreso == null || fecha_ingreso.before(checkOutDate)) {
            return "0 días 0 horas 0 minutos";
        }

        long milis = fecha_ingreso.getTime() - checkOutDate.getTime();

        long minutos = milis / (1000 * 60);
        long dias = minutos / (60 * 24);
        long horas = (minutos % (60 * 24)) / 60;
        long mins = minutos % 60;

        return dias + " días " + horas + " horas " + mins + " minutos";
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
