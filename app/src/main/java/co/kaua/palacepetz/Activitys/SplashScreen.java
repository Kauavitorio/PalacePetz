package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import co.kaua.palacepetz.Data.mobile.DtoVersion;
import co.kaua.palacepetz.Data.mobile.MobileServices;
import co.kaua.palacepetz.Firebase.ConfFirebase;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreen extends AppCompatActivity {
    ConstraintLayout base_animation_splash;

    //  Create timer
    Handler timer = new Handler();

    //  Set preferences
    SharedPreferences mPrefs;
    private static final String PREFS_NAME = "myPrefs";

    String email = "usermobile@palacepetz.com";
    String password = "mobile123456";
    private int MAIN_TIMER = 1000;

    //  Firebase
    FirebaseAuth auth;
    static final Retrofit retrofitMobile = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/mobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        base_animation_splash = findViewById(R.id.base_animation_splash);
        StartApi();

        //  Set Containers on Screen
        base_animation_splash.setVisibility(View.VISIBLE);
        auth = ConfFirebase.getFirebaseAuth();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            Log.d("FirstLogin", "First login is ok");
            verifyIfUsersLogged();
        });
    }

    public void verifyIfUsersLogged() {
        //  Verification of user preference information
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_email") && sp.contains("pref_password"))
            timer.postDelayed(this::GoToMain, MAIN_TIMER);
        else
            timer.postDelayed(this::GoToLogin,2800);
    }

    private void GoToLogin() {
        Intent goto_login = new Intent(SplashScreen.this, LoginActivity.class);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
        ActivityCompat.startActivity(SplashScreen.this, goto_login, activityOptionsCompat.toBundle());
        finish();
    }

    private void GoToMain(){
        Intent goto_main = new Intent(SplashScreen.this, MainActivity.class);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
        ActivityCompat.startActivity(SplashScreen.this, goto_main, activityOptionsCompat.toBundle());
        finish();
    }

    private void StartApi() {
        MobileServices services = retrofitMobile.create(MobileServices.class);
        Call<DtoVersion> call = services.getMobileVersion();
        call.enqueue(new Callback<DtoVersion>() {
            @Override
            public void onResponse(@NonNull Call<DtoVersion> call, @NonNull Response<DtoVersion> response) {
                MAIN_TIMER = 1;
            }

            @Override
            public void onFailure(@NonNull Call<DtoVersion> call, @NonNull Throwable t) {
                Log.d("GetMobileVersion", "Fail to start");
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}