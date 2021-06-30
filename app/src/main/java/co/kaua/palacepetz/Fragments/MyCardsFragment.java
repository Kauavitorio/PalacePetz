package co.kaua.palacepetz.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Data.Cards.AsyncCards;
import co.kaua.palacepetz.Data.Cards.CardService;
import co.kaua.palacepetz.Data.Cards.DtoCard;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.R;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@SuppressWarnings("ALL")
public class MyCardsFragment extends Fragment implements IOnBackPressed {
    //  Screen Items
    private static RecyclerView recyclerView_Cards;
    private static CardView cardContainer_AddCard, BtnMyCard_AddCard, BtnMyCard_AddNewCard;
    private static ConstraintLayout container_noCard;
    private static FragmentTransaction transaction;
    private static LoadingDialog loadingDialog;

    //  Fragments Arguments
    Bundle args;
    private static MyCardsFragment instante;

    //  User information
    private static int id_user;
    private static String name_user, _Email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user;

    //  Retrofit
    static String baseurl = "https://palacepetzapi.herokuapp.com/";
    static final Retrofit retrofitCard = new Retrofit.Builder()
            .baseUrl( baseurl + "user/cards/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_mycards, container, false);
        Ids(view);
        instante = this;
        cardContainer_AddCard.setElevation(19);
        BtnMyCard_AddCard.setElevation(20);
        BtnMyCard_AddNewCard.setElevation(20);

        args = getArguments();
        assert args != null;
        _Email = args.getString("email_user");
        id_user = args.getInt("id_user");
        getCardsInformation();

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.CheckShoppingCart();

        BtnMyCard_AddCard.setOnClickListener(v -> {
            cardContainer_AddCard.setElevation(0);
            BtnMyCard_AddCard.setElevation(0);
            Goto_CardRegistration();
        });

        BtnMyCard_AddNewCard.setOnClickListener(v -> {
            BtnMyCard_AddNewCard.setElevation(0);
            Goto_CardRegistration();
        });

        return view;
    }

    public static MyCardsFragment getInstance(){ return instante; }

    public static void getCardsInformation() {
        loadingDialog.startLoading();
        CardService cardService = retrofitCard.create(CardService.class);
        Call<DtoCard> cardCall = cardService.getCardsOfUser(id_user);
        cardCall.enqueue(new Callback<DtoCard>() {
            @Override
            public void onResponse(@NotNull Call<DtoCard> call, @NotNull Response<DtoCard> response) {
                loadingDialog.dimissDialog();
                if (response.code() == 412){
                    container_noCard.setVisibility(View.VISIBLE);
                    recyclerView_Cards.setVisibility(View.GONE);
                }else if(response.code() == 200){
                    assert response.body() != null;
                    LinearLayoutManager linearLayout = new LinearLayoutManager(getInstance().requireContext());
                    recyclerView_Cards.setLayoutManager(linearLayout);
                    AsyncCards asyncCards = new AsyncCards(recyclerView_Cards, container_noCard, (Activity) getInstance().requireContext(), id_user);
                    asyncCards.execute();
                    if (response.body().getLength() < 3){
                        BtnMyCard_AddNewCard.setVisibility(View.VISIBLE);
                    }else{
                        BtnMyCard_AddNewCard.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<DtoCard> call, @NotNull Throwable t) {
                loadingDialog.dimissDialog();
                ToastHelper.toast((Activity) getInstance().requireContext(), getInstance().requireContext().getString(R.string.weHaveAProblem));
            }
        });
    }

    private void Goto_CardRegistration() {
        CardRegistrationFragment cardregistrationFragment = new CardRegistrationFragment();
        args = new Bundle();
        args.putString("email_user", _Email);
        args.putInt("id_user", id_user);
        cardregistrationFragment.setArguments(args);
        transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutMain, cardregistrationFragment);
        transaction.commit();
    }

    private void Ids(@NonNull View view) {
        recyclerView_Cards = view.findViewById(R.id.recyclerView_Cards);
        cardContainer_AddCard = view.findViewById(R.id.cardContainer_AddCard);
        BtnMyCard_AddCard = view.findViewById(R.id.BtnMyCard_AddCard);
        BtnMyCard_AddNewCard = view.findViewById(R.id.BtnMyCard_AddNewCard);
        container_noCard = view.findViewById(R.id.container_noCard);
        loadingDialog = new LoadingDialog(getActivity());
    }

    @Override
    public boolean onBackPressed() {
        //action not popBackStack
        requireActivity().getWindow().setNavigationBarColor(requireActivity().getColor(R.color.background_top));
        MainFragment mainFragment = new MainFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("email_user", _Email);
        mainFragment.setArguments(args);
        transaction.replace(R.id.frameLayoutMain, mainFragment);
        transaction.commit();
        return true;
    }
}
