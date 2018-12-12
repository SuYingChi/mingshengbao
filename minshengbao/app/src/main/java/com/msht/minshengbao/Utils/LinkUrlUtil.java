package com.msht.minshengbao.Utils;

import android.content.Context;
import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class LinkUrlUtil {
    /**
     * //获取域名
     * @param url
     * @return
     */
    static String getDomain(String url){
        URL urls=null;
        String p="";
        try{
            urls=new URL(url);
            p=urls.getHost();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return p;
    }
    /**
     * 解析参数
     * @param context 上下文
     * @param url 连接
     * @return url
     */
    public static String replaceParamsUrl(Context context,String url){
        if (context!=null){
            if (url.contains(ConstantUtil.ACTIVITY_USER_ID)){
                String userId= SharedPreferencesUtil.getUserId(context, SharedPreferencesUtil.UserId, "");
                url=replaceParams(url,ConstantUtil.ACTIVITY_USER_ID,userId);
            }
            if (url.contains(ConstantUtil.ACTIVITY_TOKEN)){
                String password= SharedPreferencesUtil.getPassword(context, SharedPreferencesUtil.Password, "");
                url=replaceParams(url,ConstantUtil.ACTIVITY_TOKEN,password);
            }
            if (url.contains(ConstantUtil.ACTIVITY_PHONE)){
                String userName= SharedPreferencesUtil.getUserName(context, SharedPreferencesUtil.UserName, "");
                url=replaceParams(url,ConstantUtil.ACTIVITY_PHONE,userName);
            }
        }
        return url;
    }
    /**
     *  替换参数值
     * @param url 链接
     * @param key key名
     * @param value 值
     * @return
     */
    public static String replaceParams(String url, String key, String value){

        if (!TextUtils.isEmpty(url)&&!TextUtils.isEmpty(key)){
            url=url.replaceAll("("+key+"=[^&]*)",key+"="+value);
        }
        return url;
    }
    public static String containMark(Context context,String url){
        if (context!=null){
            if (!url.contains(ConstantUtil.MARK_QUESTION)){
                String password= SharedPreferencesUtil.getPassword(context, SharedPreferencesUtil.Password, "");
                String userId= SharedPreferencesUtil.getUserId(context, SharedPreferencesUtil.UserId, "");
                String userName= SharedPreferencesUtil.getUserName(context, SharedPreferencesUtil.UserName, "");
                url=url+"?activityUserId="+userId+"&activityPhone="+userName+"&activityToken="+password;
            }else {
                if (url.contains(ConstantUtil.ACTIVITY_PHONE)||url.contains(ConstantUtil.ACTIVITY_USER_ID)){
                    url=replaceParamsUrl(context,url);
                }else {
                    String password= SharedPreferencesUtil.getPassword(context, SharedPreferencesUtil.Password, "");
                    String userId= SharedPreferencesUtil.getUserId(context, SharedPreferencesUtil.UserId, "");
                    String userName= SharedPreferencesUtil.getUserName(context, SharedPreferencesUtil.UserName, "");
                    url=url+"&activityUserId="+userId+"&activityPhone="+userName+"&activityToken="+password;
                }
            }
        }
        return url;
    }
}
