package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.custom.widget.DonutProgress;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class DonutProgressDialog extends Dialog {
    private String content;
    private TextView tvContent;
    private DonutProgress donutProgress;
    public DonutProgressDialog( Context context,String content) {
        super(context,R.style.CustomDialog);
        this.content=content;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_progress_view);
        tvContent=(TextView)findViewById(R.id.tv_content);
        donutProgress=(DonutProgress)findViewById(R.id.id_progress) ;
        tvContent.setText(content);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha=0.9f;
        getWindow().setAttributes(attributes);
        setCancelable(false);
    }
    public void setDialogContent(String content){
        this.content=content;
        tvContent.setText(content);
    }

    public void setProgressMax(int max) {
        donutProgress.setMax(max);
    }
    public void setDonutProgress(float progress){
        donutProgress.setDonutProgress(progress);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(DonutProgressDialog.this.isShowing()){
                    DonutProgressDialog.this.dismiss();
                }
                break;
            default:
                break;
        }
        return true;
    }
}
