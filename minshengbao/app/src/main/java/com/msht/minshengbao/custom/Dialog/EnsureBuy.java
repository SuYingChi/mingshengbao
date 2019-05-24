package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;

/**
 * Created by hong on 2016/12/14.
 */
public class EnsureBuy extends Dialog {
    private TextView tv_customer, tv_name, tv_idcard;
    private TextView tv_type, tv_insurance;
    private TextView tv_effective, tv_cutoff;
    private RelativeLayout Rcancel, Rbuy;
    private Context context;

    public EnsureBuy(Context context) {
        super(context, R.style.PromptDialogStyle);
        this.context = context;
        setInsurance();

    }

    public EnsureBuy(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected EnsureBuy(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void setInsurance() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_buy_insurance, null);
        tv_customer = (TextView) view.findViewById(R.id.id_customer);
        tv_name = (TextView) view.findViewById(R.id.id_name);
        tv_idcard = (TextView) view.findViewById(R.id.id_idcardNo);
        tv_type = (TextView) view.findViewById(R.id.id_type);
        tv_insurance = (TextView) view.findViewById(R.id.id_amount);
        tv_effective = (TextView) view.findViewById(R.id.id_effetive);
        tv_cutoff = (TextView) view.findViewById(R.id.id_cut_off);
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
    public void setCustomerText(String customerNo) {
        tv_customer.setText(customerNo);
    }
    public void setNameText(String name) {
        tv_name.setText(name);
    }
    public void setIdcardText(String idcard) {
        tv_idcard.setText(idcard);
    }
    public void setTypeText(String type) {
        tv_type.setText(type);
    }
    public void setAmount(String amount) {
        tv_insurance.setText(amount);
    }
    public void setEffictive(String time) {
        tv_effective.setText(time);
    }
    public void setCutoffDate(String time) {
        tv_cutoff.setText(time);
    }
    public void setOnpositiveListener(View.OnClickListener listener){
        Rbuy.setOnClickListener(listener);
    }
    public void setOnNegativeListener(View.OnClickListener listener){
        Rcancel.setOnClickListener(listener);
    }
}
