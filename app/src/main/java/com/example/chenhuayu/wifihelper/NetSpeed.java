package com.example.chenhuayu.wifihelper;

import android.net.TrafficStats;

public class NetSpeed {

    private static final String TAG = NetSpeed.class.getSimpleName();
    private long lastTotalRxBytes = 0;
    private long lastTimeRxStamp = 0;
    private long lastTotalTxBytes = 0;
    private long lastTimeTxStamp = 0;

    public String getRxSpeed(int uid) {
        long nowTotalRxBytes = getTotalRxBytes(uid);
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeRxStamp));//毫秒转换
        lastTimeRxStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return String.valueOf(speed) + " kb/s";
    }

    public String getTxSpeed(int uid) {
        long nowTotalTxBytes = getTotalTxBytes(uid);
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalTxBytes - lastTotalTxBytes) * 1000 / (nowTimeStamp - lastTimeTxStamp));//毫秒转换
        lastTimeTxStamp = nowTimeStamp;
        lastTotalTxBytes = nowTotalTxBytes;
        return String.valueOf(speed) + " kb/s";
    }


    private long getTotalRxBytes(int uid) {
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB
    }

    private long getTotalTxBytes(int uid) {
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalTxBytes() / 1024);//转为KB
    }
}
