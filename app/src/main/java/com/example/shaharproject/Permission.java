package com.example.shaharproject;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by שחר on 05/09/2018.
 */

public class Permission {
    private Activity activity;

    public Permission(Activity activity)
    {
        this.activity=activity;
    }

    // Storage Permissions
    private static final int REQUEST_CODE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS


    };


    public void verifyPermissions()
    {
        // Check if we have write permission
        boolean permissioned=true;
        for(String permission:PERMISSIONS_STORAGE)
        {
            int check= ActivityCompat.checkSelfPermission(activity, permission);
            if(check!= PackageManager.PERMISSION_GRANTED)
            {
                permissioned = false;
                break;
            }

        }

        if (!permissioned)
        {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_CODE
            );
        }
    }
}
