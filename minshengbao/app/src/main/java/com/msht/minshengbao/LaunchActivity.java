package com.msht.minshengbao;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.msht.minshengbao.FunctionView.MainActivity;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;

public class LaunchActivity extends AppCompatActivity {
    private Context mContext;
    private final String mPageName = "启动页";
    boolean isFirstIn = false;
    private static final int GO_HOME = 1;
    private static final int GO_GUIDE = 2;
    private static final long SPLASH_DELAY_MILLIS = 3000;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        mContext = this;
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        if (isFirstOpen){
            SharedPreferencesUtil.Clear(this,"AppData");//清除原有数据
        }
        init();
    }
    private void init() {
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (isFirstOpen) {
            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        }
    }
    private void goHome() {
        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
        LaunchActivity.this.startActivity(intent);
        LaunchActivity.this.finish();
    }
    private void goGuide() {
        Intent intent = new Intent(LaunchActivity.this, GuideActivity.class);
        LaunchActivity.this.startActivity(intent);
        LaunchActivity.this.finish();
    }
}
