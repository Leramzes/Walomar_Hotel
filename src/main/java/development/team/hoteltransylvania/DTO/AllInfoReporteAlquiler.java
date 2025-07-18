package development.team.hoteltransylvania.DTO;

import java.sql.Timestamp;

public class AllInfoReporteAlquiler {
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
    private Timestamp fecha_desalojo;
    private int cantDays;
    private String reservationStatus;
    private int reservationStatusId;
    private String email;
    private String phone;
    private String address;
    private int dsct;
    private double cobro_extra;
    private double adelanto;
    private double pago_total_reserva;
    private int empleadoId;
    private String nombre_empleado;
    private double total_consumo_productos;
    private double total_consumo_servicios;
    private double total_penalidad;
    private String tipoAlquiler;

    public String getTipoAlquiler() {
        return tipoAlquiler;
    }

    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public void setNombre_empleado(String nombre_empleado) {
        this.nombre_empleado = nombre_empleado;
    }

    public void setTipoAlquiler(String tipoAlquiler) {
        this.tipoAlquiler = tipoAlquiler;
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

    public String getClientApellidos() {
        return clientApellidos;
    }

    public void setClientApellidos(String clientApellidos) {
        this.clientApellidos = clientApellidos;
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

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
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

    public Timestamp getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(Timestamp fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public Timestamp getFecha_desalojo() {
        return fecha_desalojo;
    }

    public void setFecha_desalojo(Timestamp fecha_desalojo) {
        this.fecha_desalojo = fecha_desalojo;
    }

    public int getCantDays() {
        return cantDays;
    }

    public void setCantDays(int cantDays) {
        this.cantDays = cantDays;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public int getReservationStatusId() {
        return reservationStatusId;
    }

    public void setReservationStatusId(int reservationStatusId) {
        this.reservationStatusId = reservationStatusId;
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

    public double getPago_total_reserva() {
        return pago_total_reserva;
    }

    public void setPago_total_reserva(double pago_total_reserva) {
        this.pago_total_reserva = pago_total_reserva;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public double getTotal_consumo_productos() {
        return total_consumo_productos;
    }

    public void setTotal_consumo_productos(double total_consumo_productos) {
        this.total_consumo_productos = total_consumo_productos;
    }

    public double getTotal_consumo_servicios() {
        return total_consumo_servicios;
    }

    public void setTotal_consumo_servicios(double total_consumo_servicios) {
        this.total_consumo_servicios = total_consumo_servicios;
    }

    public double getTotal_penalidad() {
        return total_penalidad;
    }

    public void setTotal_penalidad(double total_penalidad) {
        this.total_penalidad = total_penalidad;
    }

    @Override
    public String toString() {
        return "AllInfoReporteAlquiler{" +
                "idReservation=" + idReservation +
                ", idClient=" + idClient +
                ", clientName='" + clientName + '\'' +
                ", clientApellidos='" + clientApellidos + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", idRoom=" + idRoom +
                ", numberRoom='" + numberRoom + '\'' +
                ", roomType='" + roomType + '\'' +
                ", roomTypeId=" + roomTypeId +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", fecha_ingreso=" + fecha_ingreso +
                ", fecha_desalojo=" + fecha_desalojo +
                ", cantDays=" + cantDays +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", reservationStatusId=" + reservationStatusId +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", dsct=" + dsct +
                ", cobro_extra=" + cobro_extra +
                ", adelanto=" + adelanto +
                ", pago_total_reserva=" + pago_total_reserva +
                ", empleadoId=" + empleadoId +
                ", total_consumo_productos=" + total_consumo_productos +
                ", total_consumo_servicios=" + total_consumo_servicios +
                ", total_penalidad=" + total_penalidad +
                ", tipoAlquiler='" + tipoAlquiler + '\'' +
                '}';
    }
}
