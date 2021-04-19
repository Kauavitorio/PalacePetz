package com.kaua.palacepetz.Firebase;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Conf_Firebase {

    private static FirebaseAnalytics firebaseAnalytics;

    public static FirebaseAnalytics getFirebaseAnalytics(Context context){
        if (firebaseAnalytics == null){
            firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        return firebaseAnalytics;
    }
}
