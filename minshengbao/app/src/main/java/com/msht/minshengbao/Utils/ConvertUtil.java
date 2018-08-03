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
    /**
     *
      */
    /**
     * dip转换为px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
