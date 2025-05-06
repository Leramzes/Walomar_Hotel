package development.team.hoteltransylvania.Model;

public class Client {
    private int id;
    private String name;
    private String apPaterno;
    private String apMaterno;
    private String razonSocial;
    private String nacionalidad;
    private String direccion;
    private String telephone;
    private String email;
    private TypeDocument typeDocument;
    private String numberDocument;

    public Client(String name, String telephone, String email, TypeDocument typeDocument,
                  String numberDocument, String apPaterno, String apMaterno,
                  String razonSocial, String direccion, String nacionalidad) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.typeDocument = typeDocument;
        this.numberDocument = numberDocument;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.nacionalidad = nacionalidad;
    }

    public Client(int id, String name, String telephone, String email, TypeDocument typeDocument,
                  String numberDocument) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.typeDocument = typeDocument;
        this.numberDocument = numberDocument;
    }
    public Client(int id, String name, String apMaterno, String apPaterno, String telephone, String email, TypeDocument typeDocument,
                  String numberDocument) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.typeDocument = typeDocument;
        this.numberDocument = numberDocument;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
    }

    public Client() {
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TypeDocument getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(TypeDocument typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getNumberDocument() {
        return numberDocument;
    }

    public void setNumberDocument(String numberDocument) {
        this.numberDocument = numberDocument;
    }


    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", typeDocument=" + typeDocument +
                ", numberDocument='" + numberDocument + '\'' +
                '}';
    }
}
