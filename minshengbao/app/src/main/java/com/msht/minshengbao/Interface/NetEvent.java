package com.msht.minshengbao.Interface;

/**
 * 回传当前改变的网络状态
 */
public interface NetEvent {
    void onNetChange(boolean netMobile);
}
