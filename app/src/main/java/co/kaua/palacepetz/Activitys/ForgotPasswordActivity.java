package co.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Firebase.ConfFirebase;
import co.kaua.palacepetz.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText editForgotPassword_emailUser;
    CardView cardBtn_ChangePassword;
    TextView txt_haveAccount_forgotPassword;
    LottieAnimationView arrowGoBackForgotPassword;

    //  Tools
    InputMethodManager imm;
    LoadingDialog loadingDialog;

    //  Retrofit / Firebase
    private static FirebaseAuth firebaseAuth;

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
                firebaseAuth = ConfFirebase.getFirebaseAuth();
                firebaseAuth.sendPasswordResetEmail(editForgotPassword_emailUser.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        loadingDialog.dimissDialog();
                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.weve_sent_youanEmail_password), Toast.LENGTH_LONG).show();
                        finish();
                    }else{
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