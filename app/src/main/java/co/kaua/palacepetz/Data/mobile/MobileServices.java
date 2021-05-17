package co.kaua.palacepetz.Data.mobile;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MobileServices {

    @GET("version/")
    Call<DtoVersion> getMobileVersion();
}
