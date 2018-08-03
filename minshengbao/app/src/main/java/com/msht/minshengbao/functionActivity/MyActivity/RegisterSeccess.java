package com.msht.minshengbao.functionActivity.MyActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;

public class RegisterSeccess extends BaseActivity {
    private final int SPLASH_DISPLAY_TIME =3000;
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
        TextView tvSuccess=(TextView)findViewById(R.id.id_tv_success);
        TextView tvTip=(TextView)findViewById(R.id.id_tv_tishi);
        tvSuccess.setText("注册成功");
        tvTip.setText("即将转入到登录界面");
    }
    private void initgo() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainintent = new Intent(RegisterSeccess .this, LoginActivity.class);
                RegisterSeccess.this.startActivity(mainintent);
                RegisterSeccess.this.finish();
            }
        }, SPLASH_DISPLAY_TIME);
    }
}
