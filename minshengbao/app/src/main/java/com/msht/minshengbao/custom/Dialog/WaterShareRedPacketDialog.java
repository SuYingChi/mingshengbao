package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.msht.minshengbao.R;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterShareRedPacketDialog {


    private Context context;
    private Dialog dialog;
    private Display display;
    private OnCancelClickListener onCancelClickListener;
    private OnAdvertisingClickListener onClickListener;
    public interface OnAdvertisingClickListener {
        /**
         *  回调返回数据
         * @param view
         */
        void onClick(View view);
    }
    public interface OnCancelClickListener{
        /**
         * 取消
         * @param view
         */
        void  onCancelClick(View view);
    }
    public WaterShareRedPacketDialog setOnAdvertisingClickListener(OnAdvertisingClickListener listener){
        this.onClickListener=listener;
        return this;
    }
    public WaterShareRedPacketDialog setOnCancelClickListener(OnCancelClickListener listener){
        this.onCancelClickListener=listener;
        return this;
    }

    public WaterShareRedPacketDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }

    public WaterShareRedPacketDialog builder() {
        // 获取Dialog布局
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_red_packet_share,null);
        view.setMinimumWidth(display.getWidth());
        ImageView closeImg=view.findViewById(R.id.id_close_img);
        SimpleDraweeView advertisingImg=(SimpleDraweeView)view.findViewById(R.id.id_advertising_img);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCancelClickListener!=null){
                    onCancelClickListener.onCancelClick(view);
                }
                dialog.dismiss();
            }
        });
        advertisingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener!=null){
                    onClickListener.onClick(view);
                }
                dialog.dismiss();
            }
        });
        dialog = new Dialog(context, R.style.PromptDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow!=null){
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);
        }
        return this;
    }

    public  WaterShareRedPacketDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public WaterShareRedPacketDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }


}
