package co.kaua.palacepetz.Data.ShoppingCart;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartServices {

    @GET("/shoppingcart/{id_user}")
    Call<DtoShoppingCart> getCartUser(@Path("id_user") int id_user);

    @GET("/shoppingcart/size/{id_user}")
    Call<DtoShoppingCart> getCarSizetUser(@Path("id_user") int id_user);

    @PATCH("/shoppingcart/update/amount/{cd_prod}/{product_amount}/{totalPrice}/{sub_total}/{id_user}")
    Call<DtoShoppingCart> UpdateCartWithNewAmount(@Path("cd_prod") int cd_prod, @Path("product_amount") int productAmount, @Path("totalPrice") float totalPrice,
                                                  @Path("sub_total") float SubTotal, @Path("id_user") int id_user);

    @POST("/shoppingcart/insert")
    Call<DtoShoppingCart> insertItemOnCart(@Body DtoShoppingCart data);

    @DELETE("/shoppingcart/remove/{id_user}/{cd_prod}")
    Call<DtoShoppingCart> removeProd(@Path("id_user") int idUser,@Path("cd_prod") int cd_prod);
}
