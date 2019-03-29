package com.msht.minshengbao.ViewUI.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;

/**
 * Demo class
 * 自定义Toast
 * 多样式Toast
 * @author hong
 * @date 2019/3/29  
 */
public class CustomToast {
    private static final int STYLE_CIRCLE=0;
    private static final int STYLE_RECTANGLE=1;

    /**
     * 展示toast==LENGTH_SHORT
     *
     * @param msg
     */
    public static void show(String msg) {
        show(R.mipmap.ic_launcher,R.mipmap.ic_launcher,msg, Toast.LENGTH_SHORT,STYLE_CIRCLE);
    }

    /**
     * 展示toast==LENGTH_LONG
     *
     * @param msg
     */
    public static void showLong(String msg) {
        show(R.mipmap.ic_launcher,R.mipmap.ic_launcher,msg, Toast.LENGTH_LONG,STYLE_CIRCLE);
    }
    public static void showSuccessLong(String msg) {
        show(R.drawable.qmui_icon_notify_done,R.mipmap.ic_launcher,msg, Toast.LENGTH_LONG,STYLE_CIRCLE);
    }
    public static void showErrorLong(String msg) {
        show(R.drawable.qmui_icon_notify_error,R.mipmap.ic_launcher,msg, Toast.LENGTH_LONG,STYLE_CIRCLE);
    }
    public static void showWarningLong(String msg) {
        show(R.drawable.qmui_icon_notify_info,R.mipmap.ic_launcher,msg, Toast.LENGTH_LONG,STYLE_CIRCLE);
    }

    public static void showSuccessDialog(String msg) {
        show(R.drawable.qmui_icon_notify_done,R.drawable.qmui_icon_notify_done,msg, Toast.LENGTH_LONG,STYLE_RECTANGLE);
    }
    public static void showErrorDialog(String msg) {
        show(R.drawable.qmui_icon_notify_error,R.drawable.qmui_icon_notify_error,msg, Toast.LENGTH_LONG,STYLE_RECTANGLE);
    }
    public static void showWarningDialog(String msg) {
        show(R.drawable.qmui_icon_notify_info,R.drawable.qmui_icon_notify_info,msg, Toast.LENGTH_LONG,STYLE_RECTANGLE);
    }
    private static void onShowRectangleToast(String msg){
        show(R.mipmap.ic_launcher,R.mipmap.ic_launcher,msg, Toast.LENGTH_LONG,STYLE_RECTANGLE);
    }
    private static void show(int icon,int topIcon, String massage, int showLength,int style) {
        Context context = MyApplication.getMsbApplicationContext();
        //使用布局加载器，将编写的toast_layout布局加载进来
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_toast, null);
        //获取ImageView
        ImageView image = (ImageView) view.findViewById(R.id.id_left_icon);
        ImageView topImage=(ImageView) view.findViewById(R.id.id_top_icon);
        if (style==STYLE_RECTANGLE){
            view.setBackgroundResource(R.drawable.shape_dialog_custom_bg);
            image.setVisibility(View.GONE);
            topImage.setVisibility(View.VISIBLE);
        }
        //设置图片
        topImage.setImageResource(topIcon);
        image.setImageResource(icon);
        //获取TextView
        TextView title = (TextView) view.findViewById(R.id.id_message_text);
        //设置显示的内容
        title.setText(massage);
        Toast toast = new Toast(context);
        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
        toast.setGravity(Gravity.CENTER , 0, 0);
        if (style==STYLE_RECTANGLE){
            toast.setGravity(Gravity.CENTER , 0, 0);
        }else {
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 90);
        }
        //设置显示时间
        toast.setDuration(showLength);
        toast.setView(view);
        toast.show();
    }


}
