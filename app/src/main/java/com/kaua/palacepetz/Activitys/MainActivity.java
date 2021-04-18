package com.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kaua.palacepetz.Fragments.MainFragment;
import com.kaua.palacepetz.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 * @author Kaua Vitorio
 **/

public class MainActivity extends AppCompatActivity {
    private LottieAnimationView btnMenu_Main;
    private CircleImageView icon_ProfileUser_main;
    private BottomSheetDialog bottomSheetDialog;

    //  User information
    String email_user;

    //  Set preferences
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "myPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Ids();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email_user = bundle.getString("email_user");

        //  Get all SharedPreferences
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        MainFragment mainFragment = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("email_user", email_user);
        mainFragment.setArguments(args);
        transaction.replace(R.id.frameLayoutMain, mainFragment);
        transaction.commit();

        icon_ProfileUser_main.setOnClickListener(v -> {
            Intent goTo_profile = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(goTo_profile);
        });


        CreatingMenuSheet();
    }

    private void Ids() {
        btnMenu_Main = findViewById(R.id.btnMenu_Main);
        icon_ProfileUser_main = findViewById(R.id.icon_ProfileUser_main);
    }

    private void CreatingMenuSheet() {
        btnMenu_Main.setOnClickListener(v -> {
            btnMenu_Main.playAnimation();
            bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetTheme);

            //  Creating View for SheetMenu
            View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.adapter_menu_sheet,
                    findViewById(R.id.menu_sheet_principal));

            //  When click in this linear will to LoginActivity
            sheetView.findViewById(R.id.BtnLogOut_sheet).setOnClickListener(v1 -> {
                AlertDialog.Builder warning_alert = new AlertDialog.Builder(MainActivity.this);
                warning_alert.setTitle(getString(R.string.logout));
                warning_alert.setMessage(getString(R.string.really_want_logOut));
                warning_alert.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    mPrefs.edit().clear().apply();
                    Intent goBack_toLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(goBack_toLogin);
                    finish();
                });
                warning_alert.setNegativeButton(getString(R.string.no), null);
                warning_alert.show();
            });

            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
        });
    }
}