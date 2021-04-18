package com.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.kaua.palacepetz.R;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    @Override
    public void onBackPressed() {
        Intent goTo_Profile = new Intent(EditProfileActivity.this, ProfileActivity.class);
        startActivity(goTo_Profile);
        finish();
    }
}