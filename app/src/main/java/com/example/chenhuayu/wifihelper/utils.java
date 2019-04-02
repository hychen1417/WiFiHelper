package com.example.chenhuayu.wifihelper;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class utils {

    /**
     * 获取当前设备所连接wifi信息
     * @param context
     * @return
     */
    public static String getMyWifiInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        WifiManager mWifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifi != null && mWifi.isWifiEnabled()) {
            WifiInfo wifiInfo = mWifi.getConnectionInfo();
            DhcpInfo dhcpInfo = mWifi.getDhcpInfo();
            sb.append("SSID:" + wifiInfo.getSSID()).append("\n")
                    .append("BSSID:" + wifiInfo.getBSSID()).append("\n")
                    .append("RSSI:" + wifiInfo.getRssi()).append("\n")
                    .append("Link speed:" + wifiInfo.getLinkSpeed()).append("\n")
                    .append("Channel:" + getCurrentChannel(context)).append("\n")
                    .append("IP addr:" + intToIp(dhcpInfo.ipAddress)).append("\n")
                    .append("gateway:" + intToIp(dhcpInfo.gateway)).append("\n");
        }
        return sb.toString();
    }

    //int值转换成IP地址
    private static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

    /**
     * 获取当前WiFi频道
     *
     * @param context
     * @return
     */
    public static int getCurrentChannel(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            List<ScanResult> scanResults = wifiManager.getScanResults();
            for (ScanResult result : scanResults) {
                if (result.BSSID.equalsIgnoreCase(wifiInfo.getBSSID())
                        && result.SSID.equalsIgnoreCase(wifiInfo.getSSID()
                        .substring(1, wifiInfo.getSSID().length() - 1))) {
                    return getChannelByFrequency(result.frequency);
                }
            }
        }

        return -1;
    }

    /**
     * 根据频率获得信道
     *
     * @param frequency
     * @return
     */
    public static int getChannelByFrequency(int frequency) {
        int channel = -1;
        switch (frequency) {
            case 2412:
                channel = 1;
                break;
            case 2417:
                channel = 2;
                break;
            case 2422:
                channel = 3;
                break;
            case 2427:
                channel = 4;
                break;
            case 2432:
                channel = 5;
                break;
            case 2437:
                channel = 6;
                break;
            case 2442:
                channel = 7;
                break;
            case 2447:
                channel = 8;
                break;
            case 2452:
                channel = 9;
                break;
            case 2457:
                channel = 10;
                break;
            case 2462:
                channel = 11;
                break;
            case 2467:
                channel = 12;
                break;
            case 2472:
                channel = 13;
                break;
            case 2484:
                channel = 14;
                break;
            case 5745:
                channel = 149;
                break;
            case 5765:
                channel = 153;
                break;
            case 5785:
                channel = 157;
                break;
            case 5805:
                channel = 161;
                break;
            case 5825:
                channel = 165;
                break;
        }
        return channel;
    }

    /**
     * 获取扫描到WiFi的信息
     * @param context
     * @return
     */
    public static String getScanWifiInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        WifiManager mWifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifi != null && mWifi.isWifiEnabled()) {
            mWifi.startScan();
            List<ScanResult> scanResults = mWifi.getScanResults();  //getScanResults() 扫描到的当前设备的WiFi列表
            List<ScanResult> scanResultsNew = new ArrayList<>();
            for (ScanResult result : scanResults) {
                if (!TextUtils.isEmpty(result.SSID) && !result.capabilities.contains("[IBSS]") && !containsName(scanResultsNew, result))
                    scanResultsNew.add(result);
            }
            sb.append("wifi总数为:" + scanResultsNew.size()).append("\n");
            for (ScanResult scanResult : scanResultsNew) {
                sb.append("SSID:" + scanResult.SSID).append("\n")
                        .append("BSSID" + scanResult.BSSID).append("\n")
                        .append("RSSI:" + scanResult.level).append("\n")
                        .append("CHANNEL:" + getChannelByFrequency(scanResult.frequency)).append("\n")
                        .append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 去掉扫描重复的wifi
     * @param list
     * @param scanResult
     * @return
     */
    private static boolean containsName(List<ScanResult> list, ScanResult scanResult) {
        for (ScanResult result : list) {
            if (!TextUtils.isEmpty(result.SSID) && result.SSID.equals(scanResult.SSID) && result.capabilities.equals(scanResult.capabilities)) {
                return true;
            }
        }
        return false;
    }


}
