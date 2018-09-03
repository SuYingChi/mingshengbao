package com.msht.minshengbao.functionActivity.repairService;

import android.os.Handler;
import android.os.Bundle;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/6/20  
 */
public class PublishSuccessActivity extends BaseActivity {
    private final String mPageName="提交订单";
    private static final int SPLASH_DISPLAY_LENGTH =4000;
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
                PublishSuccessActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
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
