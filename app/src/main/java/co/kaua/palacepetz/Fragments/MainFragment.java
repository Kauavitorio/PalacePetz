package co.kaua.palacepetz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import co.kaua.palacepetz.R;

import java.util.Objects;

public class MainFragment extends Fragment {
    private ConstraintLayout btn_services_shortCut, btn_cards_shortCut, btn_myOrders_shortCut;

    private Bundle args;
    private View view;
    private static FragmentTransaction transaction;

    //  User information
    private static String _Email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_main, container, false);
        Ids();
        args = getArguments();
        assert args != null;
        _Email = args.getString("email_user");
        createShortCutsClick();

        return view;
    }

    private void createShortCutsClick() {

        //  Ser click
        btn_services_shortCut.setOnClickListener(v -> {
            ServicesFragment servicesFragment = new ServicesFragment();
            args = new Bundle();
            args.putString("email_user", _Email);
            servicesFragment.setArguments(args);
            transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutMain, servicesFragment);
            transaction.commit();
        });

        //  My Cards click
        btn_cards_shortCut.setOnClickListener(v -> {
            MyCardsFragment myCardsFragment = new MyCardsFragment();
            args = new Bundle();
            args.putString("email_user", _Email);
            myCardsFragment.setArguments(args);
            transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutMain, myCardsFragment);
            transaction.commit();
        });

        //  My Orders click
        btn_myOrders_shortCut.setOnClickListener(v -> {

        });
    }

    private void Ids() {
        btn_services_shortCut = view.findViewById(R.id.btn_services_shortCut);
        btn_cards_shortCut = view.findViewById(R.id.btn_cards_shortCut);
        btn_myOrders_shortCut = view.findViewById(R.id.btn_myOrders_shortCut);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
