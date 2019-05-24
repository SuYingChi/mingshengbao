package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.msht.minshengbao.R;

/**
 *
 * @author hong
 * @date 2016/11/17
 */
public class EnsureAddress extends Dialog {
    private TextView tv_title,tv_customer,tv_address;
    private Button btn_ensure,btn_cancel;
    private Context context;
    public EnsureAddress(Context context) {
        super(context, R.style.PromptDialogStyle);
        this.context=context;
        setAddressDialog();
    }
    public EnsureAddress(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected EnsureAddress(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void setAddressDialog() {
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_ensure_customerno,null);
        tv_title=(TextView)view.findViewById(R.id.id_tv_title);
        tv_customer=(TextView)view.findViewById(R.id.id_customerText);
        tv_address=(TextView)view.findViewById(R.id.item_left_txt);
        btn_ensure=(Button)view.findViewById(R.id.id_btn_ensure);
        btn_cancel=(Button)view.findViewById(R.id.id_cancel);
        super.setContentView(view);
    }
    public View getCustomer(){
        return tv_customer;
    }
    public View getAddress(){
        return tv_customer;
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
    public void setTitleText(String title){tv_title.setText(title);}
    public void setCustomerText(String customerNo){
        tv_customer.setText(customerNo);
    }
    public void setAddressText(String addressText){
        tv_address.setText(addressText);
    }
    public void setOnpositiveListener(View.OnClickListener listener){
        btn_ensure.setOnClickListener(listener);
    }
    public void setOnNegativeListener(View.OnClickListener listener){
        btn_cancel.setOnClickListener(listener);
    }
}
