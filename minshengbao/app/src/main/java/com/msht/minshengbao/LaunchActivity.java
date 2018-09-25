package com.msht.minshengbao;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.ShopActivity;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.NetWorkUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class LaunchActivity extends AppCompatActivity {
    private Context mContext;
    private final String     mPageName = "启动页";
    private TextView         tvTime;
    private ImageView        logoBottom;
    private SimpleDraweeView drawView;
    private View             layoutFrame;
    private CountDownTimer   timer;
    private static final int GO_HOME = 1;
    private static final int GO_GUIDE = 2;
    private boolean touchStatus=true;
    private String contentUrl;
    private static final long SPLASH_DELAY_MILLIS = 3000;
    private final  GuideHandler mHandler=new GuideHandler(this);
    private final  GetImageHandler getImageHandler=new GetImageHandler(this);
    private static  class GuideHandler extends Handler{
        private WeakReference<LaunchActivity> mWeakReference;
        private GuideHandler(LaunchActivity launchActivity) {
            mWeakReference = new WeakReference<LaunchActivity>(launchActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LaunchActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case GO_HOME:
                    activity.goHome();
                    break;
                case GO_GUIDE:
                    activity.goGuide();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class GetImageHandler extends Handler{
        private WeakReference<LaunchActivity> mWeakReference;
        private GetImageHandler(LaunchActivity launchActivity) {
            mWeakReference = new WeakReference<LaunchActivity>(launchActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LaunchActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        JSONObject jsonObject =object.optJSONObject("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.contentUrl=jsonObject.optString("content_url");
                            String logoUrl=jsonObject.optString("url");
                            if ((!TextUtils.isEmpty(logoUrl))&&(!logoUrl.equals(ConstantUtil.NULL_VALUE))){
                                activity.layoutFrame.setVisibility(View.VISIBLE);
                                activity.logoBottom.setVisibility(View.VISIBLE);
                                activity.onShowAdImage(logoUrl);
                            }
                        }else {
                            activity.init();   //计时
                            activity.layoutFrame.setVisibility(View.GONE);
                            activity.logoBottom.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.init();   //计时
                    activity.layoutFrame.setVisibility(View.GONE);
                    activity.logoBottom.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onStartWebActivity(String contentUrl) {
        Intent intent=new Intent(mContext, HtmlPageActivity.class);
        intent.putExtra("url",contentUrl);
        intent.putExtra("navigate","民生宝");
        startActivity(intent);
        finish();
    }
    private void onStartShopActivity(String contentUrl) {
        Intent intent=new Intent(mContext, ShopActivity.class);
        intent.putExtra("url",contentUrl);
        intent.putExtra("first",1);
        startActivity(intent);
        finish();
    }
    private void onShowAdImage(String logoUrl) {    //支持动画
        startCountDownTime(5);
        Uri uri = Uri.parse(logoUrl);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                 //. 其他设置（如果有的话）
                .build();
        drawView.setController(controller);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
       // AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));
        if(Build.VERSION.SDK_INT>=16){
            Window window=getWindow();
            WindowManager.LayoutParams params=window.getAttributes();
            params.systemUiVisibility=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            window.setAttributes(params);
        }
        mContext = this;
        final boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        if (isFirstOpen){
            SharedPreferencesUtil.clearPreference(this,"AppData");
        }
        tvTime =(TextView)findViewById(R.id.id_tv_time);
        drawView = (SimpleDraweeView) findViewById(R.id.id_logo_top);
        logoBottom =(ImageView)findViewById(R.id.id_logo_bottom);
        layoutFrame =findViewById(R.id.id_frame_layout);
        if (NetWorkUtil.isNetWorkEnable(mContext)){
            layoutFrame.setEnabled(true);
            initAd();
        }else {
            layoutFrame.setEnabled(false);
            init();   //计时
        }
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeCancel();
                if (isFirstOpen){
                    goGuide();
                }else {
                    goHome();
                }
            }
        });
        layoutFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!TextUtils.isEmpty(contentUrl))&&(!contentUrl.equals(ConstantUtil.NULL_VALUE))){
                    touchStatus=false;
                    if (NetUtil.getDomain(contentUrl).equals(ConstantUtil.SHOP_DOMAIN)){
                        goHome();
                        onStartShopActivity(contentUrl);
                    }else{
                        goHome();
                        onStartWebActivity(contentUrl);
                    }
                }
            }
        });
    }
    private void onNextActivity() {
        final boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        if (isFirstOpen){
            goGuide();
        }else {
            goHome();
        }
    }
    private void startCountDownTime(long time) {
        final boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        timer=new CountDownTimer(time*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeText=millisUntilFinished/1000+"秒";
                tvTime.setText(timeText);
            }
            @Override
            public void onFinish() {
                if (touchStatus){
                    if (isFirstOpen){
                        goGuide();
                    }else {
                        goHome();
                    }
                }
            }
        };
        timer.start();
    }
    private void initAd() {
        String requestUrl= UrlUtil.LAUNCHER_IMG_URL;
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,null,getImageHandler);
    }
    private void init() {
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (isFirstOpen) {
            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        }
    }
    private void goHome() {
        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
        LaunchActivity.this.startActivity(intent);
        LaunchActivity.this.finish();
    }
    private void goGuide() {
        Intent intent = new Intent(LaunchActivity.this, GuideActivity.class);
        LaunchActivity.this.startActivity(intent);
        LaunchActivity.this.finish();
    }
    private void timeCancel() {
        if (timer!=null){
            timer.cancel();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeCancel();
    }
}
