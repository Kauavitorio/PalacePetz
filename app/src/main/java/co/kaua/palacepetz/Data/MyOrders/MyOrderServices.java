package co.kaua.palacepetz.Data.MyOrders;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MyOrderServices {

    @GET("order/last/{id_user}")
    Call<DtoOrder> getLastOrder(@Path("id_user") int id_user);
}
