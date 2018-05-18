package com.msht.minshengbao.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hong on 2018/4/4.
 */

public class ToastUtil {
    public static void ToastText(Context context, String s) {
        if (context!=null){
            Toast.makeText(context,s ,Toast.LENGTH_SHORT).show();
        }
    }
}
