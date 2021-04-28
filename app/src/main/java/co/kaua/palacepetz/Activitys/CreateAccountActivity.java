package co.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Methods.MaskEditUtil;
import co.kaua.palacepetz.R;

import java.util.Objects;

import static co.kaua.palacepetz.Methods.ValidateCPF.isValidCPF;


public class CreateAccountActivity extends AppCompatActivity {
    EditText editLogin_FirstNameUserRegister, editLogin_LastNameUserRegister, editRegister_CpfUser,
    editLogin_EmailUserRegister, editLogin_PasswordUserRegister, editLogin_ConfirmPasswordUserRegister;
    TextView txt_haveAccount;
    CardView cardBtn_SingUp;
    String password_base = "", password_confirm = "";
    InputMethodManager imm;
    Handler timer = new Handler();
    LoadingDialog loadingDialog = new LoadingDialog(this);

    //  User Information to Sign Up
    String firstName, lastName, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Ids();
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
                cardBtn_SingUp.setEnabled(false);
                firstName = editLogin_FirstNameUserRegister.getText().toString().replaceAll(" ", "");
                lastName = editLogin_LastNameUserRegister.getText().toString().replaceAll(" ", "");
                email = editLogin_EmailUserRegister.getText().toString();
                password = editLogin_PasswordUserRegister.getText().toString();
                String fullName_User = firstName + " " + lastName;

                loadingDialog.startLoading();
                Toast.makeText(this, "Agora tem que fazer o app registrar kkkk ", Toast.LENGTH_SHORT).show();

                timer.postDelayed(() -> {
                    Intent goTo_SingIn = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(goTo_SingIn);
                    finish();
                },2000);
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

    private void showError(EditText editText, String error){
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