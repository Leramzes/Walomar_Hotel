package development.team.hoteltransylvania.Model;

public class PaymentMethod {
    private int id;
    private String nameMethod;
    private int status;

    public PaymentMethod() {
    }

    public PaymentMethod(int id, String nameMethod) {
        this.id = id;
        this.nameMethod = nameMethod;
    }

    public PaymentMethod(String nameMethod, int status) {
        this.nameMethod = nameMethod;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameMethod() {
        return nameMethod;
    }

    public void setNameMethod(String nameMethod) {
        this.nameMethod = nameMethod;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + id +
                ", nameMethod='" + nameMethod + '\'' +
                ", status=" + status +
                '}';
    }
}
