package co.kaua.palacepetz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.R;

import java.util.Objects;

public class ShoppingCartFragment extends Fragment implements IOnBackPressed {
    //  Screen Items
    ConstraintLayout container_noItemsOnCart, btn_seeProducts_ShoppingCart;

    //  Fragments Arguments
    Bundle args;


    //  User information
    String email_user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_shoppingcart, container, false);
        Ids(view);

        args = getArguments();
        assert args != null;
        email_user = args.getString("email_user");


        btn_seeProducts_ShoppingCart.setOnClickListener(v1 -> {
            AllProductsFragment allProductsFragment = new AllProductsFragment();
            FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            args = new Bundle();
            args.putString("email_user", email_user);
            allProductsFragment.setArguments(args);
            transaction.replace(R.id.frameLayoutMain, allProductsFragment);
            transaction.commit();
        });

        return view;
    }

    private void Ids(View view) {
        btn_seeProducts_ShoppingCart = view.findViewById(R.id.btn_seeProducts_ShoppingCart);
        container_noItemsOnCart = view.findViewById(R.id.container_noItemsOnCart);
    }

    @Override
    public boolean onBackPressed() {
        if (email_user != null) {
            //action not popBackStack
            return true;
        } else {
            Toast.makeText(getContext(), "False em", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
