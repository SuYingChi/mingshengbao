package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.msht.minshengbao.R;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class HomeAdvertisingDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private String  mUrl;
    private String  imageUrl;
    private OnAdvertisingClickListener onClickListener;
    public interface OnAdvertisingClickListener {
        /**
         *  回调返回数据
         * @param url
         */
        void onClick(String url);
    }
    public HomeAdvertisingDialog setOnAdvertisingClickListener(OnAdvertisingClickListener listener){
        this.onClickListener=listener;
        return this;
    }
    public HomeAdvertisingDialog(Context context,String image,String url) {
        this.context = context;
        this.mUrl=url;
        this.imageUrl=image;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }
    public HomeAdvertisingDialog builder() {
        // 获取Dialog布局
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_home_advertising,null);
        view.setMinimumWidth(display.getWidth());
        ImageView closeImg=view.findViewById(R.id.id_close_img);
        SimpleDraweeView advertisingImg=(SimpleDraweeView)view.findViewById(R.id.id_advertising_img);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Uri uri = Uri.parse(imageUrl);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                //. 其他设置（如果有的话）
                .build();
        advertisingImg.setController(controller);
        advertisingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener!=null){
                    onClickListener.onClick(mUrl);
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

    public  HomeAdvertisingDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public HomeAdvertisingDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
