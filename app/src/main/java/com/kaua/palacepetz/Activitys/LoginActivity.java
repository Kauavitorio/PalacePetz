package com.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.kaua.palacepetz.Adapters.LoadingDialog;
import com.kaua.palacepetz.R;

public class LoginActivity extends AppCompatActivity {
    //  Login items
    CheckBox checkbox_rememberMe;
    EditText editLogin_emailUser, editLogin_passwordUser;
    String email, password;

    //  Next Activity
    TextView txt_forgot_your_password, txt_SingUp;

    //  Login bottom
    CardView cardBtn_SingIn;

    //  Tools
    LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
    InputMethodManager imm;

    //  Set preferences
    SharedPreferences mPrefs;
    private static final String PREFS_NAME = "myPrefs";
    Handler timer = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Ids();
        cardBtn_SingIn.setElevation(20);
        verifyIfUsersLogged();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        cardBtn_SingIn.setOnClickListener(v -> {
            if (editLogin_emailUser.getText().length() == 0)
                showError(editLogin_emailUser, getString(R.string.email_required));
            else if(editLogin_passwordUser.getText().length() == 0)
                showError(editLogin_passwordUser, getString(R.string.password_required));
            else{
                email = editLogin_emailUser.getText().toString();
                password = editLogin_passwordUser.getText().toString();
                cardBtn_SingIn.setElevation(0);
                cardBtn_SingIn.setEnabled(false);
                loadingDialog.startLoading();
                timer.postDelayed(() -> loadingDialog.dimissDialog(),3000);
            }
        });

        txt_SingUp.setOnClickListener(v -> {
            Intent goTo_SingUp = new Intent(this, CreateAccountActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
            ActivityCompat.startActivity(this, goTo_SingUp, activityOptionsCompat.toBundle());
            finish();
        });
    }

    private void showError(EditText editText, String error){
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.setError(error);
        cardBtn_SingIn.setElevation(20);
    }

    private void Ids() {
        cardBtn_SingIn = findViewById(R.id.cardBtn_SingIn);
        txt_forgot_your_password = findViewById(R.id.txt_forgot_your_password);
        txt_SingUp = findViewById(R.id.txt_SingUp);
        checkbox_rememberMe = findViewById(R.id.checkbox_rememberMe);
        editLogin_emailUser = findViewById(R.id.editLogin_emailUser);
        editLogin_passwordUser = findViewById(R.id.editLogin_passwordUser);
    }

    public void verifyIfUsersLogged() {
        //  Verification of user preference information
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_email") && sp.contains("pref_password")) {
            String emailPref = sp.getString("pref_email", "not found");
            String PassPref = sp.getString("pref_password", "not found");
            checkbox_rememberMe.setChecked(sp.getBoolean("pref_check", true));
            DoLogin(emailPref, PassPref);
        }
    }

    private void DoLogin(String email, String password) {
        loadingDialog.startLoading();

    }
}