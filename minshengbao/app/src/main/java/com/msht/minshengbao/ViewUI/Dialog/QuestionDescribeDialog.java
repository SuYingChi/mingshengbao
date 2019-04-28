package com.msht.minshengbao.ViewUI.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.ButtonUI.ButtonM;
import com.msht.minshengbao.ViewUI.widget.MultiLineChooseLayout;

import java.util.ArrayList;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/4/2  
 */
public class QuestionDescribeDialog {

    private MultiLineChooseLayout multiLineChooseLayout;
    private EditText  etDeScribe;
    private String    textString="";
    private Context context;
    private Dialog dialog;
    private Display display;
    private ArrayList<String> mDataList=new ArrayList<>();
    private OnPositiveClickListener callClickListener;
    private ArrayList<String> multiResult=new ArrayList<>();
    public interface OnPositiveClickListener {
        /**
         *  回调返回数据
         * @param v v
         * @param text text
         */
        void onClick(View v,String text);
    }
    public QuestionDescribeDialog setOnPositiveClickListener(OnPositiveClickListener listener){
        this.callClickListener=listener;
        return this;
    }

    public QuestionDescribeDialog(Context context,ArrayList<String> dataList) {
        this.context = context;
        this.mDataList=dataList;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }

    public QuestionDescribeDialog builder() {
        // 获取Dialog布局
        View view= LayoutInflater.from(context).inflate(R.layout.layout_repair_question_describe,null);
        view.setMinimumWidth(display.getWidth());
        etDeScribe=(EditText)view.findViewById(R.id.id_et_info);
        multiLineChooseLayout=(MultiLineChooseLayout)view.findViewById(R.id.id_multiChoose);
        multiLineChooseLayout.setList(mDataList);
        ButtonM btnEnsure=(ButtonM)view.findViewById(R.id.id_btn_send) ;
        ButtonM btnCancel=(ButtonM) view.findViewById(R.id.id_cancel);
        btnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=textString+etDeScribe.getText().toString().trim();
                if (callClickListener!=null){
                    callClickListener.onClick(v,text);
                }
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        multiLineChooseLayout.setOnItemClickListener(new MultiLineChooseLayout.onItemClickListener() {
            @Override
            public void onItemClick(int position, String text) {
                StringBuilder sb=new StringBuilder();
                multiResult=multiLineChooseLayout.getAllItemSelectedTextWithListArray();
                if (multiResult!=null&&multiResult.size()>0){
                    for (int i=0;i<multiResult.size();i++){
                        sb.append(multiResult.get(i));
                        sb.append(",");
                    }
                    textString=sb.toString();
                }else {
                    textString="";
                }
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

    public  QuestionDescribeDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public QuestionDescribeDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
