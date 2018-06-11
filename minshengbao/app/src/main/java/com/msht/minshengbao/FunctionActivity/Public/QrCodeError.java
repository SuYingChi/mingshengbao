package com.msht.minshengbao.FunctionActivity.Public;

import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;

public class QrCodeError extends BaseActivity {
    private String error_type;
    private final int SPLASH_DISPLAY_LENGHT=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_error);
        context=this;
        setCommonHeader("扫码提示");
        error_type=getIntent().getStringExtra("error_type");
        ImageView error_img=(ImageView)findViewById(R.id.id_error_img);
        if (error_type.equals("0")){
            error_img.setImageResource(R.drawable.qrcode_error);
        }else if (error_type.equals("1")){
            error_img.setImageResource(R.drawable.qrcode_error);
        }else {

        }
        initEvent();
    }
    private void initEvent() {

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                QrCodeError.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
