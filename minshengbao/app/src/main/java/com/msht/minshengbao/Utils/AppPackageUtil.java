package com.msht.minshengbao.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 工具类
 *
 * @author hong
 * @date 2018/06/05
 */
public class AppPackageUtil {
    public static String getPackageVersionName(Context context) {
       String versionName="" ;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName=pi.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return versionName;
    }
}
