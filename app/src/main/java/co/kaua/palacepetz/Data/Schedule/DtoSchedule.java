package co.kaua.palacepetz.Data.Schedule;

public class DtoSchedule {
    private int cd_schedule, id_user, payment_type, service_type, delivery, status;
    private String cd_animal, cd_veterinary, date_schedule, time_schedule, description;
    private String nm_animal, nm_veterinary;

    public DtoSchedule (int id_user, String cd_animal, String cd_veterinary, String time_schedule_get, String date_schedule_get, int payment_type, int service_type, String description){
        this.id_user = id_user;
        this.cd_animal = cd_animal;
        this.cd_veterinary = cd_veterinary;
        this.time_schedule = time_schedule_get;
        this.date_schedule = date_schedule_get;
        this.service_type = service_type;
        this.payment_type = payment_type;
        this.description = description;
    }

    public DtoSchedule (int id_user, String cd_animal, String time_schedule, String date_schedule, int payment_type, int service_type, int delivery, String description){
        this.id_user = id_user;
        this.cd_animal = cd_animal;
        this.time_schedule = time_schedule;
        this.delivery = delivery;
        this.date_schedule = date_schedule;
        this.service_type = service_type;
        this.payment_type = payment_type;
        this.description = description;
    }

    public DtoSchedule(){}

    public DtoSchedule(int id_user, int cd_schedule) {
        this.id_user = id_user;
        this.cd_schedule = cd_schedule;
    }

    public String getNm_animal() {
        return nm_animal;
    }

    public void setNm_animal(String nm_animal) {
        this.nm_animal = nm_animal;
    }

    public String getNm_veterinary() {
        return nm_veterinary;
    }

    public void setNm_veterinary(String nm_veterinary) {
        this.nm_veterinary = nm_veterinary;
    }

    public int getCd_schedule() {
        return cd_schedule;
    }

    public void setCd_schedule(int cd_schedule) {
        this.cd_schedule = cd_schedule;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getCd_animal() {
        return cd_animal;
    }

    public void setCd_animal(String cd_animal) {
        this.cd_animal = cd_animal;
    }

    public String getCd_veterinary() {
        return cd_veterinary;
    }

    public void setCd_veterinary(String cd_veterinary) {
        this.cd_veterinary = cd_veterinary;
    }

    public int getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    public int getService_type() {
        return service_type;
    }

    public void setService_type(int service_type) {
        this.service_type = service_type;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate_schedule() {
        return date_schedule;
    }

    public void setDate_schedule(String date_schedule) {
        this.date_schedule = date_schedule;
    }

    public String getTime_schedule() {
        return time_schedule;
    }

    public void setTime_schedule(String time_schedule) {
        this.time_schedule = time_schedule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
