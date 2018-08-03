package com.msht.minshengbao.ViewUI.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.msht.minshengbao.adapter.ActionSheetAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.ButtonUI.ButtonM;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;

/**
 * Demo class
 * 自定义底部弹框
 * 选择性别，是否电梯
 * @author hong
 * @date 2018/7/10 
 */
public class MySheetDialog {
    private Context context;
    private Dialog dialog;
    private Display display;
    private String[] mList;
    private String   mTitle;
    private OnSheetItemClickListener itemClickListener;
    public interface OnSheetItemClickListener {
        /**
         *  回调返回数据
         * @param item 下标
         * @param string 性别
         */
        void onClick(String item,String string);
    }
    public MySheetDialog setOnSheetItemClickListener(OnSheetItemClickListener listener){
        this.itemClickListener=listener;
        return this;
    }
    public MySheetDialog(Context context,String title,String[] list) {
        this.context = context;
        this.mList=list;
        this.mTitle=title;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }

    public MySheetDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_my_actionsheet, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        TextView tvTitle=(TextView)view.findViewById(R.id.id_tv_title);
        ButtonM btnCancel = (ButtonM ) view.findViewById(R.id.cancelBtn);
        ListViewForScrollView mListView=(ListViewForScrollView)view.findViewById(R.id.id_list_data);
        tvTitle.setText(mTitle);
        ActionSheetAdapter mAdapter =new ActionSheetAdapter(context,mList);
        mListView.setAdapter(mAdapter);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sex=mList[position];
                String pos=String.valueOf(position);
                if (itemClickListener!=null){
                    itemClickListener.onClick(pos,sex);
                }
                dialog.dismiss();
            }
        });
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow!=null){
            dialogWindow.setGravity(Gravity.START|Gravity.BOTTOM);
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);
        }
        return this;
    }
    public MySheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public MySheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }

}
