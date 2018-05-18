package com.msht.minshengbao.ViewUI.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;


import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class ActionSheetDialog {
	private String mSendTime="尽快送达";
	private String mSendDate="";
	private Context context;
	private Dialog dialog;
	private TextView    text_ok;
	private TextView    txt_cancel;
	private RadioGroup  radioGroup1;
	private RadioGroup  radioGroup2;
	private RadioButton radioButton1;
	private RadioButton radioButton2;
	private RadioButton radioButton3;
	private RadioButton radioButton;
	private Display display;
	public OnSheetButtonOneClickListener itemClickOneListener;
	public OnSheetButtonTwoClickListener itemClickTwoListener;
	public interface OnSheetButtonOneClickListener {
		void onClick(String string);
	}
	public interface OnSheetButtonTwoClickListener {
		void onClick(String string);
	}
    public ActionSheetDialog setOnSheetButtonOneClickListener(OnSheetButtonOneClickListener listener){
		this.itemClickOneListener=listener;
		return this;
	}
	public ActionSheetDialog setOnSheetButtonTwoClickListener(OnSheetButtonTwoClickListener listener){
		this.itemClickTwoListener=listener;
		return this;
	}
	public ActionSheetDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public ActionSheetDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_actionsheet, null);

		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(display.getWidth());
		text_ok=(TextView)view.findViewById(R.id.id_text_ok);
		txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
		radioGroup1=(RadioGroup)view.findViewById(R.id.id_radiogroup1);
		radioGroup2=(RadioGroup)view.findViewById(R.id.id_radiogroup2);
		radioButton1=(RadioButton)view.findViewById(R.id.id_radio_date1);
		radioButton2=(RadioButton)view.findViewById(R.id.id_radio_date2);
		radioButton3=(RadioButton)view.findViewById(R.id.id_radio_date3);
		txt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		SimpleDateFormat formats=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format1=new SimpleDateFormat("E");
		Calendar today=Calendar.getInstance();
		Calendar tomorrow=Calendar.getInstance();
		Calendar aftertomorrow=Calendar.getInstance();
		tomorrow.add(Calendar.DAY_OF_MONTH,1);
		aftertomorrow.add(Calendar.DAY_OF_MONTH,2);
		final String mTodayDate=formats.format(today.getTime());
		String weektoday=format1.format(today.getTime());
		final String mtomorrowDate=formats.format(tomorrow.getTime());
		String weektomorrow=format1.format(tomorrow.getTime());
		final String maftertomorrowDate=formats.format(aftertomorrow.getTime());
		String weekaftertomorrow=format1.format(aftertomorrow.getTime());
		String radioText1="("+mTodayDate+" "+weektoday+")";
		String radioText2="("+mtomorrowDate+" "+weektomorrow+")";
		String radioText3="("+maftertomorrowDate+" "+weekaftertomorrow+")";
		mSendDate=mTodayDate;
		radioButton1.setText("今天"+radioText1);
		radioButton2.setText("明天"+radioText2);
		radioButton3.setText("后天"+radioText3);
		text_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (itemClickOneListener!=null){
					itemClickOneListener.onClick(mSendDate+" "+mSendTime);
				}
				dialog.dismiss();
			}
		});
		// 定义Dialog布局和参数
		radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
				switch (checkedId){
					case R.id.id_radio_date1:
						mSendDate=mTodayDate;
						break;
					case R.id.id_radio_date2:
						mSendDate=mtomorrowDate;
						break;
					case R.id.id_radio_date3:
						mSendDate=maftertomorrowDate;
						break;
				}
			}
		});
		radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
				radioButton=(RadioButton)group.findViewById(checkedId);
				mSendTime=radioButton.getText().toString().trim();
				switch (checkedId){
					case R.id.id_radio_time1:
						mSendTime="尽快送达";
						break;
					case R.id.id_radio_time2:
						mSendTime="8:00-9:00";
						break;
					case R.id.id_radio_time3:
						mSendTime="9:00-10:00";
						break;
					case R.id.id_radio_time4:
						mSendTime="10:00-11:00";
						break;
					case R.id.id_radio_time5:
						mSendTime="11:00-12:00";
						break;
				}
			}
		});
		dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);

		return this;
	}
	public ActionSheetDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}
	public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}
	public void show() {
		dialog.show();
	}
}
