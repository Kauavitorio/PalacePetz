package co.kaua.palacepetz.Data.Cards;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CardService {

    @GET("{email_user}")
    Call<DtoCard> getCardsOfUser(@Path("email_user") String _Email);

    @POST("{email_user}/{flag_card}/{number_card}/{shelflife_card}/{cvv_card}/{nmUser_card}")
    Call<DtoCard> insertNewCard(@Path("email_user") String _Email, @Path("flag_card") String flag_card, @Path("number_card") String number_card,
                                @Path("shelflife_card") String shelflife_card, @Path("cvv_card") String cvv_card, @Path("nmUser_card") String nmUser_card);

    @DELETE("{email_user}/{cd_card}")
    Call<DtoCard> removeCard(@Path("email_user") String _Email, @Path("cd_card") int cd_card);
}
