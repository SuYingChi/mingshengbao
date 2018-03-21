package com.msht.minshengbao.Base;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity {
    protected ImageView backimg;
    protected ImageView right_img;
    protected TextView  tv_navigaTile;
    protected Context   context;
    protected String    mPageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBar(this);
    }
    protected void setCommonHeader(String title) {
        mPageName=title;
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            findViewById(R.id.id_status_view).setVisibility(View.GONE);//状态栏View
        }
        backimg = (ImageView) findViewById(R.id.id_goback);
        tv_navigaTile = (TextView) findViewById(R.id.tv_navigation);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_navigaTile.setText(title);
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
