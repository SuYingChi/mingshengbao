package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.msht.minshengbao.R;

/**
 * Created by hong on 2018/1/23.
 */

public class SelfPayDialog extends Dialog {
    private ImageView closeimg;
    private RelativeLayout Rbinding;
    private Context context;

    public SelfPayDialog(@NonNull Context context) {
        super(context, R.style.PromptDialogStyle);
        this.context = context;
        setInit();
    }
    private void setInit() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_selfpay_notice, null);
        Rbinding = (RelativeLayout) view.findViewById(R.id.id_layout_binding);
        closeimg =(ImageView)view.findViewById(R.id.id_close_roundel) ;
        super.setContentView(view);
    }
    public SelfPayDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);}

    protected SelfPayDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);}
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }
    public void setOnpositiveListener(View.OnClickListener listener){
        Rbinding.setOnClickListener(listener);
    }
    public void setOnNegativeListener(View.OnClickListener listener){
        closeimg.setOnClickListener(listener);
    }
}
