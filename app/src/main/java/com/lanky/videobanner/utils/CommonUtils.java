package com.lanky.videobanner.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

/**
 * LankyBin create on 2020/11/23
 */
public class CommonUtils {
    public static boolean requestPms(Context context) {
        boolean granted = false;
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            granted = true;
        }
        return granted;
    }
}
