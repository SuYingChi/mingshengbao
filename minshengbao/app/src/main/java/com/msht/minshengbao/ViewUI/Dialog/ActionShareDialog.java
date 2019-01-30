package com.msht.minshengbao.ViewUI.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msht.minshengbao.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class ActionShareDialog {
    private Context context;
    private Dialog dialog;
    private Display display;

    private OnSheetButtonOneClickListener itemClickOneListener;
    public interface OnSheetButtonOneClickListener {
        /**
         *回调
         * @param pos
         */
        void onClick(int pos);
    }
    public  ActionShareDialog setOnSheetButtonOneClickListener(OnSheetButtonOneClickListener listener){
        this.itemClickOneListener=listener;
        return this;
    }

    public ActionShareDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }
    public ActionShareDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_share_insurance, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        TextView textOk=(TextView)view.findViewById(R.id.id_text_ok);
        Button textCancel = (Button) view.findViewById(R.id.id_btn_cancel);
        view.findViewById(R.id.id_weiXin_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickOneListener!=null){
                    itemClickOneListener.onClick(0);
                }
                dialog.dismiss();
            }
        }) ;
        view.findViewById(R.id.id_friend_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickOneListener!=null){
                    itemClickOneListener.onClick(1);
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.id_code_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickOneListener!=null){
                    itemClickOneListener.onClick(2);
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.id_copy_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickOneListener!=null){
                    itemClickOneListener.onClick(3);
                }
                dialog.dismiss();
            }
        });
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow!=null){
            dialogWindow.setGravity(Gravity.START|Gravity.BOTTOM);
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);
        }
        return this;
    }


    public ActionShareDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public ActionShareDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
