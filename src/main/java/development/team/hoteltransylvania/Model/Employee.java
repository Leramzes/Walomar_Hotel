package development.team.hoteltransylvania.Model;

public class Employee {
    private int id;
    private String name;
    private String position;
    private String email;
    private String num_doc;

    public Employee(int id, String name, String position, String email, String num_doc) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.email = email;
        this.num_doc = num_doc;
    }

    public Employee() {
    }

    public String getNum_doc() {
        return num_doc;
    }

    public void setNum_doc(String num_doc) {
        this.num_doc = num_doc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
