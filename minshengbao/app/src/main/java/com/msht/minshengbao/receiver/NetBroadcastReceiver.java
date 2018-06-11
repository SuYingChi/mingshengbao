package com.msht.minshengbao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Interface.NetEvent;
import com.msht.minshengbao.Utils.NetUtil;
import com.msht.minshengbao.Utils.NetWorkUtil;

/**
 * Created by hong on 2018/3/27.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

    public NetEvent events = BaseActivity.event;
    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            boolean netWorkState = NetWorkUtil.IsNetWorkEnable(context);
            // 接口回调传过去状态的类型
            events.onNetChange(netWorkState);
        }
    }
}
