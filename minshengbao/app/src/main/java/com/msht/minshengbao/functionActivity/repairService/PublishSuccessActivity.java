package com.msht.minshengbao.functionActivity.repairService;

import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.base.BaseActivity;
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
    private static final int SPLASH_DISPLAY_LENGTH =4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_success);
        context=this;
        mPageName="提交订单";
        TextView tvTitle=(TextView)findViewById(R.id.id_title);
        TextView tvContent=(TextView)findViewById(R.id.id_text2);
        ImageView successImg=(ImageView)findViewById(R.id.id_img_success);
        int type=getIntent().getIntExtra("type",0);
        String tvNavigation=getIntent().getStringExtra("navigation");
        setCommonHeader(tvNavigation);
        if (type==3){
           tvTitle.setText("问题提交成功");
           tvContent.setText("工作人员会及时处理！");
           successImg.setImageResource(R.drawable.pay_success_xh);
        }else {
            tvTitle.setText("订单提交成功");
            tvContent.setText("会有工作人员跟您联系！");
            successImg.setImageResource(R.drawable.publish_success_xh);
        }
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
