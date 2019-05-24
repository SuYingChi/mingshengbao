package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.msht.minshengbao.R;

/**
 * Created by hong on 2017/1/16.
 */
public class InputCustomerNo extends Dialog {
    private TextView  title;
    private EditText  Ecustomer;
    private RelativeLayout  Rnext;
    private RelativeLayout  Rcancel;
    private Context context;
    public InputCustomerNo(Context context) {
        super(context, R.style.PromptDialogStyle);
        this.context=context;
        setDialog();
    }
    private void setDialog() {
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_input_customer,null);
        title=(TextView)view.findViewById(R.id.id_tv_title);
        Ecustomer=(EditText)view.findViewById(R.id.id_et_customer);
        Rnext=(RelativeLayout)view.findViewById(R.id.id_re_next);
        Rcancel=(RelativeLayout)view.findViewById(R.id.id_re_cancel);
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
    public View getTitle(){
        return title;
    }
    public View getEditCustomer(){return  Ecustomer;}
    public void setOnNextListener(View.OnClickListener listener){
        Rnext.setOnClickListener(listener);
    }
    public void setOnNegativeListener(View.OnClickListener listener){
        Rcancel.setOnClickListener(listener);
    }
}
