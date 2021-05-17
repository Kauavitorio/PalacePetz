package co.kaua.palacepetz.Data.Cards;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CardService {

    @GET("list/{id_user}")
    Call<DtoCard> getCardsOfUser(@Path("id_user") int id_user);

    @POST("card/")
    Call<DtoCard> insertNewCard(@Body DtoCard cardInfo);

    @DELETE("{id_user}/{cd_card}")
    Call<DtoCard> removeCard(@Path("id_user") int id_user, @Path("cd_card") int cd_card);
}
