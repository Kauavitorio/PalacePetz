package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.Address.DtoZipCode;
import co.kaua.palacepetz.Data.Address.ZipCodeService;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Data.User.UserServices;
import co.kaua.palacepetz.Methods.MaskEditUtil;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.Methods.UserPermissions;
import co.kaua.palacepetz.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegisterAddressActivity extends FragmentActivity implements OnMapReadyCallback {
    private EditText editRegisterAddress_CepUser, editRegisterAddress_StreetUser, editRegisterAddress_ComplementUser,
            editRegisterAddress_NumberUser;
    private CardView cardBtn_RegisterAddress;
    private TextView txt_title_register_address, txt_BtnRegisterAddress;
    private InputMethodManager imm;

    private GoogleMap mMap;
    private LottieAnimationView arrowGoBackRegisterAddress;
    private final String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };


    //  User information
    private int id_user;
    static String name_user, _Email, cpf_user, address_user, complement, phone_user, birth_date, img_user;
    String zipcode = "01310-100";
    private double longitude = -46.6502263;
    private double latitude =  -23.5678371;
    LatLng locationMap;
    private LoadingDialog loadingDialog;


    // Retrofit
    final Retrofit retrofitZipCode = new Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    final Retrofit userRetrofit = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address);
        Ids();
        loadingDialog = new LoadingDialog(this);
        UserPermissions.validatePermissions(permissions, RegisterAddressActivity.this, 1);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_user = bundle.getInt("id_user");
        name_user = bundle.getString("name_user");
        _Email = bundle.getString("email_user");
        cpf_user = bundle.getString("cpf_user");
        address_user = bundle.getString("address_user");
        complement = bundle.getString("complement");
        if (bundle.getString("zipcode") == null)
            zipcode = "01310-100";
        else {
            zipcode = bundle.getString("zipcode");
            txt_title_register_address.setText(getString(R.string.edit_address));
            txt_BtnRegisterAddress.setText(getString(R.string.edit));
            editRegisterAddress_CepUser.setText(zipcode);
            String addressGet = address_user;
            editRegisterAddress_StreetUser.setText(addressGet.replaceAll("[0-9]", "").replaceAll("\\s+"," ")
                    .replace(",", "").trim());
            editRegisterAddress_ComplementUser.setText(complement);
            Matcher matcher = Pattern.compile("\\d+").matcher(address_user);
            if (matcher.find())
                editRegisterAddress_NumberUser.setText(matcher.group() + "");
            //  Set Edits Enables
            editRegisterAddress_StreetUser.setEnabled(true);
            editRegisterAddress_NumberUser.setEnabled(true);
            editRegisterAddress_ComplementUser.setEnabled(true);
            goToZipCode();
        }
        phone_user = bundle.getString("phone_user");
        birth_date = bundle.getString("birth_date");
        img_user = bundle.getString("img_user");

        //  Set Mask
        editRegisterAddress_CepUser.addTextChangedListener(MaskEditUtil.mask(editRegisterAddress_CepUser, MaskEditUtil.FORMAT_CEP));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_register_address);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        arrowGoBackRegisterAddress.setOnClickListener(v -> finish());


        editRegisterAddress_CepUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (editRegisterAddress_CepUser.getText().length() == 9) {
                    ToastHelper.toast(RegisterAddressActivity.this, getString(R.string.loading));
                    zipcode = editRegisterAddress_CepUser.getText().toString();
                    ZipCodeService zipCodeService = retrofitZipCode.create(ZipCodeService.class);
                    Call<DtoZipCode> zipcodeCall = zipCodeService.getAddress(zipcode);
                    zipcodeCall.enqueue(new Callback<DtoZipCode>() {
                        @Override
                        public void onResponse(@NonNull Call<DtoZipCode> call, @NonNull Response<DtoZipCode> response) {
                            if (response.isSuccessful()) {
                                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                                        editRegisterAddress_CepUser.getWindowToken(), 0);
                                assert response.body() != null;
                                zipcode = response.body().getCep();
                                editRegisterAddress_StreetUser.setText(response.body().getLogradouro());
                                editRegisterAddress_ComplementUser.setText(response.body().getComplemento());
                                goToZipCode();
                                onMapReady(mMap);
                                editRegisterAddress_NumberUser.requestFocus();

                                //  Set Edits Enables
                                editRegisterAddress_StreetUser.setEnabled(true);
                                editRegisterAddress_NumberUser.setEnabled(true);
                                editRegisterAddress_ComplementUser.setEnabled(true);
                            } else
                                ToastHelper.toast(RegisterAddressActivity.this, getString(R.string.error_in_get_your_address));
                        }

                        @Override
                        public void onFailure(@NonNull Call<DtoZipCode> call, @NonNull Throwable t) {
                            ToastHelper.toast(RegisterAddressActivity.this, getString(R.string.error_in_get_your_address));
                            Log.d("NetWorkError", t.getMessage());
                        }
                    });
                }
            }
        });

        cardBtn_RegisterAddress.setOnClickListener(v -> {
            cardBtn_RegisterAddress.setElevation(0);
            if (editRegisterAddress_CepUser.getText().length() < 9){
                showError(editRegisterAddress_CepUser, getString(R.string.zipcode_field_incorretly));
            }else if(editRegisterAddress_StreetUser.getText().length() < 5 || editRegisterAddress_StreetUser.getText().length() <= 0){
                showError(editRegisterAddress_StreetUser, getString(R.string.street_field_incorretly));
            }else if(editRegisterAddress_NumberUser.getText().length() <= 0){
                showError(editRegisterAddress_NumberUser, getString(R.string.number_field_incorretly));
            }else{
                loadingDialog.startLoading();
                String newZipCode = editRegisterAddress_CepUser.getText().toString().trim().replace(" ", "");
                String newNumber = editRegisterAddress_NumberUser.getText().toString().trim();
                String newStreet = editRegisterAddress_StreetUser.getText().toString().trim();
                String newComplement = editRegisterAddress_ComplementUser.getText().toString().trim();
                String newAddress = newStreet + " " + newNumber;
                EditUserAddres(newAddress, newComplement, newZipCode);
            }
        });
    }

    private void EditUserAddres(String newAddress, String newComplement, String newZipCode) {
        UserServices userServices = userRetrofit.create(UserServices.class);
        DtoUser userInfo = new DtoUser(newAddress, newComplement, newZipCode, id_user);
        Call<DtoUser> userCall = userServices.updateAddress(userInfo);
        userCall.enqueue(new Callback<DtoUser>() {
            @Override
            public void onResponse(@NonNull Call<DtoUser> call, @NonNull Response<DtoUser> response) {
                if (response.code() == 202 || response.isSuccessful()){
                    ToastHelper.toast(RegisterAddressActivity.this, getString(R.string.your_address_updated_successfully));
                    GoBack_toMain();
                }else if(response.code() == 404){
                    loadingDialog.dimissDialog();
                    Warnings.showWeHaveAProblem(RegisterAddressActivity.this);
                }else{
                    loadingDialog.dimissDialog();
                    Warnings.showWeHaveAProblem(RegisterAddressActivity.this);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DtoUser> call, @NonNull Throwable t) {
                loadingDialog.dimissDialog();
                Warnings.showWeHaveAProblem(RegisterAddressActivity.this);
            }
        });

    }

    private void showError(EditText editText, String error){
        editText.setError(error);
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        cardBtn_RegisterAddress.setElevation(20);
    }

    private void Ids(){
        arrowGoBackRegisterAddress = findViewById(R.id.arrowGoBackRegisterAddress);
        editRegisterAddress_CepUser = findViewById(R.id.editRegisterAddress_CepUser);
        editRegisterAddress_StreetUser = findViewById(R.id.editRegisterAddress_StreetUser);
        editRegisterAddress_ComplementUser = findViewById(R.id.editRegisterAddress_ComplementUser);
        editRegisterAddress_NumberUser = findViewById(R.id.editRegisterAddress_NumberUser);
        cardBtn_RegisterAddress = findViewById(R.id.cardBtn_RegisterAddress);
        txt_title_register_address = findViewById(R.id.txt_title_register_address);
        txt_BtnRegisterAddress = findViewById(R.id.txt_BtnRegisterAddress);
        cardBtn_RegisterAddress.setElevation(20);
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
                // Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else
                // Display appropriate message when Geocoder services are not available
                ToastHelper.toast(RegisterAddressActivity.this, getString(R.string.zipCode_is_invalid));
        } catch (Exception e) {
            // handle exception
            ToastHelper.toast(RegisterAddressActivity.this, getString(R.string.error_in_get_your_address));
            Log.d("AddressUpdate", e.toString());
        }
    }

    @SuppressWarnings({"RedundantIfStatement"})
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in location and move the camera
        String nm_Marker = editRegisterAddress_StreetUser.getText().toString();
        if (nm_Marker.equals("") || nm_Marker == null)
            nm_Marker = "Palace Petz";
        else
            nm_Marker = editRegisterAddress_StreetUser.getText().toString();
        locationMap = new LatLng(latitude, longitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(locationMap).title(nm_Marker));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationMap, 15f));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
        }else
            mMap.setMyLocationEnabled(true);
    }


    private void GoBack_toMain() {
        finish();
    }


    @Override
    public void onBackPressed() {
        GoBack_toMain();
    }
}