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
import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Activitys.RegisterAddressActivity;
import co.kaua.palacepetz.R;

public class Warnings {
    private static Dialog WarningError, Warning_Email_Password, warning_emailNotVerified, warning_emailSend,
            warning_badUsername, OrderConfirmation;

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

    //  Create Method to show address alert on purchase
    public static void show_Address_Alert_Purchase(Context context, int _IdUser, String name_user, String _Email,
                                                   String cpf_user, String address_user, String complement, String zipcode, String phone_user,
                                                   String birth_date, String img_user){
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.adapter_address_alert_pruchase);
        CardView btn_registerNow_addressAlert_purchase = dialog.findViewById(R.id.btn_registerNow_addressAlert_purchase);
        CardView btn_registerLatter_purchase = dialog.findViewById(R.id.btn_registerLatter_purchase);
        btn_registerNow_addressAlert_purchase.setElevation(20);
        btn_registerLatter_purchase.setElevation(20);

        //  When click will go to RegisterAddressActivity
        btn_registerNow_addressAlert_purchase.setOnClickListener(v -> {
            btn_registerNow_addressAlert_purchase.setElevation(0);
            Intent goTo_AddressRegister = new Intent(context, RegisterAddressActivity.class);
            goTo_AddressRegister.putExtra("id_user", _IdUser);
            goTo_AddressRegister.putExtra("name_user", name_user);
            goTo_AddressRegister.putExtra("email_user", _Email);
            goTo_AddressRegister.putExtra("cpf_user", cpf_user);
            goTo_AddressRegister.putExtra("address_user", address_user);
            goTo_AddressRegister.putExtra("complement", complement);
            goTo_AddressRegister.putExtra("zipcode", zipcode);
            goTo_AddressRegister.putExtra("phone_user", phone_user);
            goTo_AddressRegister.putExtra("birth_date", birth_date);
            goTo_AddressRegister.putExtra("img_user", img_user);
            context.startActivity(goTo_AddressRegister);
            dialog.dismiss();
        });

        //  When click will dismiss dialog
        btn_registerLatter_purchase.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public static void showOrderConfirmation(Context context){
        OrderConfirmation = new Dialog(context);

        CardView btnOk_OrderConfirmation;
        OrderConfirmation.setContentView(R.layout.adapter_order_confirm);
        OrderConfirmation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnOk_OrderConfirmation = OrderConfirmation.findViewById(R.id.btnOk_OrderConfirmation);
        btnOk_OrderConfirmation.setElevation(10);
        OrderConfirmation.setCancelable(false);

        btnOk_OrderConfirmation.setOnClickListener(v -> OrderConfirmation.dismiss());

        OrderConfirmation.show();
    }
}
