package com.example.chenhuayu.wifihelper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class NetSpeedTimer {
    private long defaultDelay = 1000;
    private long defaultPeriod = 1000;
    private static final int ERROR_CODE = -101011010;
    private int mMsgWhat = ERROR_CODE;
    private NetSpeed mNetSpeed;
    private Handler mHandler;
    private Context mContext;
    private SpeedTimerTask mSpeedTimerTask;

    public static final int NET_SPEED_TIMER_RX_DEFAULT = 101010;
    public static final int NET_SPEED_TIMER_TX_DEFAULT = 101011;

    public NetSpeedTimer(Context context, NetSpeed netSpeed, Handler handler) {
        this.mContext = context;
        this.mNetSpeed = netSpeed;
        this.mHandler = handler;
    }

    public NetSpeedTimer setDelayTime(long delay) {
        this.defaultDelay = delay;
        return this;
    }

    public NetSpeedTimer setPeriodTime(long period) {
        this.defaultPeriod = period;
        return this;
    }

    public NetSpeedTimer setHanderWhat(int what) {
        this.mMsgWhat = what;
        return this;
    }

    /**
     * 开启获取网速定时器
     */
    public void startSpeedTimer() {
        Timer timer = new Timer();
        mSpeedTimerTask = new SpeedTimerTask(mContext, mNetSpeed, mHandler,
                mMsgWhat);
        timer.schedule(mSpeedTimerTask, defaultDelay, defaultPeriod);
    }

    /**
     * 关闭定时器
     */
    public void stopSpeedTimer() {
        if (null != mSpeedTimerTask) {
            mSpeedTimerTask.cancel();
        }
    }

    /**
     * @author 静态内部类
     */
    private static class SpeedTimerTask extends TimerTask {
        private int mMsgWhat;
        private NetSpeed mNetSpeed;
        private Handler mHandler;
        private Context mContext;

        public SpeedTimerTask(Context context, NetSpeed netSpeed,
                              Handler handler, int what) {
            this.mContext = context;
            this.mHandler = handler;
            this.mNetSpeed = netSpeed;
            this.mMsgWhat = what;
        }

        @Override
        public void run() {
            if (null != mNetSpeed && null != mHandler) {
                Message obtainMessage = mHandler.obtainMessage();
                obtainMessage.what = mMsgWhat;
                if (mMsgWhat == NET_SPEED_TIMER_RX_DEFAULT) {
                    obtainMessage.obj = mNetSpeed.getRxSpeed(mContext
                            .getApplicationInfo().uid);
                    mHandler.sendMessage(obtainMessage);
                } else if (mMsgWhat == NET_SPEED_TIMER_TX_DEFAULT) {
                    obtainMessage.obj = mNetSpeed.getTxSpeed(mContext
                            .getApplicationInfo().uid);
                    mHandler.sendMessage(obtainMessage);
                }
            }
        }
    }

}
