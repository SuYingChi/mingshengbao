package com.msht.minshengbao.Utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

/**
 * Created by hong on 2017/6/9.
 */

public class LocationUtils {
    public static AMapLocationClientOption mLocationOption = null;
    public static AMapLocationClient mlocationClient;
    public static void setmLocation(Context mContext){
        mlocationClient = new AMapLocationClient(mContext);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5000);
        mlocationClient.setLocationOption(mLocationOption);
    }
    public static void setonDestroy(){
        mlocationClient.onDestroy();
    }
}
