package com.msht.minshengbao.functionActivity.repairService;

import android.os.Handler;
import android.os.Bundle;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.umeng.analytics.MobclickAgent;

public class PublishSuccess extends BaseActivity {
    private final String mPageName="提交订单";
    private final int SPLASH_DISPLAY_LENGHT=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_success);
        context=this;
        setCommonHeader("提交订单");
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
    @Override
    public void onResume() {
        super.onResume();
          MobclickAgent.onPageStart(mPageName);
         MobclickAgent.onResume(context);
    }
    @Override
    protected void onPause() {
        super.onPause();
         MobclickAgent.onPageEnd(mPageName);
         MobclickAgent.onPause(context);
    }
}
