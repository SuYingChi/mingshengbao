package com.msht.minshengbao.OkhttpUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class ThreadPoolManage {

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    
    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(6, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }
}
