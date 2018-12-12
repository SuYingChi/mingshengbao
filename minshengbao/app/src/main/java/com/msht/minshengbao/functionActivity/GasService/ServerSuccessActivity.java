package com.msht.minshengbao.functionActivity.GasService;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/9/15  
 */
public class ServerSuccessActivity extends BaseActivity {
    private static final int SPLASH_DISPLAY_TIME=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        mPageName ="服务提交成功";
        setContentView(R.layout.activity_server_success);
        findViewById(R.id.id_goback).setVisibility(View.GONE);
        Intent data=getIntent();
        String navigation=data.getStringExtra("navigation");
        boolean firstSever= SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.First_server, true);
        boolean booleanFirst=data.getBooleanExtra("boolean",false);
        setCommonHeader(navigation);

        if (booleanFirst){
            if (firstSever){
                findViewById(R.id.id_layout_second).setVisibility(View.GONE);
                findViewById(R.id.id_re_first).setVisibility(View.VISIBLE);
                SharedPreferencesUtil.putBoolean(ServerSuccessActivity.this, SharedPreferencesUtil.First_server, false);
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
                ServerSuccessActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME);
    }
}
