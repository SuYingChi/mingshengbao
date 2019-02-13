package com.msht.minshengbao.Base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AndroidWorkaround;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.events.NetWorkEvent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/3/10  
 */
public class BaseActivity extends AppCompatActivity  {
    protected ImageView backImg;
    protected ImageView rightImg;
    protected TextView  tvNavigationTile;
    protected Context   context;
    protected String    mPageName;
    /**是否拒绝网络请求的响应；true表示拒绝；false表示接收，默认false，在onDestroy中设置为true。*/
    protected boolean isOnDestroy;
    private TSnackbar snackBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBarAndNavigationBar();
       // StatusBarCompat.setTranslucentStatusBar(this);
        /**
         * 友盟统计
         */
        PushAgent.getInstance(context).onAppStart();
        EventBus.getDefault().register(this);
        setSnackBar();
        if (!VariableUtil.networkStatus){
            snackBar.show();
        }else {
            snackBar.dismiss();
        }
    }

    protected void initStatusBarAndNavigationBar() {
        //适配华为手机虚拟键遮挡tab的问题
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
        StatusBarCompat.setStatusBar(this);
    }

    private void setSnackBar() {
        snackBar = TSnackbar
                .make(findViewById(android.R.id.content), "网络连接失败,请检查您的网络设置", TSnackbar.LENGTH_LONG)
                .setIconLeft(R.drawable.network_sign_xh, 20);

        View snackBarView = snackBar.getView();
        TextView tvSnackBarText = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tvSnackBarText.setTextColor(Color.WHITE);
        //设置左侧icon
        snackBarView.setBackgroundColor(Color.parseColor("#A0333333"));
    }
    protected void setCommonHeader(String title) {
        mPageName=title;
        View  mViewStatusBarPlace = findViewById(R.id.id_status_view);
        ViewGroup.LayoutParams params = mViewStatusBarPlace.getLayoutParams();
        params.height = StatusBarCompat.getStatusBarHeight(this);
        mViewStatusBarPlace.setLayoutParams(params);
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            mViewStatusBarPlace.setVisibility(View.GONE);
        }
        backImg = (ImageView) findViewById(R.id.id_back);
        tvNavigationTile = (TextView) findViewById(R.id.tv_navigation);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvNavigationTile.setText(title);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetWorkEventBus(NetWorkEvent netMobile) {
        VariableUtil.networkStatus=netMobile.getMessage();
        if (netMobile.getMessage()){
            snackBar.dismiss();
        }else {
            snackBar.show();
        }
    }

    public boolean isLoginState(Context mContext){
       return SharedPreferencesUtil.getLstate(mContext, SharedPreferencesUtil.Lstate, false);
    }
    public boolean isOnDestroy() {
        return isOnDestroy;
    }

    public void setOnDestroy(boolean onDestroy) {
        isOnDestroy = onDestroy;
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
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        //1.取消请求
       // OkHttpManager.getInstance().cancelTag(this);
        //2.拒绝响应
        setOnDestroy(true);
    }
}
