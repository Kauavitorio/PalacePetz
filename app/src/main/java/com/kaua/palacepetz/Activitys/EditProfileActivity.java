package com.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.kaua.palacepetz.Adapters.Userpermissions;
import com.kaua.palacepetz.Methods.MaskEditUtil;
import com.kaua.palacepetz.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kaua.palacepetz.Methods.ValidateCPF.isValidCPF;

public class EditProfileActivity extends AppCompatActivity {
    private TextView txt_userName_EditProfile, txt_email_EditProfile;
    private EditText editProfile_FirstNameUser, editProfile__LastNameUser, editProfile__CPFUser,
            editProfile__CepUser, editProfile__AddressUser, editProfile__ComplementUser;
    private CardView cardBtn_ConfirmEditProfile;
    private CircleImageView icon_ProfileUser_EditProfile;
    private LottieAnimationView arrowGoBackEditProfile;
    private InputMethodManager imm;
    private final String[] permissions = { Manifest.permission.CAMERA
            , Manifest.permission.READ_EXTERNAL_STORAGE };
    AlertDialog.Builder msg;
    private final int CAMERA = 1;
    private final int GALLERY = 2;

    //  User information
    String FirstName, LastName, FullName, EmailUser, CpfUser, CepUser, AddressUser, ComplementUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Ids();
        SetEditNamesChange();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Set Mask
        editProfile__CPFUser.addTextChangedListener(MaskEditUtil.mask(editProfile__CPFUser, MaskEditUtil.FORMAT_CPF));
        editProfile__CepUser.addTextChangedListener(MaskEditUtil.mask(editProfile__CepUser, MaskEditUtil.FORMAT_CEP));

        icon_ProfileUser_EditProfile.setOnClickListener(v -> {
            Userpermissions.validatePermissions(permissions, EditProfileActivity.this, 1);
            int CameraPermission = ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA);
            int GalleryPermission = ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
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

        //  When click on back btn will go back to ProfileActivity
        arrowGoBackEditProfile.setOnClickListener(v -> GoBackToProfile());

        //  When click on Confirm Btn will valid edit and try to update user information
        cardBtn_ConfirmEditProfile.setOnClickListener(v -> {
            cardBtn_ConfirmEditProfile.setElevation(0);
            if (editProfile_FirstNameUser.getText().length() == 0 || editProfile_FirstNameUser.getText().length() < 3)
                showError(editProfile_FirstNameUser, getString(R.string.necessary_to_insert_the_First_name));
            else if (editProfile__LastNameUser.getText().length() == 0 || editProfile__LastNameUser.getText().length() < 3)
                showError(editProfile__LastNameUser, getString(R.string.necessary_to_insert_the_last_name));
            else if(!isValidCPF(editProfile__CPFUser.getText().toString())){
                showError(editProfile__CPFUser, getString(R.string.cpfinformedisInvalid));
            }else{ EditUserProfile(); }
        });
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

    private void GoBackToProfile() {
        Intent goTo_Profile = new Intent(EditProfileActivity.this, ProfileActivity.class);
        startActivity(goTo_Profile);
        finish();
    }

    private void EditUserProfile() {
        FirstName = editProfile_FirstNameUser.getText().toString().replaceAll(" ", "");
        LastName = editProfile__LastNameUser.getText().toString().replaceAll(" ", "");
        FullName = FirstName + " " + LastName;
        EmailUser = txt_email_EditProfile.getText().toString();
        CpfUser = editProfile__CPFUser.getText().toString();
        CepUser = editProfile__CepUser.getText().toString();
        AddressUser = editProfile__AddressUser.getText().toString();
        ComplementUser = editProfile__ComplementUser.getText().toString();
    }

    private void showError(EditText editText, String errorText) {
        editText.setError(errorText);
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        cardBtn_ConfirmEditProfile.setElevation(20);
    }

    private void SetEditNamesChange() {

        //  When Change text on this edit will save on variable and show to user
        editProfile_FirstNameUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                FirstName = editProfile_FirstNameUser.getText().toString().replaceAll(" ", "");
                LastName = editProfile__LastNameUser.getText().toString().replaceAll(" ", "");
                FullName = FirstName + " " + LastName;
                txt_userName_EditProfile.setText(FullName);
            }
        });

        //  When Change text on this edit will save on variable and show to user
        editProfile__LastNameUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                FirstName = editProfile_FirstNameUser.getText().toString().replaceAll(" ", "");
                LastName = editProfile__LastNameUser.getText().toString().replaceAll(" ", "");
                FullName = FirstName + " " + LastName;
                txt_userName_EditProfile.setText(FullName);
            }
        });
    }

    private void Ids() {
        arrowGoBackEditProfile = findViewById(R.id.arrowGoBackEditProfile);
        txt_userName_EditProfile = findViewById(R.id.txt_userName_EditProfile);
        txt_email_EditProfile = findViewById(R.id.txt_email_EditProfile);
        editProfile_FirstNameUser = findViewById(R.id.editProfile_FirstNameUser);
        editProfile__LastNameUser = findViewById(R.id.editProfile__LastNameUser);
        editProfile__CPFUser = findViewById(R.id.editProfile__CPFUser);
        editProfile__CepUser = findViewById(R.id.editProfile__CepUser);
        editProfile__AddressUser = findViewById(R.id.editProfile__AddressUser);
        cardBtn_ConfirmEditProfile = findViewById(R.id.cardBtn_ConfirmEditProfile);
        editProfile__ComplementUser = findViewById(R.id.editProfile__ComplementUser);
        icon_ProfileUser_EditProfile = findViewById(R.id.icon_ProfileUser_EditProfile);
        cardBtn_ConfirmEditProfile.setElevation(20);
    }

    @Override
    public void onBackPressed() { GoBackToProfile(); }
}