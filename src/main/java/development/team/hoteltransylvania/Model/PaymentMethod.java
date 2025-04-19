package development.team.hoteltransylvania.Model;

public class PaymentMethod {
    private int id;
    private String nameMethod;

    public PaymentMethod() {
    }

    public PaymentMethod(int id, String nameMethod) {
        this.id = id;
        this.nameMethod = nameMethod;
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

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + id +
                ", nameMethod='" + nameMethod + '\'' +
                '}';
    }
}
