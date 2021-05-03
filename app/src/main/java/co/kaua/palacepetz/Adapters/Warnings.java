package co.kaua.palacepetz.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.cardview.widget.CardView;

import co.kaua.palacepetz.Activitys.CreateAccountActivity;
import co.kaua.palacepetz.Activitys.LoginActivity;
import co.kaua.palacepetz.R;

public class Warnings {
    private static Dialog WarningError, Warning_Email_Password, warning_emailNotVerified, warning_emailSend,
            warning_badUsername;

    public static void showWeHaveAProblem(Context context){
        WarningError = new Dialog(context);

        CardView btnOk_WeHaveAProblem;
        WarningError.setContentView(R.layout.adapter_warning_error_crateaccount);
        WarningError.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnOk_WeHaveAProblem = WarningError.findViewById(R.id.btnOk_WeHaveAProblem);
        btnOk_WeHaveAProblem.setElevation(10);
        WarningError.setCancelable(false);

        btnOk_WeHaveAProblem.setOnClickListener(v -> WarningError.dismiss());

        WarningError.show();
    }

    //  Method to show Alert for email and password is wrong
    public static void showWarning_Email_Password(Context context){
        Warning_Email_Password = new Dialog(context);
        CardView btnOk_InvalidEmailOrPassword;
        Warning_Email_Password.setContentView(R.layout.adapter_warning_emailorpassoword);
        Warning_Email_Password.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnOk_InvalidEmailOrPassword = Warning_Email_Password.findViewById(R.id.btnOk_InvalidEmailOrPassword);

        btnOk_InvalidEmailOrPassword.setOnClickListener(v -> Warning_Email_Password.dismiss());

        Warning_Email_Password.show();
    }

    //  Create Method for show alert of email not verified
    public static void showEmailIsNotVerified(Context context) {
        warning_emailNotVerified = new Dialog(context);

        CardView btnIWillConfirmLogin;
        warning_emailNotVerified.setContentView(R.layout.adapter_emailnotverified);
        warning_emailNotVerified.setCancelable(false);
        warning_emailNotVerified.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnIWillConfirmLogin = warning_emailNotVerified.findViewById(R.id.btnIwillConfirmLogin);


        btnIWillConfirmLogin.setOnClickListener(v -> warning_emailNotVerified.dismiss());
        warning_emailNotVerified.show();
    }



    //  Create Method for show alert of email send
    public static void show_WeSendEmail_Warning(Activity activity, String email, String password){
        warning_emailSend = new Dialog(activity);
        CardView btnIWillConfirm;
        warning_emailSend.setContentView(R.layout.adapter_wesendemail);
        warning_emailSend.setCancelable(false);
        btnIWillConfirm = warning_emailSend.findViewById(R.id.btnIWillConfirm);
        warning_emailSend.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnIWillConfirm.setOnClickListener(v -> {
            Intent goTo_SingIn = new Intent(activity, LoginActivity.class);
            goTo_SingIn.putExtra("email_user", email);
            goTo_SingIn.putExtra("password_user", password);
            activity.startActivity(goTo_SingIn);
            activity.finish();
        });
        warning_emailSend.show();
    }



    //  Create Method for show alert of bad username
    public static void show_BadUsername_Warning(Context context){
        warning_badUsername = new Dialog(context);
        CardView btnOk_InappropriateUsername;
        warning_badUsername.setContentView(R.layout.adapter_warning_badusername);
        warning_badUsername.setCancelable(false);
        btnOk_InappropriateUsername = warning_badUsername.findViewById(R.id.btnOk_InappropriateUsername);
        warning_badUsername.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnOk_InappropriateUsername.setOnClickListener(v -> warning_badUsername.dismiss());
        warning_badUsername.show();
    }
}
