package com.example.chenhuayu.wifihelper;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Window;


import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 设备信息工具类
 * Created by qylk on 15/10/16.
 */
public class DeviceUtils {

    public static final int ROM_CODE_UNKNOWN = 0;
    public static final int ROM_CODE_MIUI = 1;
    public static final int ROM_CODE_EMUI = 2;
    public static final int ROM_CODE_FLYME = 4;
    public static final int ROM_CODE_VIVO = 5;
    public static final int ROM_CODE_OPPO = 6;


    /**
     * 获取当前设备型号
     * @param context
     * @return
     */
    public static int getROMCode(Context context) {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            String miuiCode = prop.getProperty("ro.miui.ui.version.code", null);
            String miuiName = prop.getProperty("ro.miui.ui.version.name", null);
            if (!TextUtils.isEmpty(miuiCode) && !TextUtils.isEmpty(miuiName)) {
                return ROM_CODE_MIUI;
            }
            if ("Xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
                return ROM_CODE_MIUI;
            }

            String emuiName = prop.getProperty("ro.build.version.emui", null);
            if (!TextUtils.isEmpty(emuiName)) {
                return ROM_CODE_EMUI;
            }
            if ("HUAWEI".equalsIgnoreCase(Build.MANUFACTURER)) {
                return ROM_CODE_EMUI;
            }

            if (!TextUtils.isEmpty(prop.getProperty("ro.build.version.opporom", null))) {
                return ROM_CODE_OPPO;
            }
            if ("OPPO".equalsIgnoreCase(Build.MANUFACTURER)) {
                return ROM_CODE_OPPO;
            }

            if (!TextUtils.isEmpty(prop.getProperty("ro.vivo.os.version", null))) {
                return ROM_CODE_VIVO;
            }
            if ("VIVO".equalsIgnoreCase(Build.MANUFACTURER)) {
                return ROM_CODE_VIVO;
            }
            try {
                if (Build.class.getMethod("hasSmartBar") != null) {
                    return ROM_CODE_FLYME;
                }
            } catch (NoSuchMethodException e) {
            }
            if ("Meizu".equalsIgnoreCase(Build.MANUFACTURER)) {
                return ROM_CODE_FLYME;
            }
        } catch (Exception e) {
        }
        return ROM_CODE_UNKNOWN;
    }

    /**
     * 获取当前设备deviceId
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = mTelephony.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    /**
     * 兼容刘海屏全屏浏览
     * @param activity
     */
    public static void supportNotchScreen(Activity activity) {
        // 小米手机兼容方案
        try {
            int flag = 0x00000100 | 0x00000200 | 0x00000400;
            Method method = Window.class.getMethod("addExtraFlags",
                    int.class);
            method.invoke(activity.getWindow(), flag);
        } catch (Throwable e) {
        }
    }

    /**
     *  获取当前设备mac地址信息
     * @param context
     * @return
     */
    public static String getWifiMac(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            return wifiInfo.getMacAddress();
        } else {
            return null;
        }
    }

    /**
     * 判断设置是否note3
     * @return
     */
    public static boolean isGNote3() {
        return "SM-N7508V".equals(Build.MODEL);
    }


    private static class BuildProperties {

        private final Properties properties;

        private BuildProperties() throws IOException {
            properties = new Properties();
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
                properties.load(fin);
            } catch (IOException e) {
            } finally {
                Closeable closeable = (Closeable)fin;
                try {
                    if (closeable != null) {
                        closeable.close();
                    }
                } catch (IOException ioe) {
                    // ignore
                }
            }
        }

        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }

        public String getProperty(final String name) {
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty() {
            return properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return properties.keys();
        }

        public Set<Object> keySet() {
            return properties.keySet();
        }

        public int size() {
            return properties.size();
        }

        public Collection<Object> values() {
            return properties.values();
        }

        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }
    }


}