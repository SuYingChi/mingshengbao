package com.msht.minshengbao.ViewUI.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;

/**
 * Created by hong on 2017/5/19.
 */

public class EnsurePublish extends Dialog {
    private TextView title;
    private TextView tv_maintype;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_address;
    private RelativeLayout Rcancel, Rbuy;
    private Context context;
    public EnsurePublish(Context context) {
        super(context, R.style.PromptDialogStyle);
        this.context = context;
        setPublish();
    }

    private void setPublish() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_publish_order, null);
        title = (TextView) view.findViewById(R.id.id_title);
        tv_name = (TextView) view.findViewById(R.id.id_name);
        tv_maintype= (TextView) view.findViewById(R.id.id_maintype);
        tv_phone= (TextView) view.findViewById(R.id.id_phoneNo);
        tv_address = (TextView) view.findViewById(R.id.id_address);
        Rcancel = (RelativeLayout) view.findViewById(R.id.id_cancel);
        Rbuy = (RelativeLayout) view.findViewById(R.id.id_ensure);
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

    public void setTitleText(String titleText) {
        title.setText(titleText);
    }
    public void setNameText(String name) {
        tv_name.setText(name);
    }
    public void setPhoneText(String phoneText) {
        tv_phone.setText(phoneText);
    }
    public void setTypeText(String type) {
        tv_maintype.setText(type);
    }
    public void setAddressText(String addressText) {
        tv_address.setText(addressText);
    }
    public void setOnpositiveListener(View.OnClickListener listener){
        Rbuy.setOnClickListener(listener);
    }
    public void setOnNegativeListener(View.OnClickListener listener){
        Rcancel.setOnClickListener(listener);
    }
}
