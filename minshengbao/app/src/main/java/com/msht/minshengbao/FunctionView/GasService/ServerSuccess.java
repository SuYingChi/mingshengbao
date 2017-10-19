package com.msht.minshengbao.FunctionView.GasService;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.GuideActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;

public class ServerSuccess extends BaseActivity {
    private View Rfirst,Rsecond;
    private final int SPLASH_DISPLAY_LENGHT=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_server_success);
        findViewById(R.id.id_goback).setVisibility(View.GONE);
        Intent naviga=getIntent();
        String navi=naviga.getStringExtra("navigation");
        boolean Firstsever= SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.First_server, true);
        boolean booleanfirst=naviga.getBooleanExtra("boolean",false);
        setCommonHeader(navi);
        mPageName ="服务提交成功";
        Rfirst=findViewById(R.id.id_re_first);
        Rsecond=findViewById(R.id.id_re_second);
        if (booleanfirst){
            if (Firstsever){
                Rsecond.setVisibility(View.GONE);
                Rfirst.setVisibility(View.VISIBLE);
                SharedPreferencesUtil.putBoolean(ServerSuccess.this, SharedPreferencesUtil.First_server, false);
            }else {
                Rsecond.setVisibility(View.VISIBLE);
                Rfirst.setVisibility(View.GONE);
            }
        }else {
            Rsecond.setVisibility(View.VISIBLE);
            Rfirst.setVisibility(View.GONE);
        }
        initEvent();
    }
    private void initEvent() {
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                ServerSuccess.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
