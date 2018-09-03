package com.msht.minshengbao.Utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

/**
 *
 * @author hong
 * @date 2017/6/9
 */

public class LocationUtils {
    public static AMapLocationClientOption mLocationOption = null;
    public static AMapLocationClient mLocationClient;
    public static void setLocation(Context mContext){
        mLocationClient = new AMapLocationClient(mContext);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5000);
        mLocationClient.setLocationOption(mLocationOption);
    }
    public static void setonDestroy(){
        mLocationClient.onDestroy();
    }
}
