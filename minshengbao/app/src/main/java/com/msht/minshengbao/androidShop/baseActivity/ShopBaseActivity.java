package com.msht.minshengbao.androidShop.baseActivity;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.gyf.barlibrary.BarParams;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OSUtils;
import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AndroidWorkaround;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
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
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * @author mshtyfb
 */
//如果activity在可见时就要刷新数据的话 在onresume里请求数据，如果只需要请求一次的话再oncreat里请求
public abstract class ShopBaseActivity extends AppCompatActivity implements IBaseView ,SwipeBackActivityBase {

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
    private SwipeBackActivityHelper mHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //这个框架滑动返回与immersionbar 不兼容
     //   initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }
    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }
    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }
    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
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
        if (OSUtils.isEMUI3_0()||OSUtils.isEMUI3_1()) {

        }
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
        if(!OSUtils.isEMUI3_0()) {
            mImmersionBar = ImmersionBar.with(this);
            //白色状态栏处理
            mImmersionBar.statusBarDarkFont(true, 0.2f);
            if (ImmersionBar.hasNavigationBar(this)) {
                BarParams barParams = ImmersionBar.with(this).getBarParams();
                if (barParams.fullScreen) {
                    mImmersionBar.fullScreen(false).navigationBarColor(R.color.black).init();
                } else {
                    mImmersionBar.init();
                }
            } else {
                mImmersionBar.init();
            }
        }else {
            //适配华为手机虚拟键遮挡tab的问题
            if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
                AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
            }
            StatusBarCompat.setStatusBar(this);
        }
    }

    protected void setSoftInPutMode() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
            PopUtil.showComfirmDialog(this,"",getResources().getString(R.string.network_error),"","",null,null,true);
            onNetError();
        } else if (TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())||"未登录".equals(s)) {
            PopUtil.toastInBottom("请登录商城");
            Intent goLogin = new Intent(this, LoginActivity.class);
            startActivity(goLogin);
        } else if(!TextUtils.isEmpty(s)){
          PopUtil.toastInCenter(s);
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
                        } else if (NetUtil.getDomain(url).equals(ConstantUtil.FIANL_SHOP_DOMAIN)) {
                            Intent intent4 = new Intent(this, HtmlPageActivity.class);
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
                startActivity(intent);
            } else if (NetUtil.getDomain(data).equals(ConstantUtil.FIANL_SHOP_DOMAIN)) {
                Intent intent = new Intent(this, HtmlPageActivity.class);
                intent.putExtra("url", data);
                startActivity(intent);
            }
        }
    }
    protected void doShopItemViewClickByUrl(String url) {
        Set<String> keys = Uri.parse(url).getQueryParameterNames();
        String data;
        if (keys.contains("goods")) {
            data = Uri.parse(url).getQueryParameter("goods");
            Intent intent = new Intent(this, ShopGoodDetailActivity.class);
            intent.putExtra("type", "2");
            intent.putExtra("goodsid", data);
            startActivity(intent);
        } else if (keys.contains("keyword")) {
            data = Uri.parse(url).getQueryParameter("keyword");
            Intent intent = new Intent(this, ShopKeywordListActivity.class);
            intent.putExtra("keyword", data);
            startActivity(intent);
        } else if (keys.contains("gc_id")) {
            data = Uri.parse(url).getQueryParameter("gc_id");
            Intent intent = new Intent(this, ShopClassDetailActivity.class);
            int index = data.indexOf("gc_id=");
            data = data.substring(index + 6).trim();
            intent.putExtra("data", data);
            startActivity(intent);
        } else {
            data = url;
            Intent intent = new Intent(this, HtmlPageActivity.class);
            intent.putExtra("url", data);
            startActivity(intent);
        }
    }
}
