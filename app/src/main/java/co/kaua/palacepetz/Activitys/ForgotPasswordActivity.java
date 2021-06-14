package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Data.User.UserServices;
import co.kaua.palacepetz.Methods.ToastHelper;
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

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText editForgotPassword_emailUser;
    CardView cardBtn_ChangePassword;
    TextView txt_haveAccount_forgotPassword;
    LottieAnimationView arrowGoBackForgotPassword;

    //  Tools
    InputMethodManager imm;
    LoadingDialog loadingDialog;

    //  Retrofit / Firebase
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        editForgotPassword_emailUser = findViewById(R.id.editForgotPassword_emailUser);
        cardBtn_ChangePassword = findViewById(R.id.cardBtn_ChangePassword);
        txt_haveAccount_forgotPassword = findViewById(R.id.txt_haveAccount_forgotPassword);
        arrowGoBackForgotPassword = findViewById(R.id.arrowGoBackForgotPassword);
        loadingDialog = new LoadingDialog(this);
        cardBtn_ChangePassword.setElevation(20);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        cardBtn_ChangePassword.setOnClickListener(v -> {
            cardBtn_ChangePassword.setElevation(0);
            if (editForgotPassword_emailUser.getText().length() == 0)
                showError(editForgotPassword_emailUser, getString(R.string.email_required));
            else if(!Patterns.EMAIL_ADDRESS.matcher(editForgotPassword_emailUser.getText()).matches())
                showError(editForgotPassword_emailUser, getString(R.string.informed_email_is_invalid));
            else{
                loadingDialog.startLoading();
                UserServices userServices = retrofitUser.create(UserServices.class);
                DtoUser userInfo = new DtoUser(editForgotPassword_emailUser.getText().toString());
                Call<DtoUser> resultRequest = userServices.requestPasswordReset(userInfo);
                resultRequest.enqueue(new Callback<DtoUser>() {
                    @Override
                    public void onResponse(@NonNull Call<DtoUser> call, @NonNull Response<DtoUser> response) {
                        if (response.code() == 200 || response.code() == 404){
                            loadingDialog.dimissDialog();
                            ToastHelper.toast(ForgotPasswordActivity.this, getString(R.string.weve_sent_youanEmail_password));
                            finish();
                        }else{
                            loadingDialog.dimissDialog();
                            Warnings.showWeHaveAProblem(ForgotPasswordActivity.this);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<DtoUser> call, @NonNull Throwable t) {
                        loadingDialog.dimissDialog();
                        Warnings.showWeHaveAProblem(ForgotPasswordActivity.this);
                    }
                });
            }
        });

        //  Finish this Activity
        txt_haveAccount_forgotPassword.setOnClickListener(v -> finish());
        arrowGoBackForgotPassword.setOnClickListener(v -> finish());

    }

    private void showError(EditText editText, String error){
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.setError(error);
        cardBtn_ChangePassword.setElevation(20);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}