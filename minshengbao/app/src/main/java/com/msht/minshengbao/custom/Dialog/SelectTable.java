package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;

/**
 *
 * @author hong
 * @date 2017/1/9
 */
public class SelectTable  extends Dialog {
    private TextView tv_title;
    private ListView mListView;
    private ImageView leftimg;
    private RelativeLayout Rcancel;
    private Context context;
    public SelectTable(Context context) {
        super(context, R.style.PromptDialogStyle);
        this.context=context;
        setSelectDialog();
    }

    private void setSelectDialog() {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_table_listview,null);
        tv_title=(TextView)view.findViewById(R.id.id_title);
        mListView=(ListView)view.findViewById(R.id.id_table_listview);
        Rcancel=(RelativeLayout)view.findViewById(R.id.id_cancel);
        super.setContentView(view);
    }

    public SelectTable(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SelectTable(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public View getListview(){
        return mListView;
    }
    public View getTitle(){
        return tv_title;
    }
    public void setLeftimg(Bitmap IMG){
        leftimg.setImageBitmap(IMG);
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

    public void setOnItemClickListeners(AdapterView.OnItemClickListener adapter){
        mListView.setOnItemClickListener(adapter);
    }
    public void setOnNegativeListener(View.OnClickListener listener){
        Rcancel.setOnClickListener(listener);
    }
}
