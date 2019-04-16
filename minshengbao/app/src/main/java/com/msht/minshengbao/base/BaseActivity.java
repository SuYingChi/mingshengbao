package com.msht.minshengbao.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.gyf.barlibrary.OSUtils;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.event.VerticalOffset;
import com.msht.minshengbao.events.NetWorkEvent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author hong
 * @date 2016/3/10  
 */
public class BaseActivity extends AppCompatActivity {
    protected ImageView backImg;
    protected ImageView rightImg;
    protected TextView tvNavigationTile;
    protected Context context;
    protected String mPageName;
    /**
     * 是否拒绝网络请求的响应；true表示拒绝；false表示接收，默认false，在onDestroy中设置为true。
     */
    protected boolean isOnDestroy;
    private TSnackbar snackBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBarAndNavigationBar();
        /**
         * 友盟统计
         */
        PushAgent.getInstance(this).onAppStart();
        EventBus.getDefault().register(this);
        setSnackBar();
        setNoNetworkBroadcast();
    }

    protected void initStatusBarAndNavigationBar() {
        if (!OSUtils.isEMUI3_0()) {
            StatusBarCompat.setTranslucentStatusBar(this);
        } else {
            StatusBarCompat.setStatusBar(this);
        }
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
        mPageName = title;
        View mViewStatusBarPlace = findViewById(R.id.id_status_view);
        ViewGroup.LayoutParams params = mViewStatusBarPlace.getLayoutParams();
        params.height = StatusBarCompat.getStatusBarHeight(this);
        mViewStatusBarPlace.setLayoutParams(params);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
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

    public boolean isLoginState(Context mContext) {
        return SharedPreferencesUtil.getLstate(mContext, SharedPreferencesUtil.Lstate, false);
    }

    public boolean isOnDestroy() {
        return isOnDestroy;
    }

    public void setOnDestroy(boolean onDestroy) {
        isOnDestroy = onDestroy;
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1) {
            Configuration newConfig = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            newConfig.fontScale = 1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Context configurationContext = createConfigurationContext(newConfig);
                resources = configurationContext.getResources();
                displayMetrics.scaledDensity = displayMetrics.density * newConfig.fontScale;
                resources.updateConfiguration(newConfig, displayMetrics);
            } else {
                resources.updateConfiguration(newConfig, displayMetrics);
            }
        }
        return resources;
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
    /**
     * 设置无网络监听广播
     */
    protected void setNoNetworkBroadcast() {
        //无网络连接提示相关
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
    }
    private NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        unregisterReceiver(networkChangeReceiver);
        //1.取消请求
        // OkHttpManager.getInstance().cancelTag(this);
        //2.拒绝响应
        setOnDestroy(true);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NetWorkEvent messageEvent) {

    }
    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                switch (networkInfo.getType()) {
                    case TYPE_MOBILE:
//                    Toast.makeText(context, "正在使用2G/3G/4G网络", Toast.LENGTH_SHORT).show();
                        break;
                    case TYPE_WIFI:
//                    Toast.makeText(context, "正在使用wifi上网", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                snackBar.dismiss();
                onNetWorkChange(true);
            } else {
                snackBar.show();
                onNetWorkChange(false);
            }
        }
    }


    protected void onNetWorkChange(boolean isAvailable) {

    }
}
