package com.example.chenhuayu.wifihelper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

public class PackageUtils {
    public static final String PACKAGE_NAME_WEIXIN = "com.tencent.mm";
    /**
     * com.amap.api.v2.apikey
     */
    private static final String DX_KEY_META_MAP_API = "com.amap.api.v2.apikey";

    public static String getVersionName(Context context) {

        String versionName = "";
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionName = pinfo.versionName;
        } catch (Exception e) {
        }
        return versionName;
    }

    public static void gotoMarket(Context context) {
        String packageName = context.getPackageName();
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getVersionCode(Context context) {
        int versionCode = -1;
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionCode = pinfo.versionCode;
        } catch (Exception e) {
        }

        return versionCode;
    }

    public static int getTargetSdkVersion(Context context) {
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.applicationInfo.targetSdkVersion;
        } catch (Exception e) {
            return 22;
        }
    }

    public static void openAppDetail(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getAppName(Context context, String pkgName) {
        String appName = "";
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_CONFIGURATIONS);
            appName = context.getPackageManager().getApplicationLabel(pinfo.applicationInfo).toString();
        } catch (Exception e) {
        }
        return appName;
    }

    public static String getAppNameId(Context context) {
        String appId = "xm";
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            appId = bundle.getString("APP_NAME");
        } catch (Exception e) {
        }
        return appId;
    }

    public static String getAMapValueByApikey(Context context) {
        String value = "";
        try {
            ApplicationInfo pinfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = pinfo.metaData.getString(DX_KEY_META_MAP_API);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    static String getMd5(Signature[] signatures) {
        String md5 = "";
        try {
            int numSignatures = signatures.length;
            for (int i = 0; i < numSignatures; i++) {
                Signature signature = signatures[i];
                byte[] signatureBytes = signature.toByteArray();
                if (signatureBytes != null) {
                    md5 = HashUtils.MD5(signatureBytes);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            md5 = e.getMessage();
        }
        return md5;
    }

    public static boolean checkApkValid(Context context, File file) {
        try {
            boolean signatureMatch = true;
            PackageInfo currentPackageInfo = getPackageInfoWithAndroidApi(context);
            PackageInfo targetPackageInfo = getPackageInfoWithAndroidApi(context, file, PackageManager.GET_SIGNATURES);
            if (targetPackageInfo == null) {
                targetPackageInfo = getPackageInfoWithAndroidApi(context, file, 0);
            } else {
                String targetMd5 = getMd5(targetPackageInfo.signatures);
                String currentMd5 = getMd5(currentPackageInfo.signatures);
                signatureMatch = TextUtils.equals(targetMd5, currentMd5);
            }
            return TextUtils.equals(targetPackageInfo.packageName, currentPackageInfo.packageName)
                    && signatureMatch
                    && targetPackageInfo.versionCode >= currentPackageInfo.versionCode;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    static PackageInfo getPackageInfoWithAndroidApi(Context context, @NonNull File file, int flags) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        if (!file.exists()) {
            return null;
        }
        packageInfo = packageManager.getPackageArchiveInfo(file.getAbsolutePath(), flags);
        if (packageInfo != null) {
            packageInfo.applicationInfo.sourceDir = file.getAbsolutePath();
            packageInfo.applicationInfo.publicSourceDir = file.getAbsolutePath();
        }
        return packageInfo;
    }

    static PackageInfo getPackageInfoWithAndroidApi(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            return packageInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isActivityFinish(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return true;
        }

        return false;
    }
}
