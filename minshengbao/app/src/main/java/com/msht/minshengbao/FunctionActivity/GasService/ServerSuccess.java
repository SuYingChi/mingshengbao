package com.msht.minshengbao.FunctionActivity.GasService;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;

public class ServerSuccess extends BaseActivity {
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
        if (booleanfirst){
            if (Firstsever){
                findViewById(R.id.id_layout_second).setVisibility(View.GONE);
                findViewById(R.id.id_re_first).setVisibility(View.VISIBLE);
                SharedPreferencesUtil.putBoolean(ServerSuccess.this, SharedPreferencesUtil.First_server, false);
            }else {
                findViewById(R.id.id_layout_second).setVisibility(View.VISIBLE);
                findViewById(R.id.id_re_first).setVisibility(View.GONE);
            }
        }else {
            findViewById(R.id.id_layout_second).setVisibility(View.VISIBLE);
            findViewById(R.id.id_re_first).setVisibility(View.GONE);
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
