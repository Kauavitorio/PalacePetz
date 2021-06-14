package co.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import co.kaua.palacepetz.R;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

public class DeviceTipsActivity extends AppCompatActivity {
    private ConstraintLayout btnContinue_Tips;
    private LottieAnimationView arrowGoBackDeviceTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_fountain);
        setTheme(R.style.DevicePresentation);
        Ids();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.getBoolean("NotFound")) {
            Snackbar.make(findViewById(R.id.layout_tips), getString(R.string.no_paired_device),
                    Snackbar.LENGTH_SHORT)
                    .show();
        }

        //  To close this activity
        arrowGoBackDeviceTips.setOnClickListener(v -> finish());

        // Open Pair Device activity
        btnContinue_Tips.setOnClickListener(v -> {
            Intent PairDevice = new Intent(getApplicationContext(), DevicePairActivity.class );
            startActivity(PairDevice);
            finish();
        });
    }

    private void Ids() {
        btnContinue_Tips = findViewById(R.id.btnContinue_Tips);
        arrowGoBackDeviceTips = findViewById(R.id.arrowGoBackDeviceTips);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}