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

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 * @author Kaua Vitorio
 **/

public class MainActivity extends AppCompatActivity {
    LottieAnimationView btnMenu_Main;
    private BottomSheetDialog bottomSheetDialog;

    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "myPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Ids();

        //  Get all SharedPreferences
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        MainFragment mainFragment = new MainFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutMain, mainFragment);
        transaction.commit();


        CreatingMenuSheet();
    }

    private void Ids() {
        btnMenu_Main = findViewById(R.id.btnMenu_Main);
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