package com.msht.minshengbao.events;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class UpdateBalanceEvent {
    private boolean message;
    public UpdateBalanceEvent(boolean message){
        this.message=message;
    }
    public boolean getMessage() {
        return message;
    }
    public void setMessage(boolean message) {
        this.message = message;
    }
}
