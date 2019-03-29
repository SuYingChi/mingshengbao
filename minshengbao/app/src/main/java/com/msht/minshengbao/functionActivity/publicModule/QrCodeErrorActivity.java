package com.msht.minshengbao.functionActivity.publicModule;

import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class QrCodeErrorActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_error);
        context=this;
        setCommonHeader("扫码提示");
        String errorType=getIntent().getStringExtra("error_type");
        ImageView errorImg=(ImageView)findViewById(R.id.id_error_img);
        if (errorType.equals("0")){
            errorImg.setImageResource(R.drawable.qrcode_error);
        }else if (errorType.equals("1")){
            errorImg.setImageResource(R.drawable.qrcode_error);
        }
        initEvent();
    }
    private void initEvent() {
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                QrCodeErrorActivity.this.finish();
            }
        }, ConstantUtil.SPLASH_DISPLAY_LENGTH);
    }
}
