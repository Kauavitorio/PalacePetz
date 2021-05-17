package co.kaua.palacepetz.Data.mobile;

import android.util.Log;

import androidx.annotation.NonNull;

import co.kaua.palacepetz.BuildConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActionMobile {

    static final Retrofit retrofitMobile = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/mobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static void StartApi() {
        MobileServices services = retrofitMobile.create(MobileServices.class);
        Call<DtoVersion> call = services.getMobileVersion();
        call.enqueue(new Callback<DtoVersion>() {
            @Override
            public void onResponse(@NonNull Call<DtoVersion> call, @NonNull Response<DtoVersion> response) {
            }

            @Override
            public void onFailure(@NonNull Call<DtoVersion> call, @NonNull Throwable t) {
                Log.d("GetMobileVersion", "Fail to start");
            }
        });
    }
}
