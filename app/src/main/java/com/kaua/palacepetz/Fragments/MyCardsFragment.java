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
import androidx.recyclerview.widget.RecyclerView;

import com.kaua.palacepetz.Activitys.MainActivity;
import com.kaua.palacepetz.Adapters.IOnBackPressed;
import com.kaua.palacepetz.R;

public class MyCardsFragment extends Fragment implements IOnBackPressed {
    //  Screen Items
    private RecyclerView recyclerView_Cards;
    private CardView cardContainer_AddCard, BtnMyCard_AddCard, BtnMyCard_AddNewCard;

    //  Fragments Arguments
    Bundle args;

    //  User information
    String email_user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_mycards, container, false);
        Ids(view);
        cardContainer_AddCard.setElevation(19);
        BtnMyCard_AddCard.setElevation(20);

        args = getArguments();
        assert args != null;
        email_user = args.getString("email_user");

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.updateCart();

        BtnMyCard_AddCard.setOnClickListener(v1 -> {
            cardContainer_AddCard.setElevation(0);
            BtnMyCard_AddCard.setElevation(0);

        });

        return view;
    }

    private void Ids(View view) {
        recyclerView_Cards = view.findViewById(R.id.recyclerView_Cards);
        cardContainer_AddCard = view.findViewById(R.id.cardContainer_AddCard);
        BtnMyCard_AddCard = view.findViewById(R.id.BtnMyCard_AddCard);
        BtnMyCard_AddNewCard = view.findViewById(R.id.BtnMyCard_AddNewCard);
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
