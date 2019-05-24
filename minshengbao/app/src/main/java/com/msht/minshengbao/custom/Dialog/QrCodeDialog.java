package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.msht.minshengbao.R;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class QrCodeDialog {

    private Bitmap bitmap;
    private Context context;
    private Dialog dialog;
    private Display display;
    private OnShareButtonClickListener itemClickTwoListener;
    private OnSaveButtonClickListener itemClickOneListener;
    public interface OnShareButtonClickListener {
        /**
         *回调
         * @param v
         */
        void onClick(View v);
    }
    public interface OnSaveButtonClickListener {
        /**
         *回调
         * @param v
         */
        void onClick(View v);
    }
    public QrCodeDialog setOnSaveButtonClickListener(OnSaveButtonClickListener  listener){
        this.itemClickOneListener=listener;
        return this;
    }
    public QrCodeDialog setOnShareButtonClickListener(OnShareButtonClickListener listener){
        this.itemClickTwoListener=listener;
        return this;
    }

    public QrCodeDialog(Context context,Bitmap bitmap) {
        this.context = context;
        this.bitmap=bitmap;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }

    public QrCodeDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_qrcode_layout, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        ImageView imageView=(ImageView)view.findViewById(R.id.id_qr_code);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }
        view.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.id_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickOneListener!=null){
                    itemClickOneListener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.id_weiXin_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickTwoListener!=null){
                    itemClickTwoListener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        dialog = new Dialog(context, R.style.PublicSheetDialogStyle);
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
    public QrCodeDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public QrCodeDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}


