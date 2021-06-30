package co.kaua.palacepetz.Activitys;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.List;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.R;
import co.kaua.palacepetz.databinding.ActivityFollowOrderBinding;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

@SuppressWarnings("ALL")
public class FollowOrderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityFollowOrderBinding binding;
    private CardView btnExitFollow;

    //  User information
    private int id_user;
    static String name_user, _Email, cpf_user, address_user, complement, phone_user, birth_date, img_user;
    String zipcode = "01310-100";
    private double longitude = -46.6502263;
    private double latitude =  -23.5678371;
    LatLng locationMap;

    //  Order Information
    private String status;
    private int cd_order, deliveryTime;

    private TextView txt_OrderCode, txt_deliveryForecast, txt_address_followOrder;
    ProgressBar _progress_state01, _progress_state02, _progress_state03, _progress_state04, _progress_state05, _progress_state06;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFollowOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_follow_order);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        getWindow().setNavigationBarColor(getColor(R.color.background_menu_sheet));

        Ids();
        getUserInfo();

        //  btn to finish activity
        btnExitFollow.setOnClickListener(v -> {
            finish();
            getWindow().setNavigationBarColor(getColor(R.color.background_top));
        });
    }

    private void getUserInfo() {
        Bundle bundle = getIntent().getExtras();
        cd_order = bundle.getInt("cd_order");
        status = bundle.getString("status");
        deliveryTime = bundle.getInt("deliveryTime");
        txt_OrderCode.setText(getString(R.string.order) + ": #" + cd_order);
        txt_deliveryForecast.setText(deliveryTime + "min");
        DtoUser user = MainActivity.getInstance().GetUserBaseInformation();
        zipcode = user.getZipcode();
        name_user = user.getName_user();
        address_user = user.getAddress_user();
        complement = user.getComplement();
        txt_address_followOrder.setText(address_user + ", " + complement + ", " + zipcode);
        goToZipCode();
        changeStatusBar();
    }

    private void changeStatusBar() {
        switch (status) {
            case "Aguardando Aprovação":
                _progress_state01.setProgress(100);
                break;
            case "Preparando Produto":
                _progress_state01.setProgress(100);
                _progress_state02.setProgress(100);
                break;
            case "A caminho":
                _progress_state01.setProgress(100);
                _progress_state02.setProgress(100);
                _progress_state03.setProgress(100);
                break;
            case "Entregue":
                _progress_state01.setProgress(100);
                _progress_state02.setProgress(100);
                _progress_state03.setProgress(100);
                _progress_state04.setProgress(100);
                _progress_state05.setProgress(100);
                txt_deliveryForecast.setText(getString(R.string.delivered));
                break;
            case "Concluído":
                _progress_state01.setProgress(100);
                _progress_state02.setProgress(100);
                _progress_state03.setProgress(100);
                _progress_state04.setProgress(100);
                _progress_state05.setProgress(100);
                _progress_state06.setProgress(100);
                txt_deliveryForecast.setText(getString(R.string.delivered));
                txt_OrderCode.setText(getString(R.string.order) + ": #" + cd_order + " (OK)");
                break;
        }
    }

    private void Ids() {
        btnExitFollow = findViewById(R.id.btnExitFollow);
        txt_OrderCode = findViewById(R.id.txt_OrderCode);
        txt_address_followOrder = findViewById(R.id.txt_address_followOrder);
        txt_deliveryForecast = findViewById(R.id.txt_deliveryForecast);
        _progress_state01 = findViewById(R.id.progress_state01);
        _progress_state02 = findViewById(R.id.progress_state02);
        _progress_state03 = findViewById(R.id.progress_state03);
        _progress_state04 = findViewById(R.id.progress_state04);
        _progress_state05 = findViewById(R.id.progress_state05);
        _progress_state06 = findViewById(R.id.progress_state06);
    }

    private void goToZipCode() {
        final Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipcode, 2);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Use the address as needed
                @SuppressLint("DefaultLocale") String message = String.format("Latitude: %f, Longitude: %f",
                        address.getLatitude(), address.getLongitude());
                latitude = address.getLatitude();
                longitude = address.getLongitude();
            } else
                // Display appropriate message when Geocoder services are not available
                ToastHelper.toast(FollowOrderActivity.this, getString(R.string.zipCode_is_invalid));
        } catch (Exception e) {
            // handle exception
            ToastHelper.toast(FollowOrderActivity.this, getString(R.string.error_in_get_your_address));
            Log.d("AddressUpdate", e.toString());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.clear();
        locationMap = new LatLng(-23.569230, -46.686980);
        mMap.addMarker(new MarkerOptions().position(locationMap).title(getString(R.string.app_name)));
        locationMap = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(locationMap).title(name_user));
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(latitude, longitude), new LatLng(-23.569230, -46.686980))
                .width(6)
                .color(getColor(R.color.edittext_base)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationMap, 11f));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
        }else
            mMap.setMyLocationEnabled(true);
    }
}