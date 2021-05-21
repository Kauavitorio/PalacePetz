package co.kaua.palacepetz.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.ShoppingCart.AsyncShoppingCart;
import co.kaua.palacepetz.Data.ShoppingCart.CartServices;
import co.kaua.palacepetz.Data.ShoppingCart.DtoShoppingCart;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.Objects;

public class ShoppingCartFragment extends Fragment implements IOnBackPressed {
    //  Screen Items
    private ConstraintLayout container_noItemsOnCart, btn_seeProducts_ShoppingCart, containerTotalShoppingCart;
    private RecyclerView RecyclerViewCart;
    private TextView txt_total_shoppingCart, txt_OrderNow_ShoppingCart;
    private SwipeRefreshLayout swipe_recycler_shoppingCart;
    private CardView btnBuy_shoppingCart;
    private static ArrayList<DtoShoppingCart> arrayList;

    //  Fragments Arguments
    Bundle args;


    //  User information
    String _Email;
    int _IdUser;

    private  LoadingDialog loadingDialog;

    //  Firebase / Retrofit
    final Retrofit retrofitCart = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_shoppingcart, container, false);
        Ids(view);
        container_noItemsOnCart.setVisibility(View.GONE);

        args = getArguments();
        assert args != null;
        _Email = args.getString("email_user");
        _IdUser = args.getInt("id_user");
        CheckShoppingCart();

        //  Set button to see all products
        btn_seeProducts_ShoppingCart.setOnClickListener(v -> {
            AllProductsFragment allProductsFragment = new AllProductsFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            args = new Bundle();
            args.putString("email_user", _Email);
            args.putInt("id_user", _IdUser);
            allProductsFragment.setArguments(args);
            transaction.replace(R.id.frameLayoutMain, allProductsFragment);
            transaction.commit();
        });

        swipe_recycler_shoppingCart.setOnRefreshListener(this::LoadCart);

        btnBuy_shoppingCart.setOnClickListener(v -> {
            btnBuy_shoppingCart.setElevation(0);
            Toast.makeText(getActivity(), "Em Desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    public void CheckShoppingCart(){
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading();
        CartServices services = retrofitCart.create(CartServices.class);
        Call<DtoShoppingCart> cartCall = services.getCartUser(_IdUser);
        cartCall.enqueue(new Callback<DtoShoppingCart>() {
            @Override
            public void onResponse(@NonNull Call<DtoShoppingCart> call, @NonNull Response<DtoShoppingCart> response) {
                if (response.code() == 200){
                    loadingDialog.dimissDialog();
                    assert response.body() != null;
                    container_noItemsOnCart.setVisibility(View.GONE);
                    RecyclerViewCart.setVisibility(View.VISIBLE);
                    containerTotalShoppingCart.setVisibility(View.VISIBLE);
                    LoadCart();
                }else if(response.code() == 204){
                    loadingDialog.dimissDialog();
                    container_noItemsOnCart.setVisibility(View.VISIBLE);
                    RecyclerViewCart.setVisibility(View.GONE);
                    containerTotalShoppingCart.setVisibility(View.GONE);
                }else{
                    loadingDialog.dimissDialog();
                    Warnings.showWeHaveAProblem(getContext());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DtoShoppingCart> call, @NonNull Throwable t) {

            }
        });
    }

    private void LoadCart() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerViewCart.setLayoutManager(layoutManager);
        AsyncShoppingCart asyncShoppingCart = new AsyncShoppingCart(RecyclerViewCart, swipe_recycler_shoppingCart, arrayList, _IdUser, getActivity()
        , txt_total_shoppingCart, txt_OrderNow_ShoppingCart);
        //noinspection unchecked
        asyncShoppingCart.execute();
    }

    private void Ids(View view) {
        btn_seeProducts_ShoppingCart = view.findViewById(R.id.btn_seeProducts_ShoppingCart);
        container_noItemsOnCart = view.findViewById(R.id.container_noItemsOnCart);
        RecyclerViewCart = view.findViewById(R.id.RecyclerViewCart);
        containerTotalShoppingCart = view.findViewById(R.id.containerTotalShoppingCart);
        swipe_recycler_shoppingCart = view.findViewById(R.id.swipe_recycler_shoppingCart);
        txt_total_shoppingCart = view.findViewById(R.id.txt_total_shoppingCart);
        txt_OrderNow_ShoppingCart = view.findViewById(R.id.txt_OrderNow_ShoppingCart);
        btnBuy_shoppingCart = view.findViewById(R.id.btnBuy_shoppingCart);
    }

    @Override
    public boolean onBackPressed() {
        if (_Email != null) {
            //action not popBackStack
            return true;
        } else {
            Toast.makeText(getContext(), "False em", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
