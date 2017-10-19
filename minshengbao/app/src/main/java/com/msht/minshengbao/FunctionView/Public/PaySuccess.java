package com.msht.minshengbao.FunctionView.Public;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.FunctionView.GasService.ServerSuccess;
import com.msht.minshengbao.R;
import com.umeng.analytics.MobclickAgent;

public class PaySuccess extends BaseActivity {
    private final String mPageName="支付成功";
    private final int SPLASH_DISPLAY_LENGHT=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        context=this;
        String type=getIntent().getStringExtra("type");
        TextView tv_notice=(TextView)findViewById(R.id.id_text);
        TextView tv_naviga=(TextView)findViewById(R.id.tv_navigation);
        View Rad=findViewById(R.id.id_re1);
        findViewById(R.id.id_goback).setVisibility(View.GONE);
        if (type.equals("0")){
            tv_naviga.setText("燃气缴费");
            Rad.setVisibility(View.VISIBLE);
            tv_notice.setText("缴费成功");
        }else if (type.equals("1")){
            tv_naviga.setText("维修支付");
            tv_notice.setText("支付成功");
            Rad.setVisibility(View.GONE);
        }else if (type.equals("2")){
            tv_naviga.setText("Ic卡充值");
            tv_notice.setText("充值成功");
            Rad.setVisibility(View.GONE);
        }
        initEvent();
    }

    private void initEvent() {
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                PaySuccess.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
