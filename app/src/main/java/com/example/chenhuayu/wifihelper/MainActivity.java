package com.example.chenhuayu.wifihelper;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import static com.example.chenhuayu.wifihelper.NetSpeedTimer.NET_SPEED_TIMER_RX_DEFAULT;
import static com.example.chenhuayu.wifihelper.NetSpeedTimer.NET_SPEED_TIMER_TX_DEFAULT;
import static com.example.chenhuayu.wifihelper.utils.getMyWifiInfo;
import static com.example.chenhuayu.wifihelper.utils.getScanWifiInfo;

public class MainActivity extends AppCompatActivity {
    private String TAG = "WifiHelper";
    private Button getWifiState, getWifiLevel, getTxRate, getRxRate;
    private TextView wifiState, wifiLevel, txRate, rxRate;
    private NetSpeedTimer mNetSpeedTimer;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NET_SPEED_TIMER_RX_DEFAULT) {
                String speed = (String) msg.obj;
                rxRate.setText(speed);
            } else if (msg.what == NetSpeedTimer.NET_SPEED_TIMER_TX_DEFAULT) {
                String speed = (String) msg.obj;
                txRate.setText(speed);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        getWifiState = findViewById(R.id.btn_get_wifi_state);
        wifiState = findViewById(R.id.tv_wifi_state);

        getWifiLevel = findViewById(R.id.btn_get_wifi_level);
        wifiLevel = findViewById(R.id.tv_wifi_level);

        getTxRate = findViewById(R.id.btn_get_wifi_tx_rate);
        txRate = findViewById(R.id.tv_wifi_tx_rate);

        getRxRate = findViewById(R.id.btn_get_wifi_rx_rate);
        rxRate = findViewById(R.id.tv_wifi_rx_rate);

        getWifiState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiState.setText(getMyWifiInfo(MainActivity.this));
            }
        });

        getWifiLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiLevel.setText(getScanWifiInfo(MainActivity.this));
            }
        });

        getRxRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNewWork(NET_SPEED_TIMER_RX_DEFAULT);
            }
        });

        getTxRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNewWork(NET_SPEED_TIMER_TX_DEFAULT);
            }
        });
    }


    private void initNewWork(int what) {
        //创建NetSpeedTimer实例
        mNetSpeedTimer = new NetSpeedTimer(this, new NetSpeed(), mHandler)
                .setHanderWhat(what)
                .setDelayTime(1000)
                .setPeriodTime(2000);
        //在想要开始执行的地方调用该段代码
        mNetSpeedTimer.startSpeedTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetSpeedTimer != null) {
            mNetSpeedTimer.stopSpeedTimer();
        }
        mHandler.removeCallbacksAndMessages(null);
    }
}
