package co.kaua.palacepetz.Data.User;

public class DtoUser {
    private int id_user, user_type, status;
    private String name_user, email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user, password;

    public DtoUser(String name_user, String email, String cpf_user, String password) {
        this.name_user = name_user;
        this.email = email;
        this.cpf_user = cpf_user;
        this.password = password;
    }

    public DtoUser(String address_user, String complement, String zipcode, int id_user){
        this.address_user = address_user;
        this.complement = complement;
        this.zipcode = zipcode;
        this.id_user = id_user;
    }

    public DtoUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public DtoUser(int id_user, String img_user) {
        this.id_user = id_user;
        this.img_user = img_user;
    }

    public DtoUser(String name_user, String cpf_user, String address_user, String complement, String zipcode, String phone_user, String birth_date, int id_user) {
        this.name_user = name_user;
        this.cpf_user = cpf_user;
        this.address_user = address_user;
        this.complement = complement;
        this.zipcode = zipcode;
        this.phone_user = phone_user;
        this.birth_date = birth_date;
        this.id_user = id_user;
    }

    public DtoUser(String email) {
        this.email = email;
    }

    public DtoUser(){}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf_user() {
        return cpf_user;
    }

    public void setCpf_user(String cpf_user) {
        this.cpf_user = cpf_user;
    }

    public String getAddress_user() {
        return address_user;
    }

    public void setAddress_user(String address_user) {
        this.address_user = address_user;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone_user() {
        return phone_user;
    }

    public void setPhone_user(String phone_user) {
        this.phone_user = phone_user;
    }

    public String getImg_user() {
        return img_user;
    }

    public void setImg_user(String img_user) {
        this.img_user = img_user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
