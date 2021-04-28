package co.kaua.palacepetz.Activitys;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import co.kaua.palacepetz.R;

public class RegisterAddressActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LottieAnimationView arrowGoBackRegisterAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address);
        Ids();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_register_address);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        arrowGoBackRegisterAddress.setOnClickListener(v -> {
            finish();
        });
    }

    private void Ids(){
        arrowGoBackRegisterAddress = findViewById(R.id.arrowGoBackRegisterAddress);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void GoBack_toMain() {
        finish();
    }


    @Override
    public void onBackPressed() {
        GoBack_toMain();
    }
}