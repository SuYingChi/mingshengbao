package com.msht.minshengbao.Utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import com.msht.minshengbao.BuildConfig;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/24 
 */
public class ConstantUtil {

    public static final int BIND_SUCCESS=1;
    public static final int REQUEST_CODE_ONE =1;
    public static final int REQUEST_CODE_TWO =2;
    public static final int REQUEST_CODE_SEVEN =7;
    public static final int MY_LOCATION_REQUEST=0;
    public static final int REQUEST_CALL_PHONE=2;
    public static  final int MY_CAMERA_REQUEST=1;
    public static final int VALUE_MINUS1=-1;
    public static final int VALUE0=0;
    public static final int VALUE1=1;
    public static final int VALUE2=2;
    public static final int VALUE3=3;
    public static final int VALUE4=4;
    public static final int VALUE5=5;
    public static final int VALUE6=6;
    public static final int VALUE7=7;
    public static final int VALUE8=8;
    public static final int VALUE9=9;
    public static final int VALUE10=10;
    public static final int VALUE11=11;
    public static final int SPLASH_DISPLAY_LENGTH=3000;
    public static final  String NULL_VALUE="null";
    public static final  String VALUE_ZERO= "0";
    public static final  String VALUE_ONE=  "1";
    public static final  String VALUE_TWO=  "2";
    public static final  String VALUE_THREE="3";
    public static final  String VALUE_FOUR= "4";
    public static final  String VALUE_FIVE= "5";
    public static final  String VALUE_SIX=  "6";
    public static final  String VALUE_SEVER="7";
    public static final  String VALUE_EIGHT="8";
    public static final  String VALUE_NINE= "9";
    public static final  String VALUE_TEN=  "10";
    public static final  String VALUE_ELEVEN="11";
    public static final  String VALUE_TWELVE="12";
    public static final  String VALUE_FOURTEEN="14";
    public static final  String VALUE_THIRTY_SIX="36";
    public static final  String VALUE_FORTY_EIGHT="48";
    public static final  String VALUE_CODE_0000="0000";
    public static final  String VALUE_CODE_0009="0009";
    public static final  String VALUE_CODE_0010="0010";
    public static final  String VALUE_CODE_0011="0011";
    public static final  String VALUE_CODE_0012="0012";
    public static final  String VALUE_CODE_1008="1008";
    public static final  String VALUE_CODE_1009="1009";
    public static final  String VALUE_ZERO1="0.0";
    public static final  String VALUE_ZERO2="0.00";
    public static final  String VALUE_FIFTEEN="15";
    public static final  String VALUE_FIFTY="50";

    public static final String SUCCESS_VALUE="success";
    public static final String FAILURE_VALUE="fail";
    public static final String CANCEL_VALUE="cancel";
    public static final String MSB_APP="msbapp";
    public static final String SHOP="shop";
    public static final String SERVICE_STATION="service_station";
    public static final String WATER="water";
    public static final String VEGETABLE="scxs";
    public static final String GAS_PAY="gas_pay";
    public static final String GAS_METER="gas_meter";
    public static final String GAS_IC_CARD="gas_iccard";
    public static final String GAS_SERVE="gas_serve";
    public static final String GAS_NORMAL_PAY="gas_normal_pay";
    public static final String GAS_IOT="gas_iot";

    public static final String REPAIR="repair";
    public static final String CLEAN="clean";
    public static final String CONVENIENCE_SERVICE="convenience_service";
    public static final String GAS_REPAIR="gas_repair";
    public static final String GAS_INSTALL="gas_install";
    public static final String GAS_RESCUE="gas_rescue";
    public static final String GAS_INTRODUCE="gas_introduce";

    public static final String INSURANCE="insurance";
    public static final String LPG_NAME="lpg";
    public static final String HOUSEHOLD_CLEAN="household_clean";
    public static final String HOUSEHOLD_REPAIR="household_repair";
    public static final String SANITARY_WARE="sanitary_ware";
    public static final String LAMP_CIRCUIT="lamp_circuit";
    public static final String OTHER_REPAIR="other_repair";
    public static final String ELECTRIC_VEHICLE_REPAIR="electric_vehicle_repair";
    public static final String ALL_SERVICE="all_service";
    public static final String INTELLIGENT_FARM="intelligent_farm";
    public static final String DRINKING_WATER="drinking_water";
    public static final String VEGETABLE_SCXS="vegetables_scxs";
    public static final String HOUSEKEEPING_CLEAN="housekeeping_clean";
    public static final String HOME_MAINTENANCE="home_maintenance";

    public static final String AIR_CONDITIONER_REPAIR="air_conditioner_repair";
    public static final String WASHING_MACHINE_REPAIR="washing_machine_repair";
    public static final String REFRIGERATOR_REPAIR="refrigerator_repair";
    public static final String HEATER_REPAIR="heater_repair";
    public static final String GAS_STOVE_REPAIR="gas_stove_repair";
    public static final String HOODS_REPAIR="hoods_repair";
    public static final String COMPUTER_REPAIR="computer_repair";
    public static final String STERILIZER_REPAIR="sterilizer_repair";

    public static final String AIR_CONDITIONER_CLEAN="air_conditioner_clean";
    public static final String WASHING_MACHINE_CLEAN="washing_machine_clean";
    public static final String REFRIGERATOR_CLEAN="refrigerator_clean";
    public static final String HEATER_CLEAN="heater_clean";
    public static final String HOODS_CLEAN="hoods_clean";
    public static final String GAS_STOVE_CLEAN="gas_stove_clean";
    public static final String HOUSE_CLEANING="house_cleaning";
    public static final String DEEP_CLEANLINESS="deep_cleanliness";
    public static final String FIRST_CLEANLINESS="first_cleanliness";
    public static final String MITE_SERVICE="mite_service";
    public static final String FLOOR_WAXING="floor_waxing";
    public static final String MATTRESS_MITE="mattress_mite";
    public static final String SOFA_CLEANLINESS="sofa_cleanliness";

    public static final String UNLOCK_CHANGE="unlock_change";
    public static final String RUSH_PIPE="rush_pipe";
    public static final String FURNITURE="furniture";
    public static final String DOOR="door";
    public static final String WINDOW="window";
    public static final String HANGER_HARDWARE="hanger_hardware";
    public static final String BURGLAR_MESH="burglar_mesh";
    public static final String WALL_PERFORATION="wall_perforation";

    public static final String WATER_PIPE="water_pipe";
    public static final String WATER_TAP="water_tap";
    public static final String SHOWER="shower";
    public static final String CLOSESTOOL="closestool";
    public static final String BATHROOM="bathroom_cabinet";
    public static final String LAMP="lamp";
    public static final String SWITCH_SOCKET="switch_socket";
    public static final String CIRCUIT="circuit";

    public static final String MESSAGE_DETAIL="message_detail";
    public static final String MESSAGE="message";
    public static final String MESSAGE_LIST="message_list";

    public static final String SHOP_ELECTRIC_WATER_HEATER="shop_electric_water_heater";
    public static final String SHOP_LAMPBLACK_MACHINE="shop_lampblack_machine";
    public static final String SHOP_GAS_STOVE="shop_gas_stove";
    public static final String SHOP_WATER_HEATER="shop_water_heater";
    public static final String SHOP_IMPORTED_RED_WINE="shop_imported_red_wine";
    public static final String SHOP_STERILIZER="shop_sterilizer";


    public static final String SHOP_DOMAIN="shop.msbapp.cn";
    public static final String FIANL_SHOP_DOMAIN= BuildConfig.DEBUG ? "dev.msbapp.cn" : "shop.msbapp.cn";
    public static final String DEBUG_SHOP_DOMAIN="dev.msbapp.cn";
    public static final String VEGETABLE_DOMAIN="jsxss.net";

    public static final String WEI_XIN_CHAT_REDIRECT="wechat_redirect";
    public static final String HTTP="http";


    /**支付方式 */
    /**银联云闪付*/
    public static final String UPACP_DIRECT="upacp_direct";
    /**翼支付*/
    public static final String BEST_PAY="best_pay";


    /**分享方式* /
     *
     */
    /**朋友圈*/
    public static final String  WEI_XIN_PLATFORM="WEIXIN_FAVORITE";


    /**参数名*/
    public static final String USER_ID="userId";
    public static final String PHONE="phone";
    public static final String USER_NAME="userName";
    public static final String PASSWORD="password";
    public static final String ACTIVITY_USER_ID="activityUserId";
    public static final String ACTIVITY_PHONE="activityPhone";
    public static final String ACTIVITY_TOKEN="activityToken";

    /**符号名*/
    public static final String MARK_QUESTION="?";
    /**通知栏 */
    public static final String NOTIFICATION_PUSH_CHANNEL_ID="push";
    public static final String NOTIFICATION_DOWNLOAD_CHANNEL_ID="download";


    /**手机厂商**/
    public final static String HUAWEI = "Huawei";
    public final static String MEIZU = "Meizu";
    public final static String XIAOMI = "Xiaomi";
    public final static String SONY = "Sony";
    public final static String OPPO = "OPPO";
    public final static String VIVO = "vivo";
    public final static String SAMSUNG = "samsung";
    public final static String LG = "LG";
    public final static String LETV = "Letv";
    public final static String ZTE = "ZTE";
    public final static String YULONG = "YuLong";
    public final static String LENOVO = "LENOVO";


    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = getMac(context);

            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMac(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context);
        } else {
            mac = getMacByJavaAPI();
            if (TextUtils.isEmpty(mac)){
                mac = getMacBySystemInterface(context);
            }
        }
        return mac;

    }

    @TargetApi(9)
    private static String getMacByJavaAPI() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                if ("wlan0".equals(netInterface.getName()) || "eth0".equals(netInterface.getName())) {
                    byte[] addr = netInterface.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        return null;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static String getMacBySystemInterface(Context context) {
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Throwable e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Throwable e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

}
