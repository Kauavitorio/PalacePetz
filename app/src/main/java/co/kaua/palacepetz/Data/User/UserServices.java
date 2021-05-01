package co.kaua.palacepetz.Data.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserServices {

    @POST("user/register")
    Call<DtoUser> registerNewUser(@Body DtoUser users);

    @POST("user/login")
    Call<DtoUser> loginUser(@Body DtoUser users);
}
