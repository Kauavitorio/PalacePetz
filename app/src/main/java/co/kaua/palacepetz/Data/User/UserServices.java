package co.kaua.palacepetz.Data.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface UserServices {

    @POST("user/register")
    Call<DtoUser> registerNewUser(@Body DtoUser users);

    @POST("user/login")
    Call<DtoUser> loginUser(@Body DtoUser users);

    @PATCH("user/update/profile/image/")
    Call<DtoUser> updateProfileImage(@Body DtoUser users);

    @PATCH("user/update/profile/")
    Call<DtoUser> updateProfile(@Body DtoUser users);

    @PATCH("user/update/address/")
    Call<DtoUser> updateAddress(@Body DtoUser users);

    @POST("user/request/password/")
    Call<DtoUser> requestPasswordReset(@Body DtoUser users);
}