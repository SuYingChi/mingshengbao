package com.msht.minshengbao.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author hong
 * @date 2017/9/22
 */

public class DateUtils {
    /**
     * 获取系统时间戳
     * @return
     */
    public static long getCurTimeLong(){
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
     * 将yyyy年MM月dd日转化yyyy-MM-dd
     * @param dateString
     * @return
     */
    public static String getStringDate(String dateString,String originString,String newString){
        SimpleDateFormat dateFormat = new SimpleDateFormat(originString,Locale.CHINA);
        SimpleDateFormat format = new SimpleDateFormat(newString,Locale.CHINA);
        Date date=new Date();
        try{
             date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return format.format(date);
    }

    /**
     * 获取年份
     * @param dateString
     * @param pattern
     * @return
     */
    public static int getYearDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern,Locale.CHINA);
        //Date date;
        Calendar calendar=Calendar.getInstance();
        try{
            Date date = dateFormat.parse(dateString);
            calendar.setTime(date);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return calendar.get(Calendar.YEAR);
    }
    /**
     * 获取月份
     * @param dateString
     * @param pattern
     * @return
     */
    public static int getMonthDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern,Locale.CHINA);
        //Date date;
        Calendar calendar=Calendar.getInstance();
        try{
            Date date = dateFormat.parse(dateString);
            calendar.setTime(date);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return calendar.get(Calendar.MONTH);
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

    /**
     * 得到指定月的天数
     * */
    public static int getMonthLastDay(int year, int month)
    {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        return a.get(Calendar.DATE);
    }
    /**
     * 获取某年某月有多少天
     */
    public static int getDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0); //输入类型为int类型
        return c.get(Calendar.DAY_OF_MONTH);
    }
}
