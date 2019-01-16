package com.msht.minshengbao.androidShop.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseFragment;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AppUtil {
    private static final String TAG = "AppUtil";
    private static PackageInfo packageInfo;

    public static boolean isLPGInstalled(String packageName) {
        PackageManager pm = MyApplication.getMsbApplicationContext().getPackageManager();
        boolean installed;
        try {
            packageInfo = pm.getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                installed = true;
            } else {
                installed = false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public static void hideSoftInput(Activity mActivity) {
        try {
            IBinder mIBinder = mActivity.getCurrentFocus().getWindowToken();
            if (mIBinder != null && (mActivity.getSystemService(INPUT_METHOD_SERVICE)) != null) {
                ((InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(mIBinder, InputMethodManager.HIDE_NOT_ALWAYS);
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) MyApplication.getMsbApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    /**
     * 检查wifi是否处开连接状态
     *
     * @return
     */
    public static boolean isWifiConnect() {
        ConnectivityManager connManager = (ConnectivityManager) MyApplication.getMsbApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifiInfo.isConnected();
    }

    public static void logout() {
        ShopSharePreferenceUtil.getInstance().setKey("");
        ShopSharePreferenceUtil.setShopSpStringValue("employeeName", "");
        ShopSharePreferenceUtil.setShopSpStringValue("siteName", "");
    }


    public static void goLogin(Context mContext) {
        Intent go = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(go);
    }

    public static void hideInput(Context context, EditText et) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), 0);

    }

    /**
     * 2 * 获取版本号 3 * @return 当前应用的版本号 4
     */
    public static int getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {

        }

        return verName;
    }

    public static String getPackageName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (Exception e) {

        }

        return verName;
    }

    //未指定应用商店，让用户选择去哪个应用商店
    public static void goMarket(Context context) {
        try {

            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());// id为包名
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);

        } catch (Exception e) {
            PopUtil.toastInBottom("请打开应用市场，搜索 " + context.getString(R.string.app_name));
            e.printStackTrace();
        }
    }

    /**
     * 跳转应用商店.
     *
     * @param context   {@link Context}
     * @param marketPkg 应用商店包名
     * @return {@code true} 跳转成功 <br> {@code false} 跳转失败
     */
    public static void toMarket(Context context, String marketPkg) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (marketPkg != null) {// 如果没给市场的包名，则系统会弹出市场的列表让你进行选择。
            intent.setPackage(marketPkg);
        }
        try {
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //三星应用市场
    public static void goToSamsungappsMarket(Context context) {
        Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + context.getPackageName());
        Intent goToMarket = new Intent();
        goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        goToMarket.setData(uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            PopUtil.toastInBottom("请打开应用市场，搜索 " + context.getString(R.string.app_name));
            e.printStackTrace();
        }
    }

    //乐视手机应用市场
    public static void goToLeTVStoreDetail(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.letv.app.appstore", "com.letv.app.appstore.appmodule.details.DetailsActivity");
        intent.setAction("com.letv.app.appstore.appdetailactivity");
        intent.putExtra("packageName", context.getPackageName());
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            PopUtil.toastInBottom("请打开应用市场，搜索 " + context.getString(R.string.app_name));
            e.printStackTrace();
        }
    }

    /**
     * 跳转索尼精选
     *
     * @param context {@link Context}
     * @param appId   索尼精选中分配得appId
     * @return {@code true} 跳转成功 <br> {@code false} 跳转失败
     */
    public static void goToSonyMarket(Context context, String appId) {
        Uri uri = Uri.parse("http://m.sonyselect.cn/" + appId);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        Intent intent = new Intent();
//        intent.setAction("com.sonymobile.playnowchina.android.action.NOTIFICATION_APP_DETAIL_PAGE");
//        intent.setAction("com.sonymobile.playnowchina.android.action.APP_DETAIL_PAGE");
//        intent.putExtra("app_id", 9115);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void replaceFragment(ShopBaseFragment showfragment, ShopBaseFragment currentFragment, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(currentFragment).commit();
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.forward_enter, R.anim.forward_exit_fragment);
        transaction.replace(R.id.fl_my_container, showfragment).commit();
    }

    public static void showFragment(ShopBaseFragment showfragment, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_my_container, showfragment).commit();
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND.trim().toUpperCase();
    }

    /*    */

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     *//*
        public static String getIMEI(Context ctx) {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
            if (tm != null) {
                return tm.getDeviceId();
            }
            return null;
        }*/
    public static String formattedOutputDecimal(double decimal) {
        DecimalFormat df = new DecimalFormat("######0.00");
        String result = df.format(decimal);
        return result;
    }

    public static String formattedDecimalToPercentage(double decimal) {
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);
        return nt.format(decimal);
    }

    public static boolean equalList(List list1, List list2) {
        if (list1.size() != list2.size())
            return false;
        for (Object object : list1) {
            if (!list2.contains(object))
                return false;
        }
        return true;
    }
}
