package com.msht.minshengbao.ViewUI.widget;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CustomRadioGroup extends LinearLayout implements MyRadioButton.IOnCheckChangedListener {
	
	private List<MyRadioButton> buttons;
	private IOnCheckChangedListener mListener;

	public CustomRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		buttons = new ArrayList<MyRadioButton>();
	}
	
	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		if (child instanceof MyRadioButton) {
			MyRadioButton button = (MyRadioButton) child;
			button.setOnCheckChangedListener(this);
			buttons.add(button);
		}
		
		super.addView(child, index, params);
	}

	/**
	 * @author �����
	 * @version 1.0
	 * @Description: ��check�ı��¼����ݸ�listener���ⲿ�� 
	 * @param checkedButton
	 * @����ʱ�䣺2015-5-11 ����2:18:27
	 */
	@Override
	public void onCheckChanged(MyRadioButton checkedButton) {
		for (MyRadioButton button : buttons) {
			if (checkedButton != button) {
				button.setChecked(false);
			} else {
				if (mListener != null) {
					mListener.onCheckedChanged(this, button.getId());
				}
			}
		}
	}
	
	public void setOnCheckedChangeListener(IOnCheckChangedListener listener) {
		this.mListener = listener;
	}
	
	public interface IOnCheckChangedListener {
		public void onCheckedChanged(CustomRadioGroup group, int checkedId);
	}
}
