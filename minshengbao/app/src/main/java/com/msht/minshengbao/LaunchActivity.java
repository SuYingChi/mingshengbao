package com.msht.minshengbao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.msht.minshengbao.Callback.ResultImgListenner;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.MainActivity;
import com.msht.minshengbao.Utils.ACache;
import com.msht.minshengbao.Utils.AndroidBug54971Workaround;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.NetWorkUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;

import org.json.JSONObject;

public class LaunchActivity extends AppCompatActivity {
    private Context mContext;
    private final String mPageName = "启动页";
    private TextView tv_time;
    private ImageView logo_bottom;
    private SimpleDraweeView draweeView;
    private View   layout_frame;
    private static final int SUCCESS=1;
    private static final int FAILURE=2;
    boolean isFirstIn = false;
    private static final int GO_HOME = 1;
    private static final int GO_GUIDE = 2;
    private static final long SPLASH_DELAY_MILLIS = 3000;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    Handler getimgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String error = object.optString("error");
                        JSONObject jsonObject =object.optJSONObject("data");
                        if(Results.equals("success")) {
                            String logourl=jsonObject.optString("url");
                            if (logourl!=null&&!(logourl.equals("null"))){
                                layout_frame.setVisibility(View.VISIBLE);
                                logo_bottom.setVisibility(View.VISIBLE);
                                Log.d("tupian",logourl);
                                ShowAdimage(logourl);
                            }
                        }else {
                            init();   //计时
                            layout_frame.setVisibility(View.GONE);
                            logo_bottom.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    init();   //计时
                    layout_frame.setVisibility(View.GONE);
                    logo_bottom.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            //init();   //计时
        }
    };
    private void ShowAdimage(String logourl) {    //支持动画
        startCountDownTime(4);
        Uri uri = Uri.parse(logourl);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                 //. 其他设置（如果有的话）
                .build();
        draweeView.setController(controller);
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
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        if (isFirstOpen){
            SharedPreferencesUtil.Clear(this,"AppData");//清除原有数据
        }
        tv_time=(TextView)findViewById(R.id.id_tv_time);
        draweeView = (SimpleDraweeView) findViewById(R.id.id_logo_top);
        logo_bottom=(ImageView)findViewById(R.id.id_logo_bottom);
        layout_frame=findViewById(R.id.id_frame_layout);
        if (NetWorkUtil.IsNetWorkEnable(mContext)){
            initAd();
        }else {
            init();   //计时
        }
    }
    private void NextActivity() {
        final boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        if (isFirstOpen){
            goGuide();
        }else {
            goHome();
        }
    }
    private void startCountDownTime(long time) {
        final boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        CountDownTimer timer=new CountDownTimer(time*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_time.setText(millisUntilFinished/1000+"秒");
            }
            @Override
            public void onFinish() {
                if (isFirstOpen){
                    goGuide();
                }else {
                    goHome();
                }
            }
        };
        timer.start();
    }
    private void initAd() {
        String avatarurl= UrlUtil.Launcher_ImgUrl;
        HttpUrlconnectionUtil.ShortTimeGet(avatarurl, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                getimgHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                getimgHandler.sendMessage(msg);
            }
        });
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
