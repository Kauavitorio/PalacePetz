package co.kaua.palacepetz.Activitys.Pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

import co.kaua.palacepetz.Activitys.MainActivity;
import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Pets.AsyncPets;
import co.kaua.palacepetz.Data.Pets.DtoPets;
import co.kaua.palacepetz.Data.Pets.PetsServices;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

@SuppressWarnings({"unchecked", "FieldCanBeLocal"})
public class AllPetsActivity extends AppCompatActivity {
    private CardView btnRegisterPet, btnBackPets;
    private LoadingDialog loadingDialog;
    private RecyclerView RecyclerAllPets;
    private ConstraintLayout container_noPets;

    //  User information
    private int id_user;
    private String birth_date;

    //  Retrofit
    final Retrofit retrofitUser = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pets);
        Ids();
        LoadPets();

        Bundle bundle = getIntent().getExtras();
        id_user = bundle.getInt("id_user");
        birth_date = bundle.getString("birth_date");

        btnBackPets.setOnClickListener(v -> finish());

        btnRegisterPet.setOnClickListener(v -> {
            if (birth_date != null && birth_date.length() > 8){
                String[] splitBirth = birth_date.split("/");
                int User_age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(splitBirth[2]);
                if (User_age >= 18 ){
                    Intent goTo_registerPet = new Intent(this, RegisterPetActivity.class);
                    goTo_registerPet.putExtra("id_user", id_user);
                    startActivity(goTo_registerPet);
                }else
                    Warnings.AgeAlert(AllPetsActivity.this);
            }else
                Warnings.RegisterBirthAlert(AllPetsActivity.this);
        });
    }

    private void LoadPets() {
        loadingDialog = new LoadingDialog(AllPetsActivity.this);
        loadingDialog.startLoading();
        PetsServices petsServices = retrofitUser.create(PetsServices.class);
        Call<DtoPets> call = petsServices.getPerts(id_user);
        call.enqueue(new Callback<DtoPets>() {
            @Override
            public void onResponse(@NonNull Call<DtoPets> call, @NonNull Response<DtoPets> response) {
                loadingDialog.dimissDialog();
                if(response.code() == 200){
                    RecyclerAllPets.setVisibility(View.VISIBLE);
                    container_noPets.setVisibility(View.GONE);
                    LinearLayoutManager linearLayout = new LinearLayoutManager(AllPetsActivity.this);
                    RecyclerAllPets.setLayoutManager(linearLayout);
                    AsyncPets pets = new AsyncPets(id_user, RecyclerAllPets, AllPetsActivity.this);
                    pets.execute();
                }else if(response.code() == 204){
                    RecyclerAllPets.setVisibility(View.GONE);
                    container_noPets.setVisibility(View.VISIBLE);
                }else
                    Warnings.showWeHaveAProblem(AllPetsActivity.this);
            }
            @Override
            public void onFailure(@NonNull Call<DtoPets> call, @NonNull Throwable t) {
                loadingDialog.dimissDialog();
                Warnings.showWeHaveAProblem(AllPetsActivity.this);
            }
        });
    }

    int timeReload = 0;
    @Override
    protected void onResume() {
        super.onResume();
        if (timeReload != 0)
            LoadPets();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeReload = 1;
    }

    private void Ids() {
        DtoUser user = MainActivity.getInstance().GetUserBaseInformation();
        id_user = user.getId_user();
        btnRegisterPet = findViewById(R.id.btnRegisterPet);
        RecyclerAllPets = findViewById(R.id.RecyclerAllPets);
        container_noPets = findViewById(R.id.container_noPets);
        btnBackPets = findViewById(R.id.btnBackPets);
    }
}