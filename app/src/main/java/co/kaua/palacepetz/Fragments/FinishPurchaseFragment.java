package co.kaua.palacepetz.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import co.kaua.palacepetz.Activitys.LoginActivity;
import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Adapters.CardsPurchase_Adapter;
import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Cards.AsyncCards;
import co.kaua.palacepetz.Data.Cards.CardService;
import co.kaua.palacepetz.Data.Cards.DtoCard;
import co.kaua.palacepetz.Data.Purchase.Async_PurchaseCard;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Data.User.UserServices;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@SuppressWarnings("unchecked")
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

    //  User information

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_finishpurchase, container, false);
        Ids();
        GetUserInformation();
        args = getArguments();
        assert args != null;
        _Email = args.getString("email_user");
        _IdUser = args.getInt("id_user");
        Total = args.getDouble("total_Cart");
        SubTotal = args.getDouble("total_Cart");
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
            Toast.makeText(getContext(), CardsPurchase_Adapter.getFlag() + "", Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(), CardsPurchase_Adapter.getNumber() + "", Toast.LENGTH_SHORT).show();
        });

        btnDiscounts.setOnClickListener(v -> {

        });

        return view;
    }
    @SuppressLint("SetTextI18n")
    private void loadText() {
        txt_SubTotal_FinishPurchase.setText(getString(R.string.subTotal) + ": R$" + SubTotal);
        txt_total_FinishPurchase.setText(getString(R.string.total) + ": R$"  + (float)(SubTotal - Discounts));
        txt_Discounts_FinishPurchase.setText(getString(R.string.discounts) + ": -R$" + Discounts);
    }
    private void Ids() {
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
        txt_address_FinishPurchase.setText(address_user);
        txt_complement_FinishPurchase.setText(complement + ", " + zipcode);
        getCardsInformation();
    }

    @Override
    public boolean onBackPressed() {
        if (_Email != null) {
            //action not popBackStack
            MainFragment mainFragment = new MainFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putString("email_user", _Email);
            args.putInt("id_user", _IdUser);
            mainFragment.setArguments(args);
            transaction.replace(R.id.frameLayoutMain, mainFragment);
            transaction.commit();
            return true;
        } else {
            return false;
        }
    }
}
