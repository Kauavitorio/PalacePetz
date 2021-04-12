package co.ex.palacepetz.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TextView;

import co.ex.palacepetz.R;

/**
 *  Copyright (c) 2021 Kauã Vitório
 *  Official repository https://github.com/Kauavitorio/PalacePetz
 *  Responsible developer: https://github.com/Kauavitorio
 * @author Kaua Vitorio
 **/

public class LoadingDialog {

    private final Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity myactivity){
        activity = myactivity;
    }

    @SuppressLint("InflateParams")
    public void  startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.adapter_custom_loading,null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

   public void dimissDialog(){
        dialog.dismiss();
    }
}
