package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.msht.minshengbao.R;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 自定义透明的dialog
 * @author hong
 * @date 2017/7/2  
 */
public class CustomDialog extends Dialog{
    private String content;
    private TextView tvContent;
    public CustomDialog(Context context, String content) {
        super(context, R.style.CustomDialog);
        this.content=content;
        initView();
    }
    public void setDialogContent(String content){
        this.content=content;
        tvContent.setText(content);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(CustomDialog.this.isShowing()){
                    CustomDialog.this.dismiss();
                }
                break;
                default:
                    break;
        }
        return true;
    }
    private void initView(){
        setContentView(R.layout.layout_refresh_dialog_view);
        tvContent=(TextView)findViewById(R.id.tv_content);
        tvContent.setText(content);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha=0.9f;
        getWindow().setAttributes(attributes);
        setCanceledOnTouchOutside(true);
        setCancelable(false);
    }
}