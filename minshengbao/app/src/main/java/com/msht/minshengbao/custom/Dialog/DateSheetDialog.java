package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.GsonImpl;
import com.msht.minshengbao.Utils.TimePickerUiUtil;
import com.msht.minshengbao.custom.widget.MyLinearLayoutManager;
import com.msht.minshengbao.custom.widget.MyNumberPicker;
import com.msht.minshengbao.adapter.SelectDateAdapter;

import java.util.Calendar;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class DateSheetDialog {

    private int    mDay=1;
    private String mSendDay="";
    private String mSendMonth="";
    private Context context;
    private Dialog dialog;
    private Display display;
    private OnSheetButtonOneClickListener itemClickOneListener;
    private OnSheetButtonTwoClickListener itemClickTwoListener;
    public interface OnSheetButtonOneClickListener {
        /**
         * 回传
         * @param string
         */
        void onClick(String string);
    }
    public interface OnSheetButtonTwoClickListener {
        /**
         *
         * @param string
         */
        void onClick(String string);
    }
    public DateSheetDialog setOnSheetButtonOneClickListener(OnSheetButtonOneClickListener listener){
        this.itemClickOneListener=listener;
        return this;
    }
    public DateSheetDialog setOnSheetButtonTwoClickListener(OnSheetButtonTwoClickListener listener){
        this.itemClickTwoListener=listener;
        return this;
    }
    public DateSheetDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            display = windowManager.getDefaultDisplay();
        }
    }

    public DateSheetDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_date_action_sheet, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        TextView textOk=(TextView)view.findViewById(R.id.id_text_ok);
        TextView textCancel = (TextView) view.findViewById(R.id.txt_cancel);
        RadioGroup radioGroup2=(RadioGroup)view.findViewById(R.id.id_radiogroup2);
        final MyNumberPicker numberPicker=(MyNumberPicker)view.findViewById(R.id.id_number_picker);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        RecyclerView mRecyclerView=(RecyclerView)view.findViewById(R.id.id_date_view);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(context));
        final SelectDateAdapter mAdapter=new SelectDateAdapter(GsonImpl.getDateList(3));
        mAdapter.setSelection(GsonImpl.getDateList(3).size()-1);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setUpdateListener(new SelectDateAdapter.UpdateListener() {
            @Override
            public void onUpdateClick(int position, String list) {
                mSendMonth=list;
                mAdapter.setSelection(position);
                mAdapter.notifyDataSetChanged();
                if (position==2){
                    numberPicker.setMaxValue(mDay);
                }else {
                    numberPicker.setMaxValue(DateUtils.getMonthLastDay(DateUtils.getYearDate(list,"yyyy年MM月"),DateUtils.getMonthDate(list,"yyyy年MM月")+1));
                }
            }
        });
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Calendar today=Calendar.getInstance();
        mDay=today.get(Calendar.DAY_OF_MONTH);
        mSendMonth=DateUtils.getCurrentDateString("yyyy年MM月");
        mSendDay=DateUtils.getCurrentDateString("dd日");
        numberPicker.setDisplayedValues(ConstantUtil.getDateNumberData());
        TimePickerUiUtil.setNumberPickerDividerColor(numberPicker,ContextCompat.getColor(context,R.color.blue_center));
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(mDay);
        numberPicker.setValue(mDay);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                mSendDay=ConstantUtil.getDateNumberData()[newValue-1];
            }
        });
        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickOneListener!=null){
                    itemClickOneListener.onClick(mSendMonth+mSendDay);
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
    public DateSheetDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public DateSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
