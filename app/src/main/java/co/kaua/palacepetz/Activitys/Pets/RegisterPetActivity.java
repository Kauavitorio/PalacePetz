package co.kaua.palacepetz.Activitys.Pets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import co.kaua.palacepetz.Adapters.LoadingDialog;
import co.kaua.palacepetz.Firebase.ConfFirebase;
import co.kaua.palacepetz.Methods.Userpermissions;
import co.kaua.palacepetz.R;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterPetActivity extends AppCompatActivity {
    private LottieAnimationView arrowGoBackRegisterPet;
    private CircleImageView icon_RegisterPet_PetImage;
    private CardView btnUploadPetImage, cardBtn_RegisterPet;
    private EditText Register_PetName;
    private final String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE };


    //  User information
    private int id_user;
    private String name_user, _Email, cpf_user, address_user, complement, zipcode, phone_user, birth_date, img_user;

    //  Pet Info
    private String petName, img_pet;

    //  Firebase | Retrofit
    StorageReference storageReference;
    //  Loading and IMAGE REQUEST
    private LoadingDialog loadingDialog;
    int PICK_IMAGE_REQUEST = 111;
    final Retrofit userRetrofit = new Retrofit.Builder()
            .baseUrl("https://palacepetzapi.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet);
        Ids();

        Bundle bundle = getIntent().getExtras();
        id_user = bundle.getInt("id_user");

        arrowGoBackRegisterPet.setOnClickListener(v -> finish());

        btnUploadPetImage.setOnClickListener(v -> OpenGallery());

        icon_RegisterPet_PetImage.setOnClickListener(v -> OpenGallery());

        cardBtn_RegisterPet.setOnClickListener(v -> {
            petName = Register_PetName.getText().toString();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageReference = ConfFirebase.getFirebaseStorage().child("user").child("pets").child("User_" + id_user + "_" + petName);
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
                            Toast.makeText(RegisterPetActivity.this, R.string.couldnt_insert , Toast.LENGTH_SHORT).show();
                            Log.d("ProfileUpload", Objects.requireNonNull(task.getException()).toString());
                        }
                        return storageReference .getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            img_pet = downloadUri+"";
                            Picasso.get().load(img_pet).into(icon_RegisterPet_PetImage);
                        } else {
                            Toast.makeText(this, getString(R.string.uploadFailed), Toast.LENGTH_SHORT).show();
                            Log.d("ProfileUpload", Objects.requireNonNull(task.getException()).getMessage());
                            loadingDialog.dimissDialog();
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterPetActivity.this, R.string.select_an_image, Toast.LENGTH_SHORT).show();
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

    private void OpenGallery() {
        //noinspection ConstantConditions
        if (petName.equals(" ") || petName.length() <= 0 || petName == null)
            Toast.makeText(this, getString(R.string.first_insert_petName), Toast.LENGTH_SHORT).show();
        else{
            Userpermissions.validatePermissions(permissions, RegisterPetActivity.this, 1);
            int GalleryPermission = ContextCompat.checkSelfPermission(RegisterPetActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (GalleryPermission == PackageManager.PERMISSION_GRANTED) {
                Intent openGallery = new Intent();
                openGallery.setType("image/*");
                openGallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(openGallery, "Select Image"), PICK_IMAGE_REQUEST);
            }
        }
    }

    private void Ids() {
        loadingDialog = new LoadingDialog(this);
        arrowGoBackRegisterPet = findViewById(R.id.arrowGoBackRegisterPet);
        icon_RegisterPet_PetImage = findViewById(R.id.icon_RegisterPet_PetImage);
        btnUploadPetImage = findViewById(R.id.btnUploadPetImage);
        cardBtn_RegisterPet = findViewById(R.id.cardBtn_RegisterPet);
        Register_PetName = findViewById(R.id.Register_PetName);
    }
}