package co.kaua.palacepetz.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import co.kaua.palacepetz.Adapters.DeviceSliderViewPagerAdapter;
import co.kaua.palacepetz.Data.DevicePresentation.DevicePresentation;
import co.kaua.palacepetz.R;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 * @author Kaua Vitorio
 **/

public class DevicePresentationActivity extends AppCompatActivity {
    private ViewPager screenPager;
    DeviceSliderViewPagerAdapter deviceSliderViewPagerAdapter;
    TabLayout tab_indicator;
    Button btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;
    LottieAnimationView arrowGoBackDevicePresentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_presentation);
        setTheme(R.style.DevicePresentation);
        arrowGoBackDevicePresentation = findViewById(R.id.arrowGoBackDevicePresentation);

        // make the activity on full screen
        /*View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);*/


        // when this activity is about to be launch we need to check if its opened before or not
        if (restorePrefData()) {
            Intent PairDevice = new Intent(getApplicationContext(), DevicePairActivity.class );
            startActivity(PairDevice);
            finish();
        }

        //  When click will finish this activity
        arrowGoBackDevicePresentation.setOnClickListener(v -> finish());

        // init views
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tab_indicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.device_presentation_button_animation);
        tvSkip = findViewById(R.id.tv_skip);


        // fill list screen
        final List<DevicePresentation> mList = new ArrayList<>();
        mList.add(new DevicePresentation("Palace Fountain", getString(R.string.text_one_devicePresentation) ,R.drawable.device_presentation_palace_fountain));
        mList.add(new DevicePresentation(getString(R.string.comfort_and_agility),getString(R.string.desc_fountain_comfort),R.drawable.device_presentation_dog_bread));
        mList.add(new DevicePresentation(getString(R.string.technology),  getString(R.string.desc_fountain_technology), R.drawable.device_presentation_with_chicken));

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        deviceSliderViewPagerAdapter = new DeviceSliderViewPagerAdapter(this,mList);
        screenPager.setAdapter(deviceSliderViewPagerAdapter);

        // setup Tab layout with viewpager
        tab_indicator.setupWithViewPager(screenPager);

        // next button click Listener
        btnNext.setOnClickListener(v -> {
            position = screenPager.getCurrentItem();
            if (position < mList.size()) {
                position++;
                screenPager.setCurrentItem(position);

            }

            if (position == mList.size()-1) { // when we reach to the last screen
                loadLastScreen();
            }
        });

        // Tab layout add change listener
        //noinspection deprecation,rawtypes
        tab_indicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1) {
                    loadLastScreen();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Get Started button click listener
        btnGetStarted.setOnClickListener(v -> {
            //open Pair Device activity
            Intent PairDevice = new Intent(getApplicationContext(), DevicePairActivity.class );
            startActivity(PairDevice);
            // also we need to save a boolean value to storage so next time when the user run the app
            // we could know that he is already checked the intro screen activity
            // i'm going to use shared preferences to that process
            savePrefsData();
            finish();
        });

        // skip button click listener
        tvSkip.setOnClickListener(v -> screenPager.setCurrentItem(mList.size()));
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        return pref.getBoolean("isDevicePre",false);
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isDevicePre",true);
        editor.apply();
    }

    // show the GET STARTED Button and hide the indicator and the next button
    private void loadLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tab_indicator.setVisibility(View.INVISIBLE);
        // setup animation
        btnGetStarted.setAnimation(btnAnim);
    }
}
