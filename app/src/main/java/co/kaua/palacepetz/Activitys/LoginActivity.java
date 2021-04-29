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
import android.os.Handler;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Firebase.ConfFirebase;
import co.kaua.palacepetz.R;

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

    //  Fountain info
    boolean isDevicePre;

    //  Firebase
    FirebaseAnalytics mFirebaseAnalytics;
    private static FirebaseAuth firebaseAuth;

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
            else if (!Patterns.EMAIL_ADDRESS.matcher(editLogin_emailUser.getText()).matches())
                showError(editLogin_emailUser, getString(R.string.informed_email_is_invalid));
            else if(editLogin_passwordUser.getText().length() == 0)
                showError(editLogin_passwordUser, getString(R.string.password_required));
            else {
               email = editLogin_emailUser.getText().toString();
                password = editLogin_passwordUser.getText().toString();
                cardBtn_SingIn.setElevation(0);
                cardBtn_SingIn.setEnabled(false);
                loadingDialog.startLoading();
                DoLogin(email, password);
            }
        });

        txt_SingUp.setOnClickListener(v -> {
            Intent goTo_SingUp = new Intent(this, CreateAccountActivity.class);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext(),R.anim.move_to_left, R.anim.move_to_right);
            ActivityCompat.startActivity(this, goTo_SingUp, activityOptionsCompat.toBundle());
            finish();
        });

        txt_forgot_your_password.setOnClickListener(v -> {
            Intent goTo_ForgotPassowrd = new Intent(this, ForgotPasswordActivity.class);
            startActivity(goTo_ForgotPassowrd);
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
            isDevicePre = sp.getBoolean("isDevicePre", false);
            checkbox_rememberMe.setChecked(sp.getBoolean("pref_check", true));
            DoLogin(emailPref, PassPref);
        }
    }

    private void DoLogin(String email, String password) {
        /*// Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = Conf_Firebase.getFirebaseAnalytics(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, email);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Test of while");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);*/
        loadingDialog.startLoading();

        firebaseAuth = ConfFirebase.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (checkbox_rememberMe.isChecked()){
                    mPrefs.edit().clear().apply();
                    boolean boollsChecked = checkbox_rememberMe.isChecked();
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("pref_email", email);
                    editor.putString("pref_password", password);
                    editor.putBoolean("pref_check", boollsChecked);
                    editor.putBoolean("isDevicePre", isDevicePre);
                    editor.apply();
                    GoToMain(email);
                }else{
                    mPrefs.edit().clear().apply();
                    GoToMain(email);
                }
            }else{
                loadingDialog.dimissDialog();
                Toast.makeText(LoginActivity.this, "Email ou senha incorreto", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void GoToMain(String email){
        Intent goTo_Main = new Intent(this, MainActivity.class);
        goTo_Main.putExtra("email_user", email);
        goTo_Main.putExtra("AddressAlert", true);
        startActivity(goTo_Main);
        finish();
    }
}