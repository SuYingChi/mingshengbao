package com.msht.minshengbao.Utils;

import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/2/19  
 */

public abstract class AbstractJson {

    private static AbstractJson json;
    AbstractJson() { }
     public static AbstractJson get() {
                if (json == null) {
                         json = new GsonImpl();
                     }
                 return json;
             }
     public abstract String toJson(Object src);
     public abstract <T> T toObject(String json, Class<T> claxx);
     public abstract <T> T toObject(byte[] bytes, Class<T> claxx);
     public abstract <T> List<T> toList(String json, Class<T> claxx);
}
