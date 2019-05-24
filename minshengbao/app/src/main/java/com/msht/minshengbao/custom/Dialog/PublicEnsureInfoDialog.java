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
import android.widget.TextView;

import com.msht.minshengbao.R;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/2/15  
 */
public class PublicEnsureInfoDialog {
    private TextView tvTitle;
    private TextView tvText1;
    private TextView tvText2;
    private TextView tvText3;
    private TextView tvText4;
    private TextView tvText5;
    private TextView tvContent1;
    private TextView tvContent2;
    private TextView tvContent3;
    private TextView tvContent4;
    private TextView tvContent5;

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
    public PublicEnsureInfoDialog setOnPositiveClickListener(OnPositiveClickListener listener){
        this.callClickListener=listener;
        return this;
    }

    public PublicEnsureInfoDialog(Context context){
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }

    public PublicEnsureInfoDialog  builder(){
        // 获取Dialog布局
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_public_ensure_info,null);
        view.setMinimumWidth(display.getWidth());
        tvTitle= (TextView) view.findViewById(R.id.id_title);
        tvText1= (TextView) view.findViewById(R.id.id_text1);
        tvText2= (TextView) view.findViewById(R.id.id_text2);
        tvText3= (TextView) view.findViewById(R.id.id_text3);
        tvText4= (TextView) view.findViewById(R.id.id_text4);
        tvText5= (TextView) view.findViewById(R.id.id_text5);
        tvContent1=(TextView)view.findViewById(R.id.id_content1);
        tvContent2=(TextView)view.findViewById(R.id.id_content2);
        tvContent3=(TextView)view.findViewById(R.id.id_content3);
        tvContent4=(TextView)view.findViewById(R.id.id_content4);
        tvContent5=(TextView)view.findViewById(R.id.id_content5);
        view.findViewById(R.id.id_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.id_ensure).setOnClickListener(new View.OnClickListener() {
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
    public PublicEnsureInfoDialog setTitleText(String name){
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(name);
        return this;
    }
    public PublicEnsureInfoDialog setTitleText1(String name){
        tvText1.setText(name);
        return this;
    }
    public PublicEnsureInfoDialog setTitleText2(String name){
        tvText2.setText(name);
        return this;
    }
    public PublicEnsureInfoDialog setTitleText3(String name){
        tvText3.setText(name);
        return this;
    }
    public PublicEnsureInfoDialog setTitleText4(String name){
        tvText4.setText(name);
        return this;
    }
    public PublicEnsureInfoDialog setTitleText5(String name){
        tvText5.setText(name);
        return this;
    }
    public PublicEnsureInfoDialog setContentText1(String text){
        if (!TextUtils.isEmpty(text)){
            tvContent1.setVisibility(View.VISIBLE);
            tvText1.setVisibility(View.VISIBLE);
        }else {
            tvText1.setVisibility(View.GONE);
            tvContent1.setVisibility(View.GONE);
        }
        tvContent1.setText(text);
        return this;
    }
    public PublicEnsureInfoDialog setContentText2(String text){
        if (!TextUtils.isEmpty(text)){
            tvContent2.setVisibility(View.VISIBLE);
            tvText2.setVisibility(View.VISIBLE);
        }else {
            tvText2.setVisibility(View.GONE);
            tvContent2.setVisibility(View.GONE);
        }
        tvContent2.setText(text);
        return this;
    }
    public PublicEnsureInfoDialog setContentText3(String text){
        if (!TextUtils.isEmpty(text)){
            tvText3.setVisibility(View.VISIBLE);
            tvContent3.setVisibility(View.VISIBLE);
        }else {
            tvText3.setVisibility(View.GONE);
            tvContent3.setVisibility(View.GONE);
        }
        tvContent3.setText(text);
        return this;
    }
    public PublicEnsureInfoDialog setContentText4(String text){
        if (!TextUtils.isEmpty(text)){
            tvText4.setVisibility(View.VISIBLE);
            tvContent4.setVisibility(View.VISIBLE);
        }else {
            tvText4.setVisibility(View.GONE);
            tvContent4.setVisibility(View.GONE);
        }
        tvContent4.setText(text);
        return this;
    }
    public PublicEnsureInfoDialog setContentText5(String text){
        if (!TextUtils.isEmpty(text)){
            tvText5.setVisibility(View.VISIBLE);
            tvContent5.setVisibility(View.VISIBLE);
        }else {
            tvText5.setVisibility(View.GONE);
            tvContent5.setVisibility(View.GONE);
        }
        tvContent5.setText(text);
        return this;
    }
    public  PublicEnsureInfoDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public PublicEnsureInfoDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
