package com.msht.minshengbao.ViewUI.Dialog;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.CircleImageView;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/9/10  
 */
public class InvoiceEnsureDialog {
    private TextView tvName;
    private TextView tvInvoiceType;
    private TextView tvEmail;
    private TextView tvTaxpayer;
    private TextView tvTaxpayerText;
    private TextView tvTitle;
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
    public InvoiceEnsureDialog setOnPositiveClickListener(OnPositiveClickListener listener){
        this.callClickListener=listener;
        return this;
    }
    public InvoiceEnsureDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }

    public InvoiceEnsureDialog builder() {
        // 获取Dialog布局
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_invoice_ensure,null);
        view.setMinimumWidth(display.getWidth());
        tvTitle=(TextView)view.findViewById(R.id.id_invoice_title);
        tvName=(TextView)view.findViewById(R.id.id_invoice_name);
        tvInvoiceType=(TextView)view.findViewById(R.id.id_invoice_type);
        tvEmail=(TextView)view.findViewById(R.id.id_tv_email);
        tvTaxpayer=(TextView)view.findViewById(R.id.id_taxpayer_num);
        tvTaxpayerText=(TextView)view.findViewById(R.id.id_taxpayer_text);
        Button btnEnsure=(Button)view.findViewById(R.id.id_btn_ensure) ;
        ImageView closeImage=(ImageView)view.findViewById(R.id.id_close_cancel);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        if (dialogWindow!=null){
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);
        }
        return this;
    }
    public InvoiceEnsureDialog setInvoiceName(String name){
        tvName.setText(name);
        return this;
    }
    public InvoiceEnsureDialog setInvoiceType(String type){
        tvInvoiceType.setText(type);
        tvTitle.setText(type);
        return this;
    }
    public InvoiceEnsureDialog setEmailText(String text){
        tvEmail.setText(text);
        return this;
    }
    public InvoiceEnsureDialog setTaxpayerNum(String num){
        if (!TextUtils.isEmpty(num)){
            tvTaxpayerText.setVisibility(View.VISIBLE);
            tvTaxpayer.setVisibility(View.VISIBLE);
            tvTaxpayer.setText(num);
        }else {
            tvTaxpayer.setVisibility(View.INVISIBLE);
            tvTaxpayerText.setVisibility(View.INVISIBLE);
        }
        return this;
    }
    public  InvoiceEnsureDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public InvoiceEnsureDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
