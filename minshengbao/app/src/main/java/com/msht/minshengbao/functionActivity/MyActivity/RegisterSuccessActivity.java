package com.msht.minshengbao.functionActivity.MyActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class RegisterSuccessActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seccess);
        setCommonHeader("注册成功");
        context=this;
        initView();
        initGo();
    }
    private void initView() {
        TextView tvSuccess=(TextView)findViewById(R.id.id_tv_success);
        TextView tvTip=(TextView)findViewById(R.id.id_tv_tishi);
        tvSuccess.setText("注册成功");
        tvTip.setText("即将转入到登录界面");
    }
    private void initGo() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainintent = new Intent(RegisterSuccessActivity.this, LoginActivity.class);
                RegisterSuccessActivity.this.startActivity(mainintent);
                RegisterSuccessActivity.this.finish();
            }
        }, 3000);
    }
}
