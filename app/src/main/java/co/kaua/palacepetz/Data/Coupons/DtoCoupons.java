package co.kaua.palacepetz.Data.Coupons;

public class DtoCoupons {
    int cd_discounts, max_uses, used;
    String name_tag, discount_total, expiry_date;


    public int getCd_discounts() {
        return cd_discounts;
    }

    public void setCd_discounts(int cd_discounts) {
        this.cd_discounts = cd_discounts;
    }

    public int getMax_uses() {
        return max_uses;
    }

    public void setMax_uses(int max_uses) {
        this.max_uses = max_uses;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public String getName_tag() {
        return name_tag;
    }

    public void setName_tag(String name_tag) {
        this.name_tag = name_tag;
    }

    public String getDiscount_total() {
        return discount_total;
    }

    public void setDiscount_total(String discount_total) {
        this.discount_total = discount_total;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }
}
