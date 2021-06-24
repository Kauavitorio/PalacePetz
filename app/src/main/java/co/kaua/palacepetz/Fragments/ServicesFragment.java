package co.kaua.palacepetz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import co.kaua.palacepetz.Activitys.Services.ScheduleAppointmentActivity;
import co.kaua.palacepetz.Activitys.Services.ScheduleBathAndTosaActivity;
import co.kaua.palacepetz.Activitys.Services.ScheduledServicesActivity;
import co.kaua.palacepetz.Adapters.IOnBackPressed;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.R;

public class ServicesFragment extends Fragment implements IOnBackPressed {
    private ConstraintLayout btn_consultation_services, btn_bath_services;
    private LinearLayout btn_all_schedules;

    private Bundle args;
    private View view;
    private static FragmentTransaction transaction;

    //  User information
    private static int _IdUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_services, container, false);
        Ids();
        args = getArguments();
        assert args != null;
        _IdUser = args.getInt("id_user");

        //  Consultation click
        btn_consultation_services.setOnClickListener(v -> {
            if (_IdUser != 0){
                Intent goTo_Schedule = new Intent(getActivity(), ScheduleAppointmentActivity.class);
                goTo_Schedule.putExtra("id_user", _IdUser);
                startActivity(goTo_Schedule);
            }else
                Warnings.NeedLoginAlert(requireActivity());
        });

        //  My Bath and Tosa click
        btn_bath_services.setOnClickListener(v -> {
            if (_IdUser != 0){
                Intent goTo_ScheduleBath = new Intent(getActivity(), ScheduleBathAndTosaActivity.class);
                goTo_ScheduleBath.putExtra("id_user", _IdUser);
                startActivity(goTo_ScheduleBath);
            }else
                Warnings.NeedLoginAlert(requireActivity());
        });

        //  All Appointments click
        btn_all_schedules.setOnClickListener(v -> {
            if (_IdUser != 0){
                Intent all_Appointments = new Intent(getActivity(), ScheduledServicesActivity.class);
                all_Appointments.putExtra("id_user", _IdUser);
                startActivity(all_Appointments);
            }else
                Warnings.NeedLoginAlert(requireActivity());
        });

        return view;
    }

    private void Ids() {
        btn_consultation_services = view.findViewById(R.id.btn_consultation_services);
        btn_bath_services = view.findViewById(R.id.btn_bath_services);
        btn_all_schedules = view.findViewById(R.id.btn_all_schedules);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onBackPressed() {
        //action not popBackStack
        requireActivity().getWindow().setNavigationBarColor(requireActivity().getColor(R.color.background_top));
        MainFragment mainFragment = new MainFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putInt("id_user", _IdUser);
        mainFragment.setArguments(args);
        transaction.replace(R.id.frameLayoutMain, mainFragment);
        transaction.commit();
        return true;
    }
}
