package com.msht.minshengbao.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hong on 2016/11/10.
 */
public class SharedPreferencesUtil {
    private static final String spFist="open_app";
    private static final String spFileName = "AppData";
    private static final String spVersion="spversion";
    private static final String Device="Device";
    public static final String FIRST_OPEN = "first_open";
    public static final String First_server="first_sever";
    public static final String UserId="userId";
    public static final String AvatarUrl="avatarurl";
    public static final String Password="password";
    public static final String shopCookie="shopcookie";
    public static final String NickName="nickname";
    public static final String UserName="username";
    public static final String passw="passw";
    public static final String Sex="sex";
    public static final String PhoneNumber="Phone";
    public static final String Lstate="state";
    public static final String VersionState="versionstate";
    public static final String Shopstate="shopstate";
    public static final String DeviceToken="devicetoken";

    public static Boolean getBoolean(Context context, String strKey,
                                       Boolean strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFist, Context.MODE_PRIVATE);
        Boolean result = setPreferences.getBoolean(strKey, strDefault);
        return result;
    }
    public static void putBoolean(Context context, String strKey,
                                  Boolean strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFist, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.commit();
    }
    public static String getUserId(Context context, String strKey,
                                 String strData){
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        String result = setPreferences.getString(strKey, strData);
        return result;
    }
    public static void putUserId(Context context, String strKey,
                                  String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }
    public static String getAvatarUrl(Context context, String strKey,
                                   String strData){
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        String result = setPreferences.getString(strKey, strData);
        return result;
    }
    public static void putAvatarUrl(Context context, String strKey,
                                 String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }
    public static String getPassword(Context context, String strKey,
                                      String strData){
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        String result = setPreferences.getString(strKey, strData);
        return result;
    }
    public static void putPassword(Context context, String strKey,
                                    String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }
    public static String getNickName(Context context, String strKey,
                                     String strData){
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        String result = setPreferences.getString(strKey, strData);
        return result;
    }
    public static void putNickName(Context context, String strKey,
                                   String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }
    public static String getUserName(Context context, String strKey,
                                     String strData){
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        String result = setPreferences.getString(strKey, strData);
        return result;
    }
    public static void putUserName(Context context, String strKey,
                                   String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }
    public static String getpassw(Context context, String strKey,
                                     String strData){
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        String result = setPreferences.getString(strKey, strData);
        return result;
    }
    public static void putpassw(Context context, String strKey,
                                   String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }

    public static Boolean getLstate(Context context, String strKey,
                                     Boolean strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        Boolean result = setPreferences.getBoolean(strKey, strDefault);
        return result;
    }

    public static void putLstate(Context context, String strKey,
                                  Boolean strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.commit();
    }
    public static String getSex(Context context, String strKey,
                                    String strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        String result = setPreferences.getString(strKey, strDefault);
        return result;
    }

    public static void putSex(Context context, String strKey,
                                 String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }
    public static String getPhoneNumber(Context context, String strKey,
                                String strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        String result = setPreferences.getString(strKey, strDefault);
        return result;
    }

    public static void putPhoneNumber(Context context, String strKey,
                              String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }
    public static String getStringData(Context context, String strKey,
                                        String strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        String result = setPreferences.getString(strKey, strDefault);
        return result;
    }
    public static void putStringData(Context context, String strKey,
                                      String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }
    public static String getDeviceData(Context context, String strKey,
                                        String strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                Device, Context.MODE_PRIVATE);
        String result = setPreferences.getString(strKey, strDefault);
        return result;
    }

    public static void putDeviceData(Context context, String strKey,
                                      String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                Device, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }
    public static void Clear(Context context, String strKey){
        SharedPreferences activityPreferences= context.getSharedPreferences(strKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=activityPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
