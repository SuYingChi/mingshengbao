package com.msht.minshengbao.MoveSelectAddress;

import com.amap.api.services.core.LatLonPoint;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/3/6  
 */
public class LatLngEntity {
    /**经度**/
    private final double longitude;
    /**纬度**/
    private final double latitude;

    /**
     * @param value e.g "113.4114889842685,23.172753522587282"
     */
    public LatLngEntity(String value) {
        this.longitude = Double.parseDouble(value.split(",")[0]);
        this.latitude = Double.parseDouble(value.split(",")[1]);
    }
    public LatLngEntity(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public LatLngEntity(LatLonPoint latLonPoint) {
        this.longitude = latLonPoint.getLongitude();
        this.latitude = latLonPoint.getLatitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
