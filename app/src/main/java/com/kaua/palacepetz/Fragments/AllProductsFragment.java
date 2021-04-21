package com.kaua.palacepetz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.kaua.palacepetz.Adapters.IOnBackPressed;
import com.kaua.palacepetz.Data.Products.AsyncProducts;
import com.kaua.palacepetz.Data.Products.DtoMenu;
import com.kaua.palacepetz.R;

import java.util.ArrayList;
import java.util.Objects;

public class AllProductsFragment extends Fragment implements IOnBackPressed {
    //  Fragment Tools
    View view;
    private RecyclerView recyclerView_Products;
    private SwipeRefreshLayout SwipeRefreshProducts;
    private LottieAnimationView anim_loading_allProducts;

    //  User information
    String email_user;

    //  Filter Tools
    private CardView card_filter_lowestPrice, card_filter_biggestPrice, card_filter_popular;
    ArrayList<DtoMenu> arrayListDto = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_allproducts, container, false);
        Ids();
        Bundle args = getArguments();
        assert args != null;
        email_user = args.getString("email_user");
        loadAllProducts();

        SwipeRefreshProducts.setOnRefreshListener(this::loadAllProducts);

        //  When click here will filter all products by lowest price
        card_filter_lowestPrice.setOnClickListener(v -> {
            setFilterElevation();
            card_filter_lowestPrice.setElevation(0);
        });

        //  When click here will filter all products by bigger price
        card_filter_biggestPrice.setOnClickListener(v -> {
            setFilterElevation();
            card_filter_biggestPrice.setElevation(0);
        });

        //  When click here will filter all products by popular
        card_filter_popular.setOnClickListener(v -> {
            setFilterElevation();
            card_filter_popular.setElevation(0);
        });


        return view;
    }

    private void loadAllProducts() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager (2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_Products.setLayoutManager(layoutManager);
        arrayListDto.clear();
        AsyncProducts asyncProducts = new AsyncProducts(recyclerView_Products, SwipeRefreshProducts, anim_loading_allProducts, arrayListDto, email_user, getActivity());
        //noinspection unchecked
        asyncProducts.execute();
    }

    private void setFilterElevation() {
        card_filter_lowestPrice.setElevation(20);
        card_filter_biggestPrice.setElevation(20);
        card_filter_popular.setElevation(20);
    }

    private void Ids() {
        card_filter_lowestPrice = view.findViewById(R.id.card_filter_lowestPrice);
        card_filter_biggestPrice = view.findViewById(R.id.card_filter_biggestPrice);
        card_filter_popular = view.findViewById(R.id.card_filter_popular);
        recyclerView_Products = view.findViewById(R.id.recyclerView_Products);
        SwipeRefreshProducts = view.findViewById(R.id.SwipeRefreshProducts);
        anim_loading_allProducts = view.findViewById(R.id.anim_loading_allProducts);
        setFilterElevation();
    }

    @Override
    public boolean onBackPressed() {
        if (email_user != null) {
            //action not popBackStack
            MainFragment mainFragment = new MainFragment();
            FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putString("email_user", email_user);
            mainFragment.setArguments(args);
            transaction.replace(R.id.frameLayoutMain, mainFragment);
            transaction.commit();
            return true;
        } else {
            Toast.makeText(getContext(), "False em", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
