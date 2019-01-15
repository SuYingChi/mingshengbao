package com.msht.minshengbao.androidShop.event;


public class NoNetworkHintEvent {

    private int networkState;

    public NoNetworkHintEvent(int networkState) {
        super();
        this.networkState = networkState;
    }

    public int getNetworkState() {
        return networkState;
    }

}
