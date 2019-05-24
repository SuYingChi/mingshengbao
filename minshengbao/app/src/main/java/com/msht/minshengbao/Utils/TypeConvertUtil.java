package com.msht.minshengbao.Utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class TypeConvertUtil {
    public static String listToString(List<String> list){
        String result="";
        if (list!=null){
            boolean flag=false;
            StringBuilder stringBuilder=new StringBuilder();
            for (String string:list){
                if (flag){
                    stringBuilder.append(",");
                }else {
                    flag=true;
                }
                stringBuilder.append(string);
            }
            result=stringBuilder.toString();
        }
        return result;
    }

    /**把String转化为float
     *
     * @param number
     * @param defaultValue
     * @return
     */
    public static float convertToFloat(String number, float defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            return defaultValue;
        }

    }

    /**把String转化为double
     * @param number 字符串
     * @param defaultValue 默认值
     * @return
     */
    public static double convertToDouble(String number, double defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            return defaultValue;
        }

    }

    /**把String转化为int
     *
     * @param number
     * @param defaultValue
     * @return
     */
    public static int convertToInt(String number, int defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * string 转化数字相加
     * @param num1
     * @param num2
     * @return
     */
    public static String getStringAddToDouble(String num1,String num2){
        double value=convertToDouble(num1,0)+convertToDouble(num2,0);
        return String.valueOf(value);
    }
}
