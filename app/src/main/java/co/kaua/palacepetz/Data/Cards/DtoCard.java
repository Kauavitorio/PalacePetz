package co.kaua.palacepetz.Data.Cards;

public class DtoCard {
    int cd_card, id_user;
    int length;
    String flag_card, number_card, shelflife_card, cvv_card, nmUser_card;

    public DtoCard(int id_user, String flag_card, String number_card, String shelflife_card, String cvv_card, String nmUser_card) {
        this.id_user = id_user;
        this.flag_card = flag_card;
        this.number_card = number_card;
        this.shelflife_card = shelflife_card;
        this.cvv_card = cvv_card;
        this.nmUser_card = nmUser_card;
    }

    public DtoCard(){}

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getCd_card() {
        return cd_card;
    }

    public void setCd_card(int cd_card) {
        this.cd_card = cd_card;
    }

    public String getShelflife_card() {
        return shelflife_card;
    }

    public void setShelflife_card(String shelflife_card) {
        this.shelflife_card = shelflife_card;
    }

    public String getFlag_card() {
        return flag_card;
    }

    public void setFlag_card(String flag_card) {
        this.flag_card = flag_card;
    }

    public String getNumber_card() {
        return number_card;
    }

    public void setNumber_card(String number_card) {
        this.number_card = number_card;
    }

    public String getCvv_card() {
        return cvv_card;
    }

    public void setCvv_card(String cvv_card) {
        this.cvv_card = cvv_card;
    }

    public String getNmUser_card() {
        return nmUser_card;
    }

    public void setNmUser_card(String nmUser_card) {
        this.nmUser_card = nmUser_card;
    }
}
