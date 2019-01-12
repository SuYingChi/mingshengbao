package com.msht.minshengbao;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.msht.minshengbao.OkhttpUtil.SSLSocketClient;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.AppPackageUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.functionActivity.WaterApp.WaterMainActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/4/2  
 */
public class MyApplication extends Application {
    public static Context instances;
    @Override
    public void onCreate() {
        super.onCreate();
        instances=this;
        Fresco.initialize(this);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "7dc4d7937bab57666f9188e5667e5930");
        UMShareAPI.get(this);
        CrashReport.initCrashReport(getApplicationContext(), "118eae5408", false);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_stub)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
               // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
            .build(); // 创建配置过得DisplayImageOption对象
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
            .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
            .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        /** okHttp默认的配置生成OkHttpClient*/
        initUPush();
    }

    private void initUPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        /*通知栏按数量显示 **/
        mPushAgent.setDisplayNotificationNumber(10);
        // mPushAgent.setDebugMode(false);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                SharedPreferencesUtil.putDeviceData(getApplicationContext(),SharedPreferencesUtil.DeviceToken,deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) { }
        });

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);
            }

            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                return super.getNotification(context, uMessage);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void launchApp(Context context, UMessage msg) {
                if (!msg.extra.isEmpty()){
                    String url=msg.extra.get("url");
                    if (AppActivityUtil.isAppAlive(context)){
                        if (AppActivityUtil.isLoginState(context)){
                            AppActivityUtil.onPushStartActivity(context,url);
                        }else {
                            AppActivityUtil.onLoginActivity(context,url);
                        }
                    }else {
                        onStartMainActivity(context,url);
                    }
                }else {
                    super.launchApp(context, msg);
                }
            }
            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
            }
            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
            }
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                if (!msg.extra.isEmpty()){
                    String url=msg.extra.get("url");
                    if (AppActivityUtil.isAppAlive(context)){
                        if (AppActivityUtil.isLoginState(context)){
                            AppActivityUtil.onPushStartActivity(context,url);
                        }else {
                            AppActivityUtil.onLoginActivity(context,url);
                        }
                    }else {
                        onStartMainActivity(context,url);
                    }
                }
            }
        };
        /* 使用自定义的NotificationHandler**/
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }
    {
    PlatformConfig.setWeixin("wx33f335ace862eca1", "38e97727e3226f7141c3c647736bdb68");
    //新浪微博
    PlatformConfig.setSinaWeibo("4049059641", "22c648140a8ac43032e26bb3bcec71b3","http://sns.whalecloud.com/sina2/callback");
     }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    private void onStartMainActivity(Context context, String url) {
        Intent intent=new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pushUrl",url);
        context.startActivity(intent);
    }
    public static Context getContext() {
        return instances;
    }
}
