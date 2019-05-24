package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
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
public class BitmapQrCodeDialog {
    private Bitmap bitmap;
    private Context context;
    private Dialog dialog;
    private Display display;
    private OnShareButtonClickListener itemClickTwoListener;
    private OnSaveButtonClickListener itemClickOneListener;
    private OnShareButtonTwoClickListener itemClickThreadListener;
    public interface OnShareButtonClickListener {
        /**
         *回调
         * @param v
         * @param layout
         */
        void onClick(View v,View layout);
    }
    public interface OnSaveButtonClickListener {
        /**
         *回调
         * @param v
         * @param layout
         */
        void onClick(View v,View layout);
    }
    public interface OnShareButtonTwoClickListener {
        /**
         *回调
         * @param v
         * @param layout
         */
        void onClick(View v,View layout);
    }
    public BitmapQrCodeDialog setOnSaveButtonClickListener(OnSaveButtonClickListener listener){
        this.itemClickOneListener=listener;
        return this;
    }
    public BitmapQrCodeDialog setOnShareButtonClickListener(OnShareButtonClickListener listener){
        this.itemClickTwoListener=listener;
        return this;
    }
    public BitmapQrCodeDialog setOnShareButtonTwoClickListener(OnShareButtonTwoClickListener listener){
        this.itemClickThreadListener=listener;
        return this;
    }
    public BitmapQrCodeDialog(Context context,Bitmap bitmap) {
        this.context = context;
        this.bitmap=bitmap;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }
    public BitmapQrCodeDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_bitmap_qr_code, null);
        // 设置Dialog最小宽度为屏幕宽度
        ImageView imageView=(ImageView)view.findViewById(R.id.id_qr_code);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }
        final View qrCodeLayout=view.findViewById(R.id.id_qrCode_layout);
        view.findViewById(R.id.id_close_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.id_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickOneListener!=null){
                    itemClickOneListener.onClick(v,qrCodeLayout);
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.id_weiXin_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickTwoListener!=null){
                    itemClickTwoListener.onClick(v,qrCodeLayout);
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.id_weiXin_circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickThreadListener!=null){
                    itemClickThreadListener.onClick(view,qrCodeLayout);
                }
                dialog.dismiss();
            }
        });
        qrCodeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (itemClickOneListener!=null){
                    itemClickOneListener.onClick(view,view);
                }
                dialog.dismiss();
                return false;
            }
        });
        dialog = new Dialog(context, R.style.PublicSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        display.getMetrics(displayMetrics);
        if (dialogWindow!=null){
            dialogWindow.setGravity(Gravity.CENTER_VERTICAL);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width=displayMetrics.widthPixels;
            lp.height=displayMetrics.heightPixels;
            /*lp.x = 0;
            lp.y = 0;*/
            dialogWindow.setAttributes(lp);
        }
        return this;
    }
    public BitmapQrCodeDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public BitmapQrCodeDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
