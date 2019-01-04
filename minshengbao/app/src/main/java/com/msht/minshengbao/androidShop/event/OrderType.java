package com.msht.minshengbao.androidShop.event;

public class OrderType {
    public int getTabPosition() {
        return tabPosition;
    }

    public void setTabPosition(int tabPosition) {
        this.tabPosition = tabPosition;
    }

    private  int tabPosition;

    public OrderType(int tabPosition) {
        this.tabPosition = tabPosition;
    }
}
