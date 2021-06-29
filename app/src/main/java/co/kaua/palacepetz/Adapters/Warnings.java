package co.kaua.palacepetz.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import co.kaua.palacepetz.Activitys.Developers;
import co.kaua.palacepetz.Activitys.Help.HelpActivity;
import co.kaua.palacepetz.Activitys.LoginActivity;
import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Activitys.Pets.AllPetsActivity;
import co.kaua.palacepetz.Activitys.RegisterAddressActivity;
import co.kaua.palacepetz.Activitys.Services.ScheduledServicesActivity;
import co.kaua.palacepetz.Activitys.SplashScreen;
import co.kaua.palacepetz.Data.Pets.DtoPets;
import co.kaua.palacepetz.Data.Pets.PetsServices;
import co.kaua.palacepetz.Data.Schedule.DtoSchedule;
import co.kaua.palacepetz.Data.Schedule.ScheduleServices;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("FieldCanBeLocal")
public class Warnings extends MainActivity {
    private static Dialog WarningError, Warning_Email_Password,
            warning_badUsername, LogOutDialog, EmployeeWarning, warning_badPetName, PetWarning, NeedLoginWarning;
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
    public static void showEmailIsNotVerified(Activity context) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
        //  Creating View for SheetMenu
        View sheetView = LayoutInflater.from(context).inflate(R.layout.adapter_emailnotverified,
                context.findViewById(R.id.sheet_email_not_verified));

        sheetView.findViewById(R.id.btnOk_WillConfirm).setOnClickListener(v -> bottomSheetDialog.dismiss());


        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    //  Create Method for show alert of email send
    public static void show_WeSendEmail_Warning(Activity activity, String email, String password){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        //  Creating View for SheetMenu
        bottomSheetDialog.setCancelable(false);
        View sheetView = LayoutInflater.from(activity).inflate(R.layout.adapter_wesendemail,
                activity.findViewById(R.id.sheet_wesendemail_menu));

        sheetView.findViewById(R.id.btnIWillConfirm_mainSheet).setOnClickListener(v -> {
            Intent goTo_SingIn = new Intent(activity, LoginActivity.class);
            goTo_SingIn.putExtra("email_user", email);
            goTo_SingIn.putExtra("password_user", password);
            activity.startActivity(goTo_SingIn);
            activity.finish();
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
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
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
        //  Creating View for SheetMenu
        View sheetView = LayoutInflater.from(context).inflate(R.layout.adapter_needlogin_sheet,
                context.findViewById(R.id.sheet_need_login_menu));

        sheetView.findViewById(R.id.btn_loginYes_now).setOnClickListener(v -> {
            SharedPreferences mPrefs;
            mPrefs = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
            mPrefs.edit().clear().apply();
            Intent login = new Intent(context, LoginActivity.class);
            context.startActivity(login);
            context.finish();
            bottomSheetDialog.dismiss();
        });

        sheetView.findViewById(R.id.btn_loginNo_later).setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
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

    public static void showScheduleIsSuccessful(Activity activity) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        //  Creating View for SheetMenu
        View sheetView = LayoutInflater.from(activity).inflate(R.layout.adapter_schedule_confirm,
                activity.findViewById(R.id.sheet_schedule_confirm));
        sheetView.findViewById(R.id.btnOk_OrderConfirmation).setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    //  Cancel Schedule dialog
    public static void CancelSchedule(Context context, int cd_schedule) {
        LogOutDialog = new Dialog(context);
        DtoUser user = MainActivity.getInstance().GetUserBaseInformation();
        DtoSchedule schedule = new DtoSchedule(user.getId_user(), cd_schedule);

        TextView txtMsg_alert, txtPositiveBtn_alert, txtCancel_alert;
        CardView PositiveBtn_alert;
        LogOutDialog.setContentView(R.layout.adapter_comum_alert);
        LogOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtMsg_alert = LogOutDialog.findViewById(R.id.txtMsg_alert);
        txtCancel_alert = LogOutDialog.findViewById(R.id.txtCancel_alert);
        PositiveBtn_alert = LogOutDialog.findViewById(R.id.PositiveBtn_alert);
        txtPositiveBtn_alert = LogOutDialog.findViewById(R.id.txtPositiveBtn_alert);
        txtMsg_alert.setText(context.getString(R.string.really_want_cancel_this_schedule));
        txtPositiveBtn_alert.setText(context.getString(R.string.yes));

        PositiveBtn_alert.setOnClickListener(v -> {
            LogOutDialog.dismiss();
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
            //  Creating View for SheetMenu
            View sheetView = LayoutInflater.from(context).inflate(R.layout.adapter_menu_sheet_cancel_schedule_reason,
                    ((Activity) context).findViewById(R.id.sheet_menu_cancel_schedule_reason));

            sheetView.findViewById(R.id.btn_card_unavailable_for_the_day).setOnClickListener(v2 -> {
                bottomSheetDialog.dismiss();
                schedule.setDescription(context.getString(R.string.i_will_be_unavailable_for_the_day));
                Action_CancelSchedule(context, schedule);
            });

            sheetView.findViewById(R.id.btn_card_i_cant_go_at_the_scheduled_time).setOnClickListener(v2 -> {
                bottomSheetDialog.dismiss();
                schedule.setDescription(context.getString(R.string.i_cant_go_at_the_scheduled_time));
                Action_CancelSchedule(context, schedule);
            });

            sheetView.findViewById(R.id.btn_card_other_reasons).setOnClickListener(v2 -> {
                bottomSheetDialog.dismiss();
                //  Create Desc SheetMenu
                BottomSheetDialog bottomSheetDialog_Desc = new BottomSheetDialog(context, R.style.BottomSheetTheme);
                View sheetView_Desc = LayoutInflater.from(context).inflate(R.layout.adapter_menu_sheet_cancel_schedule_other_reason,
                        ((Activity) context).findViewById(R.id.sheet_menu_cancel_schedule_other_reason));
                EditText editText_reason_desc = sheetView_Desc.findViewById(R.id.editText_reason_desc);

                sheetView_Desc.findViewById(R.id.btn_confirm_cancel_desc).setOnClickListener(v1 -> {
                    bottomSheetDialog_Desc.dismiss();
                    String desc = editText_reason_desc.getText().toString().trim();
                    if(desc.replace(" ", "").length() <= 0)
                        schedule.setDescription("Motivo do cancelamento não foi informado.");
                    else
                        schedule.setDescription(desc);
                    Action_CancelSchedule(context, schedule);
                });

                bottomSheetDialog_Desc.setContentView(sheetView_Desc);
                bottomSheetDialog_Desc.show();
            });


            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();

        });

        txtCancel_alert.setOnClickListener(v -> LogOutDialog.dismiss());

        LogOutDialog.show();
    }

    private static void Action_CancelSchedule(Context context, @NonNull DtoSchedule schedule){
        loadingDialog = new LoadingDialog((Activity) context);
        loadingDialog.startLoading();
        ScheduleServices services = retrofitUser.create(ScheduleServices.class);
        Call<DtoSchedule> call = services.CancelSchedule(schedule.getCd_schedule(), schedule.getId_user(), schedule.getDescription());
        call.enqueue(new Callback<DtoSchedule>() {
            @Override
            public void onResponse(@NonNull Call<DtoSchedule> call, @NonNull Response<DtoSchedule> response) {
                loadingDialog.dimissDialog();
                if(response.code() == 200) {
                    ToastHelper.toast((Activity) context, context.getString(R.string.appointment_canceled_successfully));
                    ScheduledServicesActivity.LoadSchedules();
                }
                else Warnings.showWeHaveAProblem(context);
            }
            @Override
            public void onFailure(@NonNull Call<DtoSchedule> call, @NonNull Throwable t) {
                loadingDialog.dimissDialog();
                Warnings.showWeHaveAProblem(context);
            }
        });
    }

    public static void showAccountDisable(@NonNull Context context){
        mPrefs = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
        mPrefs.edit().clear().apply();
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
        //  Creating View for SheetMenu
        View sheetView = LayoutInflater.from(context).inflate(R.layout.adapter_sheet_account_disable,
                ((Activity)context).findViewById(R.id.sheet_account_disable));
        sheetView.findViewById(R.id.btnOk_account_disable).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    public static void showDevelopers(@NonNull Context context){
        //  Get all SharedPreferences
        mPrefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(sp.getInt("pref_developers", 0) == 0){
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
            //  Creating View for SheetMenu
            bottomSheetDialog.setCancelable(false);
            View sheetView = LayoutInflater.from(context).inflate(R.layout.adapter_sheet_developers,
                    ((Activity)context).findViewById(R.id.sheet_developers));

            sheetView.findViewById(R.id.btnOk_Developers).setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                ApplyDevelopersPref(context, 1);
            });

            sheetView.findViewById(R.id.btnDevelopers).setOnClickListener(v -> {
                Intent i = new Intent(context, Developers.class);
                context.startActivity(i);
                bottomSheetDialog.dismiss();
                ApplyDevelopersPref(context, 1);
            });

            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
        }
    }

    //  Create Show Payment In the place
    public static void PaymentInThePlace(Context context) {
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

        txtMsg_alert.setText(context.getString(R.string.notice_payment_at_the_time));
        txtPositiveBtn_alert.setText(context.getString(R.string.ok));

        PositiveBtn_alert.setOnClickListener(v -> WarningError.dismiss());

        WarningError.show();
    }

    public static void ApplyDevelopersPref(@NonNull Context context, int status){
        mPrefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt("pref_developers", status);
        editor.apply();
    }
}
