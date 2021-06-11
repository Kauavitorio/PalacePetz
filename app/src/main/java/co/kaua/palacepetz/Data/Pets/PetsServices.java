package co.kaua.palacepetz.Data.Pets;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PetsServices {

    @GET("user/pet/{id_user}")
    Call<DtoPets> getPerts(@Path("id_user") int id_user);

    @POST("user/pet/insert")
    Call<DtoPets> insertNewPet(@Body DtoPets dtoPets);

    @PATCH("user/pet/update")
    Call<DtoPets> updatePet(@Body DtoPets dtoPets);

    @DELETE("user/pet/remove/{cd_animal}/{id_user}")
    Call<DtoPets> DeletePet(@Path("cd_animal") int cd_animal, @Path("id_user") int id_user);
}
