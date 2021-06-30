package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Data.User.UserServices;
import co.kaua.palacepetz.Methods.MaskEditUtil;
import co.kaua.palacepetz.Methods.Notifications;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.Methods.ValidateCPF;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Objects;

import static co.kaua.palacepetz.Methods.ValidateCPF.isValidCPF;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/


public class CreateAccountActivity extends AppCompatActivity {
    private EditText editLogin_FirstNameUserRegister, editLogin_LastNameUserRegister, editRegister_CpfUser,
    editLogin_EmailUserRegister, editLogin_PasswordUserRegister, editLogin_ConfirmPasswordUserRegister;
    private TextView txt_haveAccount;
    private CardView cardBtn_SingUp;
    private String password_base = "", password_confirm = "";
    private InputMethodManager imm;
    private final LoadingDialog loadingDialog = new LoadingDialog(this);

    //  User Information to Sign Up
    @SuppressWarnings("FieldCanBeLocal")
    private String firstName, lastName, email, cpf_user, password;

    //  Retrofit
    private static final String BASE_URL = "https://palacepetzapi.herokuapp.com/";
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Ids();
        cardBtn_SingUp.setElevation(20);
        checking_password_have_minimum_characters();
        checking_if_all_password_is_equal();
        setCpfListener();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Set Mask
        editRegister_CpfUser.addTextChangedListener(MaskEditUtil.mask(editRegister_CpfUser, MaskEditUtil.FORMAT_CPF));

        txt_haveAccount.setOnClickListener(v -> {
            Intent go_SingIn = new Intent(this, LoginActivity.class);
            startActivity(go_SingIn);
            finish();
        });

        //  Edit btn go click
        editLogin_ConfirmPasswordUserRegister.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                SignUp();
                return true;
            }
            return false;
        });

        cardBtn_SingUp.setOnClickListener(v -> SignUp());
    }

    private void SignUp() {
        cardBtn_SingUp.setElevation(0);
        imm.hideSoftInputFromWindow(editLogin_ConfirmPasswordUserRegister.getWindowToken(), 0);
        password_base = editLogin_PasswordUserRegister.getText().toString();
        password_confirm = editLogin_ConfirmPasswordUserRegister.getText().toString();

        firstName = editLogin_FirstNameUserRegister.getText().toString().replaceAll(" ", "");
        lastName = editLogin_LastNameUserRegister.getText().toString().replaceAll(" ", "");
        email = editLogin_EmailUserRegister.getText().toString();
        password = editLogin_PasswordUserRegister.getText().toString();

        if (firstName.length() == 0 || editLogin_FirstNameUserRegister.getText().length() < 3)
            showError(editLogin_FirstNameUserRegister, getString(R.string.necessary_to_insert_the_First_name));
        else if(lastName.length() == 0 || editLogin_LastNameUserRegister.getText().length() < 3)
            showError(editLogin_LastNameUserRegister, getString(R.string.necessary_to_insert_the_last_name));
        else if(editLogin_EmailUserRegister.getText().length() == 0)
            showError(editLogin_EmailUserRegister, getString(R.string.email_required));
        else if(!Patterns.EMAIL_ADDRESS.matcher(editLogin_EmailUserRegister.getText()).matches())
            showError(editLogin_EmailUserRegister, getString(R.string.informed_email_is_invalid));
        else if(!isValidCPF(editRegister_CpfUser.getText().toString()))
            showError(editRegister_CpfUser, getString(R.string.cpfinformedisInvalid));
        else if(editLogin_PasswordUserRegister.getText().toString().indexOf(' ') >= 0)
            showError(editLogin_PasswordUserRegister, getString(R.string.password_cannot_contain_spaces));
        else if (!editLogin_PasswordUserRegister.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"))
            showError(editLogin_PasswordUserRegister, getString(R.string.password_needs));
        else if(!password_base.equals(password_confirm))
            showError(editLogin_ConfirmPasswordUserRegister, getString(R.string.passwords_do_not_match));
        else{
            loadingDialog.startLoading();
            cardBtn_SingUp.setEnabled(false);
            firstName = editLogin_FirstNameUserRegister.getText().toString().replaceAll(" ", "");
            lastName = editLogin_LastNameUserRegister.getText().toString().replaceAll(" ", "");
            email = editLogin_EmailUserRegister.getText().toString();
            cpf_user = editRegister_CpfUser.getText().toString();
            password = editLogin_PasswordUserRegister.getText().toString();
            String fullName_User = firstName + " " + lastName;
            UserServices userServices = retrofitUser.create(UserServices.class);
            DtoUser userInformation = new DtoUser(fullName_User, email, cpf_user, password);
            Call<DtoUser> UserCall = userServices.registerNewUser(userInformation);
            UserCall.enqueue(new Callback<DtoUser>() {
                @Override
                public void onResponse(@NonNull Call<DtoUser> call, @NonNull Response<DtoUser> response) {
                    if(response.code() == 201 || response.code() == 200){

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                            Notifications.show(CreateAccountActivity.this);

                        Log.d("UserStatus", "User successfully registered with the API");
                        Log.d("UserStatus", "Create User With Email: success");
                        Log.d("UserStatus", "Email sent.");
                        Warnings.show_WeSendEmail_Warning(CreateAccountActivity.this, email, password);
                        cardBtn_SingUp.setElevation(20);
                        loadingDialog.dimissDialog();
                    }else if(response.code() == 406){
                        loadingDialog.dimissDialog();
                        cardBtn_SingUp.setElevation(20);
                        cardBtn_SingUp.setEnabled(true);
                        Warnings.show_BadUsername_Warning(CreateAccountActivity.this);
                    }else if (response.code() == 409){
                        loadingDialog.dimissDialog();
                        cardBtn_SingUp.setElevation(20);
                        ToastHelper.toast(CreateAccountActivity.this, getString(R.string.authFailed_thisEmail));
                        cardBtn_SingUp.setEnabled(true);
                    }else if (response.code() == 412){
                        loadingDialog.dimissDialog();
                        cardBtn_SingUp.setElevation(20);
                        ToastHelper.toast(CreateAccountActivity.this, getString(R.string.authFailed_thisCpf));
                        cardBtn_SingUp.setEnabled(true);
                    }else{
                        cardBtn_SingUp.setEnabled(true);
                        loadingDialog.dimissDialog();
                        cardBtn_SingUp.setElevation(20);
                        Warnings.showWeHaveAProblem(CreateAccountActivity.this);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<DtoUser> call, @NonNull Throwable t) {
                    Warnings.showWeHaveAProblem(CreateAccountActivity.this);
                    loadingDialog.dimissDialog();
                    cardBtn_SingUp.setElevation(20);
                    cardBtn_SingUp.setEnabled(true);
                }
            });
        }
    }

    private void setCpfListener() {
        editRegister_CpfUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String cpf_input = editRegister_CpfUser.getText().toString();
                if (cpf_input.length() == 14)
                    if(!ValidateCPF.isValidCPF(cpf_input))
                        showError(editRegister_CpfUser, getString(R.string.cpfinformedisInvalid));
            }
        });
    }

    private void Ids() {
        cardBtn_SingUp = findViewById(R.id.cardBtn_SingUp);
        editLogin_PasswordUserRegister = findViewById(R.id.editRegister_PassowrdUser);
        editLogin_ConfirmPasswordUserRegister = findViewById(R.id.editRegister_ConfirmPasswordUser);
        editLogin_FirstNameUserRegister = findViewById(R.id.editRegister_FirstNameUser);
        editLogin_LastNameUserRegister = findViewById(R.id.editRegister__LastNameUser);
        editLogin_EmailUserRegister = findViewById(R.id.editRegister_EmailUser);
        editRegister_CpfUser = findViewById(R.id.editRegister_CpfUser);
        txt_haveAccount = findViewById(R.id.txt_haveAccount);
    }

    private void showError(@NonNull EditText editText, String error){
        editText.setError(error);
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        cardBtn_SingUp.setElevation(20);
    }

    private void checking_password_have_minimum_characters() {
        final TextInputLayout floating_Password_Label = findViewById(R.id.passwordCreateAccount_text_input_layout);
        password_base = editLogin_PasswordUserRegister.getText().toString();
        editLogin_PasswordUserRegister.setBackgroundResource(R.drawable.custom_edittext_base);
        Objects.requireNonNull(floating_Password_Label.getEditText()).addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (editLogin_PasswordUserRegister.getText().toString().indexOf(' ') >= 0){
                    floating_Password_Label.setError(getString(R.string.password_cannot_contain_spaces));
                    floating_Password_Label.setErrorEnabled(true);
                }else {
                    floating_Password_Label.setErrorEnabled(false);
                }
                if (!editLogin_PasswordUserRegister.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")){
                    floating_Password_Label.setError(getString(R.string.password_needs));
                    floating_Password_Label.setErrorEnabled(true);
                }else {
                    floating_Password_Label.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void checking_if_all_password_is_equal() {
        final TextInputLayout floating_ConfirmPassword_Label = findViewById(R.id.ConfirmpasswordCreateAccount_text_input_layout);
        editLogin_ConfirmPasswordUserRegister.setBackgroundResource(R.drawable.custom_edittext_base);
        Objects.requireNonNull(floating_ConfirmPassword_Label.getEditText()).addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                password_base = editLogin_PasswordUserRegister.getText().toString();
                password_confirm = editLogin_ConfirmPasswordUserRegister.getText().toString();
                if (!password_base.equals(password_confirm)) {
                    floating_ConfirmPassword_Label.setError(getString(R.string.passwords_do_not_match));
                    floating_ConfirmPassword_Label.setErrorEnabled(true);
                } else {
                    floating_ConfirmPassword_Label.setErrorEnabled(false);
                }
                if (editLogin_ConfirmPasswordUserRegister.getText().toString().indexOf(' ') >= 0){
                    floating_ConfirmPassword_Label.setError(getString(R.string.password_cannot_contain_spaces));
                    floating_ConfirmPassword_Label.setErrorEnabled(true);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent goTo_SingIn = new Intent(this, LoginActivity.class);
        startActivity(goTo_SingIn);
        finish();
    }
}