package com.msht.minshengbao.Base;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Interface.NetEvent;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity implements NetEvent {

    public static NetEvent event;
    protected ImageView backImg;
    protected ImageView rightImg;
    protected TextView  tvNavigationTile;
    protected Context   context;
    protected String    mPageName;
    protected boolean   networkStatus=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBar(this);
        event=this;
    }
    protected void setCommonHeader(String title) {
        mPageName=title;
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            findViewById(R.id.id_status_view).setVisibility(View.GONE);
        }
        backImg = (ImageView) findViewById(R.id.id_goback);
        tvNavigationTile = (TextView) findViewById(R.id.tv_navigation);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvNavigationTile.setText(title);
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
    @Override
    public void onNetChange(boolean netMobile) {
        networkStatus=netMobile;
    }
}
