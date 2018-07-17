package com.msht.minshengbao.Utils;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 拨打电话
 * @author hong
 * @date 2018/7/4  
 */
public class CallPhoneUtil {
    public static void callPhone(final Context context, final String phone) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(callIntent);
        }else {
            ToastUtil.ToastText(context,"请您允许使用拨打电话权限");
        }
    }
}
