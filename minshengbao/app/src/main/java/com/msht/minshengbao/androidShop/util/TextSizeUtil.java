package com.msht.minshengbao.androidShop.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;

import com.msht.minshengbao.R;


/**
 * Created by lulei on 2017/9/6.
 */

public class TextSizeUtil {

    //大小显示价格串
    public static SpannableString getPriceSpannableString(Context context, String s, String s1, int style1, int style2) {
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new TextAppearanceSpan(context, style1), 0, s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextAppearanceSpan(context, style2), s1.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    //改动某一段字符串的风格
    public static SpannableString changeTextSpannableString(Context context, String s, int style) {
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new TextAppearanceSpan(context, style), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    //大小显示价格串
    private SpannableString getPriceSpannableString(Context context, String s) {
        String moneyRmb = context.getResources().getString(R.string.monetary_unit);
        SpannableString spannableString = new SpannableString(s);
        int position = s.indexOf(".");
        spannableString.setSpan(new TextAppearanceSpan(context, R.style.search_good_small), 0, moneyRmb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextAppearanceSpan(context, R.style.search_good_big), moneyRmb.length(), position, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextAppearanceSpan(context, R.style.search_good_small), position, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


}
