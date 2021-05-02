package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Methods.Userpermissions;

import co.kaua.palacepetz.Firebase.ConfFirebase;
import co.kaua.palacepetz.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {
    //  Activity Items
    private TextView txt_userName_profile, txt_email_profile, txt_EditProfile_profile;
    private EditText Profile_FirstNameUser, Profile_LastNameUser, Profile_CFCUser,
            Profile_CepUser, Profile_AddressUser, Profile_ComplementUser;
    private CircleImageView icon_ProfileUser_profile;
    private LottieAnimationView arrowGoBackProfile;
    private CardView cardBtn_EditProfile, btnSeeMyAnimals;
    Handler timer = new Handler();
    private final String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
    AlertDialog.Builder msg;

    //  User information
    private int id_user;
    private String name_user, _Email, cpf_user, address_user, complement, zipcode, phone_user, img_user;

    //  Firebase
    StorageReference storageReference;
    //  Loading and IMAGE REQUEST
    private LoadingDialog loadingDialog;
    int PICK_IMAGE_REQUEST = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Ids();
        cardBtn_EditProfile.setElevation(20);
        loadingDialog = new LoadingDialog(ProfileActivity.this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id_user = bundle.getInt("id_user");
        name_user = bundle.getString("name_user");
        _Email = bundle.getString("email_user");
        cpf_user = bundle.getString("cpf_user");
        address_user = bundle.getString("address_user");
        complement = bundle.getString("complement");
        zipcode = bundle.getString("zipcode");
        phone_user = bundle.getString("phone_user");
        img_user = bundle.getString("img_user");
        msg = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.change_profile_photo))
                .setNegativeButton(getString(R.string.cancel), null)
                .setMessage(getString(R.string.select_upload_method));

        DoProfileImgAlert();
        loadUserInfo();

        icon_ProfileUser_profile.setOnClickListener(v -> {
            Userpermissions.validatePermissions(permissions, ProfileActivity.this, 1);
            int GalleryPermission = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (GalleryPermission == PackageManager.PERMISSION_GRANTED){
                msg.setNeutralButton(getString(R.string.gallery), (dialog, which) -> OpenGallery());
            }
            if (GalleryPermission == PackageManager.PERMISSION_GRANTED)
                msg.show();
        });

        cardBtn_EditProfile.setOnClickListener(v -> GoTo_EditProfile());

        txt_EditProfile_profile.setOnClickListener(v -> GoTo_EditProfile());

        arrowGoBackProfile.setOnClickListener(v -> finish());

        btnSeeMyAnimals.setOnClickListener(v -> {

        });
    }

    private void loadUserInfo() {
        if (img_user == null || img_user.equals(""))
            Log.d("UserStatus", "Not User image");
        else
            Picasso.get().load(img_user).into(icon_ProfileUser_profile);
        String[] FullUserName = name_user.split(" ");
        txt_userName_profile.setText(name_user);
        txt_email_profile.setText(_Email);
        Profile_FirstNameUser.setText(FullUserName[0]);
        Profile_LastNameUser.setText(FullUserName[1]);
        Profile_CFCUser.setText(cpf_user);
        Profile_CepUser.setText(zipcode);
        Profile_AddressUser.setText(address_user);
        Profile_ComplementUser.setText(complement);
    }

    private void GoTo_EditProfile() {
        cardBtn_EditProfile.setElevation(0);
        Intent goTo_EditProfile = new Intent(ProfileActivity.this, EditProfileActivity.class);
        goTo_EditProfile.putExtra("id_user", id_user);
        goTo_EditProfile.putExtra("name_user", name_user);
        goTo_EditProfile.putExtra("email_user", _Email);
        goTo_EditProfile.putExtra("cpf_user", cpf_user);
        goTo_EditProfile.putExtra("address_user", address_user);
        goTo_EditProfile.putExtra("complement", complement);
        goTo_EditProfile.putExtra("zipcode", zipcode);
        goTo_EditProfile.putExtra("phone_user", phone_user);
        goTo_EditProfile.putExtra("img_user", img_user);
        startActivity(goTo_EditProfile);
        finish();
    }

    private void OpenGallery() {
        Intent openGallery = new Intent();
        openGallery.setType("image/*");
        openGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(openGallery, "Select Image"), PICK_IMAGE_REQUEST);
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
        Profile_AddressUser = findViewById(R.id.Profile_AddressUser);
        cardBtn_EditProfile = findViewById(R.id.cardBtn_EditProfile);
        icon_ProfileUser_profile = findViewById(R.id.icon_ProfileUser_profile);
        Profile_LastNameUser = findViewById(R.id.Profile_LastNameUser);
        Profile_CFCUser = findViewById(R.id.Profile_CFCUser);
        Profile_CepUser = findViewById(R.id.Profile_CepUser);
        Profile_ComplementUser = findViewById(R.id.Profile_ComplementUser);
        btnSeeMyAnimals = findViewById(R.id.btnSeeMyAnimals);
        txt_EditProfile_profile = findViewById(R.id.txt_EditProfile_profile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageReference = ConfFirebase.getFirebaseStorage().child("user").child("profile").child("User_" + id_user);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //getting image from gallery
                if(filePath != null) {
                    loadingDialog.startLoading();

                    //uploading the image
                    storageReference .putFile(filePath).continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            loadingDialog.dimissDialog();
                            Toast.makeText(ProfileActivity.this, R.string.couldnt_insert , Toast.LENGTH_SHORT).show();
                            Log.d("ProfileUpload", Objects.requireNonNull(task.getException()).toString());
                        }
                        return storageReference .getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            loadingDialog.dimissDialog();
                            Uri downloadUri = task.getResult();
                            img_user = downloadUri+"";
                            Picasso.get().load(img_user).into(icon_ProfileUser_profile);
                            UpdateUserImage(id_user, img_user);
                        } else {
                            Toast.makeText(this, getString(R.string.uploadFailed), Toast.LENGTH_SHORT).show();
                            Log.d("ProfileUpload", Objects.requireNonNull(task.getException()).getMessage());
                            loadingDialog.dimissDialog();
                        }
                    });
                }
                else {
                    Toast.makeText(ProfileActivity.this, R.string.select_an_image, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(this, getString(R.string.weHaveAProblem), Toast.LENGTH_SHORT).show();
                Log.d("ProfileUpload", ex.toString());
            }
        }
    }

    private void UpdateUserImage(int id_user, String img_user) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals("android.permission.READ_EXTERNAL_STORAGE")  && grantResults[i] == 0){
                // Gallery ok
                OpenGallery();
                Log.d("PermissionStatus", "Gallery permission has been GRANTED");
            }
        }
    }
}