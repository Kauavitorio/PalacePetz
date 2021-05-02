package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Data.User.UserServices;
import co.kaua.palacepetz.Firebase.ConfFirebase;
import co.kaua.palacepetz.Methods.MaskEditUtil;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Objects;

import static co.kaua.palacepetz.Methods.ValidateCPF.isValidCPF;


public class CreateAccountActivity extends AppCompatActivity {
    private EditText editLogin_FirstNameUserRegister, editLogin_LastNameUserRegister, editRegister_CpfUser,
    editLogin_EmailUserRegister, editLogin_PasswordUserRegister, editLogin_ConfirmPasswordUserRegister;
    private TextView txt_haveAccount;
    private CardView cardBtn_SingUp;
    private String password_base = "", password_confirm = "";
    private InputMethodManager imm;
    private final LoadingDialog loadingDialog = new LoadingDialog(this);
    private Dialog warning_emailSend, WarningError, warning_badUsername;

    //  User Information to Sign Up
    private String firstName, lastName, email, cpf_user, password;

    //  Retrofit / Firebase
    private FirebaseAuth mAuth;
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
        warning_emailSend = new Dialog(this);
        WarningError = new Dialog(this);
        warning_badUsername = new Dialog(this);
        cardBtn_SingUp.setElevation(20);
        checking_password_have_minimum_characters();
        checking_if_all_password_is_equal();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Set Mask
        editRegister_CpfUser.addTextChangedListener(MaskEditUtil.mask(editRegister_CpfUser, MaskEditUtil.FORMAT_CPF));

        txt_haveAccount.setOnClickListener(v -> {
            Intent go_SingIn = new Intent(this, LoginActivity.class);
            startActivity(go_SingIn);
            finish();
        });

        cardBtn_SingUp.setOnClickListener(v -> {
            cardBtn_SingUp.setElevation(0);
            password_base = editLogin_PasswordUserRegister.getText().toString();
            password_confirm = editLogin_ConfirmPasswordUserRegister.getText().toString();

            firstName = editLogin_FirstNameUserRegister.getText().toString().replaceAll(" ", "");
            lastName = editLogin_LastNameUserRegister.getText().toString().replaceAll(" ", "");
            email = editLogin_EmailUserRegister.getText().toString();
            password = editLogin_PasswordUserRegister.getText().toString();

            if (firstName.length() == 0 || editLogin_FirstNameUserRegister.getText().length() < 3){
                showError(editLogin_FirstNameUserRegister, getString(R.string.necessary_to_insert_the_First_name));
            }else if(lastName.length() == 0 || editLogin_LastNameUserRegister.getText().length() < 3){
                showError(editLogin_LastNameUserRegister, getString(R.string.necessary_to_insert_the_last_name));
            }else if(editLogin_EmailUserRegister.getText().length() == 0){
                showError(editLogin_EmailUserRegister, getString(R.string.email_required));
            }else if(!Patterns.EMAIL_ADDRESS.matcher(editLogin_EmailUserRegister.getText()).matches()){
                showError(editLogin_EmailUserRegister, getString(R.string.informed_email_is_invalid));
            }else if(!isValidCPF(editRegister_CpfUser.getText().toString())){
                showError(editRegister_CpfUser, getString(R.string.cpfinformedisInvalid));
            }else if(editLogin_PasswordUserRegister.getText().toString().indexOf(' ') >= 0){
                showError(editLogin_PasswordUserRegister, getString(R.string.password_cannot_contain_spaces));
            }else if (!editLogin_PasswordUserRegister.getText().toString().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")){
                showError(editLogin_PasswordUserRegister, getString(R.string.password_needs));
            }else if(!password_base.equals(password_confirm)){
                showError(editLogin_ConfirmPasswordUserRegister, getString(R.string.passwords_do_not_match));
            }else{
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
                            Log.d("UserStatus", "User successfully registered with the API");
                            mAuth = ConfFirebase.getFirebaseAuth();
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    Log.d("UserStatus", "Create User With Email: success");
                                    Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()){
                                            Log.d("UserStatus", "Email sent.");
                                            Show_WeSendEmail_Warning(email, password);
                                            cardBtn_SingUp.setElevation(20);
                                        }
                                    });
                                }else{
                                    // If sign in fails, display a message to the user.
                                    Log.w("UserStatus", "Create User With Email: failure\n", task.getException());
                                    loadingDialog.dimissDialog();
                                    cardBtn_SingUp.setEnabled(true);
                                    Toast.makeText(CreateAccountActivity.this, R.string.authFailed_thisEmail,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else if(response.code() == 406){
                            loadingDialog.dimissDialog();
                            cardBtn_SingUp.setElevation(20);
                            cardBtn_SingUp.setEnabled(true);
                            Show_BadUsername_Warning();
                        }else if (response.code() == 409){
                            loadingDialog.dimissDialog();
                            cardBtn_SingUp.setElevation(20);
                            Toast.makeText(CreateAccountActivity.this, R.string.authFailed_thisEmail, Toast.LENGTH_SHORT).show();
                            cardBtn_SingUp.setEnabled(true);
                        }else{
                            cardBtn_SingUp.setEnabled(true);
                            loadingDialog.dimissDialog();
                            showRegisterError();
                            cardBtn_SingUp.setElevation(20);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<DtoUser> call, @NonNull Throwable t) {
                        showRegisterError();
                        loadingDialog.dimissDialog();
                        cardBtn_SingUp.setElevation(20);
                        cardBtn_SingUp.setEnabled(true);
                    }
                });
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

    //  Create Method for show alert of email send
    private void Show_WeSendEmail_Warning(String email, String password){
        CardView btnIWillConfirm;
        warning_emailSend.setContentView(R.layout.adapter_wesendemail);
        warning_emailSend.setCancelable(false);
        btnIWillConfirm = warning_emailSend.findViewById(R.id.btnIWillConfirm);
        warning_emailSend.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnIWillConfirm.setOnClickListener(v -> {
            Intent goTo_SingIn = new Intent(CreateAccountActivity.this, LoginActivity.class);
            goTo_SingIn.putExtra("email_user", email);
            goTo_SingIn.putExtra("password_user", password);
            startActivity(goTo_SingIn);
            finish();
        });
        warning_emailSend.show();
    }

    //  Create Method for show alert of bad username
    private void Show_BadUsername_Warning(){
        CardView btnOk_InappropriateUsername;
        warning_badUsername.setContentView(R.layout.adapter_warning_badusername);
        warning_badUsername.setCancelable(false);
        btnOk_InappropriateUsername = warning_badUsername.findViewById(R.id.btnOk_InappropriateUsername);
        warning_badUsername.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnOk_InappropriateUsername.setOnClickListener(v -> warning_badUsername.dismiss());
        warning_badUsername.show();
    }

    private void showError(EditText editText, String error){
        editText.setError(error);
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        cardBtn_SingUp.setElevation(20);
    }

    //  Create method to user see email error
    private void showRegisterError(){
        CardView btnOk_WeHaveAProblem;
        WarningError.setContentView(R.layout.adapter_warning_error_crateaccount);
        WarningError.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnOk_WeHaveAProblem = WarningError.findViewById(R.id.btnOk_WeHaveAProblem);
        btnOk_WeHaveAProblem.setElevation(10);
        WarningError.setCancelable(false);

        btnOk_WeHaveAProblem.setOnClickListener(v -> WarningError.dismiss());

        WarningError.show();
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