package com.guzman.realidadvirtual.UTILS;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;


public class Permisos {

    private Activity activity;
    public static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 3;

    public Permisos(Activity activity) {


        this.activity = activity;
    }

    private String[] permisos = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            };




    public void SolicitarPermisos() {
        if (ActivityCompat.checkSelfPermission(activity, permisos[0]) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, permisos, MULTIPLE_PERMISSIONS_REQUEST_CODE);
        }
    }

    public boolean VerficarPermisos() {
        return (ActivityCompat.checkSelfPermission(activity, permisos[0]) == PackageManager.PERMISSION_GRANTED);
    }


}
