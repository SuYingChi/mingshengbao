package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterPublicDialog {
    private TextView tvNotice;
    private Button   btnKnow;
    private TextView tvTitle;
    private ImageView viewImg;
    private View waterLayout;
    private Context context;
    private Dialog dialog;
    private Display display;
    private OnPositiveClickListener callClickListener;
    public interface OnPositiveClickListener {
        /**
         *  回调返回数据
         * @param v v
         */
        void onClick(View v);
    }
    public WaterPublicDialog setOnPositiveClickListener(OnPositiveClickListener listener){
        this.callClickListener=listener;
        return this;
    }
    public WaterPublicDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }
    public WaterPublicDialog builder() {
        // 获取Dialog布局
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_water_public,null);
        view.setMinimumWidth(display.getWidth());
        waterLayout=view.findViewById(R.id.id_water_quality);
        tvTitle=(TextView)view.findViewById(R.id.id_tv_title);
        tvNotice=(TextView)view.findViewById(R.id.id_tv_notice);
        btnKnow=(Button)view.findViewById(R.id.id_btn_ensure) ;
        viewImg=(ImageView)view.findViewById(R.id.id_view_img);
        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callClickListener!=null){
                    callClickListener.onClick(v);
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

    public WaterPublicDialog setTitleText(String name){
        if (!TextUtils.isEmpty(name)){
            tvTitle.setVisibility(View.VISIBLE);
        }else {
            tvTitle.setVisibility(View.GONE);
        }
        tvTitle.setText(name);
        return this;
    }
    public WaterPublicDialog setNoticeText(String string){
        if (!TextUtils.isEmpty(string)){
            tvNotice.setVisibility(View.VISIBLE);
        }else {
            tvNotice.setVisibility(View.GONE);
        }
        tvNotice.setText(string);
        return this;
    }
    public WaterPublicDialog setButtonText(String string){
        btnKnow.setText(string);
        return this;
    }
    public  WaterPublicDialog setImageVisibility(boolean visibility) {
        if (visibility){
            viewImg.setVisibility(View.VISIBLE);
        }else {
            viewImg.setVisibility(View.GONE);
        }
        return this;
    }
    public  WaterPublicDialog setViewVisibility(boolean visibility) {
        if (visibility){
            waterLayout.setVisibility(View.VISIBLE);
        }else {
            waterLayout.setVisibility(View.GONE);
        }
        return this;
    }
    public  WaterPublicDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public WaterPublicDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
