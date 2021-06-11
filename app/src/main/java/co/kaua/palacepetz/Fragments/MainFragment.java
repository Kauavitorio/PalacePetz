package co.kaua.palacepetz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Products.AsyncPopularProductsMain;
import co.kaua.palacepetz.Data.Products.AsyncProducts_SearchMain;
import co.kaua.palacepetz.Data.Products.DtoProducts;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Methods.CheckSearch;
import co.kaua.palacepetz.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

@SuppressWarnings({"ConstantConditions", "StaticFieldLeak", "unchecked"})
public class MainFragment extends Fragment {
    private ConstraintLayout btn_services_shortCut, btn_allProducts_shortCut, btn_cards_shortCut, btn_myOrders_shortCut;
    private AutoCompleteTextView edit_search;
    private RecyclerView RecyclerPopularProducts;
    private LottieAnimationView loadingPopularProducts;
    private final ArrayList<DtoProducts> arrayListDto = new ArrayList<>();

    private Bundle args;
    private View view;
    private static FragmentTransaction transaction;
    private final ArrayList<String> SuggestionsSearch = new ArrayList<>();
    private String[] SuggestionsString;
    private static MainFragment instance;

    //  User information
    private static int _IdUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_main, container, false);
        Ids();
        instance = this;
        SuggestionsString = new String[] { getContext().getString(R.string.services), getContext().getString(R.string.myCards),
                getContext().getString(R.string.my_shopping_cart), getContext().getString(R.string.myOrders), getContext().getString(R.string.products),
                getContext().getString(R.string.palaceFountain), getContext().getString(R.string.edit_address), getString(R.string.editProfile)};
        SuggestionsSearch.addAll(Arrays.asList(SuggestionsString));

        assert args != null;
        DtoUser user = MainActivity.getInstance().GetUserBaseInformation();
        _IdUser = user.getId_user();
        createShortCutsClick();
        AsyncProducts_SearchMain asyncProductsSearchMain = new AsyncProducts_SearchMain(_IdUser, getActivity());
        asyncProductsSearchMain.execute();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerPopularProducts.setLayoutManager(layoutManager);
        arrayListDto.clear();
        AsyncPopularProductsMain async = new AsyncPopularProductsMain(RecyclerPopularProducts, loadingPopularProducts, arrayListDto, _IdUser, getActivity());
        //noinspection unchecked
        async.execute();

        return view;
    }

    public static MainFragment getInstance() { return instance; }

    public void UpdateSearch(ArrayList<String> list){
        SuggestionsSearch.clear();
        SuggestionsSearch.addAll(Arrays.asList(SuggestionsString));
        SuggestionsSearch.addAll(list);
        edit_search.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, SuggestionsSearch));
    }

    private void createShortCutsClick() {
        //  Ser click
        btn_services_shortCut.setOnClickListener(v -> {
            ServicesFragment servicesFragment = new ServicesFragment();
            args = new Bundle();
            args.putInt("id_user", _IdUser);
            servicesFragment.setArguments(args);
            transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutMain, servicesFragment);
            transaction.commit();
        });

        //  My Cards click
        btn_cards_shortCut.setOnClickListener(v -> {
            if(_IdUser != 0){
                MyCardsFragment myCardsFragment = new MyCardsFragment();
                args = new Bundle();
                args.putInt("id_user", _IdUser);
                myCardsFragment.setArguments(args);
                transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayoutMain, myCardsFragment);
                transaction.commit();
            }else
                Warnings.NeedLoginAlert(requireActivity());
        });

        //  My Orders click
        btn_myOrders_shortCut.setOnClickListener(v -> {
            if (_IdUser != 0){
                MyOrdersFragment myOrdersFragment = new MyOrdersFragment();
                args = new Bundle();
                args.putInt("id_user", _IdUser);
                myOrdersFragment.setArguments(args);
                transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayoutMain, myOrdersFragment);
                transaction.commit();
            }else
                Warnings.NeedLoginAlert(requireActivity());
        });

        //  All Products
        btn_allProducts_shortCut.setOnClickListener(v -> {
            AllProductsFragment productsFragment = new AllProductsFragment();
            args = new Bundle();
            args.putInt("id_user", _IdUser);
            args.putString("search", null);
            productsFragment.setArguments(args);
            transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutMain, productsFragment);
            transaction.commit();
        });

        edit_search.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, SuggestionsSearch));
        edit_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                DoSearch(edit_search.getText().toString().trim());
                return true;
            }
            return false;
        });
    }

    private void DoSearch(@NonNull String searchText) {
        if (!searchText.equals("") || searchText.replace(" ", "").length() > 0){
            edit_search.setText("");
            if (    searchText.equals(getContext().getString(R.string.services)) || searchText.equals(getContext().getString(R.string.myCards)) ||
                    searchText.equals(getContext().getString(R.string.my_shopping_cart)) || searchText.equals(getContext().getString(R.string.myOrders)) ||
                    searchText.equals(getContext().getString(R.string.products)) || searchText.equals(getContext().getString(R.string.palaceFountain)) ||
                    searchText.equals(getContext().getString(R.string.edit_address)) || searchText.equals(getString(R.string.editProfile)))

                CheckSearch.DoSearchCut(searchText, getContext());
            else{
                AllProductsFragment productsFragment = new AllProductsFragment();
                args = new Bundle();
                args.putInt("id_user", _IdUser);
                args.putString("search", searchText);
                productsFragment.setArguments(args);
                transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayoutMain, productsFragment);
                transaction.commit();
            }
        }else
            Toast.makeText(getContext(), getString(R.string.search_cant_be_null), Toast.LENGTH_SHORT).show();
    }

    private void Ids() {
        args = getArguments();
        edit_search = view.findViewById(R.id.edit_Search_Main);
        btn_services_shortCut = view.findViewById(R.id.btn_services_shortCut);
        btn_cards_shortCut = view.findViewById(R.id.btn_cards_shortCut);
        btn_myOrders_shortCut = view.findViewById(R.id.btn_myOrders_shortCut);
        RecyclerPopularProducts = view.findViewById(R.id.RecyclerPopularProducts);
        loadingPopularProducts = view.findViewById(R.id.loadingPopularProducts);
        btn_allProducts_shortCut = view.findViewById(R.id.btn_allProducts_shortCut);
        btn_services_shortCut.setElevation(20);
        btn_cards_shortCut.setElevation(20);
        btn_myOrders_shortCut.setElevation(20);
        btn_allProducts_shortCut.setElevation(20);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity main = (MainActivity) getContext();
        main.CheckShoppingCart();
    }
}
