package co.kaua.palacepetz.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
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

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Adapters.Warnings;
import co.kaua.palacepetz.Data.User.DtoUser;
import co.kaua.palacepetz.Data.User.UserServices;
import co.kaua.palacepetz.Methods.ToastHelper;
import co.kaua.palacepetz.Methods.UserPermissions;
import co.kaua.palacepetz.Firebase.ConfFirebase;
import co.kaua.palacepetz.Methods.MaskEditUtil;
import co.kaua.palacepetz.R;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static co.kaua.palacepetz.Methods.ValidateCPF.isValidCPF;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

public class EditProfileActivity extends AppCompatActivity {
    private TextView txt_userName_EditProfile, txt_email_EditProfile;
    private EditText editProfile_FirstNameUser, editProfile__LastNameUser, editProfile__CPFUser,
    editProfile__PHONEUser, editProfile__birth_dateUser;
    private CardView cardBtn_ConfirmEditProfile, btnEditAddress;
    private CircleImageView icon_ProfileUser_EditProfile;
    private LottieAnimationView arrowGoBackEditProfile;
    private InputMethodManager imm;
    private final String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
    private final Calendar myCalendar = Calendar.getInstance();
    private static DatePickerDialog.OnDateSetListener date;

    //  User information
    private int id_user;
    private String FirstName, LastName, FullName, _Email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user;

    //  Loading and IMAGE REQUEST
    private LoadingDialog loadingDialog;
    int PICK_IMAGE_REQUEST = 111;

    //  Firebase | Retrofit
    StorageReference storageReference;
    final Retrofit userRetrofit = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Ids();
        loadingDialog = new LoadingDialog(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            id_user = bundle.getInt("id_user");
            FullName = bundle.getString("name_user");
            _Email = bundle.getString("email_user");
            cpf_user = bundle.getString("cpf_user");
            address_user = bundle.getString("address_user");
            complement = bundle.getString("complement");
            zipcode = bundle.getString("zipcode");
            phone_user = bundle.getString("phone_user");
            birth_date = bundle.getString("birth_date");
            img_user = bundle.getString("img_user");
        }
        SetEditNamesChange();
        loadUserInfo();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //  Creating Calendar
        date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        //  Set Mask
        editProfile__CPFUser.addTextChangedListener(MaskEditUtil.mask(editProfile__CPFUser, MaskEditUtil.FORMAT_CPF));
        editProfile__PHONEUser.addTextChangedListener(MaskEditUtil.mask(editProfile__PHONEUser, MaskEditUtil.FORMAT_FONE));
        editProfile__birth_dateUser.addTextChangedListener(MaskEditUtil.mask(editProfile__birth_dateUser, MaskEditUtil.FORMAT_DATE));

        icon_ProfileUser_EditProfile.setOnClickListener(v -> {
            UserPermissions.validatePermissions(permissions, EditProfileActivity.this, 1);
            int GalleryPermission = ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (GalleryPermission == PackageManager.PERMISSION_GRANTED)
                OpenGallery();
        });

        btnEditAddress.setOnClickListener(v -> GotoEditAddress());

        editProfile__birth_dateUser.setOnClickListener(v -> ShowCalendar());

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

    private void GotoEditAddress() {
        btnEditAddress.setElevation(0);
        Intent goTo_AddressRegister = new Intent(EditProfileActivity.this, RegisterAddressActivity.class);
        goTo_AddressRegister.putExtra("id_user", id_user);
        goTo_AddressRegister.putExtra("name_user", FullName);
        goTo_AddressRegister.putExtra("email_user", _Email);
        goTo_AddressRegister.putExtra("cpf_user", cpf_user);
        goTo_AddressRegister.putExtra("address_user", address_user);
        goTo_AddressRegister.putExtra("complement", complement);
        goTo_AddressRegister.putExtra("zipcode", zipcode);
        goTo_AddressRegister.putExtra("phone_user", phone_user);
        goTo_AddressRegister.putExtra("birth_date", birth_date);
        goTo_AddressRegister.putExtra("img_user", img_user);
        startActivity(goTo_AddressRegister);
        finish();
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
                            ToastHelper.toast(EditProfileActivity.this, getString(R.string.couldnt_insert));
                            Log.d("ProfileUpload", Objects.requireNonNull(task.getException()).toString());
                        }
                        return storageReference .getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            img_user = downloadUri+"";
                            UpdateUserImage(id_user, img_user);
                        } else {
                            ToastHelper.toast(EditProfileActivity.this, getString(R.string.uploadFailed));
                            Log.d("ProfileUpload", Objects.requireNonNull(task.getException()).getMessage());
                            loadingDialog.dimissDialog();
                        }
                    });
                }
                else
                    ToastHelper.toast(EditProfileActivity.this, getString(R.string.select_an_image));
            } catch (Exception ex) {
                Warnings.showWeHaveAProblem(EditProfileActivity.this);
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

    private void loadUserInfo() {
        DtoUser user = MainActivity.getInstance().GetUserBaseInformation();
        FullName = user.getName_user();
        _Email = user.getEmail();
        cpf_user = user.getCpf_user();
        address_user = user.getAddress_user();
        complement = user.getComplement();
        zipcode = user.getZipcode();
        phone_user = user.getPhone_user();
        birth_date = user.getBirth_date();
        img_user = user.getImg_user();
        if (img_user == null || img_user.equals(""))
            Log.d("UserStatus", "Not User image");
        else
            Picasso.get().load(img_user).into(icon_ProfileUser_EditProfile);
        String[] FullUserName = FullName.split(" ");
        txt_userName_EditProfile.setText(FullName);
        txt_email_EditProfile.setText(_Email);
        editProfile_FirstNameUser.setText(FullUserName[0]);
        editProfile__LastNameUser.setText(FullUserName[1]);
        editProfile__CPFUser.setText(cpf_user);
        editProfile__PHONEUser.setText(phone_user);
        editProfile__birth_dateUser.setText(birth_date);
    }

    private void GoBackToProfile() {
        Intent goTo_Profile = new Intent(EditProfileActivity.this, ProfileActivity.class);
        goTo_Profile.putExtra("id_user", id_user);
        goTo_Profile.putExtra("name_user", FullName);
        goTo_Profile.putExtra("email_user", _Email);
        goTo_Profile.putExtra("cpf_user", cpf_user);
        goTo_Profile.putExtra("address_user", address_user);
        goTo_Profile.putExtra("complement", complement);
        goTo_Profile.putExtra("zipcode", zipcode);
        goTo_Profile.putExtra("phone_user", phone_user);
        goTo_Profile.putExtra("birth_date", birth_date);
        goTo_Profile.putExtra("img_user", img_user);
        startActivity(goTo_Profile);
        finish();
    }

    private void UpdateUserImage(int id_user, String img_user) {
        UserServices userServices = userRetrofit.create(UserServices.class);
        DtoUser userInfo = new DtoUser(id_user, img_user);
        Call<DtoUser> userCall = userServices.updateProfileImage(userInfo);
        userCall.enqueue(new Callback<DtoUser>() {
            @Override
            public void onResponse(@NonNull Call<DtoUser> call, @NonNull Response<DtoUser> response) {
                if (response.code() == 200){
                    loadingDialog.dimissDialog();
                    Picasso.get().load(img_user).into(icon_ProfileUser_EditProfile);
                }else{
                    loadingDialog.dimissDialog();
                    Warnings.showWeHaveAProblem(EditProfileActivity.this);
                }
            }
            @Override
            public void onFailure(@NonNull Call<DtoUser> call, @NonNull Throwable t) {
                loadingDialog.dimissDialog();
                Warnings.showWeHaveAProblem(EditProfileActivity.this);
            }
        });
    }

    private void EditUserProfile() {
        loadingDialog.startLoading();
        FirstName = editProfile_FirstNameUser.getText().toString().replaceAll(" ", "");
        LastName = editProfile__LastNameUser.getText().toString().replaceAll(" ", "");
        FullName = FirstName + " " + LastName;
        cpf_user = editProfile__CPFUser.getText().toString();
        phone_user = editProfile__PHONEUser.getText().toString();
        birth_date = editProfile__birth_dateUser.getText().toString();

        UserServices userServices = userRetrofit.create(UserServices.class);
        DtoUser userInfo = new DtoUser(FullName, cpf_user, address_user, complement, zipcode, phone_user, birth_date, id_user);
        Call<DtoUser> userCall = userServices.updateProfile(userInfo);
        userCall.enqueue(new Callback<DtoUser>() {
            @Override
            public void onResponse(@NonNull Call<DtoUser> call, @NonNull Response<DtoUser> response) {
                loadingDialog.dimissDialog();
                if (response.code() == 200){
                    Picasso.get().load(img_user).into(icon_ProfileUser_EditProfile);
                    GoBackToProfile();
                }else if(response.code() == 406)
                    Warnings.show_BadUsername_Warning(EditProfileActivity.this);
                else
                    Warnings.showWeHaveAProblem(EditProfileActivity.this);
            }
            @Override
            public void onFailure(@NonNull Call<DtoUser> call, @NonNull Throwable t) {
                loadingDialog.dimissDialog();
                Warnings.showWeHaveAProblem(EditProfileActivity.this);
            }
        });
    }

    private void ShowCalendar(){
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String dateSelected = sdf.format(myCalendar.getTime());

        editProfile__birth_dateUser.setText(dateSelected);
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
        cardBtn_ConfirmEditProfile = findViewById(R.id.cardBtn_ConfirmEditProfile);
        icon_ProfileUser_EditProfile = findViewById(R.id.icon_ProfileUser_EditProfile);
        editProfile__birth_dateUser = findViewById(R.id.editProfile__birth_dateUser);
        editProfile__PHONEUser = findViewById(R.id.editProfile__PHONEUser);
        btnEditAddress = findViewById(R.id.btnEditAddress);
        btnEditAddress.setElevation(10);
        cardBtn_ConfirmEditProfile.setElevation(20);
    }

    @SuppressWarnings("deprecation")
    private void OpenGallery() {
        Intent openGallery = new Intent();
        openGallery.setType("image/*");
        openGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(openGallery, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onBackPressed() { GoBackToProfile(); }
}