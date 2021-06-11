package co.kaua.palacepetz.Data.Products;

import co.kaua.palacepetz.Data.Historic.DtoHistoric;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductsServices {

    @POST("user/register-historic-product/{id_user}/{cd_prod}")
    Call<DtoProducts> saveOnHistoric(@Path("id_user") int id_user, @Path("cd_prod") int cd_prod);

    @GET("historic-product/{id_user}")
    Call<DtoHistoric> getHistoric(@Path("id_user") int id_user);

    @GET("list/filter/name/{nm_product}")
    Call<DtoProducts> getFilterName(@Path("nm_product") String nm_product);

    @GET("products/details/{cd_prod}")
    Call<DtoProducts> getDetails(@Path("cd_prod") int cd_prod);

    @DELETE("historic-product/{id_user}")
    Call<DtoHistoric> clearHistoric(@Path("id_user") int id_user);
}
