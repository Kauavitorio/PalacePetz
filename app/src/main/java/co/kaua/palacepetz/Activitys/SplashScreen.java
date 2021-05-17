package co.kaua.palacepetz.Activitys;

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

import co.kaua.palacepetz.Firebase.ConfFirebase;
import co.kaua.palacepetz.R;

import static co.kaua.palacepetz.Data.mobile.ActionMobile.StartApi;

public class SplashScreen extends AppCompatActivity {
    ConstraintLayout base_animation_splash;

    //  Create timer
    Handler timer = new Handler();

    //  Set preferences
    SharedPreferences mPrefs;
    private static final String PREFS_NAME = "myPrefs";

    String email = "usermobile@palacepetz.com";
    String password = "mobile123456";

    //  Firebase
    FirebaseAuth auth;

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
            GoToLogin();
        else
            timer.postDelayed(this::GoToLogin,2800);
    }

    private void GoToLogin() {
        Intent goto_login = new Intent(SplashScreen.this, LoginActivity.class);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
        ActivityCompat.startActivity(SplashScreen.this, goto_login, activityOptionsCompat.toBundle());
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}