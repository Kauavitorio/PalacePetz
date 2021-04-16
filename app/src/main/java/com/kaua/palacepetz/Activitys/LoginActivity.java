package com.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.kaua.palacepetz.R;

public class LoginActivity extends AppCompatActivity {
    //  Login items
    CheckBox checkbox_rememberMe;
    EditText editLogin_emailUser, editLogin_passwordUser;

    //  Next Activity
    TextView txt_forgot_your_password, txt_SingUp;

    //  Login bottom
    LottieAnimationView anim_login_SingIn;
    TextView txt_SingInLogin;
    CardView cardBtn_SingIn;

    //  Set preferences
    SharedPreferences mPrefs;
    private static final String PREFS_NAME = "myPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Ids();
        cardBtn_SingIn.setElevation(20);
        verifyIfUsersLogged();

        cardBtn_SingIn.setOnClickListener(v -> {
            cardBtn_SingIn.setElevation(0);
            cardBtn_SingIn.setEnabled(false);
            txt_SingInLogin.setVisibility(View.GONE);
            anim_login_SingIn.setVisibility(View.VISIBLE);

        });
    }

    private void Ids() {
        cardBtn_SingIn = findViewById(R.id.cardBtn_SingIn);
        txt_forgot_your_password = findViewById(R.id.txt_forgot_your_password);
        txt_SingUp = findViewById(R.id.txt_SingUp);
        anim_login_SingIn = findViewById(R.id.anim_login_SingIn);
        txt_SingInLogin = findViewById(R.id.txt_SingInLogin);
        checkbox_rememberMe = findViewById(R.id.checkbox_rememberMe);
        editLogin_emailUser = findViewById(R.id.editLogin_emailUser);
        editLogin_passwordUser = findViewById(R.id.editLogin_passwordUser);
    }

    public void verifyIfUsersLogged() {
        //  Verification of user preference information
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_email") && sp.contains("pref_password")) {
            String emailPref = sp.getString("pref_email", "not found");
            String PassPref = sp.getString("pref_password", "not found");
            checkbox_rememberMe.setChecked(sp.getBoolean("pref_check", true));
            DoLogin(emailPref, PassPref);
        }
    }

    private void DoLogin(String email, String password) {

    }
}