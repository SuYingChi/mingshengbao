package com.msht.minshengbao.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hong on 2017/9/22.
 */

public class DateUtils {
    /**
     * 获取系统时间戳
     * @return
     */
    public static long getCurTimeLong(){
        Date date=new Date(System.currentTimeMillis());
        return System.currentTimeMillis();
    }
    /**
     * 获取当前时间
     * @param pattern
     * @return
     */
    public static String getCurDate(String pattern){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern,Locale.CHINA);
        return sDateFormat.format(new java.util.Date());
    }

    public static String getCurrentDateString(String pattern){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern,Locale.CHINA);
        Date date=new Date(System.currentTimeMillis());
        return sDateFormat.format(date);
    }
    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern,Locale.CHINA);
        return format.format(date);
    }

    /**
     * 获取系统日期戳
     * @return
     */
    public static long getCurDateLong(String pattern){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern,Locale.CHINA);
        Date date=new Date(System.currentTimeMillis());
        return getStringToDate(sDateFormat.format(date),pattern);
    }
    /**
     * 将字符串转为时间戳
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern,Locale.CHINA);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @param str2 the second date
     * @return true <br/>false
     */
    public static boolean isDateBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = false;
        } else if (dt1.getTime() <= dt2.getTime()) {
            isBigger = true;
        }
        return isBigger;
    }
}
