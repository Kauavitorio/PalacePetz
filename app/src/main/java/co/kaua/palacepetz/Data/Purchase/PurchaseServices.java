package co.kaua.palacepetz.Data.Purchase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PurchaseServices {

    @POST("finish-order")
    Call<DtoPurchase> finish_order (@Body DtoPurchase purchaseInfo);

    @GET("{id_user}")
    Call<DtoPurchase> getOrder (@Path("id_user") int id_user);
}
