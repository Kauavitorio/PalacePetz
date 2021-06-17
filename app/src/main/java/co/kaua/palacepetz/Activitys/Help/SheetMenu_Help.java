package co.kaua.palacepetz.Activitys.Help;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.R;

public class SheetMenu_Help extends HelpActivity{

    public static void ShowReceiveDiscountSheet(Activity activity){
        BottomSheetDialog bottomSheetDialog = GenerateBaseSheet(activity, activity.getString(R.string.i_do_not_receive_discount_coupon),
                activity.getString(R.string.desc_do_not_receive_discount));
        bottomSheetDialog.show();
    }

    public static void ShowWorkAtPalaceSheet(Activity activity){
        BottomSheetDialog bottomSheetDialog = GenerateBaseSheet(activity, activity.getString(R.string.i_want_to_work_at_palace),
                activity.getString(R.string.desc_work_at_palace));
        bottomSheetDialog.show();
    }

    public static void ShowBeASupplierSheet(Activity activity){
        BottomSheetDialog bottomSheetDialog = GenerateBaseSheet(activity, activity.getString(R.string.i_want_to_be_a_supplier),
                activity.getString(R.string.desc_be_a_supplier));
        bottomSheetDialog.show();
    }

    public static void ShowChangeAccountDetails(Activity activity){
        DtoUser user = MainActivity.getInstance().GetUserBaseInformation();
        if (user.getId_user() != 0){
            MainActivity.getInstance().OpenProfile();
            activity.finish();
        }
        else
            Warnings.NeedLoginAlert(activity);
    }

    public static void ShowAccountSheet(Activity activity){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        //  Creating View for SheetMenu
        View sheetView = LayoutInflater.from(activity).inflate(R.layout.adapter_menu_sheet_account_help,
                activity.findViewById(R.id.sheet_menu_account_help));

        //  Close sheet click
        sheetView.findViewById(R.id.btn_close_sheet_account_help).setOnClickListener(v -> bottomSheetDialog.dismiss());

        //  Account Has Hacked click
        sheetView.findViewById(R.id.btn_card_my_account_has_been_hacked_sheet_help).setOnClickListener(v -> {
            BottomSheetDialog Dialog = GenerateBaseSheet(activity, activity.getString(R.string.my_account_has_been_hacked),
                    activity.getString(R.string.desc_my_account_has_been_hacked));
            Dialog.show();
            bottomSheetDialog.dismiss();
        });

        //  Change Profile Data click
        sheetView.findViewById(R.id.btn_card_i_want_to_change_account_data_sheet_help).setOnClickListener(v -> {
            DtoUser user = MainActivity.getInstance().GetUserBaseInformation();
            if (user.getId_user() != 0){
                MainActivity.getInstance().OpenProfile();
                activity.finish();
            }
            else
                Warnings.NeedLoginAlert(activity);
            bottomSheetDialog.dismiss();
        });

        //  I want to delete my account click
        sheetView.findViewById(R.id.btn_card_i_want_to_delete_my_account_sheet_help).setOnClickListener(v -> {
            BottomSheetDialog Dialog = GenerateBaseSheet(activity, activity.getString(R.string.i_want_to_delete_my_account),
                    activity.getString(R.string.desc_delete_account));
            Dialog.show();
            bottomSheetDialog.dismiss();
        });

        //  Change Password click
        sheetView.findViewById(R.id.btn_card_change_my_password_help_sheet_account).setOnClickListener(v -> {
            BottomSheetDialog Dialog = GenerateBaseSheet(activity, activity.getString(R.string.i_want_to_change_my_password),
                    activity.getString(R.string.desc_i_want_to_change_my_password));
            Dialog.show();
            bottomSheetDialog.dismiss();
        });

        //  Security tips
        sheetView.findViewById(R.id.btn_card_security_tips_sheet_account).setOnClickListener(v -> {
            BottomSheetDialog Dialog = GenerateBaseSheet(activity, activity.getString(R.string.security_tips),
                    activity.getString(R.string.desc_security_tips));
            Dialog.show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    public static void ShowAboutPalaceSheet(Activity activity){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        //  Creating View for SheetMenu
        View sheetView = LayoutInflater.from(activity).inflate(R.layout.adapter_menu_sheet_about_palace_help,
                activity.findViewById(R.id.sheet_menu_about_palace_help));

        //  Close sheet click
        sheetView.findViewById(R.id.btn_close_sheet_about_palace_help).setOnClickListener(v -> bottomSheetDialog.dismiss());

        //  Account Has Hacked click
        sheetView.findViewById(R.id.btn_card_who_are_we_help).setOnClickListener(v -> {
            BottomSheetDialog Dialog = GenerateBaseSheet(activity, activity.getString(R.string.who_are_we),
                    activity.getString(R.string.desc_who_are_we));
            Dialog.show();
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    public static void ShowPolicyAndTermsSheet(Activity activity){
        BottomSheetDialog bottomSheetDialog = GenerateBaseSheet(activity, activity.getString(R.string.policy_and_terms),
                activity.getString(R.string.desc_policy_and_terms));
        bottomSheetDialog.show();
    }

    @NonNull
    private static BottomSheetDialog GenerateBaseSheet(Activity activity, String title, String description){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        //  Creating View for SheetMenu
        View sheetView = LayoutInflater.from(activity).inflate(R.layout.adapter_menu_sheet_base_descriptions_alert_help,
                activity.findViewById(R.id.sheet_menu_receive_base_descriptions_help));
        TextView txtTitleDesc = sheetView.findViewById(R.id.txt_title_desc_base_help_sheet);
        TextView txtDesc = sheetView.findViewById(R.id.txt_desc_base_help_sheet);

        //  Set Sheet Title
        txtTitleDesc.setText(title);
        //  Set Desc Sheet
        txtDesc.setText(description);
        //  Set Dismiss Click
        sheetView.findViewById(R.id.btn_ok_sheet_base_description_help).setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.setContentView(sheetView);
        return bottomSheetDialog;
    }
}
