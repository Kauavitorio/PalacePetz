package co.kaua.palacepetz.Activitys.Pets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import co.kaua.palacepetz.R;

public class AllPetsActivity extends AppCompatActivity {
    CardView btnRegisterPet;

    //  User information
    private int id_user;
    private String name_user, _Email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pets);
        Ids();

        Bundle bundle = getIntent().getExtras();
        id_user = bundle.getInt("id_user");

        btnRegisterPet.setOnClickListener(v -> {
            Intent goTo_registerPet = new Intent(this, RegisterPetActivity.class);
            goTo_registerPet.putExtra("id_user", id_user);
            startActivity(goTo_registerPet);
        });
    }

    private void Ids() {
        btnRegisterPet = findViewById(R.id.btnRegisterPet);
    }
}