package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;

/**
 * Created by hong on 2018/4/2.
 */

public class WaterDialog extends Dialog {
    private TextView tv_title;
    private TextView tv_contents1;
    private TextView tv_contents2;
    private ImageView img_diaog;
    private Context   mContext;
    private Button    btn_left;
    private Button    btn_right;
    private Button    btn_ensure;
    private View      btn_layout;

    public WaterDialog(@NonNull Context context) {
        super(context, R.style.PromptDialogStyle);
        this.mContext = context;
        setWaterDialog();
    }
    public WaterDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }
    protected WaterDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    private void setWaterDialog() {
        View view= LayoutInflater.from(mContext).inflate(R.layout.dialog_water_delivery,null);
        tv_title=(TextView)view.findViewById(R.id.id_tv_title) ;
        tv_contents1=(TextView)view.findViewById(R.id.id_tv_contents1);
        tv_contents2=(TextView)view.findViewById(R.id.id_tv_content2);
        btn_left=(Button)view.findViewById(R.id.id_left_button);
        btn_right=(Button)view.findViewById(R.id.id_right_button);
        btn_ensure=(Button)view.findViewById(R.id.id_btn_ensure);
        btn_layout=view.findViewById(R.id.id_btn_layout);
        img_diaog=(ImageView)view.findViewById(R.id.id_dialog_img);
        super.setContentView(view);
    }
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    public void setTitleText(String title){
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(title);
    }
    public void setContentText1(String contentText1){
        tv_contents1.setVisibility(View.VISIBLE);
        tv_contents1.setText(contentText1);
    }
    public void setContentText2(String contentText2){
        tv_contents2.setVisibility(View.VISIBLE);
        tv_contents2.setText(contentText2);
    }
    public void setImageIcon(int img){
        img_diaog.setImageResource(img);
    }
    public View getBtn_layout(){
        return btn_layout;
    }
    public View getBtnEnsure(){
        return btn_ensure;
    }
    public View getContents2(){
       return tv_contents2;
    }
    public void setOnpositiveListener(String text,View.OnClickListener listener){
        btn_right.setText(text);
        btn_right.setOnClickListener(listener);
    }
    public void setOnNegativeListener(String text,View.OnClickListener listener){
        btn_left.setText(text);
        btn_left.setOnClickListener(listener);
    }
    public void setOnSingleNegativeListener(View.OnClickListener listener){
        btn_ensure.setOnClickListener(listener);
    }
}
