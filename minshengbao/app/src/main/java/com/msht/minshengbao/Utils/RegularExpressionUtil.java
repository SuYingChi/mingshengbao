package com.msht.minshengbao.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 正则表达式
 * @author hong
 * @date 2018/6/26
 */
public class RegularExpressionUtil {
    private static Pattern NUMBER_PATTERN = Pattern.compile("1[0-9]{10}");
    private static String eMailText="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|((["+
            "a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private static Pattern NUMERIC_PATTERN=Pattern.compile("[0-9]*");

    public static boolean isPhone(String phone){
        Matcher matcher=NUMBER_PATTERN.matcher(phone);
        return matcher.matches();
    }
    public static boolean isEmail(String email){
        Pattern p=Pattern.compile(eMailText);
        Matcher m=p.matcher(email);
        return m.matches();
    }
    public static boolean isNumeric(String str){
        Matcher isNum=NUMERIC_PATTERN.matcher(str);
        return isNum.matches();
    }
}
