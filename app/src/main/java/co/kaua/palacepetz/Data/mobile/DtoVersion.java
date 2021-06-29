package co.kaua.palacepetz.Data.mobile;

public class DtoVersion {
    int cd_version, versionCode, dev_alert;
    String versionName;

    public int getDev_alert() {
        return dev_alert;
    }

    public void setDev_alert(int dev_alert) {
        this.dev_alert = dev_alert;
    }

    public int getCd_version() {
        return cd_version;
    }

    public void setCd_version(int cd_version) {
        this.cd_version = cd_version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
