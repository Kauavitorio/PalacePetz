package co.kaua.palacepetz.Data.Pets;

public class DtoPets {
    private int cd_animal, id_user;
    private String nm_animal, breed_animal, age_animal, species_animal, image_animal, weight_animal;

    public DtoPets(){}

    public DtoPets(String nm_animal, String breed_animal, String age_animal, String weight_animal, String species_animal, String image_animal, int id_user) {
        this.nm_animal = nm_animal;
        this.breed_animal = breed_animal;
        this.age_animal = age_animal;
        this.species_animal = species_animal;
        this.image_animal = image_animal;
        this.weight_animal = weight_animal;
        this.id_user = id_user;
    }

    public DtoPets(int cd_animal, String nm_animal, String breed_animal, String age_animal, String weight_animal, String species_animal, String image_animal, int id_user) {
        this.cd_animal = cd_animal;
        this.nm_animal = nm_animal;
        this.breed_animal = breed_animal;
        this.age_animal = age_animal;
        this.species_animal = species_animal;
        this.image_animal = image_animal;
        this.weight_animal = weight_animal;
        this.id_user = id_user;
    }

    public DtoPets(int cd_animal, int id_user) {
        this.cd_animal = cd_animal;
        this.id_user = id_user;
    }

    public String getWeight_animal() {
        return weight_animal;
    }

    public void setWeight_animal(String weight_animal) {
        this.weight_animal = weight_animal;
    }

    public int getCd_animal() {
        return cd_animal;
    }

    public void setCd_animal(int cd_animal) {
        this.cd_animal = cd_animal;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNm_animal() {
        return nm_animal;
    }

    public void setNm_animal(String nm_animal) {
        this.nm_animal = nm_animal;
    }

    public String getBreed_animal() {
        return breed_animal;
    }

    public void setBreed_animal(String breed_animal) {
        this.breed_animal = breed_animal;
    }

    public String getAge_animal() {
        return age_animal;
    }

    public void setAge_animal(String age_animal) {
        this.age_animal = age_animal;
    }

    public String getSpecies_animal() {
        return species_animal;
    }

    public void setSpecies_animal(String species_animal) {
        this.species_animal = species_animal;
    }

    public String getImage_animal() {
        return image_animal;
    }

    public void setImage_animal(String image_animal) {
        this.image_animal = image_animal;
    }
}
