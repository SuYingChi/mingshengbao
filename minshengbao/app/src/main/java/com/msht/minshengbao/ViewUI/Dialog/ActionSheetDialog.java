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
import java.util.Locale;


/**
 * @author hong
 */
public class ActionSheetDialog {
	private int    mHour;
	private String mSendTime="尽快送达";
	private String mSendDate="";
	private Context context;
	private Dialog dialog;
	private RadioButton radioButton2;
	private RadioButton radioButton3;
	private RadioButton radioButton4;
	private RadioButton radioButton5;
	private RadioButton radioButton6;
	private RadioButton radioButton7;
	private RadioButton radioButton;
	private Display display;
	private OnSheetButtonOneClickListener itemClickOneListener;
	private OnSheetButtonTwoClickListener itemClickTwoListener;
	public interface OnSheetButtonOneClickListener {
		/**
		 *
		 * @param string
		 */
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
		if (windowManager!=null){
			display = windowManager.getDefaultDisplay();
		}
	}

	public ActionSheetDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_actionsheet, null);

		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(display.getWidth());
		TextView textOk=(TextView)view.findViewById(R.id.id_text_ok);
		TextView textCancel = (TextView) view.findViewById(R.id.txt_cancel);
		RadioGroup radioGroup1=(RadioGroup)view.findViewById(R.id.id_radiogroup1);
		RadioGroup radioGroup2=(RadioGroup)view.findViewById(R.id.id_radiogroup2);
		RadioButton radioButtonDate1=(RadioButton)view.findViewById(R.id.id_radio_date1);
		RadioButton radioButtonDate2=(RadioButton)view.findViewById(R.id.id_radio_date2);
		RadioButton radioButtonDate3=(RadioButton)view.findViewById(R.id.id_radio_date3);
		radioButton2=(RadioButton)view.findViewById(R.id.id_radio_time2);
		radioButton3=(RadioButton)view.findViewById(R.id.id_radio_time3);
		radioButton4=(RadioButton)view.findViewById(R.id.id_radio_time4);
		radioButton5=(RadioButton)view.findViewById(R.id.id_radio_time5);
		radioButton6=(RadioButton)view.findViewById(R.id.id_radio_time6);
		radioButton7=(RadioButton)view.findViewById(R.id.id_radio_time7);
		textCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		SimpleDateFormat formats=new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		SimpleDateFormat format1=new SimpleDateFormat("E",Locale.CHINA);
		Calendar today=Calendar.getInstance();
		Calendar tomorrow=Calendar.getInstance();
		Calendar afterTomorrow=Calendar.getInstance();
		mHour=today.get(Calendar.HOUR_OF_DAY);
		tomorrow.add(Calendar.DAY_OF_MONTH,1);
		afterTomorrow.add(Calendar.DAY_OF_MONTH,2);
		final String mTodayDate=formats.format(today.getTime());
		String weektoday=format1.format(today.getTime());
		final String mtomorrowDate=formats.format(tomorrow.getTime());
		String weektomorrow=format1.format(tomorrow.getTime());
		final String maftertomorrowDate=formats.format(afterTomorrow.getTime());
		String weekaftertomorrow=format1.format(afterTomorrow.getTime());
		String radioText1="("+mTodayDate+" "+weektoday+")";
		String radioText2="("+mtomorrowDate+" "+weektomorrow+")";
		String radioText3="("+maftertomorrowDate+" "+weekaftertomorrow+")";
		mSendDate=mTodayDate;
		radioButtonDate1.setText("今天"+radioText1);
		radioButtonDate2.setText("明天"+radioText2);
		radioButtonDate3.setText("后天"+radioText3);
		radioButtonDate3.setVisibility(View.GONE);
		onCheckHourTime();
		textOk.setOnClickListener(new OnClickListener() {
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
						onCheckHourTime();
						break;
					case R.id.id_radio_date2:
						mSendDate=mtomorrowDate;
						onSetVisibilityView();
						break;
					case R.id.id_radio_date3:
						mSendDate=maftertomorrowDate;
						onSetVisibilityView();
						break;
						default:
							onSetVisibilityView();
							break;
				}
			}
		});
		radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
				radioButton=(RadioButton)group.findViewById(checkedId);
				mSendTime=radioButton.getText().toString().trim();
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
	/**
	 * 根据当前时间，控制时间列表显示
	 */
	private void onCheckHourTime() {
		switch (mHour){
			case 9:
				radioButton2.setVisibility(View.VISIBLE);
				radioButton3.setVisibility(View.VISIBLE);
				radioButton4.setVisibility(View.VISIBLE);
				radioButton5.setVisibility(View.VISIBLE);
				radioButton6.setVisibility(View.VISIBLE);
				radioButton7.setVisibility(View.VISIBLE);
				break;
			case 11:
				radioButton2.setVisibility(View.GONE);
				radioButton3.setVisibility(View.VISIBLE);
				radioButton4.setVisibility(View.VISIBLE);
				radioButton5.setVisibility(View.VISIBLE);
				radioButton6.setVisibility(View.VISIBLE);
				radioButton7.setVisibility(View.VISIBLE);
				break;
			case 13:
				radioButton2.setVisibility(View.GONE);
				radioButton3.setVisibility(View.GONE);
				radioButton4.setVisibility(View.VISIBLE);
				radioButton5.setVisibility(View.VISIBLE);
				radioButton6.setVisibility(View.VISIBLE);
				radioButton7.setVisibility(View.VISIBLE);
				break;
			case 15:
				radioButton2.setVisibility(View.GONE);
				radioButton3.setVisibility(View.GONE);
				radioButton4.setVisibility(View.GONE);
				radioButton5.setVisibility(View.VISIBLE);
				radioButton6.setVisibility(View.VISIBLE);
				radioButton7.setVisibility(View.VISIBLE);
				break;
			case 17:
				radioButton2.setVisibility(View.GONE);
				radioButton3.setVisibility(View.GONE);
				radioButton4.setVisibility(View.GONE);
				radioButton5.setVisibility(View.GONE);
				radioButton6.setVisibility(View.VISIBLE);
				radioButton7.setVisibility(View.VISIBLE);
				break;
			case 19:
				radioButton2.setVisibility(View.GONE);
				radioButton3.setVisibility(View.GONE);
				radioButton4.setVisibility(View.GONE);
				radioButton5.setVisibility(View.GONE);
				radioButton6.setVisibility(View.GONE);
				radioButton7.setVisibility(View.VISIBLE);

				break;
			case 21:
				radioButton2.setVisibility(View.GONE);
				radioButton3.setVisibility(View.GONE);
				radioButton4.setVisibility(View.GONE);
				radioButton5.setVisibility(View.GONE);
				radioButton6.setVisibility(View.GONE);
				radioButton7.setVisibility(View.GONE);
				break;
			default:
				if (mHour>=21){
					radioButton2.setVisibility(View.GONE);
					radioButton3.setVisibility(View.GONE);
					radioButton4.setVisibility(View.GONE);
					radioButton5.setVisibility(View.GONE);
					radioButton6.setVisibility(View.GONE);
					radioButton7.setVisibility(View.GONE);
				}else {
					radioButton2.setVisibility(View.VISIBLE);
					radioButton3.setVisibility(View.VISIBLE);
					radioButton4.setVisibility(View.VISIBLE);
					radioButton5.setVisibility(View.VISIBLE);
					radioButton6.setVisibility(View.VISIBLE);
					radioButton7.setVisibility(View.VISIBLE);
				}
				break;
		}
	}
	private void onSetVisibilityView(){
		radioButton2.setVisibility(View.VISIBLE);
		radioButton3.setVisibility(View.VISIBLE);
		radioButton4.setVisibility(View.VISIBLE);
		radioButton5.setVisibility(View.VISIBLE);
		radioButton6.setVisibility(View.VISIBLE);
		radioButton7.setVisibility(View.VISIBLE);
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
