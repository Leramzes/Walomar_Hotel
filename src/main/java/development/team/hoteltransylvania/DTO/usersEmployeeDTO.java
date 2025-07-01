package development.team.hoteltransylvania.DTO;

import java.util.Date;

public class usersEmployeeDTO {
    private int id_employee;
    private int id_user;
    private String name_employee;
    private String name_user;
    private String email_user;
    private String tipo_user;
    private String estado_user;
    private Date fecha_caducidad;

    public usersEmployeeDTO(int id_employee, int id_user, String name_employee, String name_user, String email_user, String tipo_user, String estado_user) {
        this.id_employee = id_employee;
        this.id_user = id_user;
        this.name_employee = name_employee;
        this.name_user = name_user;
        this.email_user = email_user;
        this.tipo_user = tipo_user;
        this.estado_user = estado_user;
    }
    public usersEmployeeDTO(int id_employee, int id_user, String name_employee, String name_user, String email_user, String tipo_user, String estado_user, Date fecha_caducidad) {
        this.id_employee = id_employee;
        this.id_user = id_user;
        this.name_employee = name_employee;
        this.name_user = name_user;
        this.email_user = email_user;
        this.tipo_user = tipo_user;
        this.estado_user = estado_user;
        this.fecha_caducidad = fecha_caducidad;
    }

    public usersEmployeeDTO() {

    }

    public int getId_employee() {
        return id_employee;
    }

    public void setId_employee(int id_employee) {
        this.id_employee = id_employee;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getName_employee() {
        return name_employee;
    }

    public void setName_employee(String name_employee) {
        this.name_employee = name_employee;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getTipo_user() {
        return tipo_user;
    }

    public void setTipo_user(String tipo_user) {
        this.tipo_user = tipo_user;
    }

    public String getEstado_user() {
        return estado_user;
    }

    public void setEstado_user(String estado_user) {
        this.estado_user = estado_user;
    }

    public Date getFecha_caducidad() {
        return fecha_caducidad;
    }

    public void setFecha_caducidad(Date fecha_caducidad) {
        this.fecha_caducidad = fecha_caducidad;
    }

    @Override
    public String toString() {
        return "usersEmployeeDTO{" +
                "id_employee=" + id_employee +
                ", id_user=" + id_user +
                ", name_employee='" + name_employee + '\'' +
                ", name_user='" + name_user + '\'' +
                ", email_user='" + email_user + '\'' +
                ", tipo_user='" + tipo_user + '\'' +
                ", estado_user='" + estado_user + '\'' +
                '}';
    }
}
