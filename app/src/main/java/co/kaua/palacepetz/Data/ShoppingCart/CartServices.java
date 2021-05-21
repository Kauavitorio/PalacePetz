package co.kaua.palacepetz.Data.ShoppingCart;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartServices {

    @GET("/shoppingcart/{id_user}")
    Call<DtoShoppingCart> getCartUser(@Path("id_user") int id_user);

    @GET("/shoppingcart/size/{id_user}")
    Call<DtoShoppingCart> getCarSizetUser(@Path("id_user") int id_user);

    @POST("/shoppingcart/insert")
    Call<DtoShoppingCart> insertItemOnCart(@Body DtoShoppingCart data);
}
