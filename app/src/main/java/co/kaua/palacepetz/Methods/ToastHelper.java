package co.kaua.palacepetz.Methods;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import co.kaua.palacepetz.R;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 *  @author Kaua Vitorio
 **/

@SuppressWarnings({"deprecation", "UseCompatLoadingForDrawables"})
public abstract class ToastHelper {

    public static void toast(@NonNull Activity activity, String msg){
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.adapter_custom_toast, activity.findViewById(R.id.custom_toast_layout));
        layout.setBackgroundDrawable(activity.getDrawable(R.drawable.background_custom_toast));
        TextView tv = layout.findViewById(R.id.txt_custom_toast);
        tv.setText(msg);
        Toast toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
