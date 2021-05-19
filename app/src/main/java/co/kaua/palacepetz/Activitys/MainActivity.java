package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.BuildConfig;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Data.User.UserServices;
import co.kaua.palacepetz.Data.mobile.DtoVersion;
import co.kaua.palacepetz.Data.mobile.MobileServices;
import co.kaua.palacepetz.Fragments.AllProductsFragment;
import co.kaua.palacepetz.Fragments.MainFragment;
import co.kaua.palacepetz.Fragments.MyCardsFragment;
import co.kaua.palacepetz.Fragments.ServicesFragment;
import co.kaua.palacepetz.Fragments.ShoppingCartFragment;
import co.kaua.palacepetz.R;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static co.kaua.palacepetz.Data.mobile.ActionMobile.StartApi;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {
    //  Screen items
    private CardView base_QuantityItemsCart_main;
    private LottieAnimationView btnMenu_Main;
    private CircleImageView icon_ProfileUser_main;
    private BottomSheetDialog bottomSheetDialog;
    private ConstraintLayout Btn_container_ShoppingCart;
    private static FragmentTransaction transaction;
    private TextView txt_QuantityCart_main;
    Dialog warning_update;
    int Count = 0;

    //  Fragments Arguments
    private static Bundle args;
    private Bundle bundle;

    //  User information
    private int id_user;
    private String name_user, _Email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user, _Password;

    //  Set preferences
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "myPrefs";

    //  Shopping Cart Items
    private static int cartSize = 0;

    //  Firebase / Retrofit
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final Retrofit retrofitMobile = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/mobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Ids();
        bundle = getIntent().getExtras();
        id_user = bundle.getInt("id_user");
        name_user = bundle.getString("name_user");
        _Email = bundle.getString("email_user");
        cpf_user = bundle.getString("cpf_user");
        address_user = bundle.getString("address_user");
        complement = bundle.getString("complement");
        zipcode = bundle.getString("zipcode");
        phone_user = bundle.getString("phone_user");
        birth_date = bundle.getString("birth_date");
        img_user = bundle.getString("img_user");
        _Password = bundle.getString("password");
        if (address_user == null || address_user.equals(""))
            ShowAddressAlert();
        if (img_user == null || img_user.equals(""))
            Log.d("UserStatus", "Not User image");
        else
            Picasso.get().load(img_user).into(icon_ProfileUser_main);

        //  Set items gone
        base_QuantityItemsCart_main.setVisibility(View.GONE);

        //  Get all SharedPreferences
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        MainFragment mainFragment = new MainFragment();
        args = new Bundle();
        args.putString("email_user", _Email);
        mainFragment.setArguments(args);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutMain, mainFragment);
        transaction.commit();

        icon_ProfileUser_main.setOnClickListener(v -> {
            Intent goTo_profile = new Intent(MainActivity.this, ProfileActivity.class);
            goTo_profile.putExtra("id_user", id_user);
            goTo_profile.putExtra("name_user", name_user);
            goTo_profile.putExtra("email_user", _Email);
            goTo_profile.putExtra("cpf_user", cpf_user);
            goTo_profile.putExtra("address_user", address_user);
            goTo_profile.putExtra("complement", complement);
            goTo_profile.putExtra("zipcode", zipcode);
            goTo_profile.putExtra("phone_user", phone_user);
            goTo_profile.putExtra("birth_date", birth_date);
            goTo_profile.putExtra("img_user", img_user);
            goTo_profile.putExtra("password", _Password);
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
                args.putInt("id_user", id_user);
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
                goTo_ProductDetails.putExtra("id_user", id_user);
                startActivity(goTo_ProductDetails);
                bottomSheetDialog.dismiss();
            });

            //  Show My Cards Fragment
            myCards.setOnClickListener(v1 -> {
                MyCardsFragment myCardsFragment = new MyCardsFragment();
                args = new Bundle();
                args.putString("email_user", _Email);
                args.putInt("id_user", id_user);
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
                goTo_AddressRegister.putExtra("id_user", id_user);
                goTo_AddressRegister.putExtra("name_user", name_user);
                goTo_AddressRegister.putExtra("email_user", _Email);
                goTo_AddressRegister.putExtra("cpf_user", cpf_user);
                goTo_AddressRegister.putExtra("address_user", address_user);
                goTo_AddressRegister.putExtra("complement", complement);
                goTo_AddressRegister.putExtra("zipcode", zipcode);
                goTo_AddressRegister.putExtra("phone_user", phone_user);
                goTo_AddressRegister.putExtra("birth_date", birth_date);
                goTo_AddressRegister.putExtra("img_user", img_user);
                startActivity(goTo_AddressRegister);
                dialog.dismiss();
            });

            //  When click will dismiss dialog
            btn_registerLatter_addressAlert.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }
    }

    @Override protected void onResume() {
        super.onResume();
        GetUserInformation();

        //  Mobile Information
        int versionCode = BuildConfig.VERSION_CODE;
        warning_update = new Dialog(this);
        MobileServices services = retrofitMobile.create(MobileServices.class);
        Call<DtoVersion> call = services.getMobileVersion();
        call.enqueue(new Callback<DtoVersion>() {
            @Override
            public void onResponse(@NonNull Call<DtoVersion> call, @NonNull Response<DtoVersion> response) {
                switch (response.code()){
                    case 200:
                        assert response.body() != null;
                        if ( versionCode < response.body().getVersionCode()){
                            if (Count != 1){
                                CardView btnUpdateNow, btnUpdateLater;
                                warning_update.setContentView(R.layout.adapter_appneedupdate);
                                warning_update.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                warning_update.setCancelable(false);
                                btnUpdateNow = warning_update.findViewById(R.id.btnUpdateNow);
                                btnUpdateLater = warning_update.findViewById(R.id.btnUpdateLater);

                                btnUpdateNow.setOnClickListener(v -> {
                                    String url = "https://play.google.com/store/apps/details?id=co.kaua.palacepetz";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                    warning_update.dismiss();
                                    Count = 1;
                                });

                                btnUpdateLater.setOnClickListener(v -> {
                                    warning_update.dismiss();
                                    Count = 1;
                                });
                                warning_update.show();
                                Log.d("MobileVersion", "Need update: " + versionCode);
                            }
                        }
                        break;
                    case 204:
                        Log.d("GetMobileVersion", "No version on database");
                        break;
                    case 500:
                        Log.d("GetMobileVersion", "Fail to start");
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<DtoVersion> call, @NonNull Throwable t) {
                Log.d("GetMobileVersion", "Fail to start");
            }
        });
    }

    private void GetUserInformation() {
        UserServices usersService = retrofitUser.create(UserServices.class);
        DtoUser dtoUser = new DtoUser(_Email, _Password);
        Call<DtoUser> resultLogin = usersService.loginUser(dtoUser);
        resultLogin.enqueue(new Callback<DtoUser>() {
            @Override
            public void onResponse(@NonNull Call<DtoUser> call, @NonNull Response<DtoUser> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    id_user = response.body().getId_user();
                    name_user = response.body().getName_user();
                    _Email = response.body().getEmail();
                    cpf_user = response.body().getCpf_user();
                    address_user = response.body().getAddress_user();
                    complement = response.body().getComplement();
                    zipcode = response.body().getZipcode();
                    phone_user = response.body().getPhone_user();
                    birth_date = response.body().getBirth_date();
                    img_user = response.body().getImg_user();
                    if (img_user == null || img_user.equals(""))
                        Log.d("UserStatus", "Not User image");
                    else
                        Picasso.get().load(img_user).into(icon_ProfileUser_main);
                }else if (response.code() == 401){
                    Toast.makeText(MainActivity.this, getString(R.string.we_verify_yourEmailOrPassword), Toast.LENGTH_LONG).show();
                    Intent goTo_login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(goTo_login);
                    finish();
                }
            }
            @Override
            public void onFailure(@NonNull Call<DtoUser> call, @NonNull Throwable t) {
                Log.d("UserStatus", "Error to get user information on main\n" + t.getMessage());
            }
        });
    }

    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayoutMain);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}