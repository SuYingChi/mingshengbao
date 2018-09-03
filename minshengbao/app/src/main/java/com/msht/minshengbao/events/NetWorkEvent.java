package com.msht.minshengbao.events;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/8/25  
 */
public class NetWorkEvent {
    private boolean message;
    public NetWorkEvent(boolean message){
        this.message=message;
    }
    public boolean getMessage() {
        return message;
    }
    public void setMessage(boolean message) {
        this.message = message;
    }
}
