package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
 * @date 2019/5/22 
 */
public class EnsureDialog {
    private TextView tvCustomerNo, tvAddress;
    private Button btnEnsure, btnCancel;
    private TextView tvTitle;
    private Context context;
    private Dialog dialog;
    private OnPositiveClickListener callClickListener;
    public interface OnPositiveClickListener {
        /**
         *  回调返回数据
         * @param v v
         */
        void onClick(View v);
    }
    public EnsureDialog setOnPositiveClickListener(OnPositiveClickListener listener){
        this.callClickListener=listener;
        return this;
    }

    public EnsureDialog(Context context) {
        this.context = context;
    }

    public EnsureDialog builder() {
        // 获取Dialog布局
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_ensure_customerno,null);
        tvTitle=(TextView)view.findViewById(R.id.id_tv_title);
        tvCustomerNo =(TextView)view.findViewById(R.id.id_customerText);
        tvAddress =(TextView)view.findViewById(R.id.item_left_txt);
        btnEnsure =(Button)view.findViewById(R.id.id_btn_ensure);
        btnCancel =(Button)view.findViewById(R.id.id_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnEnsure.setOnClickListener(new View.OnClickListener() {
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
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            DisplayMetrics displayMetrics=new DisplayMetrics();
            if (windowManager.getDefaultDisplay()!=null){
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            }
            if (dialogWindow!=null){
                dialogWindow.setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.x = 0;
                lp.y = 0;
                dialogWindow.setAttributes(lp);
            }
        }
        return this;
    }

    public EnsureDialog setTitleText(String name){
        if (!TextUtils.isEmpty(name)){
            tvTitle.setVisibility(View.VISIBLE);
        }else {
            tvTitle.setVisibility(View.GONE);
        }
        tvTitle.setText(name);
        return this;
    }
    public EnsureDialog setContentOneText(String string){
        tvCustomerNo.setText(string);
        return this;
    }
    public EnsureDialog setContentTwoText(String string){
        tvAddress.setText(string);
        return this;
    }
    public EnsureDialog setRightButtonText(String string){
        btnEnsure.setText(string);
        return this;
    }
    public EnsureDialog setLeftButtonText(String string){
        btnCancel.setText(string);
        return this;
    }
    public  EnsureDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public EnsureDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
