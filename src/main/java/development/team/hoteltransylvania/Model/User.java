package development.team.hoteltransylvania.Model;

import java.util.Date;

public class User {
    private int id;
    private Employee employee;
    private String username;
    private String password;
    private StatusUser statusUser;
    private Date caducidad;

    public User() {
    }

    public User(int id, Employee employee, String username, String password, StatusUser statusUser) {
        this.id = id;
        this.employee = employee;
        this.username = username;
        this.password = password;
        this.statusUser = statusUser;
    }
    public User(int id, Employee employee, String username, String password, StatusUser statusUser,
                Date caducidad) {
        this.id = id;
        this.employee = employee;
        this.username = username;
        this.password = password;
        this.statusUser = statusUser;
        this.caducidad = caducidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StatusUser getStatusUser() {
        return statusUser;
    }

    public void setStatusUser(StatusUser statusUser) {
        this.statusUser = statusUser;
    }

    public Date getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(Date caducidad) {
        this.caducidad = caducidad;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", employee=" + employee +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", statusUser=" + statusUser +
                '}';
    }
}
