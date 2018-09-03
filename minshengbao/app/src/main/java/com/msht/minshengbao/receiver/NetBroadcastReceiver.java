package com.msht.minshengbao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.msht.minshengbao.Utils.NetWorkUtil;
import com.msht.minshengbao.events.NetWorkEvent;

import org.greenrobot.eventbus.EventBus;

/**
 *
 * @author hong
 * @date 2018/3/27
 */

public class NetBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction()!=null&&intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                boolean netWorkState = NetWorkUtil.isNetWorkEnable(context);
                // 接口回调传过去状态的类型
            EventBus.getDefault().post(new NetWorkEvent(netWorkState));
            //events.onNetChange(netWorkState);
        }
    }
}
