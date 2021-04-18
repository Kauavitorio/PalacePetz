package com.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.kaua.palacepetz.Adapters.Userpermissions;
import com.kaua.palacepetz.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {
    //  Activity Items
    TextView txt_userName_profile, txt_email_profile;
    EditText Profile_FirstNameUser, Profile__LastNameUser, Profile__CFCUser,
            Profile__CepUser, Profile__AddressUser, Profile__ComplementUser;
    CircleImageView icon_ProfileUser_profile;
    LottieAnimationView arrowGoBackProfile;
    CardView cardBtn_EditProfile;
    Handler timer = new Handler();
    private final String[] permissions = { Manifest.permission.CAMERA
            , Manifest.permission.READ_EXTERNAL_STORAGE };
    AlertDialog.Builder msg;
    private final int CAMERA = 1;
    private final int GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Ids();
        msg = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.change_profile_photo))
                .setNegativeButton(getString(R.string.cancel), null)
                .setMessage(getString(R.string.select_payment_method));

        DoProfileImgAlert();

        icon_ProfileUser_profile.setOnClickListener(v -> {
            Userpermissions.validatePermissions(permissions, ProfileActivity.this, 1);
            int CameraPermission = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA);
            int GalleryPermission = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (CameraPermission == PackageManager.PERMISSION_GRANTED){
                msg.setPositiveButton(getString(R.string.camera), (dialog, which) -> {
                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamera, CAMERA);
                });
            }
            if (GalleryPermission == PackageManager.PERMISSION_GRANTED){
                msg.setNeutralButton(getString(R.string.gallery), (dialog, which) -> {
                    Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentGallery, GALLERY);
                });
            }
            if (GalleryPermission == PackageManager.PERMISSION_GRANTED || CameraPermission == PackageManager.PERMISSION_GRANTED)
                msg.show();
        });

        cardBtn_EditProfile.setElevation(20);

        cardBtn_EditProfile.setOnClickListener(v -> {
            cardBtn_EditProfile.setElevation(0);
            Intent goTo_EditProfile = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(goTo_EditProfile);
            finish();
        });

        arrowGoBackProfile.setOnClickListener(v -> finish());
    }

    private void DoProfileImgAlert() {
        icon_ProfileUser_profile.setBorderColor(getColor(R.color.gradient_end_color));
        timer.postDelayed(() -> icon_ProfileUser_profile.setBorderColor(getColor(R.color.black)),1000);
        timer.postDelayed(() -> icon_ProfileUser_profile.setBorderColor(getColor(R.color.gradient_end_color)),1500);
        timer.postDelayed(() -> icon_ProfileUser_profile.setBorderColor(getColor(R.color.black)),2500);
    }

    private void Ids() {
        arrowGoBackProfile = findViewById(R.id.arrowGoBackProfile);
        txt_userName_profile = findViewById(R.id.txt_userName_profile);
        txt_email_profile = findViewById(R.id.txt_email_profile);
        Profile_FirstNameUser = findViewById(R.id.Profile_FirstNameUser);
        Profile__AddressUser = findViewById(R.id.Profile__AddressUser);
        cardBtn_EditProfile = findViewById(R.id.cardBtn_EditProfile);
        icon_ProfileUser_profile = findViewById(R.id.icon_ProfileUser_profile);
        Profile__LastNameUser = findViewById(R.id.Profile__LastNameUser);
        Profile__CFCUser = findViewById(R.id.Profile__CFCUser);
        Profile__CepUser = findViewById(R.id.Profile__CepUser);
        Profile__ComplementUser = findViewById(R.id.Profile__ComplementUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Under development", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals("android.permission.CAMERA")  && grantResults[i] == 0){
                // Camera ok
                Log.d("PermissionStatus", "Camera permission has been GRANTED");
            }
            if (permissions[i].equals("android.permission.READ_EXTERNAL_STORAGE")  && grantResults[i] == 0){
                // Gallery ok
                Log.d("PermissionStatus", "Gallery permission has been GRANTED");
            }
        }
    }
}