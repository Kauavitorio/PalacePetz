package co.kaua.palacepetz.Activitys.Help;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.MyOrders.DtoOrder;
import co.kaua.palacepetz.Data.MyOrders.MyOrderServices;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HelpActivity extends AppCompatActivity {
    private CardView btnBackHelp;
    private ConstraintLayout container_last_order_help_ac, base_problem_with_this_order_help;
    private LoadingDialog loadingDialog;

    //  Common Question
    private CardView btn_i_do_not_receive_discount, btn_i_want_to_change_account_details, btn_i_want_to_work_at_palace, btn_i_want_to_be_a_supplier;

    //  Category Buttons
    private CardView btn_account_category, btn_about_palace, btn_policy_category;

    //  User information
    private static int id_user;
    private static String nm_user;

    //  Retrofit
    private final Retrofit retrofitBase = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Ids();
        DtoUser userInfo  = MainActivity.getInstance().GetUserBaseInformation();
        id_user = userInfo.getId_user();
        nm_user = userInfo.getName_user();
        if(id_user != 0)
            GetLastOrder();
        else{
            container_last_order_help_ac.setVisibility(View.GONE);
            base_problem_with_this_order_help.setVisibility(View.GONE);
        }
        setCategoryClicks();
        setCommonQuestion();

        btnBackHelp.setOnClickListener(v -> finish());
    }

    private void GetLastOrder() {
        loadingDialog = new LoadingDialog(HelpActivity.this);
        loadingDialog.startLoading();
        container_last_order_help_ac.setVisibility(View.GONE);
        base_problem_with_this_order_help.setVisibility(View.GONE);
        MyOrderServices services = retrofitBase.create(MyOrderServices.class);
        Call<DtoOrder> call = services.getLastOrder(id_user);
        call.enqueue(new Callback<DtoOrder>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<DtoOrder> call, @NonNull Response<DtoOrder> response) {
                loadingDialog.dimissDialog();
                if(response.code() == 404){
                    container_last_order_help_ac.setVisibility(View.GONE);
                    base_problem_with_this_order_help.setVisibility(View.GONE);
                }else if(response.code() == 200){
                    container_last_order_help_ac.setVisibility(View.VISIBLE);
                    base_problem_with_this_order_help.setVisibility(View.VISIBLE);
                    TextView txt_order_id = findViewById(R.id.txt_order_id_help);
                    TextView txt_status = findViewById(R.id.txt_status_order_help_ic);
                    TextView txt_held_in = findViewById(R.id.txt_held_in_order_help_ic);
                    TextView txt_order_status = findViewById(R.id.txt_order_status_help);
                    CardView base_order_status_help = findViewById(R.id.base_order_status_help);
                    assert response.body() != null;
                    txt_order_id.setText(getString(R.string.order) + " #" + response.body().getCd_order());
                    txt_held_in.setText(response.body().getDate_order());
                    txt_order_status.setText(response.body().getStatus());
                    String[] splitNm_User = nm_user.split(" ");
                    switch (response.body().getStatus()) {
                        case "Aguardando Aprovação":
                            txt_status.setText(splitNm_User[0] + ", " + getString(R.string.waiting_for_be_approved));
                            base_order_status_help.setCardBackgroundColor(getColor(R.color.edittext_base));
                            break;
                        case "Preparando Produto":
                            txt_status.setText(splitNm_User[0] + ", " + getString(R.string.we_are_preparing_your_order));
                            base_order_status_help.setCardBackgroundColor(getColor(R.color.background_bottom));
                            break;
                        case "A caminho":
                            txt_status.setText(splitNm_User[0] + ", " + getString(R.string.your_order_is_on_its_way));
                            base_order_status_help.setCardBackgroundColor(getColor(R.color.cards_background));
                            break;
                        case "Entregue":
                            txt_status.setText(splitNm_User[0] + ", " + getString(R.string.your_order_has_been_delivered));
                            base_order_status_help.setCardBackgroundColor(getColor(R.color.elo));
                            break;
                        case "Concluído":
                            txt_status.setText(splitNm_User[0] + ", " + getString(R.string.your_order_has_already_been_completed));
                            base_order_status_help.setCardBackgroundColor(getColor(R.color.order_finish));
                            break;
                    }
                }else
                    Warnings.showWeHaveAProblem(HelpActivity.this);
            }

            @Override
            public void onFailure(@NonNull Call<DtoOrder> call, @NonNull Throwable t) {
                loadingDialog.dimissDialog();
                container_last_order_help_ac.setVisibility(View.GONE);
                base_problem_with_this_order_help.setVisibility(View.GONE);
                Warnings.showWeHaveAProblem(HelpActivity.this);
            }
        });

    }

    private void setCommonQuestion() {
        //  Receive Discount Coupon
        btn_i_do_not_receive_discount.setOnClickListener(v -> SheetMenu_Help.ShowReceiveDiscountSheet(HelpActivity.this));
        //  Change account details
        btn_i_want_to_change_account_details.setOnClickListener(v -> SheetMenu_Help.ShowChangeAccountDetails(HelpActivity.this));
        //  Work at Palace
        btn_i_want_to_work_at_palace.setOnClickListener(v -> SheetMenu_Help.ShowWorkAtPalaceSheet(HelpActivity.this));

        //  Be a supplier click
        btn_i_want_to_be_a_supplier.setOnClickListener(v -> SheetMenu_Help.ShowBeASupplierSheet(HelpActivity.this));
    }

    private void setCategoryClicks() {
        //  Account Click
        btn_account_category.setOnClickListener(v -> SheetMenu_Help.ShowAccountSheet(HelpActivity.this));

        //  About Palace Click
        btn_about_palace.setOnClickListener(v -> SheetMenu_Help.ShowAboutPalaceSheet(HelpActivity.this));

        //  Policy and Terms click
        btn_policy_category.setOnClickListener(v -> SheetMenu_Help.ShowPolicyAndTermsSheet(HelpActivity.this));
    }

    private void Ids() {
        //  Category IDs
        btn_account_category = findViewById(R.id.btn_card_account_category_help);
        btn_about_palace = findViewById(R.id.btn_card_about_palace_category_help);
        btn_policy_category = findViewById(R.id.btn_card_policy_and_terms_category_help); // End Category IDs

        //  Common Question IDs
        btn_i_do_not_receive_discount = findViewById(R.id.btn_i_do_not_receive_discount);
        btn_i_want_to_change_account_details = findViewById(R.id.btn_i_want_to_change_account_details);
        btn_i_want_to_work_at_palace = findViewById(R.id.btn_i_want_to_work_at_palace); // Common Question IDs

        btnBackHelp = findViewById(R.id.btnBackHelp);
        container_last_order_help_ac = findViewById(R.id.container_last_order_help_ac);
        base_problem_with_this_order_help = findViewById(R.id.base_problem_with_this_order_help);
        btn_i_want_to_be_a_supplier = findViewById(R.id.btn_i_want_to_be_a_supplier);
    }
}