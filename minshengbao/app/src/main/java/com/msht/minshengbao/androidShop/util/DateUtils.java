package com.msht.minshengbao.androidShop.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hong on 2017/9/22.
 */

public class DateUtils {
    /**
     * 获取系统时间戳
     *
     * @return
     */
    public static long getCurTimeLong() {
        Date date = new Date(System.currentTimeMillis());
        long time = System.currentTimeMillis();
        return time;
    }

    /**
     * 获取当前时间
     *
     * @param pattern
     * @return
     */
    public static String getCurDate(String pattern) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new java.util.Date());
    }

    /**
     * 时间戳转换成字符窜
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(String milSecond, String pattern) {
        Date date = new Date(Long.parseLong(milSecond));
        SimpleDateFormat format = new SimpleDateFormat(pattern,Locale.CHINA);
        return format.format(date);
    }
    public static String secondFormatToDate(String milSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milSecond) * 1000);//转换为毫秒
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年-MM月-dd日",Locale.CHINA);
        String dateString = format.format(date);
       return dateString;
    }
    public static String secondFormatToLeftDay(Long  milSecond) {
        Long remainSecond = milSecond;
        int hour;
        int day = (int)(remainSecond /86400);
        if(remainSecond-86400*day>=0){
            remainSecond= remainSecond-86400*day;
        }
         hour =  (int)(remainSecond/3600);
        if(remainSecond-hour*3600>=0){
            remainSecond= remainSecond-hour*3600;
        }
        int fen = (int) (remainSecond / 60);
        if(remainSecond-fen*60>=0){
            remainSecond= remainSecond-fen*60;
        }

        return day+"天"+hour+"小时"+fen+"分钟"+remainSecond+"秒";
    }
    /**
     * 将字符串转为时间戳
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
