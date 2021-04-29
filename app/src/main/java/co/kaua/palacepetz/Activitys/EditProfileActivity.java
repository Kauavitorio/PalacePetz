package co.kaua.palacepetz.Activitys;

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
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Methods.Userpermissions;
import co.kaua.palacepetz.Firebase.ConfFirebase;
import co.kaua.palacepetz.Methods.MaskEditUtil;
import co.kaua.palacepetz.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static co.kaua.palacepetz.Methods.ValidateCPF.isValidCPF;

public class EditProfileActivity extends AppCompatActivity {
    private TextView txt_userName_EditProfile, txt_email_EditProfile;
    private EditText editProfile_FirstNameUser, editProfile__LastNameUser, editProfile__CPFUser,
            editProfile__CepUser, editProfile__AddressUser, editProfile__ComplementUser;
    private CardView cardBtn_ConfirmEditProfile;
    private CircleImageView icon_ProfileUser_EditProfile;
    private LottieAnimationView arrowGoBackEditProfile;
    private InputMethodManager imm;
    private final String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
    AlertDialog.Builder msg;

    //  User information
    String FirstName, LastName, FullName, _Email, CpfUser, CepUser, AddressUser, ComplementUser;


    //  Firebase
    StorageReference storageReference;
    //  Loading and IMAGE REQUEST
    private LoadingDialog loadingDialog;
    int PICK_IMAGE_REQUEST = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Ids();
        loadingDialog = new LoadingDialog(EditProfileActivity.this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        _Email = bundle.getString("email_user");
        SetEditNamesChange();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Create AlertDialog
        msg = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.change_profile_photo))
                .setNegativeButton(getString(R.string.cancel), null)
                .setMessage(getString(R.string.select_upload_method));

        //  Set Mask
        editProfile__CPFUser.addTextChangedListener(MaskEditUtil.mask(editProfile__CPFUser, MaskEditUtil.FORMAT_CPF));
        editProfile__CepUser.addTextChangedListener(MaskEditUtil.mask(editProfile__CepUser, MaskEditUtil.FORMAT_CEP));

        icon_ProfileUser_EditProfile.setOnClickListener(v -> {
            Userpermissions.validatePermissions(permissions, EditProfileActivity.this, 1);
            int GalleryPermission = ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (GalleryPermission == PackageManager.PERMISSION_GRANTED){
                msg.setNeutralButton(getString(R.string.gallery), (dialog, which) -> OpenGallery());
            }
            if (GalleryPermission == PackageManager.PERMISSION_GRANTED)
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
        storageReference = ConfFirebase.getFirebaseStorage().child("user").child("profile").child("User_" + _Email);
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
                            Toast.makeText(EditProfileActivity.this, R.string.couldnt_insert , Toast.LENGTH_SHORT).show();
                            Log.d("ProfileUpload", Objects.requireNonNull(task.getException()).toString());
                        }
                        return storageReference .getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            loadingDialog.dimissDialog();
                            Uri downloadUri = task.getResult();
                            Toast.makeText(this, ""+ downloadUri, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, getString(R.string.uploadFailed), Toast.LENGTH_SHORT).show();
                            Log.d("ProfileUpload", Objects.requireNonNull(task.getException()).getMessage());
                            loadingDialog.dimissDialog();
                        }
                    });
                }
                else {
                    Toast.makeText(EditProfileActivity.this, R.string.select_an_image, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(this, getString(R.string.weHaveAProblem), Toast.LENGTH_SHORT).show();
                Log.d("ProfileUpload", ex.toString());
            }
        }
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

    private void GoBackToProfile() {
        Intent goTo_Profile = new Intent(EditProfileActivity.this, ProfileActivity.class);
        goTo_Profile.putExtra("email_user", _Email);
        startActivity(goTo_Profile);
        finish();
    }

    private void EditUserProfile() {
        FirstName = editProfile_FirstNameUser.getText().toString().replaceAll(" ", "");
        LastName = editProfile__LastNameUser.getText().toString().replaceAll(" ", "");
        FullName = FirstName + " " + LastName;
        _Email = txt_email_EditProfile.getText().toString();
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

    private void OpenGallery() {
        Intent openGallery = new Intent();
        openGallery.setType("image/*");
        openGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(openGallery, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onBackPressed() { GoBackToProfile(); }
}