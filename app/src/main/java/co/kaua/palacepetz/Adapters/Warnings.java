package co.kaua.palacepetz.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import co.kaua.palacepetz.Activitys.LoginActivity;
import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Activitys.Pets.AllPetsActivity;
import co.kaua.palacepetz.Activitys.RegisterAddressActivity;
import co.kaua.palacepetz.Data.Pets.DtoPets;
import co.kaua.palacepetz.Data.Pets.PetsServices;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("FieldCanBeLocal")
public class Warnings extends MainActivity {
    private static Dialog WarningError, Warning_Email_Password, warning_emailNotVerified, warning_emailSend,
            warning_badUsername, OrderConfirmation, LogOutDialog, EmployeeWarning, warning_badPetName, PetWarning, NeedLoginWarning;
    @SuppressLint("StaticFieldLeak")
    private static LoadingDialog loadingDialog;

    //  Set preferences
    private static SharedPreferences mPrefs;

    //  Retrofit
    private final static Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

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

    //  Create Show Logout Message
    public static void LogoutDialog(Activity context, @NonNull BottomSheetDialog bottomSheetDialog) {
        LogOutDialog = new Dialog(context);
        loadingDialog = new LoadingDialog(context);

        TextView txtMsg_alert, txtPositiveBtn_alert, txtCancel_alert;
        CardView PositiveBtn_alert;
        LogOutDialog.setContentView(R.layout.adapter_comum_alert);
        LogOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtMsg_alert = LogOutDialog.findViewById(R.id.txtMsg_alert);
        txtCancel_alert = LogOutDialog.findViewById(R.id.txtCancel_alert);
        PositiveBtn_alert = LogOutDialog.findViewById(R.id.PositiveBtn_alert);
        txtPositiveBtn_alert = LogOutDialog.findViewById(R.id.txtPositiveBtn_alert);
        txtMsg_alert.setText(context.getString(R.string.really_want_logOut));
        txtPositiveBtn_alert.setText(context.getString(R.string.yes));

        PositiveBtn_alert.setOnClickListener(v -> {
            loadingDialog.startLoading();
            mPrefs = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
            mPrefs.edit().clear().apply();
            Intent goBack_toLogin = new Intent(context, LoginActivity.class);
            context.startActivity(goBack_toLogin);
            context.finish();
        });

        txtCancel_alert.setOnClickListener(v -> LogOutDialog.dismiss());

        bottomSheetDialog.dismiss();
        LogOutDialog.show();
    }

    //  Create Show Logout Message
    public static void EmployeeAlert(Activity context) {
        EmployeeWarning = new Dialog(context);

        TextView txtMsg_alert, txtPositiveBtn_alert, txtCancel_alert;
        CardView PositiveBtn_alert;
        EmployeeWarning.setContentView(R.layout.adapter_comum_alert);
        EmployeeWarning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtMsg_alert = EmployeeWarning.findViewById(R.id.txtMsg_alert);
        txtCancel_alert = EmployeeWarning.findViewById(R.id.txtCancel_alert);
        PositiveBtn_alert = EmployeeWarning.findViewById(R.id.PositiveBtn_alert);
        txtPositiveBtn_alert = EmployeeWarning.findViewById(R.id.txtPositiveBtn_alert);
        txtMsg_alert.setText(context.getString(R.string.employee_warning));
        txtPositiveBtn_alert.setText(context.getString(R.string.ok));
        txtCancel_alert.setVisibility(View.GONE);

        PositiveBtn_alert.setOnClickListener(v -> EmployeeWarning.dismiss());

        EmployeeWarning.show();
    }

    //  Create Method for show alert of bad pet Name
    public static void show_BadPetName_Warning(Context context){
        warning_badPetName = new Dialog(context);
        CardView btnOk_InappropriateUsername;
        TextView txt_wehaveAProblemAlert;
        warning_badPetName.setContentView(R.layout.adapter_warning_badusername);
        warning_badPetName.setCancelable(false);
        btnOk_InappropriateUsername = warning_badPetName.findViewById(R.id.btnOk_InappropriateUsername);
        txt_wehaveAProblemAlert = warning_badPetName.findViewById(R.id.txt_wehaveAProblemAlert);
        txt_wehaveAProblemAlert.setText(R.string.inappropriatePetName);

        warning_badPetName.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnOk_InappropriateUsername.setOnClickListener(v -> warning_badPetName.dismiss());
        warning_badPetName.show();
    }

    //  Create Show Delete Pet
    public static void DeletePetAlert(Activity context, int cd_animal, int id_user) {
        PetWarning = new Dialog(context);

        TextView txtMsg_alert, txtPositiveBtn_alert, txtCancel_alert;
        CardView PositiveBtn_alert;
        PetWarning.setContentView(R.layout.adapter_comum_alert);
        PetWarning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtMsg_alert = PetWarning.findViewById(R.id.txtMsg_alert);
        txtCancel_alert = PetWarning.findViewById(R.id.txtCancel_alert);
        PositiveBtn_alert = PetWarning.findViewById(R.id.PositiveBtn_alert);
        txtPositiveBtn_alert = PetWarning.findViewById(R.id.txtPositiveBtn_alert);
        txtMsg_alert.setText(context.getString(R.string.want_delete_this_pet));
        txtPositiveBtn_alert.setText(context.getString(R.string.yes));

        PositiveBtn_alert.setOnClickListener(v -> {
            loadingDialog = new LoadingDialog(context);
            loadingDialog.startLoading();
            PetsServices petsServices = retrofitUser.create(PetsServices.class);
            Call<DtoPets> call = petsServices.DeletePet(cd_animal, id_user);
            call.enqueue(new Callback<DtoPets>() {
                @Override
                public void onResponse(@NonNull Call<DtoPets> call, @NonNull Response<DtoPets> response) {
                    loadingDialog.dimissDialog();
                    PetWarning.dismiss();
                    if (response.code() == 200){
                        context.finish();
                        Intent i = new Intent(context, AllPetsActivity.class);
                        i.putExtra("id_user", id_user);
                        context.startActivity(i);
                        PetWarning.dismiss();
                    }else
                        showWeHaveAProblem(context);
                }
                @Override
                public void onFailure(@NonNull Call<DtoPets> call, @NonNull Throwable t) {
                    loadingDialog.dimissDialog();
                    showWeHaveAProblem(context);
                    PetWarning.dismiss();
                }
            });

        });

        txtCancel_alert.setOnClickListener(v -> PetWarning.dismiss());

        PetWarning.show();
    }

    //  Create Show Need Login Message
    public static void NeedLoginAlert(Activity context) {
        NeedLoginWarning = new Dialog(context);
        SharedPreferences mPrefs;
        mPrefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        TextView txtMsg_alert, txtPositiveBtn_alert, txtCancel_alert;
        CardView PositiveBtn_alert;
        NeedLoginWarning.setContentView(R.layout.adapter_comum_alert);
        NeedLoginWarning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtMsg_alert = NeedLoginWarning.findViewById(R.id.txtMsg_alert);
        txtCancel_alert = NeedLoginWarning.findViewById(R.id.txtCancel_alert);
        PositiveBtn_alert = NeedLoginWarning.findViewById(R.id.PositiveBtn_alert);
        txtPositiveBtn_alert = NeedLoginWarning.findViewById(R.id.txtPositiveBtn_alert);
        PositiveBtn_alert.setElevation(20);

        txtMsg_alert.setText(context.getString(R.string.need_login_msg));
        txtCancel_alert.setText(context.getString(R.string.no));
        txtPositiveBtn_alert.setText(context.getString(R.string.yes));

        PositiveBtn_alert.setOnClickListener(v -> {
            PositiveBtn_alert.setElevation(0);
            mPrefs.edit().clear().apply();
            Intent login = new Intent(context, LoginActivity.class);
            context.startActivity(login);
            context.finish();
            NeedLoginWarning.dismiss();
        });

        txtCancel_alert.setOnClickListener(c -> NeedLoginWarning.dismiss());

        NeedLoginWarning.show();
    }

    //  Create Show Need Login Message but with shortCut
    public static void NeedLoginWithShortCutAlert(Activity context, int shortCutId) {
        NeedLoginWarning = new Dialog(context);
        SharedPreferences mPrefs;
        mPrefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        TextView txtMsg_alert, txtPositiveBtn_alert, txtCancel_alert;
        CardView PositiveBtn_alert;
        NeedLoginWarning.setContentView(R.layout.adapter_comum_alert);
        NeedLoginWarning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtMsg_alert = NeedLoginWarning.findViewById(R.id.txtMsg_alert);
        txtCancel_alert = NeedLoginWarning.findViewById(R.id.txtCancel_alert);
        PositiveBtn_alert = NeedLoginWarning.findViewById(R.id.PositiveBtn_alert);
        txtPositiveBtn_alert = NeedLoginWarning.findViewById(R.id.txtPositiveBtn_alert);
        PositiveBtn_alert.setElevation(20);

        txtMsg_alert.setText(context.getString(R.string.need_login_msg));
        txtCancel_alert.setText(context.getString(R.string.no));
        txtPositiveBtn_alert.setText(context.getString(R.string.yes));

        PositiveBtn_alert.setOnClickListener(v -> {
            PositiveBtn_alert.setElevation(0);
            mPrefs.edit().clear().apply();
            Intent login = new Intent(context, LoginActivity.class);
            login.putExtra("shortcut", shortCutId);
            context.startActivity(login);
            context.finish();
            NeedLoginWarning.dismiss();
        });

        txtCancel_alert.setOnClickListener(c -> NeedLoginWarning.dismiss());

        NeedLoginWarning.show();
    }

    //  Create Show Register Birth Message
    public static void RegisterBirthAlert(Activity context) {
        PetWarning = new Dialog(context);

        TextView txtMsg_alert, txtPositiveBtn_alert, txtCancel_alert;
        CardView PositiveBtn_alert;
        PetWarning.setContentView(R.layout.adapter_comum_alert);
        PetWarning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtMsg_alert = PetWarning.findViewById(R.id.txtMsg_alert);
        txtCancel_alert = PetWarning.findViewById(R.id.txtCancel_alert);
        PositiveBtn_alert = PetWarning.findViewById(R.id.PositiveBtn_alert);
        txtPositiveBtn_alert = PetWarning.findViewById(R.id.txtPositiveBtn_alert);
        PositiveBtn_alert.setElevation(20);

        txtMsg_alert.setText(context.getString(R.string.need_register_birth));
        txtCancel_alert.setText(context.getString(R.string.no));
        txtPositiveBtn_alert.setText(context.getString(R.string.yes));

        PositiveBtn_alert.setOnClickListener(v -> context.finish());

        txtCancel_alert.setOnClickListener(c -> PetWarning.dismiss());

        PetWarning.show();
    }

    //  Create Show Age Alert Message
    public static void AgeAlert(Activity context) {
        PetWarning = new Dialog(context);

        TextView txtMsg_alert, txtPositiveBtn_alert, txtCancel_alert;
        CardView PositiveBtn_alert;
        PetWarning.setContentView(R.layout.adapter_comum_alert);
        PetWarning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtMsg_alert = PetWarning.findViewById(R.id.txtMsg_alert);
        txtCancel_alert = PetWarning.findViewById(R.id.txtCancel_alert);
        PositiveBtn_alert = PetWarning.findViewById(R.id.PositiveBtn_alert);
        txtPositiveBtn_alert = PetWarning.findViewById(R.id.txtPositiveBtn_alert);
        PositiveBtn_alert.setElevation(20);
        txtCancel_alert.setVisibility(View.GONE);

        txtMsg_alert.setText(context.getString(R.string.to_register_a_pet_need_be_biggest_18));
        txtPositiveBtn_alert.setText(context.getString(R.string.ok));

        PositiveBtn_alert.setOnClickListener(v -> PetWarning.dismiss());

        PetWarning.show();
    }

    //  Create Show Product Not Fount Message
    public static void ProductNotFoundAlert(Context context) {
        WarningError = new Dialog(context);

        TextView txtMsg_alert, txtPositiveBtn_alert, txtCancel_alert;
        CardView PositiveBtn_alert;
        WarningError.setContentView(R.layout.adapter_comum_alert);
        WarningError.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtMsg_alert = WarningError.findViewById(R.id.txtMsg_alert);
        txtCancel_alert = WarningError.findViewById(R.id.txtCancel_alert);
        PositiveBtn_alert = WarningError.findViewById(R.id.PositiveBtn_alert);
        txtPositiveBtn_alert = WarningError.findViewById(R.id.txtPositiveBtn_alert);
        PositiveBtn_alert.setElevation(20);
        txtCancel_alert.setVisibility(View.GONE);

        txtMsg_alert.setText(context.getString(R.string.product_not_fount));
        txtPositiveBtn_alert.setText(context.getString(R.string.ok));

        PositiveBtn_alert.setOnClickListener(v -> WarningError.dismiss());

        WarningError.show();
    }
}
