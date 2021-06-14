package co.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.R;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

@SuppressWarnings("FieldCanBeLocal")
public class IntroActivity extends AppCompatActivity {
    private LinearLayout btn_continue_without_account;
    private CardView btn_login_intro;
    private final Handler timer = new Handler();
    private LoadingDialog loadingDialog;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "First_See";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        btn_continue_without_account = findViewById(R.id.btn_continue_without_account);
        btn_login_intro = findViewById(R.id.btn_login_intro);
        btn_login_intro.setElevation(20);
        getWindow().setStatusBarColor(getColor(R.color.edittext_base));

        //  Click to go to login
        btn_login_intro.setOnClickListener(v -> {
            btn_login_intro.setElevation(0);
            Intent login = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        });

        btn_continue_without_account.setOnClickListener(v -> {
            getWindow().setStatusBarColor(getColor(R.color.background_bottom));
            loadingDialog = new LoadingDialog(IntroActivity.this);
            loadingDialog.startLoading();
            mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean("viewed", true);
            editor.apply();
            timer.postDelayed(() -> {
                Intent main = new Intent(IntroActivity.this, MainActivity.class);
                main.putExtra("shortcut", 0);
                startActivity(main);
                finish();
                loadingDialog.dimissDialog();
            }, 700);
        });
    }
}