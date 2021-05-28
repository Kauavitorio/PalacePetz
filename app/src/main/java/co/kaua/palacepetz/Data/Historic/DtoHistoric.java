package co.kaua.palacepetz.Data.Historic;

public class DtoHistoric {
    int cd_historic, id_user, cd_prod;
    String datetime, image_prod, nm_product, product_price;

    public int getCd_historic() {
        return cd_historic;
    }

    public void setCd_historic(int cd_historic) {
        this.cd_historic = cd_historic;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getCd_prod() {
        return cd_prod;
    }

    public void setCd_prod(int cd_prod) {
        this.cd_prod = cd_prod;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getImage_prod() {
        return image_prod;
    }

    public void setImage_prod(String image_prod) {
        this.image_prod = image_prod;
    }

    public String getNm_product() {
        return nm_product;
    }

    public void setNm_product(String nm_product) {
        this.nm_product = nm_product;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
}
