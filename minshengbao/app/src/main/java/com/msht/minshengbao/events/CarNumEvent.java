package com.msht.minshengbao.events;

public class CarNumEvent {
    public int getCarNum() {
        return carNum;
    }

    public void setCarNum(int carNum) {
        this.carNum = carNum;
    }

    private  int carNum;

    public CarNumEvent(int num) {
        carNum= num;
    }
}
