package co.kaua.palacepetz.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Adapters.CardsPurchase_Adapter;
import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Cards.CardService;
import co.kaua.palacepetz.Data.Cards.DtoCard;
import co.kaua.palacepetz.Data.Coupons.CouponsServices;
import co.kaua.palacepetz.Data.Coupons.DtoCoupons;
import co.kaua.palacepetz.Data.Purchase.Async_PurchaseCard;
import co.kaua.palacepetz.Data.Purchase.DtoPurchase;
import co.kaua.palacepetz.Data.Purchase.PurchaseServices;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings({"unchecked", "FieldCanBeLocal", "unused"})
public class FinishPurchaseFragment extends Fragment implements IOnBackPressed {
    //  Fragment Tools
    private View view;
    private Bundle args;
    private CardView BtnMyCard_AddCard_FinishPurchase, btnApprove_FinishPurchase;
    private TextView txt_SubTotal_FinishPurchase, txt_Discounts_FinishPurchase, txt_total_FinishPurchase,
            txt_address_FinishPurchase, txt_complement_FinishPurchase;
    private RecyclerView recycler_cards_purchase;
    private ConstraintLayout container_noCard_FinishPurchase;
    private static FragmentTransaction transaction;
    private LoadingDialog loadingDialog;
    private ImageView btnDiscounts;
    private Dialog couponDialog;
    private String Used_Coupon = "";

    //  Set preferences
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "CouponsPrefs";

    //  User information
    private int _IdUser;
    private String name_user, _Email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user;

    //  Purchase Info
    private double SubTotal, Total, Discounts = 0;

    //  Retrofit
    String baseurl = "https://palacepetzapi.herokuapp.com/";
    final Retrofit retrofitCard = new Retrofit.Builder()
            .baseUrl( baseurl + "user/cards/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final Retrofit retrofitOrder = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/order/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_finishpurchase, container, false);
        Ids();
        args = getArguments();
        assert args != null;
        _Email = args.getString("email_user");
        _IdUser = args.getInt("id_user");
        Total = args.getDouble("total_Cart");
        SubTotal = args.getDouble("total_Cart");
        GetUserInformation();

        //  Verification of user preference information
        mPrefs = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences sp = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("discount_total")){
            Used_Coupon = sp.getString("name_tag", "not found");
            Discounts = Double.parseDouble(sp.getString("discount_total", "not found"));
        }
        loadText();

        BtnMyCard_AddCard_FinishPurchase.setOnClickListener(v -> {
            CardRegistrationFragment cardregistrationFragment = new CardRegistrationFragment();
            args = new Bundle();
            args.putString("email_user", _Email);
            args.putInt("id_user", _IdUser);
            cardregistrationFragment.setArguments(args);
            transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutMain, cardregistrationFragment);
            transaction.commit();
        });

        btnApprove_FinishPurchase.setOnClickListener(v -> {
            try {
                int card_id = CardsPurchase_Adapter.getCardId();
                if (card_id != 0){
                    loadingDialog = new LoadingDialog(getActivity());
                    loadingDialog.startLoading();
                    DtoPurchase purchaseInfo = new DtoPurchase(_IdUser, card_id,
                            txt_Discounts_FinishPurchase.getText().toString().replace(getString(R.string.discounts) + ": -R$", ""),
                            Used_Coupon,"R$" + SubTotal, "R$" +  txt_total_FinishPurchase.getText().toString().replace(getString(R.string.total) + ": R$", ""));
                    PurchaseServices services = retrofitOrder.create(PurchaseServices.class);
                    Call<DtoPurchase> call = services.finish_order(purchaseInfo);
                    call.enqueue(new Callback<DtoPurchase>() {
                        @Override
                        public void onResponse(@NonNull Call<DtoPurchase> call, @NonNull Response<DtoPurchase> response) {
                            if (response.code() == 201){
                                loadingDialog.dimissDialog();
                                mPrefs.edit().clear().apply();
                                requireActivity().getWindow().setNavigationBarColor(requireActivity().getColor(R.color.background_top));
                                MyOrdersFragment ordersFragment = new MyOrdersFragment();
                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                Bundle args = new Bundle();
                                args.putString("email_user", _Email);
                                args.putInt("id_user", _IdUser);
                                ordersFragment.setArguments(args);
                                transaction.replace(R.id.frameLayoutMain, ordersFragment);
                                transaction.commit();

                                //  Reloading user cart
                                MainActivity mainActivity = (MainActivity) getActivity();
                                assert mainActivity != null;
                                mainActivity.CheckShoppingCart();

                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetTheme);
                                //  Creating View for SheetMenu
                                View sheetView = LayoutInflater.from(requireContext()).inflate(R.layout.adapter_order_confirm,
                                        getActivity().findViewById(R.id.sheet_order_confirm));
                                sheetView.findViewById(R.id.btnOk_OrderConfirmation).setOnClickListener(v -> bottomSheetDialog.dismiss());
                                bottomSheetDialog.setContentView(sheetView);
                                bottomSheetDialog.show();
                            }else{
                                loadingDialog.dimissDialog();
                                Warnings.showWeHaveAProblem(getContext());
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<DtoPurchase> call, @NonNull Throwable t) {
                            loadingDialog.dimissDialog();
                            Warnings.showWeHaveAProblem(getContext());
                        }
                    });
                }else
                    ToastHelper.toast(requireActivity(), getString(R.string.necessary_select_a_creditCard));
            }catch (Exception ex){
                Log.d("FinishPurchase", ex.toString());
                ToastHelper.toast(requireActivity(), getString(R.string.necessary_select_a_creditCard));
            }
        });

        btnDiscounts.setOnClickListener(v -> showCouponAlert());

        return view;
    }

    private void showCouponAlert() {
        CardView btnApply_Coupon;
        EditText editText_coupon;
        couponDialog.setContentView(R.layout.adapter_apply_cupom);
        couponDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnApply_Coupon = couponDialog.findViewById(R.id.btnApply_Coupon);
        editText_coupon = couponDialog.findViewById(R.id.editText_coupon);
        btnApply_Coupon.setElevation(10);

        btnApply_Coupon.setOnClickListener(v -> {
            if (editText_coupon.getText().toString().length() <= 0)
                ToastHelper.toast(requireActivity(), getString(R.string.coupon_can_not_be_null));
            else{
                //  Verification of user preference information
                mPrefs = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences sp = requireContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                hideKeyboardFrom(requireContext(), view);
                if (sp.contains("discount_total")) {
                    AlertDialog.Builder couponAlert = new AlertDialog.Builder(getContext())
                            .setTitle(R.string.you_already_have_apply_coupon)
                            .setMessage(R.string.msg_coupon_alredyApply)
                            .setPositiveButton(R.string.cancel, null)
                            .setNegativeButton(getString(R.string.dismiss_coupon), (dialog, which) -> {
                                AlertDialog.Builder warning_coupon = new AlertDialog.Builder(getContext())
                                        .setTitle(R.string.warning)
                                        .setMessage(R.string.are_you_sure_doYouWantDeleteCoupon)
                                        .setPositiveButton(R.string.no, null)
                                        .setNegativeButton(R.string.yes, (dialog1, which1) -> ApplyNewCoupon(btnApply_Coupon, editText_coupon));
                                warning_coupon.show();
                            });
                    couponAlert.show();
                    Discounts = Double.parseDouble(sp.getString("discount_total", "not found"));
                    loadText();
                }else
                    ApplyNewCoupon(btnApply_Coupon, editText_coupon);
            }
        });
        couponDialog.show();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void ApplyNewCoupon(CardView btnApply_Coupon, EditText editText_coupon) {
        btnApply_Coupon.setElevation(0);
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading();
        Used_Coupon = editText_coupon.getText().toString().replaceAll("\\s", "").toLowerCase();
        CouponsServices services = retrofitUser.create(CouponsServices.class);
        Call<DtoCoupons> call = services.getCoupon(Used_Coupon, _IdUser);
        call.enqueue(new Callback<DtoCoupons>() {
            @Override
            public void onResponse(@NonNull Call<DtoCoupons> call, @NonNull Response<DtoCoupons> response) {
                if (response.code() == 200){
                    loadingDialog.dimissDialog();
                    assert response.body() != null;
                    Discounts = Double.parseDouble(response.body().getDiscount_total());
                    ToastHelper.toast(requireActivity(), getString(R.string.coupon_successfully_apply));
                    loadText();

                    mPrefs.edit().clear().apply();
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("discount_total", response.body().getDiscount_total());
                    editor.putString("name_tag", Used_Coupon);
                    editor.apply();
                    couponDialog.dismiss();
                }else if(response.code() == 401){
                    loadingDialog.dimissDialog();
                    ToastHelper.toast(requireActivity(), getString(R.string.coupon_used_before));
                    couponDialog.dismiss();
                }
                else if(response.code() == 204){
                    loadingDialog.dimissDialog();
                    ToastHelper.toast(requireActivity(), getString(R.string.coupon_not_exist));
                    couponDialog.dismiss();
                }
                else if (response.code() == 423){
                    loadingDialog.dimissDialog();
                    ToastHelper.toast(requireActivity(), getString(R.string.expired_coupon));
                    couponDialog.dismiss();
                }
                else{
                    loadingDialog.dimissDialog();
                    couponDialog.dismiss();
                    Warnings.showWeHaveAProblem(getContext());
                }
            }
            @Override
            public void onFailure(@NonNull Call<DtoCoupons> call, @NonNull Throwable t) {
                loadingDialog.dimissDialog();
                couponDialog.dismiss();
                Warnings.showWeHaveAProblem(getContext());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadText() {
        double percentage = Discounts / 100.0;
        double valor_final = SubTotal - (percentage * SubTotal);
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
        numberFormat.setMaximumFractionDigits(2);
        txt_SubTotal_FinishPurchase.setText(getString(R.string.subTotal) + ": R$" + SubTotal);
        txt_total_FinishPurchase.setText(getString(R.string.total) + ": R$"  + numberFormat.format(valor_final));
        txt_Discounts_FinishPurchase.setText(getString(R.string.discounts) + ": -R$" + numberFormat.format((percentage * SubTotal)));
    }

    private void Ids() {
        couponDialog = new Dialog(getContext());
        btnApprove_FinishPurchase = view.findViewById(R.id.btnApprove_FinishPurchase);
        container_noCard_FinishPurchase = view.findViewById(R.id.container_noCard_FinishPurchase);
        recycler_cards_purchase = view.findViewById(R.id.recycler_cards_purchase);
        BtnMyCard_AddCard_FinishPurchase = view.findViewById(R.id.BtnMyCard_AddCard_FinishPurchase);
        txt_SubTotal_FinishPurchase = view.findViewById(R.id.txt_SubTotal_FinishPurchase);
        txt_Discounts_FinishPurchase = view.findViewById(R.id.txt_Discounts_FinishPurchase);
        txt_total_FinishPurchase = view.findViewById(R.id.txt_total_FinishPurchase);
        txt_address_FinishPurchase = view.findViewById(R.id.txt_address_FinishPurchase);
        txt_complement_FinishPurchase = view.findViewById(R.id.txt_complement_FinishPurchase);
        btnDiscounts = view.findViewById(R.id.btnDiscounts);
    }

    private void getCardsInformation() {
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading();
        CardService cardService = retrofitCard.create(CardService.class);
        Call<DtoCard> cardCall = cardService.getCardsOfUser(_IdUser);
        cardCall.enqueue(new Callback<DtoCard>() {
            @Override
            public void onResponse(@NotNull Call<DtoCard> call, @NotNull Response<DtoCard> response) {
                loadingDialog.dimissDialog();
                if (response.code() == 412){
                    container_noCard_FinishPurchase.setVisibility(View.VISIBLE);
                    recycler_cards_purchase.setVisibility(View.GONE);
                }else if(response.code() == 200){
                    assert response.body() != null;
                    LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
                    recycler_cards_purchase.setLayoutManager(linearLayout);
                    Async_PurchaseCard async_purchaseCard = new Async_PurchaseCard(recycler_cards_purchase, container_noCard_FinishPurchase,
                            getActivity(), _IdUser);
                    async_purchaseCard.execute();
                    container_noCard_FinishPurchase.setVisibility(View.GONE);
                    recycler_cards_purchase.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(@NotNull Call<DtoCard> call, @NotNull Throwable t) {
                loadingDialog.dimissDialog();
                Warnings.showWeHaveAProblem(getContext());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void GetUserInformation() {
        //  Get User Information
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        DtoUser dtoUserMain = mainActivity.GetUserBaseInformation();
        _Email = dtoUserMain.getEmail();
        name_user = dtoUserMain.getName_user();
        cpf_user = dtoUserMain.getCpf_user();
        address_user = dtoUserMain.getAddress_user();
        complement = dtoUserMain.getComplement();
        zipcode = dtoUserMain.getZipcode();
        phone_user = dtoUserMain.getPhone_user();
        birth_date = dtoUserMain.getBirth_date();
        img_user = dtoUserMain.getImg_user();
        if (address_user == null || address_user.replaceAll("\\s", "").equals("") || zipcode == null
        || zipcode.replaceAll("\\s", "").equals("")){
            GoTo_mainFragment();
            Warnings.show_Address_Alert_Purchase(getContext(), _IdUser, name_user, _Email, cpf_user, address_user, complement,
                    zipcode, phone_user, birth_date, img_user);
        }

        txt_address_FinishPurchase.setText(address_user);
        txt_complement_FinishPurchase.setText(complement + ", " + zipcode);
        getCardsInformation();
    }

    @Override
    public boolean onBackPressed() {
        if (_Email != null) {
            //action not popBackStack
            GoTo_mainFragment();
            return true;
        } else {
            return false;
        }
    }

    private void GoTo_mainFragment() {
        requireActivity().getWindow().setNavigationBarColor(requireActivity().getColor(R.color.background_top));
        MainFragment mainFragment = new MainFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("email_user", _Email);
        args.putInt("id_user", _IdUser);
        mainFragment.setArguments(args);
        transaction.replace(R.id.frameLayoutMain, mainFragment);
        transaction.commit();
    }
}
