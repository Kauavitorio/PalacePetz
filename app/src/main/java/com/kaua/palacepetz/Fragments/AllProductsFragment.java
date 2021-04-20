package com.kaua.palacepetz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kaua.palacepetz.Adapters.IOnBackPressed;
import com.kaua.palacepetz.R;

import java.util.Objects;

public class AllProductsFragment extends Fragment implements IOnBackPressed {

    //  User information
    String email_user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_allproducts, container, false);
        Bundle args = getArguments();
        assert args != null;
        email_user = args.getString("email_user");
        Toast.makeText(getContext(), email_user, Toast.LENGTH_SHORT).show();

        return view;
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
