package co.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.Fragments.AllProductsFragment;
import co.kaua.palacepetz.Fragments.MainFragment;
import co.kaua.palacepetz.Fragments.MyCardsFragment;
import co.kaua.palacepetz.Fragments.ServicesFragment;
import co.kaua.palacepetz.Fragments.ShoppingCartFragment;
import co.kaua.palacepetz.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

public class MainActivity extends AppCompatActivity {
    //  Screen items
    private CardView base_QuantityItemsCart_main;
    private LottieAnimationView btnMenu_Main;
    private CircleImageView icon_ProfileUser_main;
    private BottomSheetDialog bottomSheetDialog;
    private ConstraintLayout Btn_container_ShoppingCart;
    private static FragmentTransaction transaction;
    private TextView txt_QuantityCart_main;

    //  Fragments Arguments
    private static Bundle args;
    private Bundle bundle;

    //  User information
    String _Email;

    //  Set preferences
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "myPrefs";

    //  Shopping Cart Items
    private static int cartSize = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Ids();
        Intent intent = getIntent();
        bundle = intent.getExtras();
        _Email = bundle.getString("email_user");

        //  Set items gone
        base_QuantityItemsCart_main.setVisibility(View.GONE);

        //  Get all SharedPreferences
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        ShowAddressAlert();

        MainFragment mainFragment = new MainFragment();
        args = new Bundle();
        args.putString("email_user", _Email);
        mainFragment.setArguments(args);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutMain, mainFragment);
        transaction.commit();

        icon_ProfileUser_main.setOnClickListener(v -> {
            Intent goTo_profile = new Intent(MainActivity.this, ProfileActivity.class);
            goTo_profile.putExtra("email_user", _Email);
            startActivity(goTo_profile);
        });

        //  Click to open ShoppingCart Fragment
        Btn_container_ShoppingCart.setOnClickListener(v -> {
            ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
            args = new Bundle();
            args.putString("email_user", _Email);
            shoppingCartFragment.setArguments(args);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutMain, shoppingCartFragment);
            transaction.commit();
        });

        CreatingMenuSheet();
    }

    @SuppressLint("SetTextI18n")
    public void updateCart(){
        cartSize++;
        txt_QuantityCart_main.setText(cartSize + "");
        base_QuantityItemsCart_main.setVisibility(View.VISIBLE);
    }

    private void Ids() {
        btnMenu_Main = findViewById(R.id.btnMenu_Main);
        icon_ProfileUser_main = findViewById(R.id.icon_ProfileUser_main);
        Btn_container_ShoppingCart = findViewById(R.id.Btn_container_ShoppingCart);
        txt_QuantityCart_main = findViewById(R.id.txt_QuantityCart_main);
        base_QuantityItemsCart_main = findViewById(R.id.base_QuantityItemsCart_main);
    }

    private void CreatingMenuSheet() {
        btnMenu_Main.setOnClickListener(v -> {
            btnMenu_Main.playAnimation();
            bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetTheme);
            //  Creating View for SheetMenu
            View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.adapter_menu_sheet,
                    findViewById(R.id.menu_sheet_principal));

            CardView home = sheetView.findViewById(R.id.BtnHomeSheetMenu);
            CardView products = sheetView.findViewById(R.id.BtnProductsSheetMenu);
            CardView palaceFountain = sheetView.findViewById(R.id.BtnFountainsSheetMenu);
            CardView myOrders = sheetView.findViewById(R.id.BtnMyOrdersSheetMenu);
            CardView myCards = sheetView.findViewById(R.id.BtnMyCardsSheetMenu);
            CardView historic = sheetView.findViewById(R.id.BtnHistoricSheetMenu);
            CardView services = sheetView.findViewById(R.id.BtnServicesSheetMenu);

            //  Show Main Fragment
            home.setOnClickListener(v1 -> {
                MainFragment mainFragment = new MainFragment();
                args = new Bundle();
                args.putString("email_user", _Email);
                mainFragment.setArguments(args);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayoutMain, mainFragment);
                transaction.commit();
                bottomSheetDialog.dismiss();
            });

            //  Show All Products fragment
            products.setOnClickListener(v1 -> {
                AllProductsFragment allProductsFragment = new AllProductsFragment();
                args = new Bundle();
                args.putString("email_user", _Email);
                allProductsFragment.setArguments(args);
                transaction = getSupportFragmentManager().beginTransaction();
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
                Intent goTo_ProductDetails = new Intent(this, ProductDetailsActivity.class);
                goTo_ProductDetails.putExtra("email_user", _Email);
                startActivity(goTo_ProductDetails);
                bottomSheetDialog.dismiss();
            });

            //  Show My Cards Fragment
            myCards.setOnClickListener(v1 -> {
                MyCardsFragment myCardsFragment = new MyCardsFragment();
                args = new Bundle();
                args.putString("email_user", _Email);
                myCardsFragment.setArguments(args);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayoutMain, myCardsFragment);
                transaction.commit();
                bottomSheetDialog.dismiss();
            });

            services.setOnClickListener(v1 -> {
                ServicesFragment servicesFragment = new ServicesFragment();
                args = new Bundle();
                args.putString("email_user", _Email);
                servicesFragment.setArguments(args);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayoutMain, servicesFragment);
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

    private void ShowAddressAlert(){
        if (bundle.getBoolean("AddressAlert")){
            Log.d("AddressAlertStatus", "Alert ON");
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.adapter_address_alert);
            CardView btn_registerNow_addressAlert = dialog.findViewById(R.id.btn_registerNow_addressAlert);
            CardView btn_registerLatter_addressAlert = dialog.findViewById(R.id.btn_registerLatter_addressAlert);
            btn_registerNow_addressAlert.setElevation(20);
            btn_registerLatter_addressAlert.setElevation(20);

            //  When click will go to RegisterAddressActivity
            btn_registerNow_addressAlert.setOnClickListener(v -> {
                btn_registerNow_addressAlert.setElevation(0);
                Intent goTo_AddressRegister = new Intent(MainActivity.this, RegisterAddressActivity.class);
                startActivity(goTo_AddressRegister);
                dialog.dismiss();
            });

            //  When click will dismiss dialog
            btn_registerLatter_addressAlert.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }
    }

    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayoutMain);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}