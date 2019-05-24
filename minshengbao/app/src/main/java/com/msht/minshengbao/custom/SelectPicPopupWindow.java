package com.msht.minshengbao.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.msht.minshengbao.R;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class SelectPicPopupWindow extends PopupWindow {

	private Button takePhotoBtn, pickPhotoBtn, cancelBtn;
	private View mMenuView;

	private Context mContext;

	@SuppressLint("InflateParams")
	public SelectPicPopupWindow(Context mContext, OnClickListener onClickListener) {
		//super(context);
		/*LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
		this.mMenuView=LayoutInflater.from(mContext).inflate(R.layout.layout_dialog_pic, null);
		//mMenuView = inflater.inflate(R.layout.layout_dialog_pic, null);
		takePhotoBtn = (Button) mMenuView.findViewById(R.id.takePhotoBtn);
		pickPhotoBtn = (Button) mMenuView.findViewById(R.id.pickPhotoBtn);
		cancelBtn = (Button) mMenuView.findViewById(R.id.cancelBtn);

		//cancelBtn.setOnClickListener(onClickListener);
		pickPhotoBtn.setOnClickListener(onClickListener);
		takePhotoBtn.setOnClickListener(onClickListener);
		cancelBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//销毁弹出框
				dismiss();
			}
		});
		

		//this.setContentView(mMenuView);
		this.setOutsideTouchable(true);
		this.setContentView(this.mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);

		this.setHeight(LayoutParams.WRAP_CONTENT);

		this.setFocusable(true);

		this.setAnimationStyle(R.style.Animation_PopupAnimation);

		ColorDrawable dw = new ColorDrawable(0x80000000);
		this.setBackgroundDrawable(dw);

		this.mMenuView.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}
