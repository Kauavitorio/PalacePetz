package co.kaua.palacepetz.Data.Purchase;

public class DtoPurchase {
    int id_user, cd_card;
    String discount, coupom, sub_total, totalPrice;

    public DtoPurchase(){}

    public DtoPurchase(int id_user, int cd_card, String discount, String coupom, String sub_total, String totalPrice) {
        this.id_user = id_user;
        this.cd_card = cd_card;
        this.discount = discount;
        this.coupom = coupom;
        this.sub_total = sub_total;
        this.totalPrice = totalPrice;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getCd_card() {
        return cd_card;
    }

    public void setCd_card(int cd_card) {
        this.cd_card = cd_card;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCoupom() {
        return coupom;
    }

    public void setCoupom(String coupom) {
        this.coupom = coupom;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
