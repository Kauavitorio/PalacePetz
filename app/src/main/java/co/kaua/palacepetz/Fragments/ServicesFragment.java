package co.kaua.palacepetz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import co.kaua.palacepetz.Activitys.Services.ScheduleAppointmentActivity;
import co.kaua.palacepetz.Activitys.Services.ScheduleBathAndTosaActivity;
import co.kaua.palacepetz.R;

public class ServicesFragment extends Fragment {
    private ConstraintLayout btn_consultation_services, btn_bath_services;

    private Bundle args;
    private View view;
    private static FragmentTransaction transaction;

    //  User information
    private static String _Email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activity_services, container, false);
        Ids();
        args = getArguments();
        assert args != null;
        _Email = args.getString("email_user");

        //  Consultation click
        btn_consultation_services.setOnClickListener(v -> {
            Intent goTo_Schedule = new Intent(getActivity(), ScheduleAppointmentActivity.class);
            goTo_Schedule.putExtra("email_user", _Email);
            startActivity(goTo_Schedule);
        });

        //  My Bath and Tosa click
        btn_bath_services.setOnClickListener(v -> {
            Intent goTo_ScheduleBath = new Intent(getActivity(), ScheduleBathAndTosaActivity.class);
            goTo_ScheduleBath.putExtra("email_user", _Email);
            startActivity(goTo_ScheduleBath);
        });

        return view;
    }

    private void Ids() {
        btn_consultation_services = view.findViewById(R.id.btn_consultation_services);
        btn_bath_services = view.findViewById(R.id.btn_bath_services);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
