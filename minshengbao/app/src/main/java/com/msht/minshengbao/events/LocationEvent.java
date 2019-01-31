package com.msht.minshengbao.events;

public class LocationEvent {
   public String city;
   public String cityId;

    public LocationEvent(String city, String cityId) {
        this.city = city;
        this.cityId = cityId;
    }
}
