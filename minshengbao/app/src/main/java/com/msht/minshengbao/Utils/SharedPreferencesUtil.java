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
    private static final String CONTROL="control";
    public static final String CONTROL_TYPE="CONTROL_TYPE";
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
    public static final String LPG_USER_ID="lpgUserId";
    public static final String LPG_USER_NAME="lpgUserName";
    public static final String LPG_MOBILE="MOBILE";
    public static final String LPG_SEX="lpgSex";

    public static Boolean getBoolean(Context context, String strKey,
                                       Boolean strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFist, Context.MODE_PRIVATE);
        return setPreferences.getBoolean(strKey, strDefault);
    }
    public static void putBoolean(Context context, String strKey,
                                  Boolean strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFist, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.apply();
    }
    public static String getUserId(Context context, String strKey,
                                 String strData){
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        return setPreferences.getString(strKey, strData);
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
        return setPreferences.getString(strKey, strData);
    }
    public static void putAvatarUrl(Context context, String strKey,
                                 String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.apply();
    }
    public static String getPassword(Context context, String strKey,
                                      String strData){
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        return setPreferences.getString(strKey, strData);
    }
    public static void putPassword(Context context, String strKey,
                                    String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.apply();
    }
    public static String getNickName(Context context, String strKey,
                                     String strData){
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        return setPreferences.getString(strKey, strData);
    }
    public static void putNickName(Context context, String strKey,
                                   String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.apply();
    }
    public static String getUserName(Context context, String strKey,
                                     String strData){
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        return setPreferences.getString(strKey, strData);
    }
    public static void putUserName(Context context, String strKey,
                                   String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.apply();
    }
    public static String getpassw(Context context, String strKey,
                                     String strData){
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        return setPreferences.getString(strKey, strData);
    }
    public static void putpassw(Context context, String strKey,
                                   String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.apply();
    }

    public static Boolean getLstate(Context context, String strKey,
                                     Boolean strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        return setPreferences.getBoolean(strKey, strDefault);
    }

    public static void putLstate(Context context, String strKey,
                                  Boolean strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.apply();
    }
    public static String getSex(Context context, String strKey,
                                    String strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        return setPreferences.getString(strKey, strDefault);
    }

    public static void putSex(Context context, String strKey,
                                 String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.apply();
    }
    public static String getPhoneNumber(Context context, String strKey,
                                String strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        return setPreferences.getString(strKey, strDefault);
    }

    public static void putPhoneNumber(Context context, String strKey,
                              String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.apply();
    }
    public static String getStringData(Context context, String strKey,
                                        String strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        return setPreferences.getString(strKey, strDefault);
    }
    public static void putStringData(Context context, String strKey,
                                      String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.apply();
    }
    public static String getDeviceData(Context context, String strKey,
                                        String strDefault) {//strDefault  boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                Device, Context.MODE_PRIVATE);
        return setPreferences.getString(strKey, strDefault);
    }

    public static void putDeviceData(Context context, String strKey,
                                      String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                Device, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.apply();
    }
    public static int getControlType(Context context, String strKey, int strDefault){
        SharedPreferences setPreferences = context.getSharedPreferences(
                CONTROL, Context.MODE_PRIVATE);
        return setPreferences.getInt(strKey, strDefault);
    }
    public static void putControlType(Context context,String strKey,int strData){
        SharedPreferences activityPreferences = context.getSharedPreferences(
                CONTROL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putInt(strKey, strData);
        editor.apply();
    }

    public static void clearPreference(Context context, String strKey){
        SharedPreferences activityPreferences= context.getSharedPreferences(strKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=activityPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
