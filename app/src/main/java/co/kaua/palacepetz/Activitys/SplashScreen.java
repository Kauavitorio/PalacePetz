package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

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
    private final Handler timer = new Handler();

    //  Set preferences
    SharedPreferences mPrefs;
    private static final String PREFS_NAME = "myPrefs";

    final String email = "usermobile@palacepetz.com";
    final String password = "mobile123456";
    private int MAIN_TIMER = 1000;

    //  Firebase
    FirebaseAuth auth;
    static final Retrofit retrofitMobile = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/mobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        base_animation_splash = findViewById(R.id.base_animation_splash);
        launchShortcuts();
        StartApi();

        //  Set Containers on Screen
        base_animation_splash.setVisibility(View.VISIBLE);
        auth = ConfFirebase.getFirebaseAuth();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            Log.d("FirstLogin", "First login is ok");
            verifyIfUsersLogged();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void launchShortcuts() {
        ShortcutManager mShortcutManager = getSystemService(ShortcutManager.class);

        //  Intent for go to Products
        Intent intent_Products = new Intent(this, MainActivity.class);
        intent_Products.putExtra("shortcut", 10);
        intent_Products.setAction(Intent.ACTION_VIEW);

        //  Intent for go to shopping Cart
        Intent intent_Cart = new Intent(this, MainActivity.class);
        intent_Cart.putExtra("shortcut", 20);
        intent_Cart.setAction(Intent.ACTION_VIEW);

        //  Intent for go to Services
        Intent intent_Services = new Intent(this, MainActivity.class);
        intent_Services.putExtra("shortcut", 30);
        intent_Services.setAction(Intent.ACTION_VIEW);

        //  Intent for go to Services
        Intent intent_Fountain = new Intent(this, MainActivity.class);
        intent_Fountain.putExtra("shortcut", 40);
        intent_Fountain.setAction(Intent.ACTION_VIEW);

        ShortcutInfo mShortcutProduct = CreateShortCut(intent_Products, getString(R.string.products),
                getString(R.string.see_all_products), R.drawable.products_sheetmenu, 10);

        ShortcutInfo mShortcutShoppingCart = CreateShortCut(intent_Cart, getString(R.string.my_shopping_cart),
                getString(R.string.see_my_shoppingCart), R.mipmap.ic_shopping_cart, 20);

        ShortcutInfo mShortcutServices = CreateShortCut(intent_Services, getString(R.string.services),
                getString(R.string.schedule_a_service), R.drawable.ic_consultation, 30);

        ShortcutInfo mShortcutPalaceFountain = CreateShortCut(intent_Fountain, getString(R.string.palaceFountain),
                getString(R.string.manage_Fountain), R.drawable.fountain_sheetmenu, 40);

        ShortcutInfo mShortcutCards = CreateShortCut(intent_Services, getString(R.string.myCards),
                getString(R.string.myCards), R.drawable.cards_icon, 50);

        mShortcutManager.setDynamicShortcuts(Arrays.asList(mShortcutProduct, mShortcutShoppingCart, mShortcutServices, mShortcutCards, mShortcutPalaceFountain));
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public ShortcutInfo CreateShortCut(Intent intent, String ShortLabel, String LongLabel, int icon, int id) {
        return new ShortcutInfo.Builder(this, "Shortcut_Services_" + id)
                .setShortLabel(ShortLabel)
                .setLongLabel(LongLabel)
                .setIcon(Icon.createWithResource(this, icon))
                .setIntent(intent)
                .build();
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
        goto_main.putExtra("shortcut", 0);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
        ActivityCompat.startActivity(SplashScreen.this, goto_main, activityOptionsCompat.toBundle());
        finish();
    }

    private void StartApi() {
        MobileServices services = retrofitMobile.create(MobileServices.class);
        Call<DtoVersion> call = services.getMobileVersion();
        call.enqueue(new Callback<DtoVersion>() {
            @Override
            public void onResponse(@NonNull Call<DtoVersion> call, @NonNull Response<DtoVersion> response) { MAIN_TIMER = 1; }
            @Override
            public void onFailure(@NonNull Call<DtoVersion> call, @NonNull Throwable t) { Log.d("GetMobileVersion", "Fail to start"); }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}