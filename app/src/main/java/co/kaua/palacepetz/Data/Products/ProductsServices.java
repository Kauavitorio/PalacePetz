package co.kaua.palacepetz.Data.Products;

import co.kaua.palacepetz.Data.Historic.DtoHistoric;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductsServices {

    @POST("user/register-historic-product/{id_user}/{cd_prod}")
    Call<DtoProducts> saveOnHistoric(@Path("id_user") int id_user, @Path("cd_prod") int cd_prod);

    @POST("historic-product/{id_user}")
    Call<DtoHistoric> getHistoric(@Path("id_user") int id_user);

    @DELETE("historic-product/{id_user}")
    Call<DtoHistoric> clearHistoric(@Path("id_user") int id_user);
}
