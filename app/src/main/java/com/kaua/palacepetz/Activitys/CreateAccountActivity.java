package com.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.kaua.palacepetz.R;


public class CreateAccountActivity extends AppCompatActivity {
    EditText editLogin_FirstNameUserRegister, editLogin_LastNameUserRegister, editLogin_EmailUserRegister,
    editLogin_PasswordUserRegister, editLogin_ConfirmPasswordUserRegister;
    CardView cardBtn_SingUp;
    String password_base = "", password_confirm = "";
    InputMethodManager imm;


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

        cardBtn_SingUp.setOnClickListener(v -> {
            cardBtn_SingUp.setElevation(0);
            if (editLogin_FirstNameUserRegister.getText() == null || editLogin_FirstNameUserRegister.getText().length() < 3){
                showError(editLogin_FirstNameUserRegister, getString(R.string.necessary_to_insert_the_First_name));
            }else if(editLogin_LastNameUserRegister.getText() == null || editLogin_LastNameUserRegister.getText().length() < 3){
                showError(editLogin_LastNameUserRegister, getString(R.string.necessary_to_insert_the_last_name));
            }
        });

    }

    private void Ids() {
        cardBtn_SingUp = findViewById(R.id.cardBtn_SingUp);
        editLogin_PasswordUserRegister = findViewById(R.id.editLogin_PassowrdUserRegister);
        editLogin_ConfirmPasswordUserRegister = findViewById(R.id.editLogin_ConfirmPasswordUserRegister);
        editLogin_FirstNameUserRegister = findViewById(R.id.editLogin_FirstNameUserRegister);
        editLogin_LastNameUserRegister = findViewById(R.id.editLogin_LastNameUserRegister);
        editLogin_EmailUserRegister = findViewById(R.id.editLogin_EmailUserRegister);
    }

    private void showError(EditText editText, String error){
        editText.setError(error);
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        cardBtn_SingUp.setElevation(20);
    }

    private void checking_password_have_minimum_characters() {
        final TextInputLayout floating_Password_Label = (TextInputLayout) findViewById(R.id.passwordCreateAccount_text_input_layout);
        password_base = editLogin_PasswordUserRegister.getText().toString();
        editLogin_PasswordUserRegister.setBackgroundResource(R.drawable.custom_edittext_base);
        floating_Password_Label.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() > 0 && text.length() < 8) {
                    floating_Password_Label.setError(getString(R.string.minimum_eight_characters));
                    floating_Password_Label.setErrorEnabled(true);
                    checking_if_all_password_is_equal();
                } else {
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
        final TextInputLayout floating_ConfirmPassword_Label = (TextInputLayout) findViewById(R.id.ConfirmpasswordCreateAccount_text_input_layout);
        editLogin_ConfirmPasswordUserRegister.setBackgroundResource(R.drawable.custom_edittext_base);
        floating_ConfirmPassword_Label.getEditText().addTextChangedListener(new TextWatcher() {
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