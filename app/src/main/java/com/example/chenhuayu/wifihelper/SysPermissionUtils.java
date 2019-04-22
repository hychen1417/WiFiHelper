package com.example.chenhuayu.wifihelper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

public class SysPermissionUtils {
    public static boolean shouldRequestPermission(Context context, String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = false;
        try {
            if (PackageUtils.getTargetSdkVersion(context) >= Build.VERSION_CODES.M) {
                result = ContextCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED;
            } else {
                result = PermissionChecker.checkSelfPermission(context, permission)
                        != PermissionChecker.PERMISSION_GRANTED;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int checkIntPermissionGranted(Context context, String permission) {
        return shouldRequestPermission(context, permission) ? 0 : 1;
    }

    public static boolean checkPermissionGranted(Context context, String permission) {
        return !shouldRequestPermission(context, permission);
    }

    public static boolean checkNotifyPermissionEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            return manager.areNotificationsEnabled();
        }
        return true;
    }
}
