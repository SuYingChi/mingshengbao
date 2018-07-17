package com.msht.minshengbao.Utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈转换工具类
 * @author hong
 * @date 2018/7/3  
 */
public class ConvertUtil {
    /**
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
