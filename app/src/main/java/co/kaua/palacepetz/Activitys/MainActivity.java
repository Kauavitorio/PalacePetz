package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import co.kaua.palacepetz.Activitys.Help.HelpActivity;
import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.BuildConfig;
import co.kaua.palacepetz.Data.ShoppingCart.CartServices;
import co.kaua.palacepetz.Data.ShoppingCart.DtoShoppingCart;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Data.User.UserServices;
import co.kaua.palacepetz.Data.mobile.DtoVersion;
import co.kaua.palacepetz.Data.mobile.MobileServices;
import co.kaua.palacepetz.Fragments.AllProductsFragment;
import co.kaua.palacepetz.Fragments.HistoricFragment;
import co.kaua.palacepetz.Fragments.MainFragment;
import co.kaua.palacepetz.Fragments.MyCardsFragment;
import co.kaua.palacepetz.Fragments.MyOrdersFragment;
import co.kaua.palacepetz.Fragments.ServicesFragment;
import co.kaua.palacepetz.Fragments.ShoppingCartFragment;
import co.kaua.palacepetz.Methods.CaptureAct;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.R;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

@SuppressWarnings({"FieldCanBeLocal", "StaticFieldLeak"})
public class MainActivity extends AppCompatActivity {

    //  Screen items
    private CardView base_QuantityItemsCart_main;
    private LottieAnimationView btnMenu_Main;
    private CircleImageView icon_ProfileUser_main;
    private BottomSheetDialog bottomSheetDialog;
    private static FragmentTransaction transaction;
    private Animation CartAnim;
    private int Count = 0;
    private static MainActivity instance;
    private static DtoUser dtoUser;

    //  Fragments Arguments
    private static Bundle args;
    private Bundle bundle;

    //  User information
    @SuppressWarnings("unused")
    private static int _IdUser, status;
    private static String name_user, _Email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user, _Password;

    //  Set preferences
    private SharedPreferences mPrefs;
    public static final String PREFS_NAME = "myPrefs";

    //  Firebase / Retrofit
    private final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private final Retrofit retrofitMobile = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/mobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Ids();
        instance = this;

        //  Get all SharedPreferences
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        bundle = getIntent().getExtras();
        if (sp.contains("pref_email") && sp.contains("pref_password")){
            _IdUser = sp.getInt("pref_id_user", 0);
            status = sp.getInt("pref_status", 0);
            name_user = sp.getString("pref_name_user", null);
            _Email = sp.getString("pref_email", null);
            cpf_user = sp.getString("pref_cpf_user", null);
            address_user = sp.getString("pref_address_user", null);
            complement = sp.getString("pref_complement", null);
            zipcode = sp.getString("pref_zipcode", null);
            phone_user = sp.getString("pref_phone_user", null);
            birth_date = sp.getString("pref_birth_date", null);
            img_user = sp.getString("pref_img_user", null);
            _Password = sp.getString("pref_password", null);

            if (address_user == null || address_user.equals(""))
                ShowAddressAlert();
            if (img_user == null || img_user.equals(""))
                Log.d("UserStatus", "Not User image");
            else
                Picasso.get().load(img_user).into(icon_ProfileUser_main);
        }else
            _IdUser = 0;

        dtoUser.setId_user(_IdUser);


        if(bundle.getInt("shortcut") != 0){
            switch (bundle.getInt("shortcut")){
                case 10:
                    OpenAllProducts();
                    break;
                case 20:
                    if (_IdUser == 0){
                        LoadMainFragment();
                        Warnings.NeedLoginWithShortCutAlert(MainActivity.this, 20);
                    }
                    else
                        OpenShoppingCart();
                    break;
                case 30:
                    OpenServices();
                    break;
                case 40:
                    if (_IdUser == 0){
                        LoadMainFragment();
                        Warnings.NeedLoginWithShortCutAlert(MainActivity.this, 40);
                    }
                    else{
                        LoadMainFragment();
                        OpenFountain();
                    }
                    break;
                case 50:
                    if (_IdUser == 0){
                        LoadMainFragment();
                        Warnings.NeedLoginWithShortCutAlert(MainActivity.this, 50);
                    }
                    else
                        OpenMyCards();
                    break;
            }
        }else
            LoadMainFragment();

        //  Set items gone
        base_QuantityItemsCart_main.setVisibility(View.GONE);

        CreatingMenuSheet();
        CreateMenuSheetUser();
    }

    private void LoadMainFragment() {
        MainFragment mainFragment = new MainFragment();
        args = new Bundle();
        args.putInt("id_user", _IdUser);
        mainFragment.setArguments(args);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutMain, mainFragment);
        transaction.commit();
    }

    public final void OpenMyCards() {
        if (_IdUser != 0){
        MyCardsFragment myCardsFragment = new MyCardsFragment();
        args = new Bundle();
        args.putString("email_user", _Email);
        args.putInt("id_user", _IdUser);
        myCardsFragment.setArguments(args);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutMain, myCardsFragment);
        transaction.commit();
        }else
            Warnings.NeedLoginAlert(MainActivity.this);
    }

    public final void OpenAddressEdit(){
        if (_IdUser != 0){
            Intent goTo_AddressRegister = new Intent(MainActivity.this, RegisterAddressActivity.class);
            goTo_AddressRegister.putExtra("id_user", _IdUser);
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
        }else
            Warnings.NeedLoginAlert(MainActivity.this);
    }

    int cardSize = 0;
    @SuppressLint("SetTextI18n")
    public void CheckShoppingCart(){
        if (_IdUser != 0){
            if (cardSize != 0){
                base_QuantityItemsCart_main.setVisibility(View.VISIBLE);
                CartAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.cart_size_animation_gone);
                base_QuantityItemsCart_main.setAnimation(CartAnim);
            }
            else
                base_QuantityItemsCart_main.setVisibility(View.GONE);

            CartServices services = retrofitUser.create(CartServices.class);
            Call<DtoShoppingCart> cartCall = services.getCarSizetUser(_IdUser);
            cartCall.enqueue(new Callback<DtoShoppingCart>() {
                @Override
                public void onResponse(@NonNull Call<DtoShoppingCart> call, @NonNull Response<DtoShoppingCart> response) {
                    if (response.code() == 200){
                        assert response.body() != null;
                        cardSize = response.body().getLength();
                        if (cardSize != 0){
                            base_QuantityItemsCart_main.setVisibility(View.VISIBLE);
                            CartAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cart_size_animation_gone);
                            base_QuantityItemsCart_main.setAnimation(CartAnim);
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<DtoShoppingCart> call, @NonNull Throwable t) {}
            });
        }
    }
    
    public void ReOpenCart(){
        OpenShoppingCart();
    }

    private void Ids() {
        dtoUser = new DtoUser();
        btnMenu_Main = findViewById(R.id.btnMenu_Main);
        icon_ProfileUser_main = findViewById(R.id.icon_ProfileUser_main);
        base_QuantityItemsCart_main = findViewById(R.id.base_QuantityItemsCart_main);
    }

    private void CreatingMenuSheet() {
        btnMenu_Main.setOnClickListener(v -> {
            getWindow().setNavigationBarColor(getColor(R.color.background_top));
            btnMenu_Main.playAnimation();
            bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetTheme);
            //  Creating View for SheetMenu
            View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.adapter_menu_sheet,
                    findViewById(R.id.menu_sheet_principal));

            CardView home = sheetView.findViewById(R.id.BtnHomeSheetMenu);
            CardView products = sheetView.findViewById(R.id.BtnProductsSheetMenu);
            CardView historic = sheetView.findViewById(R.id.BtnHistoricSheetMenu);
            CardView services = sheetView.findViewById(R.id.BtnServicesSheetMenu);
            CardView qrcode = sheetView.findViewById(R.id.BtnQrCode);

            //  Show Main Fragment
            home.setOnClickListener(v1 -> {
                LoadMainFragment();
                bottomSheetDialog.dismiss();
            });

            historic.setOnClickListener(v1 -> {
                bottomSheetDialog.dismiss();
                if (_IdUser != 0){
                HistoricFragment historicFragment = new HistoricFragment();
                args = new Bundle();
                args.putInt("id_user", _IdUser);
                historicFragment.setArguments(args);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayoutMain, historicFragment);
                transaction.commit();
                }else
                    Warnings.NeedLoginAlert(MainActivity.this);
            });

            //  Show All Products fragment
            products.setOnClickListener(v1 -> {
                OpenAllProducts();
                bottomSheetDialog.dismiss();
            });

            services.setOnClickListener(v1 -> {
                OpenServices();
                bottomSheetDialog.dismiss();
            });

            qrcode.setOnClickListener(v1 -> {
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setCaptureActivity(CaptureAct.class);
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(false);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt(getString(R.string.scan_the_qr_code));
                integrator.initiateScan();
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null){
                String prodResult = result.getContents();
                String[] cd_prodResult = prodResult.split("--");
                try {
                    if(cd_prodResult[2].toLowerCase().equals("p1a2") || cd_prodResult[2].toLowerCase().equals("palaceetz") || cd_prodResult[2].toLowerCase().equals("qrpalace")){
                        int cd_prod = Integer.parseInt(cd_prodResult[1]);
                        Intent i = new Intent(this, ProductDetailsActivity.class);
                        i.putExtra("cd_prod", cd_prod);
                        startActivity(i);
                        Log.d("QrCodeStatus", "OK " + "\n QrCodeResult: " + prodResult);
                    }else
                        ToastHelper.toast(this, getString(R.string.desc_no_qr_not_palace));
                }
                catch (Exception ex){
                    ToastHelper.toast(this, getString(R.string.desc_no_qr_not_palace));
                    Log.d("QrCodeStatus", ex.toString() + "\n QrCodeResult: " + prodResult);
                }
            }else
                ToastHelper.toast(this, getString(R.string.no_results));
        }else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public final void OpenMyOrders() {
        if (_IdUser != 0){
            MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
            args = new Bundle();
            args.putInt("id_user", _IdUser);
            myOrdersFragment.setArguments(args);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutMain, myOrdersFragment);
            transaction.commit();
        }else
            Warnings.NeedLoginAlert(MainActivity.this);
    }

    @SuppressLint("SetTextI18n")
    private void CreateMenuSheetUser(){
        icon_ProfileUser_main.setOnClickListener(v -> {
                getWindow().setNavigationBarColor(getColor(R.color.background_top));
                bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetTheme);
                //  Creating View for SheetMenu
                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.adapter_menu_sheet_user,
                        findViewById(R.id.menu_sheet_user));

                //  Item  user adaptation
                TextView txt_nameUser = sheetView.findViewById(R.id.txt_nmUser_Sheet);
                CardView container_shoppingAmount = sheetView.findViewById(R.id.base_QuantityItemsCart_sheet);
                TextView txt_QuantityCart_sheet = sheetView.findViewById(R.id.txt_QuantityCart_sheet);
                TextView txt_logout = sheetView.findViewById(R.id.txt_sheet_LogOut);
                ImageView img_logout = sheetView.findViewById(R.id.Img_BtnLogOutSheetMenu);
                if (_IdUser == 0){
                    txt_logout.setText(R.string.login);
                    img_logout.setImageResource(R.drawable.ic_albatross);
                    sheetView.findViewById(R.id.BtnLogOutSheetMenu).setOnClickListener(v1 -> {
                        bottomSheetDialog.dismiss();
                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(login);
                        finish();
                    });
                }else
                    //  When click in this linear will to Show LogOut Message
                    sheetView.findViewById(R.id.BtnLogOutSheetMenu).setOnClickListener(v1 -> Warnings.LogoutDialog(MainActivity.this, bottomSheetDialog));

                if (_IdUser != 0)
                txt_nameUser.setText(getString(R.string.hello) + " " + name_user);
                else
                txt_nameUser.setText(getString(R.string.hello)+ " " + getString(R.string.anonymous_user));

                if (cardSize != 0){
                    container_shoppingAmount.setVisibility(View.VISIBLE);
                    CartAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.cart_size_animation);
                    container_shoppingAmount.setAnimation(CartAnim);
                    txt_QuantityCart_sheet.setText(cardSize + "");
                }else
                    container_shoppingAmount.setVisibility(View.GONE);

                if (img_user == null || img_user.equals(""))
                    Log.d("UserStatus", "Not User image");
                else
                    Picasso.get().load(img_user).into( (CircleImageView) sheetView.findViewById(R.id.icon_ProfileUser_sheetMenu));

                //  Interactive items
                ConstraintLayout btn_user_profile_sheet = sheetView.findViewById(R.id.btn_user_profile_sheet);
                CardView BtnFountainsSheetUserMenu = sheetView.findViewById(R.id.BtnFountainsSheetUserMenu);
                ConstraintLayout BtnMyShoppingCartSheetMenu = sheetView.findViewById(R.id.BtnMyShoppingCartSheetMenu);
                CardView BtnMyCardsSheetUserMenu = sheetView.findViewById(R.id.BtnMyCardsSheetUserMenu);
                CardView BtnMyOrdersSheetUserMenu = sheetView.findViewById(R.id.BtnMyOrdersSheetUserMenu);

                //  Click to open profile activity
                btn_user_profile_sheet.setOnClickListener(v1 -> {
                    OpenProfile();
                    bottomSheetDialog.dismiss();
                });

                //  Click to open palace fountain (Iot)
                BtnFountainsSheetUserMenu.setOnClickListener(v1 -> {
                    OpenFountain();
                    bottomSheetDialog.dismiss();
                });

                //  Click to open shopping cart
                BtnMyShoppingCartSheetMenu.setOnClickListener(v1 -> {
                    OpenShoppingCart();
                    bottomSheetDialog.dismiss();
                });

                //  Click to open cards
                BtnMyCardsSheetUserMenu.setOnClickListener(v1 -> {
                    OpenMyCards();
                    bottomSheetDialog.dismiss();
                });

                //  Click to open my orders
                BtnMyOrdersSheetUserMenu.setOnClickListener(v1 -> {
                    OpenMyOrders();
                    bottomSheetDialog.dismiss();
                });

                //  Click to open help activity
                sheetView.findViewById(R.id.BtnHelpSheetMenu).setOnClickListener(v1 -> {
                    OpenHelp();
                    bottomSheetDialog.dismiss();
                });

                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
        });
    }

    public final void OpenHelp() {
        Intent i = new Intent(MainActivity.this, HelpActivity.class);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
        ActivityCompat.startActivity(MainActivity.this, i, activityOptionsCompat.toBundle());
    }

    public final void OpenProfile() {
        if (_IdUser != 0){
            getWindow().setNavigationBarColor(getColor(R.color.background_top));
            Intent goTo_profile = new Intent(MainActivity.this, ProfileActivity.class);
            goTo_profile.putExtra("id_user", _IdUser);
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
        }else
            Warnings.NeedLoginAlert(MainActivity.this);
    }

    public final void OpenFountain() {
        if (_IdUser != 0){
        Intent goTo_DevicePresentation = new Intent(this, DevicePresentationActivity.class);
        startActivity(goTo_DevicePresentation);
        }else
            Warnings.NeedLoginAlert(MainActivity.this);
    }

    public final void OpenServices() {
        ServicesFragment servicesFragment = new ServicesFragment();
        args = new Bundle();
        args.putInt("id_user", _IdUser);
        servicesFragment.setArguments(args);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutMain, servicesFragment);
        transaction.commit();
    }

    public final void OpenAllProducts() {
        AllProductsFragment allProductsFragment = new AllProductsFragment();
        args = new Bundle();
        args.putInt("id_user", _IdUser);
        args.putString("search", null);
        allProductsFragment.setArguments(args);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutMain, allProductsFragment);
        transaction.commit();
    }

    public final void OpenShoppingCart() {
        if (_IdUser != 0){
            ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
            args = new Bundle();
            args.putString("email_user", _Email);
            args.putInt("id_user", _IdUser);
            shoppingCartFragment.setArguments(args);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutMain, shoppingCartFragment);
            transaction.commit();
        }else
            Warnings.NeedLoginAlert(MainActivity.this);
    }

    public static MainActivity getInstance() {
        return instance;
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
                goTo_AddressRegister.putExtra("id_user", _IdUser);
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

    public DtoUser GetUserBaseInformation(){
        dtoUser.setName_user(name_user);
        dtoUser.setEmail(_Email);
        dtoUser.setPassword(_Password);
        dtoUser.setCpf_user(cpf_user);
        dtoUser.setAddress_user(address_user);
        dtoUser.setComplement(complement);
        dtoUser.setZipcode(zipcode);
        dtoUser.setPhone_user(phone_user);
        dtoUser.setBirth_date(birth_date);
        dtoUser.setImg_user(img_user);
        dtoUser.setId_user(_IdUser);
        return dtoUser;
    }

    @Override protected void onResume() {
        super.onResume();
        GetUserInformation();
        CheckShoppingCart();

        //  Mobile Information
        int versionCode = BuildConfig.VERSION_CODE;
        MobileServices services = retrofitMobile.create(MobileServices.class);
        Call<DtoVersion> call = services.getMobileVersion();
        call.enqueue(new Callback<DtoVersion>() {
            @Override
            public void onResponse(@NonNull Call<DtoVersion> call, @NonNull Response<DtoVersion> response) {
                switch (response.code()){
                    case 200:
                        assert response.body() != null;
                        int developers = response.body().getDev_alert();
                        if(developers == 1)
                            Warnings.showDevelopers(MainActivity.this);

                        if ( versionCode < response.body().getVersionCode()){
                            if (Count != 1){
                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetTheme);
                                //  Creating View for SheetMenu
                                bottomSheetDialog.setCancelable(false);
                                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.adapter_appneedupdate,
                                        findViewById(R.id.sheet_app_update_menu));
                                TextView txt_version_app = sheetView.findViewById(R.id.txt_version_app);
                                String newVersionToUpdate = response.body().getVersionName();
                                txt_version_app.setText(getString(R.string.have_new_version, newVersionToUpdate));

                                sheetView.findViewById(R.id.btn_update_now).setOnClickListener(v -> {
                                    String url = "https://play.google.com/store/apps/details?id=co.kaua.palacepetz";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                    Count = 1;
                                    bottomSheetDialog.dismiss();
                                });

                                sheetView.findViewById(R.id.btn_update_later).setOnClickListener(v -> {
                                    bottomSheetDialog.dismiss();
                                    Count = 1;
                                });


                                bottomSheetDialog.setContentView(sheetView);
                                bottomSheetDialog.show();
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
        if (_IdUser != 0){
            UserServices usersService = retrofitUser.create(UserServices.class);
            DtoUser dtoUser = new DtoUser(_Email, _Password);
            Call<DtoUser> resultLogin = usersService.loginUser(dtoUser);
            resultLogin.enqueue(new Callback<DtoUser>() {
                @Override
                public void onResponse(@NonNull Call<DtoUser> call, @NonNull Response<DtoUser> response) {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        _IdUser = response.body().getId_user();
                        name_user = response.body().getName_user();
                        _Email = response.body().getEmail();
                        cpf_user = response.body().getCpf_user();
                        address_user = response.body().getAddress_user();
                        complement = response.body().getComplement();
                        zipcode = response.body().getZipcode();
                        phone_user = response.body().getPhone_user();
                        birth_date = response.body().getBirth_date();
                        status = response.body().getStatus();
                        if (!response.body().getImg_user().equals(img_user)){
                            img_user = response.body().getImg_user();
                            if (img_user == null || img_user.equals(""))
                                Log.d("UserStatus", "Not User image");
                            else
                                Picasso.get().load(img_user).into(icon_ProfileUser_main);
                        }
                        TryUpdatePreferences();
                    }else if (response.code() == 401){
                        ToastHelper.toast(MainActivity.this, getString(R.string.we_verify_yourEmailOrPassword));
                        Intent goTo_login = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(goTo_login);
                        finish();
                    }else if (response.code() == 410){
                        Intent goTo_login = new Intent(MainActivity.this, LoginActivity.class);
                        goTo_login.putExtra("disable", true);
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
    }

    private void TryUpdatePreferences() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_email") && sp.contains("pref_password")){
            mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("pref_name_user", name_user);
            editor.putInt("pref_id_user", _IdUser);
            editor.putString("pref_cpf_user", cpf_user);
            editor.putString("pref_address_user", address_user);
            editor.putString("pref_complement", complement);
            editor.putString("pref_zipcode", zipcode);
            editor.putString("pref_birth_date", birth_date);
            editor.putString("pref_phone_user", phone_user);
            editor.putString("pref_img_user", img_user);
            editor.apply();
        }
    }

    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayoutMain);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}