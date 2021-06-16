package co.kaua.palacepetz.Activitys.Help;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;

import co.kaua.palacepetz.R;

public class HelpActivity extends AppCompatActivity {
    private CardView btnBackHelp;

    //  Common Question
    private CardView btn_i_do_not_receive_discount, btn_i_want_to_change_account_details, btn_i_want_to_work_at_palace;

    //  Category Buttons
    private CardView btn_account_category, btn_about_palace, btn_about_palace_app, btn_policy_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Ids();
        setCategoryClicks();
        setCommonQuestion();

        btnBackHelp.setOnClickListener(v -> finish());
    }

    private void setCommonQuestion() {
        //  Receive Discount Coupon
        btn_i_do_not_receive_discount.setOnClickListener(v -> SheetMenu_Help.ShowReceiveDiscountSheet(HelpActivity.this));
        //  Change account details
        btn_i_want_to_change_account_details.setOnClickListener(v -> SheetMenu_Help.ShowChangeAccountDetails(HelpActivity.this));
        //  Work at Palace
        btn_i_want_to_work_at_palace.setOnClickListener(v -> SheetMenu_Help.ShowWorkAtPalaceSheet(HelpActivity.this));
    }

    private void setCategoryClicks() {
        //  Account Click
        btn_account_category.setOnClickListener(v -> SheetMenu_Help.AccountSheet.ShowAccountSheet(HelpActivity.this));
    }

    private void Ids() {
        //  Category IDs
        btn_account_category = findViewById(R.id.btn_card_account_category_help);
        btn_about_palace = findViewById(R.id.btn_card_about_palace_category_help);
        btn_about_palace_app = findViewById(R.id.btn_card_about_palace_app_category_help);
        btn_policy_category = findViewById(R.id.btn_card_policy_and_terms_category_help); // End Category IDs

        //  Common Question IDs
        btn_i_do_not_receive_discount = findViewById(R.id.btn_i_do_not_receive_discount);
        btn_i_want_to_change_account_details = findViewById(R.id.btn_i_want_to_change_account_details);
        btn_i_want_to_work_at_palace = findViewById(R.id.btn_i_want_to_work_at_palace); // Common Question IDs

        btnBackHelp = findViewById(R.id.btnBackHelp);
    }
}