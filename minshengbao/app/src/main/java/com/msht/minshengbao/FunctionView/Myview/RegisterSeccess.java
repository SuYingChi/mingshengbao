package com.msht.minshengbao.FunctionView.Myview;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.umeng.analytics.MobclickAgent;

public class RegisterSeccess extends BaseActivity {
    private final int SPLASH_DISPLAY_LENGHT=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seccess);
        setCommonHeader("注册成功");
        context=this;
        initview();
        initgo();
    }
    private void initview() {
        TextView tv_success=(TextView)findViewById(R.id.id_tv_success);
        TextView tv_tishi=(TextView)findViewById(R.id.id_tv_tishi);
        tv_success.setText("注册成功");
        tv_tishi.setText("即将转入到登录界面");
    }
    private void initgo() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainintent = new Intent(RegisterSeccess .this, LoginView.class);
                RegisterSeccess.this.startActivity(mainintent);
                RegisterSeccess.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
