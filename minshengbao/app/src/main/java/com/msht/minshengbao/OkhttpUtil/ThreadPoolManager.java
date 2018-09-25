package com.msht.minshengbao.OkhttpUtil;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class ThreadPoolManager {
    private static volatile ThreadPoolManager mInstance;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private ThreadPoolManager(Context context){
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable  r) {
                return new Thread(r,Thread.currentThread().getName());
            }
        });
    }
    /**
     * 获取单例引用
     * @return
     */
    public static ThreadPoolManager getInstance(Context context){
        ThreadPoolManager inst = mInstance;
        if (inst == null) {
            synchronized (ThreadPoolManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new ThreadPoolManager(context.getApplicationContext());
                    mInstance = inst;
                }
            }
        }
        return inst;
    }
    public void onThreadPoolDateStart(){
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new RegularlyCheckTimeTask(), 0, 3,
                TimeUnit.MINUTES);
    }
    private class RegularlyCheckTimeTask implements Runnable {
        @Override
        public void run() {
        }
    }
}
