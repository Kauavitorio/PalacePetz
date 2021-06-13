package co.kaua.palacepetz.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Cards.CardService;
import co.kaua.palacepetz.Data.Cards.DtoCard;
import co.kaua.palacepetz.Methods.MaskEditUtil;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CardRegistrationFragment extends Fragment implements IOnBackPressed {
    private EditText edit_cardNumber_cardRegister, edit_shelf_life_cardRegister, edit_cvv_cardRegister, edit_nameCard_cardRegister;
    private CardView cardBtn_addCard_registration;
    private  InputMethodManager imm;

    //  Cards Flags
    private ImageView img_btnMasterCard, img_btnVisaCard, img_btnMaestroCard;
    private ConstraintLayout cardBtn_MasterCard, cardBtnVisa, cardBtn_Maestro;

    //  Card Pre View
    private CardView cardPreView_register;
    private ImageView imgFlag_Pre_view_Card;
    private Drawable cardFlagSelected;
    private int colorMasterColor;
    private int colorVisaColor;
    private int colorMaestroColor;
    private TextView txt_shelfLife_cardRegister, txt_numberCard_cardRegister, txt_nmCard_cardRegister;

    //  User information
    private int id_user;
    private String name_user, _Email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user;

    //  Card Information
    private static String card_flag, card_number, card_namePrinted, card_shelfLife, card_cvv;

    //  Fragments Arguments
    Bundle args;
    @SuppressWarnings("FieldCanBeLocal")
    private static FragmentTransaction transaction;


    //  Flags Images
    private final String flag_visa = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fvisa.png?alt=media&token=e9dd2e2b-dd30-444e-b745-2a1dd2273db9";
    private final String flag_mastercard = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmastercard.png?alt=media&token=79df43fd-494c-4160-93f1-7194266f76b9";
    private final String flag_maestro = "https://firebasestorage.googleapis.com/v0/b/coffeeforcode.appspot.com/o/cards_flag%2Fmaestro.png?alt=media&token=28fd5789-f277-4027-8b0f-9ff1f38b2d5d";

    //  Retrofit
    String baseurl = "https://palacepetzapi.herokuapp.com/";
    final Retrofit retrofitCard = new Retrofit.Builder()
            .baseUrl( baseurl + "user/register/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activty_card_registration, container, false);
        Ids(view);
        args = getArguments();
        assert args != null;
        _Email = args.getString("email_user");
        id_user = args.getInt("id_user");

        imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Set Mask
        edit_cardNumber_cardRegister.addTextChangedListener(MaskEditUtil.mask(edit_cardNumber_cardRegister, MaskEditUtil.FORMAT_NUMCARD));
        edit_shelf_life_cardRegister.addTextChangedListener(MaskEditUtil.mask(edit_shelf_life_cardRegister, MaskEditUtil.FORMAT_DATAVALI));

        InitialLoading_Flags(cardFlagSelected, colorMasterColor);
        setCardsFlagsClick(cardFlagSelected, colorMasterColor, colorVisaColor, colorMaestroColor);
        setEditText_TextChanged();

        //  When click will try to insert new card on User Account
        cardBtn_addCard_registration.setOnClickListener(v -> {
            cardBtn_addCard_registration.setElevation(0);
            if (card_flag == null || card_flag.equals(" "))
                ToastHelper.toast(requireActivity(), getString(R.string.no_flag_selected));
            else if (card_number == null || card_number.length() < 19){
                showError(edit_cardNumber_cardRegister, getString(R.string.cardNumber_wrongInsert));
            } else if (card_shelfLife == null || card_shelfLife.length() < 5) {
                showError(edit_shelf_life_cardRegister, getString(R.string.shelfLife_incorrectly));
            }else if (card_cvv == null || card_cvv.length() < 3){
                showError(edit_cvv_cardRegister, getString(R.string.cvv_inserted_incorrectly));
            }else if(card_namePrinted == null || card_namePrinted.length() < 5){
                showError(edit_nameCard_cardRegister, getString(R.string.name_incorrectly_inserted));
            }else{
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.startLoading();
                CardService cardService = retrofitCard.create(CardService.class);
                DtoCard cardInfo = new DtoCard(id_user, card_flag, card_number, card_shelfLife, card_cvv, card_namePrinted);
                Call<DtoCard> cardCall = cardService.insertNewCard(cardInfo);
                cardCall.enqueue(new Callback<DtoCard>() {
                    @Override
                    public void onResponse(@NotNull Call<DtoCard> call, @NotNull Response<DtoCard> response) {
                        if (response.code() == 201){
                            loadingDialog.dimissDialog();
                            goTo_allCard();
                        }else if(response.code() == 207){
                            loadingDialog.dimissDialog();
                            ToastHelper.toast(requireActivity(), getString(R.string.you_have_moreOf_theree_cards));
                            goTo_allCard();
                        }else if(response.code() == 409){
                            loadingDialog.dimissDialog();
                            ToastHelper.toast(requireActivity(), getString(R.string.card_already_inserted));
                        }else{
                            loadingDialog.dimissDialog();
                            Warnings.showWeHaveAProblem(getContext());
                            goTo_allCard();
                        }
                    }
                    @Override
                    public void onFailure(@NotNull Call<DtoCard> call, @NotNull Throwable t) {
                        loadingDialog.dimissDialog();
                        cardBtn_addCard_registration.setEnabled(true);
                        cardBtn_addCard_registration.setElevation(20);
                        Warnings.showWeHaveAProblem(getContext());
                        Log.d("ErroNetWork", t.getMessage());
                        goTo_allCard();
                    }
                });
            }
        });

        return view;
    }

    private void goTo_allCard() {
        MyCardsFragment MyCardsFragment = new MyCardsFragment();
        args = new Bundle();
        args.putString("email_user", _Email);
        args.putInt("id_user", id_user);
        MyCardsFragment.setArguments(args);
        transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutMain, MyCardsFragment);
        transaction.commit();
    }

    private void showError(EditText editText, String error){
        editText.setError(error);
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        cardBtn_addCard_registration.setElevation(20);
    }

    private void setEditText_TextChanged() {
        edit_cardNumber_cardRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_cardNumber_cardRegister.getText().length() > 0){
                    String numberCard = edit_cardNumber_cardRegister.getText().toString();
                    card_number = numberCard;
                    txt_numberCard_cardRegister.setText(numberCard);
                }else{
                    txt_numberCard_cardRegister.setText(R.string.ex_numberCard_card);
                    card_number = null;
                }
            }
        });

        edit_shelf_life_cardRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_shelf_life_cardRegister.getText().length() > 0){
                    String shelflife = edit_shelf_life_cardRegister.getText().toString();
                    card_shelfLife = shelflife;
                    txt_shelfLife_cardRegister.setText(shelflife);
                }else{
                    txt_shelfLife_cardRegister.setText(R.string.ex_shelfLife_card);
                    card_shelfLife = null;
                }
            }
        });

        edit_cvv_cardRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_cvv_cardRegister.getText().length() > 0){
                    card_cvv = edit_cvv_cardRegister.getText().toString();
                }else{
                    card_cvv = null;
                }
            }
        });

        edit_nameCard_cardRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_nameCard_cardRegister.getText().length() > 0){
                    String namePrintedCard = edit_nameCard_cardRegister.getText().toString();
                    card_namePrinted = namePrintedCard;
                    txt_nmCard_cardRegister.setText(namePrintedCard);
                }else{
                    txt_nmCard_cardRegister.setText(R.string.fulanodetal);
                    card_namePrinted = null;
                }
            }
        });
    }

    private void InitialLoading_Flags(Drawable cardFlagSelected, int colorMasterColor) {
        Picasso.get().load(flag_mastercard).into(img_btnMasterCard);
        Picasso.get().load(flag_visa).into(img_btnVisaCard);
        Picasso.get().load(flag_maestro).into(img_btnMaestroCard);
        cardBtn_MasterCard.setBackground(cardFlagSelected);
        cardPreView_register.setCardBackgroundColor(colorMasterColor);
        card_flag = "MasterCard";
        Picasso.get().load(flag_mastercard).into(imgFlag_Pre_view_Card);
    }

    private void setCardsFlagsClick(Drawable cardFlagSelected, int colorMasterColor, int colorVisaColor, int colorMaestroColor) {
        cardBtn_MasterCard.setOnClickListener(v -> SetFlags(cardFlagSelected, colorMasterColor, "MasterCard", cardBtn_MasterCard, flag_mastercard));

        cardBtnVisa.setOnClickListener(v -> SetFlags(cardFlagSelected, colorVisaColor, "Visa", cardBtnVisa, flag_visa));

        cardBtn_Maestro.setOnClickListener(v -> SetFlags(cardFlagSelected, colorMaestroColor, "Maestro", cardBtn_Maestro, flag_maestro));
    }

    private void SetFlags(Drawable cardFlagSelected, int colorVisaColor, String flag, ConstraintLayout cardBtnVisa, String flag_visa) {
        removeAllFlagSelected();
        card_flag = flag;
        cardBtnVisa.setBackground(cardFlagSelected);
        cardPreView_register.setCardBackgroundColor(colorVisaColor);
        Picasso.get().load(flag_visa).into(imgFlag_Pre_view_Card);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("UseCompatLoadingForDrawables")
    private void Ids(View view) {
        imgFlag_Pre_view_Card = view.findViewById(R.id.imgFlag_Pre_view_Card);
        cardBtn_MasterCard = view.findViewById(R.id.cardBtn_MasterCard);
        cardBtnVisa = view.findViewById(R.id.cardBtnVisa);
        cardBtn_Maestro = view.findViewById(R.id.cardBtn_Maestro);
        cardPreView_register = view.findViewById(R.id.cardPreView_register);
        img_btnMasterCard = view.findViewById(R.id.img_btnMasterCard);
        img_btnVisaCard = view.findViewById(R.id.img_btnVisaCard);
        img_btnMaestroCard = view.findViewById(R.id.img_btnMaestroCard);
        edit_cardNumber_cardRegister = view.findViewById(R.id.edit_cardNumber_cardRegister);
        edit_shelf_life_cardRegister = view.findViewById(R.id.edit_shelf_life_cardRegister);
        edit_cvv_cardRegister = view.findViewById(R.id.edit_cvv_cardRegister);
        edit_nameCard_cardRegister = view.findViewById(R.id.edit_nameCard_cardRegister);
        txt_nmCard_cardRegister = view.findViewById(R.id.txt_nmCard_cardRegister);
        txt_numberCard_cardRegister = view.findViewById(R.id.txt_numberCard_cardRegister);
        txt_shelfLife_cardRegister = view.findViewById(R.id.txt_shelfLife_cardRegister);
        cardBtn_addCard_registration = view.findViewById(R.id.cardBtn_addCard_registration);
        cardBtn_addCard_registration.setElevation(20);

        cardFlagSelected = requireContext().getResources().getDrawable(R.drawable.card_flag_selected);
        colorMasterColor = requireContext().getResources().getColor(R.color.mastercard);
        colorVisaColor = requireContext().getResources().getColor(R.color.visacard);
        colorMaestroColor = requireContext().getResources().getColor(R.color.maestrocard);
    }

    private void removeAllFlagSelected() {
        //noinspection deprecation
        @SuppressLint("UseCompatLoadingForDrawables") Drawable cardFlagDefault = getResources().getDrawable(R.drawable.default_card_flag);
        cardBtn_MasterCard.setBackground(cardFlagDefault);
        cardBtnVisa.setBackground(cardFlagDefault);
        cardBtn_Maestro.setBackground(cardFlagDefault);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onBackPressed() {
        if (_Email != null) {
            //action not popBackStack
            MainFragment mainFragment = new MainFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putString("email_user", _Email);
            args.putInt("id_user", id_user);
            mainFragment.setArguments(args);
            transaction.replace(R.id.frameLayoutMain, mainFragment);
            transaction.commit();
            return true;
        } else {
            return false;
        }
    }
}
