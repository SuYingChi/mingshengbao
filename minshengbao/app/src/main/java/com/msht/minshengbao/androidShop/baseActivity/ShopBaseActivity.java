package com.msht.minshengbao.androidShop.baseActivity;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.androidShop.activity.ShopClassDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopGoodDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopKeywordListActivity;
import com.msht.minshengbao.androidShop.activity.ShopSpecialActivity;
import com.msht.minshengbao.androidShop.activity.ShopUrlActivity;
import com.msht.minshengbao.androidShop.customerview.LoadingDialog;
import com.msht.minshengbao.androidShop.shopBean.ErrorBaseData;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.NetworkChangeReceiver;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.IBaseView;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * @author mshtyfb
 */
//如果activity在可见时就要刷新数据的话 在onresume里请求数据，如果只需要请求一次的话再oncreat里请求
public abstract class ShopBaseActivity extends AppCompatActivity implements IBaseView, BGASwipeBackHelper.Delegate {

    private BGASwipeBackHelper mSwipeBackHelper;
    protected ImmersionBar mImmersionBar;
    protected LoadingDialog centerLoadingDialog;
    private Context context;
    private TSnackbar snackBar;
    private String monetary_unit;
    public static final int ISNETWORK = 0x1000;
    public static final int NONETWORK = 0x1001;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private Unbinder butterKnifeBind;
    private RelativeLayout layoutNoNetwork;
    private MyApplication application;
    private RelativeLayout layoutEmpty;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSoftInPutMode();
        context = this;
        application = MyApplication.getInstance();
        /**
         * 友盟统计
         */
        PushAgent.getInstance(context).onAppStart();
        setSnackBar();
        if (!VariableUtil.networkStatus) {
            snackBar.show();
        } else {
            snackBar.dismiss();
        }
        monetary_unit = context.getResources().getString(R.string.monetary_unit);
        setLanguage(Locale.getDefault());
        setLayout();
        butterKnifeBind = ButterKnife.bind(this);
        initImmersionBar();
        setNoNetworkBroadcast();
        //获取购物车商品
        postCartCount();
        //获取站内未读消息
        postUnreadMessageCount();
        //给浸入式页面设置topbar 防止与状态栏重叠
        // ImmersionBar.setTitleBar(this, mToolbar);

    }

    private void postUnreadMessageCount() {

    }

    //获取购物车商品种类和数量
    private void postCartCount() {

    }

    /**
     * 设置语言
     *
     * @param locale
     */
    protected void setLanguage(Locale locale) {
        Configuration config = getResources().getConfiguration();//获取系统的配置
        config.locale = locale;//将语言更改为默认语言
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());//更新配置
    }

    /**
     * 设置无网络监听广播
     */
    protected void setNoNetworkBroadcast() {
        //无网络连接提示相关
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
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

    protected abstract void setLayout();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    //是否拒绝网络请求的响应；true表示拒绝；false表示接收，默认false，在onDestroy中设置为true。
    protected boolean isOnDestroy;

    public boolean isOnDestroy() {
        return isOnDestroy;
    }

    public void setOnDestroy(boolean onDestroy) {
        isOnDestroy = onDestroy;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
        MobclickAgent.onPause(this);
        //1.取消请求
        OkHttpUtils.getInstance().cancelTag(this);
        //2.拒绝响应
        setOnDestroy(true);
        butterKnifeBind.unbind();
        unregisterReceiver(networkChangeReceiver);
    }

    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    protected void setSoftInPutMode() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }

    @Override
    public void showLoading() {
        showCenterLodaingDialog();
    }

    @Override
    public void dismissLoading() {
        hideCenterLoadingDialog();
    }

    protected void showCenterLodaingDialog() {
        if (!isFinishing() && centerLoadingDialog == null) {
            centerLoadingDialog = new LoadingDialog(this);
            centerLoadingDialog.show();
        } else if (!isFinishing() && !centerLoadingDialog.isShowing()) {
            centerLoadingDialog.show();
        }
    }


    protected void hideCenterLoadingDialog() {
        if (centerLoadingDialog != null && centerLoadingDialog.isShowing() && !isFinishing()) {
            centerLoadingDialog.dismiss();
        }
    }

    @Override
    public void onError(String s) {
        if (!AppUtil.isNetworkAvailable()) {
            PopUtil.toastInBottom(R.string.network_error);
            onNetError();
        } else if (TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())) {
            PopUtil.toastInBottom("请登录商城");
            Intent goLogin = new Intent(this, LoginActivity.class);
            startActivity(goLogin);
            this.finish();
        } else {
            ErrorBaseData errorBaseData = JsonUtil.toBean(s, ErrorBaseData.class);
            if(errorBaseData!=null) {
                PopUtil.toastInCenter(errorBaseData.getDatas().getError());
            }else {
                PopUtil.toastInCenter(s);
            }
            switch (s) {
                case "未登录":
                    AppUtil.logout();
                    Intent goLogin = new Intent(this, LoginActivity.class);
                    startActivity(goLogin);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public String getKey() {
        return ShopSharePreferenceUtil.getInstance().getKey();
    }

    @Override
    public String getUserId() {
        return ShopSharePreferenceUtil.getInstance().getUserId();
    }

    @Override
    public String getLoginPassword() {
        return ShopSharePreferenceUtil.getInstance().getPassword();
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {

    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }

    @Override
    public void onLogout() {
        AppUtil.logout();
    }

    @Override
    public void onNetError() {

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /***
     * 设置无网络内容区域 ，需要显示无网络连接的子activity，自己调一下该方法，自己布局文件要include layout_no_network
     */
    protected void initNoNetworkLayout() {
        layoutNoNetwork = (RelativeLayout) findViewById(R.id.rlNoNetwork);
        TextView tvReload = (TextView) findViewById(R.id.tvReload);
        if (application.checkConnection()) {
            showNoNetwork();
        } else {
            hideNoNetwork();
        }
        tvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (application.checkConnection()) {
                    layoutNoNetwork.setVisibility(View.GONE);
                } else {
                    layoutNoNetwork.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    protected void hideNoNetwork() {
        layoutNoNetwork.setVisibility(View.VISIBLE);
    }

    protected void showNoNetwork() {
        layoutNoNetwork.setVisibility(View.GONE);
    }

    /**
     * 显示空标志
     */
    protected void showLayoutEmpty() {
        layoutEmpty.setVisibility(View.VISIBLE);
    }

    protected void hideLayoutEmpty() {
        layoutEmpty.setVisibility(View.GONE);
    }

    /**
     * 设置空标记的显示 子activity，自己调一下该方法，自己布局文件要include actvity_base_empty
     */
    protected void initLayoutEmpty(int resId, String emptyTitle, String emptyBody, String btnText, View.OnClickListener listener) {
        layoutEmpty = (RelativeLayout) findViewById(R.id.layoutEmpty);
        ImageView imgEmptyLogo = (ImageView) findViewById(R.id.imgEmptyLogo);
        TextView tvEmptyTitle = (TextView) findViewById(R.id.tvEmptyTitle);
        TextView tvEmptyBody = (TextView) findViewById(R.id.tvEmptyBody);
        Button btnChoose = (Button) findViewById(R.id.btnChoose);
        btnChoose.setVisibility(View.VISIBLE);
        btnChoose.setText(btnText);
        btnChoose.setOnClickListener(listener);
        imgEmptyLogo.setImageResource(resId);
        tvEmptyTitle.setText(emptyTitle);
        tvEmptyBody.setText(emptyBody);
    }

    protected void doNotAdClick(Map<String, String> map) {
        if (map.containsKey("type")) {
            switch (map.get("type")) {
                case "keyword":
                    Intent intent = new Intent(this,ShopKeywordListActivity.class);
                    if(map.containsKey("data")){
                        intent.putExtra("keyword",map.get("data"));
                    }
                    startActivity(intent);
                    break;
                case "goods":
                    Intent intent2 = new Intent(this,ShopGoodDetailActivity.class);
                    if(map.containsKey("data")){
                        intent2.putExtra("goodsid",map.get("data"));
                    }
                    if(map.containsKey("price")){
                        intent2.putExtra("price",map.get("price"));
                    }
                    intent2.putExtra("type", "2");
                    startActivity(intent2);
                    break;
                case "special":
                    Intent intent3 = new Intent(this,ShopSpecialActivity.class);
                    if(map.containsKey("data")){
                        intent3.putExtra("special",map.get("data"));
                    }
                    startActivity(intent3);
                    break;
                case "url":
                    String url;
                    if(map.containsKey("data")){
                        url = map.get("data");
                        if (url.contains("gc_id=")) {
                            Intent intent4 = new Intent(this, ShopClassDetailActivity.class);
                            int index = url.indexOf("gc_id=");
                            url = url.substring(index + 6).trim();
                            intent4.putExtra("data", url);
                            intent4.putExtra("title", "民生商城");
                            startActivity(intent4);
                        } else if (NetUtil.getDomain(url).equals(ConstantUtil.SHOP_DOMAIN)||NetUtil.getDomain(url).equals(ConstantUtil.DEBUG_SHOP_DOMAIN)) {
                            Intent intent4 = new Intent(this, ShopUrlActivity.class);
                            intent4.putExtra("url", url);
                            startActivity(intent4);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
    protected void onShopItemViewClick(String type, String data) {
        LogUtils.e(ShopSharePreferenceUtil.getInstance().getKey());
        if(TextUtils.equals(type,"goods")) {
            Intent intent = new Intent(this, ShopGoodDetailActivity.class);
            intent.putExtra("type", "2");
            intent.putExtra("goodsid", data);
            startActivity(intent);
        }else if(TextUtils.equals(type,"keyword")){
            Intent intent = new Intent(this, ShopKeywordListActivity.class);
            intent.putExtra("keyword", data);
            startActivity(intent);
        }else if ("url".equals(type)) {
            if (data.contains("gc_id=")) {
                Intent intent = new Intent(this, ShopClassDetailActivity.class);
                int index = data.indexOf("gc_id=");
                data = data.substring(index + 6).trim();
                intent.putExtra("data", data);
                intent.putExtra("title", "民生商城");
                startActivity(intent);
            } else if (NetUtil.getDomain(data).equals(ConstantUtil.SHOP_DOMAIN)||NetUtil.getDomain(data).equals(ConstantUtil.DEBUG_SHOP_DOMAIN)) {
                Intent intent = new Intent(this, ShopUrlActivity.class);
                intent.putExtra("url", data);
                startActivity(intent);
            }
        }
    }
}
