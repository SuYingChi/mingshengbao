package com.msht.minshengbao.Utils;

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
}
