package com.msht.minshengbao.androidShop.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;

import com.msht.minshengbao.R;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * @author lulei
 * Created 2017/12/5 10:46
 * @copyright Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license http://www.shopnc.net
 * @link http://www.shopnc.net
 * <p>
 * 文字显示工具类
 */
public class StringUtil {

    /**
     * 大小显示价格串
     *
     * @param context
     * @param appPrice 价格显示
     * @param style1   小数点后格式（style）
     * @param style2   小数点前格式（style）
     * @return
     */
    public static SpannableString getPriceSpannable12String(Context context, String appPrice, int style1, int style2) {
        String monetary_unit = context.getResources().getString(R.string.monetary_unit) + " ";
        String s = monetary_unit + appPrice;
        int position;
        if (!s.contains(".")) {
            s += ".00";
        }else if(s.substring(s.indexOf(".")+1).length()==1){
            s = s.substring(0,s.indexOf(".")+2)+"0";
        }else if(s.substring(s.indexOf(".")+1).length()==0){
            s = s.substring(0,s.indexOf(".")+2)+"00";
        }else if(s.substring(s.indexOf(".")+1).length()>2){
            s = s.substring(0,s.indexOf(".")+3);
        }
        position = s.indexOf(".");
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new TextAppearanceSpan(context, style1), 0, monetary_unit.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextAppearanceSpan(context, style2), monetary_unit.length(), position, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextAppearanceSpan(context, style1), position, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 半角转化为全角的方法
     */
    public static String ToSBC(String input) {
        // 半角转全角：
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {

            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127 && c[i] > 32)
                c[i] = (char) (c[i] + 65248);

        }
        return new String(c);
    }

    // 全角转化为半角的方法

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (isChinese(c[i])) {
                if (c[i] == 12288) {
                    c[i] = (char) 32;
                    continue;
                }
                if (c[i] > 65280 && c[i] < 65375)
                    c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 利用编码的方式判断字符是否为汉字的方法
     *
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 商品名称（带标签）
     *
     * @param context
     * @param goodsName
     * @param drawable
     * @return
     */
    public static SpannableString getGoodsNameString(Context context, String goodsName, Drawable drawable) {
        SpannableString span = new SpannableString(goodsName);
        Bitmap bitmap = DrawbleUtil.drawableToBitmap(drawable);
        Bitmap fitBitmap = DrawbleUtil.scale(bitmap, 0.5f, 0.5f);
        MyImageSpan image = new MyImageSpan(context, fitBitmap);
        span.setSpan(image, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
    /**
     * URLEncoder编码
     */
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }
        return "";
    }

    /**
     * URLDecoder解码
     */
    public static String toURLDecoder(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String url = new String(paramString.getBytes(), "UTF-8");
            url = URLDecoder.decode(url, "UTF-8");
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


}
