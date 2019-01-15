package com.msht.minshengbao;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.msht.minshengbao.OkhttpUtil.SSLSocketClient;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.AppPackageUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.androidShop.customerview.ShopRefreshHeader;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.MLoggerInterceptor;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.functionActivity.WaterApp.WaterMainActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import okhttp3.OkHttpClient;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author hong
 * @date 2016/4/2  
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    static {

        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public ShopRefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ShopRefreshHeader(context);
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                ClassicsFooter footer = new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
                footer.setDrawableArrowSize(14);//设置箭头的大小（dp单位）
                footer.setDrawableProgressSize(14);//设置图片的大小（dp单位）、
                footer.setProgressDrawable(context.getResources().getDrawable(R.drawable.loading));
                footer.setAccentColor(context.getResources().getColor(R.color.text_hint));//设置强调颜色
                footer.setTextSizeTitle(14);
                footer.setFinishDuration(0);
                return footer;
            }
        });


    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    private List<String> list;

    public static Context getMsbApplicationContext() {

        return instance.getApplicationContext();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        String liststr;
        if (!TextUtils.isEmpty(ShopSharePreferenceUtil.getInstance().getKey())) {
            liststr = ShopSharePreferenceUtil.getShopSpStringValue(ShopSharePreferenceUtil.getInstance().getUserId());
        } else {
            liststr = ShopSharePreferenceUtil.getShopSpStringValue("noLoginSearch");
        }
        if (TextUtils.isEmpty(liststr) || liststr.equals("null")) {
            list = new ArrayList<String>();
        } else {
            list = JsonUtil.jsonArrayToList(liststr);
        }

        Fresco.initialize(this);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "7dc4d7937bab57666f9188e5667e5930");
        UMShareAPI.get(this);
        BGASwipeBackHelper.init(this, null);

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
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new MLoggerInterceptor("http", true))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        OkHttpUtils.initClient(okHttpClient);
        initUPush();
    }

    public static MyApplication getInstance() {
        return instance;
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
                SharedPreferencesUtil.putDeviceData(getApplicationContext(), SharedPreferencesUtil.DeviceToken, deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
            }
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
                    LogUtils.e(msg.extra.get("url"));
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
                    LogUtils.e(msg.extra.get("url"));
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
        PlatformConfig.setSinaWeibo("4049059641", "22c648140a8ac43032e26bb3bcec71b3", "http://sns.whalecloud.com/sina2/callback");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 检测网络连接
     *
     * @return
     */
    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    public void addSearchHis(String searchKeyWord) {
        if (list.contains(searchKeyWord)) {
            list.remove(list.indexOf(searchKeyWord));
            list.add(0, searchKeyWord);
        } else {
            list.add(searchKeyWord);
        }
    }

    private void onStartMainActivity(Context context, String url) {
        Intent intent=new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pushUrl",url);
        context.startActivity(intent);
    }
}
