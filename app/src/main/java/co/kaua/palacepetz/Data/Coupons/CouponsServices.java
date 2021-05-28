package co.kaua.palacepetz.Data.Coupons;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CouponsServices {

    @GET("discounts/apply/{name_tag}/{id_user}")
    Call<DtoCoupons> getCoupon (@Path("name_tag") String name_tag, @Path("id_user") int id_user);
}
