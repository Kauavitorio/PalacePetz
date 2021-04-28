package co.kaua.palacepetz.Firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfFirebase {

    private static FirebaseAnalytics firebaseAnalytics;
    private static StorageReference firebaseStorage;
    private static FirebaseAuth firebaseAuth;

    public static FirebaseAnalytics getFirebaseAnalytics(Context context){
        if (firebaseAnalytics == null){
            firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        return firebaseAnalytics;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static StorageReference getFirebaseStorage(){
        if (firebaseStorage == null){
            firebaseStorage = FirebaseStorage.getInstance().getReference();
            Log.d("ProfileUpload", firebaseStorage + ""); // gs://palacepetz-22a1b.appspot.com
        }
        return firebaseStorage;
    }
}
