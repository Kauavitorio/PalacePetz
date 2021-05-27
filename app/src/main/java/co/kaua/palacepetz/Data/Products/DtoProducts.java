package co.kaua.palacepetz.Data.Products;

import android.graphics.Bitmap;

public class DtoProducts {
    int cd_prod, cd_category, amount, popular;
    float product_price;
    String nm_category, nm_product, species, description, date_prod, shelf_life, image_prod;

    public int getCd_prod() {
        return cd_prod;
    }

    public void setCd_prod(int cd_prod) {
        this.cd_prod = cd_prod;
    }

    public int getCd_category() {
        return cd_category;
    }

    public void setCd_category(int cd_category) {
        this.cd_category = cd_category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPopular() {
        return popular;
    }

    public void setPopular(int popular) {
        this.popular = popular;
    }

    public float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(float product_price) {
        this.product_price = product_price;
    }

    public String getNm_category() {
        return nm_category;
    }

    public void setNm_category(String nm_category) {
        this.nm_category = nm_category;
    }

    public String getNm_product() {
        return nm_product;
    }

    public void setNm_product(String nm_product) {
        this.nm_product = nm_product;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_prod() {
        return date_prod;
    }

    public void setDate_prod(String date_prod) {
        this.date_prod = date_prod;
    }

    public String getShelf_life() {
        return shelf_life;
    }

    public void setShelf_life(String shelf_life) {
        this.shelf_life = shelf_life;
    }

    public String getImage_prod() {
        return image_prod;
    }

    public void setImage_prod(String image_prod) {
        this.image_prod = image_prod;
    }
}
