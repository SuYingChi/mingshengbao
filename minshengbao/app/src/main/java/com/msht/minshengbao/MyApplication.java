package com.msht.minshengbao;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by hong on 2016/8/29.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
            .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                    // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
            .build(); // 创建配置过得DisplayImageOption对象
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
            .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
            .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        PushAgent mPushAgent = PushAgent.getInstance(this);
       // mPushAgent.setDebugMode(false);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                SharedPreferencesUtil.putDeviceData(getApplicationContext(),SharedPreferencesUtil.DeviceToken,deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {

            }
        });
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
}
