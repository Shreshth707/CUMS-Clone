package com.example.cogentwebservicesproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class ProgressBar {
    private Activity activity;
    private AlertDialog alertDialog;

    ProgressBar(Activity activity){
        this.activity = activity;
    }

    void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.progressbar,null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismissDialog(){
        alertDialog.dismiss();
    }
}
