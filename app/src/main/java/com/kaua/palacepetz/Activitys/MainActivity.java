package com.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kaua.palacepetz.Adapters.IOnBackPressed;
import com.kaua.palacepetz.Fragments.AllProductsFragment;
import com.kaua.palacepetz.Fragments.DetailsProductsFragment;
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

            ConstraintLayout home = sheetView.findViewById(R.id.BtnHomeSheetMenu);
            ConstraintLayout products = sheetView.findViewById(R.id.BtnProductsSheetMenu);
            ConstraintLayout palaceFountain = sheetView.findViewById(R.id.BtnFountainsSheetMenu);
            ConstraintLayout myOrders = sheetView.findViewById(R.id.BtnMyOrdersSheetMenu);
            TextView txt_home = sheetView.findViewById(R.id.txt_sheet_home);
            TextView txt_products = sheetView.findViewById(R.id.txt_sheet_Products);
            TextView txt_palaceFountain = sheetView.findViewById(R.id.txt_sheet_Fountain);
            TextView txt_myOrders = sheetView.findViewById(R.id.txt_sheet_MyOrders);

            //  Show Main Fragment
            home.setOnClickListener(v1 -> {
                MainFragment mainFragment = new MainFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString("email_user", email_user);
                mainFragment.setArguments(args);
                transaction.replace(R.id.frameLayoutMain, mainFragment);
                transaction.commit();
                bottomSheetDialog.dismiss();
            });

            //  Show All Products fragment
            products.setOnClickListener(v1 -> {
                AllProductsFragment allProductsFragment = new AllProductsFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString("email_user", email_user);
                allProductsFragment.setArguments(args);
                transaction.replace(R.id.frameLayoutMain, allProductsFragment);
                transaction.commit();
                bottomSheetDialog.dismiss();
            });

            //  Show Palace Fountain Fragment
            palaceFountain.setOnClickListener(v1 -> {
                Intent goTo_DevicePresentation = new Intent(this, DevicePresentationActivity.class);
                startActivity(goTo_DevicePresentation);
                bottomSheetDialog.dismiss();
            });


            //  Show My Orders Fragment
            myOrders.setOnClickListener(v1 -> {
                DetailsProductsFragment detailsProductsFragment = new DetailsProductsFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString("email_user", email_user);
                detailsProductsFragment.setArguments(args);
                transaction.replace(R.id.frameLayoutMain, detailsProductsFragment);
                transaction.commit();
                bottomSheetDialog.dismiss();
            });

            //  When click in this linear will to LoginActivity
            sheetView.findViewById(R.id.BtnLogOutSheetMenu).setOnClickListener(v1 -> {
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

    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayoutMain);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}