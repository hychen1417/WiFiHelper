package com.example.chenhuayu.wifihelper;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;


import static com.example.chenhuayu.wifihelper.SysPermissionUtils.checkPermissionGranted;

public class PermissionCheckHelper {
    public static Dialog showPermissionWaningDialog(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        int romCode = DeviceUtils.getROMCode(context);
        if (romCode != DeviceUtils.ROM_CODE_OPPO && romCode != DeviceUtils.ROM_CODE_VIVO && Build.VERSION.SDK_INT >= 21) {
            builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PackageUtils.openAppDetail(context);
                }
            });
        }
        builder.setNegativeButton("好的", null);
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static boolean checkPermissionAndShowDialog(final Context context, String permission) {
        if (context == null) {
            return true;
        }
        if (!checkPermissionGranted(context, permission)) {
            if (Manifest.permission.CAMERA.equals(permission)) {
                showPermissionWaningDialog(context, R.string.permission_camera_message).show();
            } else if (Manifest.permission.WRITE_CALENDAR.equals(permission)) {
                showPermissionWaningDialog(context, R.string.permission_calendar_message).show();
            } else if (Manifest.permission.RECORD_AUDIO.equals(permission)) {
                showPermissionWaningDialog(context, R.string.permission_record_message).show();
            } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                showPermissionWaningDialog(context, R.string.permission_storage_message).show();
            } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                showPermissionWaningDialog(context, R.string.permission_storage_message).show();
            } else if (Manifest.permission.READ_CALENDAR.equals(permission)) {
            } else {
                return true;
            }
            return false;
        }
        return true;
    }

}
