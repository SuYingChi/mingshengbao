package com.msht.minshengbao.androidShop.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.androidShop.ShopConstants;


public class ShopSharePreferenceUtil {

    private static ShopSharePreferenceUtil shopSharePreferenceUtil;
    private SharedPreferences loginSp;
    private SharedPreferences.Editor loginEditor;
    private String orderType;


    private ShopSharePreferenceUtil(String sharePreferencefileName) {
        loginSp = MyApplication.getMsbApplicationContext().getSharedPreferences(sharePreferencefileName, Context.MODE_PRIVATE);
        loginEditor = loginSp.edit();
    }

    public static ShopSharePreferenceUtil getInstance() {
        if (shopSharePreferenceUtil == null) {
            synchronized (ShopSharePreferenceUtil.class) {
                if (shopSharePreferenceUtil == null) {
                    shopSharePreferenceUtil = new ShopSharePreferenceUtil(ShopConstants.SHOP_SP);
                }
            }
        }
        return shopSharePreferenceUtil;
    }


    public static SharedPreferences getSharePreferenceFile(String sharePreferencefileName) {
        return MyApplication.getMsbApplicationContext().getSharedPreferences(sharePreferencefileName, Context.MODE_PRIVATE);
    }

    public void setKey(String token) {
        loginEditor.putString("key", token);
        loginEditor.commit();
    }

    public String getKey() {
        return getSPStringValue(ShopConstants.SHOP_SP, "key");
    }
    public String getUserId() {
        return getSPStringValue(ShopConstants.SHOP_SP, "userId");
    }
    public String getPassword() {
        return getSPStringValue(ShopConstants.SHOP_SP, "password");
    }
    public static String getSPStringValue(String fileName, String key) {
        SharedPreferences sharePreferenceFile = getSharePreferenceFile(fileName);
        return sharePreferenceFile.getString(key, "");
    }

    public static String getShopSpStringValue(String key) {
        SharedPreferences sharePreferenceFile = getSharePreferenceFile(ShopConstants.SHOP_SP);
        return sharePreferenceFile.getString(key, "");
    }

    public static int getLoginSpIntValue(String key) {
        SharedPreferences sharePreferenceFile = getSharePreferenceFile(ShopConstants.SHOP_SP);
        return sharePreferenceFile.getInt(key, 0);
    }

    public static void setSPStringValue(String fileName, String key, String value) {
        SharedPreferences sharePreferenceFile = getSharePreferenceFile(fileName);
        sharePreferenceFile.edit().putString(key, value).apply();
    }

    public static void setShopSpStringValue(String key, String value) {
        SharedPreferences sharePreferenceFile = getSharePreferenceFile(ShopConstants.SHOP_SP);
        sharePreferenceFile.edit().putString(key, value).apply();
    }

    public static void setLoginSpIntValue(String key, int value) {
        SharedPreferences sharePreferenceFile = getSharePreferenceFile(ShopConstants.SHOP_SP);
        sharePreferenceFile.edit().putInt(key, value).apply();
    }
    public static void Clear(Context context, String strKey){
        SharedPreferences activityPreferences= context.getSharedPreferences(strKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=activityPreferences.edit();
        editor.clear();
        editor.apply();
    }
    public  void ClearLoginSp(){
        loginEditor.clear();
        loginEditor.apply();
    }
}
