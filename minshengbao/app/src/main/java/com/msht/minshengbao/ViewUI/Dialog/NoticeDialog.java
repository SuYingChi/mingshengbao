package com.msht.minshengbao.ViewUI.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;

/**
 * Created by hong on 2017/9/18.
 */

public class NoticeDialog extends Dialog {
    private TextView text_notice;
    private Context context;

    public NoticeDialog(@NonNull Context context) {
        super(context, R.style.PromptDialogStyle);
        this.context = context;
        setInit();
    }
    public NoticeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }
    private void setInit() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_notice_notopen, null);
        text_notice = (TextView) view.findViewById(R.id.id_text_notice);
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
    public void setNoticeText(String text) {
        text_notice.setText(text);
    }
}
