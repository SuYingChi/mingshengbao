package com.msht.minshengbao.FunctionView.repairService;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.FunctionView.GasService.ServerSuccess;
import com.msht.minshengbao.R;
import com.umeng.analytics.MobclickAgent;

public class PublishSuccess extends AppCompatActivity {
    private final String mPageName="提交订单";
    private Context mContext;
    private final int SPLASH_DISPLAY_LENGHT=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_success);
        initHeader();
        initEvent();
    }
    private void initEvent() {
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                PublishSuccess.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
    private void initHeader() {
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.tv_navigation)).setText(mPageName);
    }

    @Override
    public void onResume() {
        super.onResume();
          MobclickAgent.onPageStart(mPageName);
         MobclickAgent.onResume(mContext);
    }
    @Override
    protected void onPause() {
        super.onPause();
         MobclickAgent.onPageEnd(mPageName);
         MobclickAgent.onPause(mContext);
    }
}
