package co.kaua.palacepetz.Data.MyOrders;

public class DtoOrder {
    private int cd_order, id_user, product_amount, cd_card, deliveryTime;
    private String cpf_user, discount, coupom, sub_total, totalPrice, order_products, date_order, status, payment;

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public int getCd_order() {
        return cd_order;
    }

    public void setCd_order(int cd_order) {
        this.cd_order = cd_order;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getProduct_amount() {
        return product_amount;
    }

    public void setProduct_amount(int product_amount) {
        this.product_amount = product_amount;
    }

    public int getCd_card() {
        return cd_card;
    }

    public void setCd_card(int cd_card) {
        this.cd_card = cd_card;
    }

    public String getCpf_user() {
        return cpf_user;
    }

    public void setCpf_user(String cpf_user) {
        this.cpf_user = cpf_user;
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

    public String getOrder_products() {
        return order_products;
    }

    public void setOrder_products(String order_products) {
        this.order_products = order_products;
    }

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
