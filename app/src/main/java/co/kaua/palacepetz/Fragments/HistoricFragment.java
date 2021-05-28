package co.kaua.palacepetz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.R;

public class HistoricFragment extends Fragment implements IOnBackPressed {
    //  Fragment Tools
    private View view;
    private Bundle args;


    //  User information
    private int _IdUser;
    private String name_user, _Email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_historic, container, false);
        Ids();
        args = getArguments();
        assert args != null;
        _Email = args.getString("email_user");
        _IdUser = args.getInt("id_user");

        return view;
    }

    private void Ids() {

    }

    @Override
    public boolean onBackPressed() {
        if (_Email != null) {
            //action not popBackStack
            requireActivity().getWindow().setNavigationBarColor(requireActivity().getColor(R.color.background_top));
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
