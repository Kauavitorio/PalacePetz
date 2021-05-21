package co.kaua.palacepetz.Data.ShoppingCart;

public class DtoShoppingCart {
    int cd_cart, cd_prod, id_user, product_amount, length;
    float product_price, totalPrice, sub_total;

    public DtoShoppingCart(int cd_prod, int id_user, int product_amount, float product_price, float totalPrice, float sub_total) {
        this.cd_prod = cd_prod;
        this.id_user = id_user;
        this.product_amount = product_amount;
        this.product_price = product_price;
        this.totalPrice = totalPrice;
        this.sub_total = sub_total;
    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getCd_cart() {
        return cd_cart;
    }

    public void setCd_cart(int cd_cart) {
        this.cd_cart = cd_cart;
    }

    public int getCd_prod() {
        return cd_prod;
    }

    public void setCd_prod(int cd_prod) {
        this.cd_prod = cd_prod;
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

    public float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(float product_price) {
        this.product_price = product_price;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getSub_total() {
        return sub_total;
    }

    public void setSub_total(float sub_total) {
        this.sub_total = sub_total;
    }
}
