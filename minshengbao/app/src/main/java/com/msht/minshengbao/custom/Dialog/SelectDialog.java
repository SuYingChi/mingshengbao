package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.R;
import com.msht.minshengbao.custom.ButtonUI.ButtonM;
import com.msht.minshengbao.adapter.SelectAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/9/19  
 */
public class SelectDialog {

    private Context context;
    private Dialog dialog;
    private Display display;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private TextView tvTitle;
    private int thisPosition;
    private SelectDialog.OnSheetItemClickListener itemClickListener;
    public interface OnSheetItemClickListener {
        /**
         *  回调返回数据
         * @param item 下标
         */
        void onClick(int item);
    }
    public SelectDialog setOnSheetItemClickListener(OnSheetItemClickListener listener){
        this.itemClickListener=listener;
        return this;
    }
    public SelectDialog(Context context,ArrayList<HashMap<String, String>>  list,int pos) {
        this.context = context;
        this.mList=list;
        thisPosition=pos;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }
    public SelectDialog  builder(){
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_select_dialog, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        tvTitle=(TextView)view.findViewById(R.id.id_title);
        ButtonM btnCancel = (ButtonM ) view.findViewById(R.id.cancelBtn);
        XRecyclerView mRecyclerView=(XRecyclerView) view.findViewById(R.id.id_XRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        final SelectAdapter mAdapter =new SelectAdapter(context,mList,thisPosition);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mAdapter.setClickCallBack(new SelectAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                thisPosition=pos;
                if (itemClickListener!=null){
                    itemClickListener.onClick(pos);
                }
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        mAdapter.setItemRadioClickBack(new SelectAdapter.ItemRadioClickBack() {
            @Override
            public void onItemRadioBack(int pos) {
                thisPosition=pos;
                if (itemClickListener!=null){
                    itemClickListener.onClick(pos);
                }
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog = new Dialog(context, R.style.PromptDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow!=null){
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            dialogWindow.setAttributes(lp);
        }
        return this;
    }

    public  SelectDialog setTitleText(String titleText) {
        tvTitle.setText(titleText);
        return this;
    }
    public SelectDialog  setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public SelectDialog  setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
