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
    public static boolean isPhone(String phone){
        Matcher matcher=NUMBER_PATTERN.matcher(phone);
        return matcher.matches();
    }
}
