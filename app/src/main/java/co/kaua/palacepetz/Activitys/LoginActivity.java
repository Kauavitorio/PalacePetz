package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.analytics.FirebaseAnalytics;



import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Data.User.UserServices;
import co.kaua.palacepetz.Firebase.ConfFirebase;
import co.kaua.palacepetz.R;
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

public class LoginActivity extends AppCompatActivity {
    //  Login items
    private EditText editLogin_emailUser, editLogin_passwordUser;
    private LottieAnimationView progressDogLogin;
    private TextView txt_SingInLogin;

    //  User information
    private int id_user, user_type;
    private String name_user, _Email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user, password;

    //  Next Activity
    private TextView txt_forgot_your_password, txt_SingUp;

    //  Login bottom
    private CardView cardBtn_SingIn;

    //  Tools
    private LoadingDialog loadingDialog;
    private static InputMethodManager imm;

    //  Set preferences
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "myPrefs";
    private int SHORTCUT_ID = 0;

    //  Fountain info
    boolean isDevicePre;

    //  Retrofit / Firebase
    FirebaseAnalytics mFirebaseAnalytics;
    @SuppressWarnings("FieldCanBeLocal")
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Ids();
        getWindow().setStatusBarColor(getColor(R.color.background_bottom));
        cardBtn_SingIn.setElevation(20);
        verifyIfUsersLogged();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (!(bundle == null)){
            _Email = bundle.getString("email_user");
            password = bundle.getString("password_user");
            SHORTCUT_ID = bundle.getInt("shortcut");
            editLogin_emailUser.setText(_Email);
            editLogin_passwordUser.setText(password);
        }

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Btn to Sing In
        cardBtn_SingIn.setOnClickListener(v -> {
            if (editLogin_emailUser.getText().length() == 0)
                showError(editLogin_emailUser, getString(R.string.email_required));
            else if (!Patterns.EMAIL_ADDRESS.matcher(editLogin_emailUser.getText()).matches())
                showError(editLogin_emailUser, getString(R.string.informed_email_is_invalid));
            else if(editLogin_passwordUser.getText().length() == 0)
                showError(editLogin_passwordUser, getString(R.string.password_required));
            else {
                _Email = editLogin_emailUser.getText().toString().trim();
                password = editLogin_passwordUser.getText().toString().trim();
                cardBtn_SingIn.setElevation(0);
                cardBtn_SingIn.setEnabled(false);
                progressDogLogin.setVisibility(View.VISIBLE);
                txt_SingInLogin.setVisibility(View.GONE);
                DoLogin(_Email, password);
            }
        });

        txt_SingUp.setOnClickListener(v -> {
            Intent goTo_SingUp = new Intent(this, CreateAccountActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
            ActivityCompat.startActivity(this, goTo_SingUp, activityOptionsCompat.toBundle());
            finish();
        });

        txt_forgot_your_password.setOnClickListener(v -> {
            Intent goTo_ForgotPassword = new Intent(this, ForgotPasswordActivity.class);
            startActivity(goTo_ForgotPassword);
        });
    }

    private void showError(@NonNull EditText editText, String error){
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.setError(error);
        cardBtn_SingIn.setElevation(20);
    }

    private void Ids() {
        cardBtn_SingIn = findViewById(R.id.cardBtn_SingIn);
        txt_forgot_your_password = findViewById(R.id.txt_forgot_your_password);
        txt_SingUp = findViewById(R.id.txt_SingUp);
        editLogin_emailUser = findViewById(R.id.editLogin_emailUser);
        editLogin_passwordUser = findViewById(R.id.editLogin_passwordUser);
        progressDogLogin = findViewById(R.id.progressDogLogin);
        txt_SingInLogin = findViewById(R.id.txt_SingInLogin);
        loadingDialog = new LoadingDialog(LoginActivity.this);
    }

    public void verifyIfUsersLogged() {
        //  Verification of user preference information
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_email") && sp.contains("pref_password")) {
            String emailPref = sp.getString("pref_email", "not found");
            String PassPref = sp.getString("pref_password", "not found");
            isDevicePre = sp.getBoolean("isDevicePre", false);
            DoLogin(emailPref, PassPref);
        }
    }

    private void DoLogin(String email, String password) {
        try {
            loadingDialog.startLoading();
            UserServices usersService = retrofitUser.create(UserServices.class);
            DtoUser dtoUser = new DtoUser(email, password);
            Call<DtoUser> resultLogin = usersService.loginUser(dtoUser);
            resultLogin.enqueue(new Callback<DtoUser>() {
                @Override
                public void onResponse(@NonNull Call<DtoUser> call, @NonNull Response<DtoUser> response) {
                    if (response.code() == 200){
                        // Obtain the FirebaseAnalytics instance.
                        mFirebaseAnalytics = ConfFirebase.getFirebaseAnalytics(LoginActivity.this);
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id_user + "");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name_user);
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "user");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
                        String emailUser;
                        assert response.body() != null;
                        user_type = response.body().getUser_type();
                        if(user_type == 0){
                            id_user = response.body().getId_user();
                            name_user = response.body().getName_user();
                            emailUser = response.body().getEmail();
                            cpf_user = response.body().getCpf_user();
                            address_user = response.body().getAddress_user();
                            complement = response.body().getComplement();
                            zipcode = response.body().getZipcode();
                            birth_date = response.body().getBirth_date();
                            phone_user = response.body().getPhone_user();
                            img_user = response.body().getImg_user();
                            mPrefs.edit().clear().apply();
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("pref_email", email);
                            editor.putString("pref_password", password);
                            editor.putString("pref_name_user", name_user);
                            editor.putInt("pref_id_user", id_user);
                            editor.putString("pref_cpf_user", cpf_user);
                            editor.putString("pref_address_user", address_user);
                            editor.putString("pref_complement", complement);
                            editor.putString("pref_zipcode", zipcode);
                            editor.putString("pref_birth_date", birth_date);
                            editor.putString("pref_phone_user", phone_user);
                            editor.putString("pref_img_user", img_user);
                            editor.putBoolean("isDevicePre", isDevicePre);
                            editor.apply();
                            SharedPreferences prefsFirst = getSharedPreferences("First_See", MODE_PRIVATE);
                            SharedPreferences.Editor prefsFirstIntro = prefsFirst.edit();
                            prefsFirstIntro.putBoolean("viewed", true);
                            prefsFirstIntro.apply();
                            GoToMain(id_user, name_user, emailUser, cpf_user, address_user, complement,
                                    zipcode, phone_user, birth_date, img_user, password);
                        }else{
                            Warnings.EmployeeAlert(LoginActivity.this);
                            editLogin_emailUser.setText(null);
                            editLogin_passwordUser.setText(null);
                            cardBtn_SingIn.setElevation(20);
                            cardBtn_SingIn.setEnabled(true);
                            progressDogLogin.setVisibility(View.GONE);
                            txt_SingInLogin.setVisibility(View.VISIBLE);
                            loadingDialog.dimissDialog();
                        }
                    }else if(response.code() == 405){
                        cardBtn_SingIn.setEnabled(true);
                        cardBtn_SingIn.setElevation(20);
                        loadingDialog.dimissDialog();
                        progressDogLogin.setVisibility(View.GONE);
                        txt_SingInLogin.setVisibility(View.VISIBLE);
                        Warnings.showEmailIsNotVerified(LoginActivity.this);
                    }else if(response.code() == 401){
                        mPrefs.edit().clear().apply();
                        cardBtn_SingIn.setElevation(20);
                        loadingDialog.dimissDialog();
                        editLogin_passwordUser.setText(null);
                        cardBtn_SingIn.setEnabled(true);
                        progressDogLogin.setVisibility(View.GONE);
                        txt_SingInLogin.setVisibility(View.VISIBLE);
                        Warnings.showWarning_Email_Password(LoginActivity.this);
                    }else{
                        loadingDialog.dimissDialog();
                        Warnings.showWeHaveAProblem(LoginActivity.this);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<DtoUser> call, @NonNull Throwable t) {
                    loadingDialog.dimissDialog();
                    Warnings.showWeHaveAProblem(LoginActivity.this);
                }
            });
        }catch (Exception ex){
            mPrefs.edit().clear().apply();
            Log.d("Login", ex.toString());
        }
    }

    private void GoToMain(int id_user, String name_user, String emailUser, String cpf_user, String address_user, String complement,
                          String zipcode, String phone_user, String birth_date, String img_user, String password) {
        Intent goTo_Main = new Intent(this, MainActivity.class);
        goTo_Main.putExtra("id_user", id_user);
        goTo_Main.putExtra("name_user", name_user);
        goTo_Main.putExtra("email_user", emailUser);
        goTo_Main.putExtra("cpf_user", cpf_user);
        goTo_Main.putExtra("address_user", address_user);
        goTo_Main.putExtra("complement", complement);
        goTo_Main.putExtra("zipcode", zipcode);
        goTo_Main.putExtra("phone_user", phone_user);
        goTo_Main.putExtra("birth_date", birth_date);
        goTo_Main.putExtra("img_user", img_user);
        goTo_Main.putExtra("password", password);
        goTo_Main.putExtra("shortcut", SHORTCUT_ID);
        goTo_Main.putExtra("AddressAlert", true);
        startActivity(goTo_Main);
        finish();
    }
}