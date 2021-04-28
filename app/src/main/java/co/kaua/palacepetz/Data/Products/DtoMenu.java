package co.kaua.palacepetz.Data.Products;

import android.graphics.Bitmap;

public class DtoMenu {
    int cd_prod, qntd_prod, cd_cat, popular;
    float price_prod;
    private String  nm_prod, date_prod, size, bonusDesc, nm_cat, img_prod_st;
    private Bitmap img_prod;

    public DtoMenu(){}

    public DtoMenu(int cd_prod, int qntd_prod, String size, String bonusDesc, int cd_cat, int popular, float price_prod, Bitmap img_prod, String nm_prod, String date_prod, String nm_cat) {
        this.cd_prod = cd_prod;
        this.qntd_prod = qntd_prod;
        this.size = size;
        this.bonusDesc = bonusDesc;
        this.cd_cat = cd_cat;
        this.nm_cat = nm_cat;
        this.popular = popular;
        this.price_prod = price_prod;
        this.img_prod = img_prod;
        this.nm_prod = nm_prod;
        this.date_prod = date_prod;
    }

    public String getImg_prod_st() {
        return img_prod_st;
    }

    public void setImg_prod_st(String img_prod_st) {
        this.img_prod_st = img_prod_st;
    }

    public int getCd_prod() {
        return cd_prod;
    }

    public void setCd_prod(int cd_prod) {
        this.cd_prod = cd_prod;
    }

    public int getQntd_prod() {
        return qntd_prod;
    }

    public void setQntd_prod(int qntd_prod) {
        this.qntd_prod = qntd_prod;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBonusDesc() {
        return bonusDesc;
    }

    public void setBonusDesc(String bonusDesc) {
        this.bonusDesc = bonusDesc;
    }

    public int getCd_cat() {
        return cd_cat;
    }

    public void setCd_cat(int cd_cat) {
        this.cd_cat = cd_cat;
    }

    public String getNm_cat() {
        return nm_cat;
    }

    public void setNm_cat(String nm_cat) {
        this.nm_cat = nm_cat;
    }

    public int getPopular() {
        return popular;
    }

    public void setPopular(int popular) {
        this.popular = popular;
    }

    public float getPrice_prod() {
        return price_prod;
    }

    public void setPrice_prod(float price_prod) {
        this.price_prod = price_prod;
    }

    public Bitmap getImg_prod() {
        return img_prod;
    }

    public void setImg_prod(Bitmap img_prod) {
        this.img_prod = img_prod;
    }

    public String getNm_prod() {
        return nm_prod;
    }

    public void setNm_prod(String nm_prod) {
        this.nm_prod = nm_prod;
    }

    public String getDate_prod() {
        return date_prod;
    }

    public void setDate_prod(String date_prod) {
        this.date_prod = date_prod;
    }

    @Override
    public String toString() {
        return img_prod + "\n" + nm_prod + "\n" + price_prod + "\n" + qntd_prod + "\n" + cd_cat + "\n" + date_prod + "\n" + popular + "\n";
    }
}
