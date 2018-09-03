package com.msht.minshengbao.ViewUI.widget;

/**
 * 说明
 * <p>
 * android:layout_column="1" 第几行
 * android:layout_row="0"    第几列
 * android:checked="true"    是否选中
 *
 * 图片资源里面设置的是state_selected属性不是state_checked
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;

import com.msht.minshengbao.R;

import java.lang.reflect.Field;

/**
 * @author hp
 * @ClassName MRadioButton
 * @Description:
 * @date 2018/1/5 0005
 */
public class MRadioButton extends RelativeLayout implements Checkable {
    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private boolean isChecked;

    private static final int GRAGVITY_TOP = 0 ;
    //	private static final int GRAGVITY_BOTTOM = 1 ;
    //	private static final int GRAGVITY_LEFT = 2 ;
    //	private static final int GRAGVITY_RIGHT = 3 ;
    private static final int GRAGVITY_CENTER = 4 ;

    private static final int ID_TEXT1 = 10086;
    private static final int ID_TEXT2 = 10000;
    private static final int ID_TEXT3 = 10001;
    private static final int ID_MAIN  = 10002;
    private static final int ID_LAYOUT= 10003;
    private RelativeLayout   mMainLayout;
    private RelativeLayout   mLayout1;
    private RelativeLayout   mLayout2;
    private CheckedTextView mTextView1;
    private CheckedTextView  mTextView2;
    private CheckedTextView  mTextView3;
    private ColorStateList textColor1;
    private ColorStateList   textColor2;
    private int textSize1 =  16;
    private int textSize2 =  14;
    private String textStr1 ="";
    private String textStr2 ="";
    private String textStr3 ="";

    private boolean mChecked;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    public MRadioButton(Context context) {
        this(context, null);
    }

    public MRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        defStyleAttr = getInternalR("attr", "radioButtonStyle");
        final TypedArray a = context.obtainStyledAttributes(
                attrs, getInternalRS("styleable", "CompoundButton"), defStyleAttr, defStyleRes);
        if (a.hasValue(getInternalR("styleable", "CompoundButton_checked"))) {
            final boolean checked = a.getBoolean(getInternalR("styleable", "CompoundButton_checked"), false);
            setChecked(checked);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w("MINE", "MotionEvent:" + event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performClick();
                break;
                default:
                    break;
        }
        return super.onTouchEvent(event);
    }

    private void initDrawable(Context context, AttributeSet attrs) {

        int gravity = 0;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.myCompoundButton);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.myCompoundButton_TextGravity:
                    gravity = a.getInt(attr, GRAGVITY_CENTER);
                    break;
                case R.styleable.myCompoundButton_checked:
                    setChecked(a.getBoolean(attr, false));
                    break;
                case R.styleable.myCompoundButton_text1:
                    textStr1 = a.getString(attr);
                    break;
                case R.styleable.myCompoundButton_textColor1:
                    textColor1 = a.getColorStateList(attr);
                    break;
                case R.styleable.myCompoundButton_textSize1:
                    textSize1 = a.getDimensionPixelOffset(attr, 16);
                    break;
                case R.styleable.myCompoundButton_text2:
                    textStr2 = a.getString(attr);
                    break;
                case R.styleable.myCompoundButton_textColor2:
                    textColor2 = a.getColorStateList(attr);
                    break;
                case R.styleable.myCompoundButton_textSize2:
                    textSize2 = a.getDimensionPixelOffset(attr, 14);
                    break;
                case R.styleable.myCompoundButton_text3:
                    textStr3 = a.getString(attr);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        doWithGravity(gravity);
    }

    private void doWithGravity(int gravity) {
        RelativeLayout.LayoutParams textParam1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams textParam2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams textParam3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textParam1.addRule(RelativeLayout.ALIGN_BOTTOM);
        mTextView1.setGravity(GRAGVITY_CENTER);
        mTextView1.setLayoutParams(textParam1);
        mTextView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize1);
        mTextView1.setTextColor(textColor1 != null ? textColor1 : ColorStateList.valueOf(0xFF000000));
        mTextView1.setText(textStr1);
        mLayout2.addView(mTextView1);
        textParam2.addRule(RelativeLayout.BELOW, ID_LAYOUT);
        textParam2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mTextView2.setGravity(GRAGVITY_CENTER);
        mTextView2.setLayoutParams(textParam2);
        mTextView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize2);
        mTextView2.setTextColor(textColor2 != null ? textColor2 : ColorStateList.valueOf(0xFF000000));
        mTextView2.setText(textStr2);
        mLayout1.addView(mTextView2);

        textParam3.addRule(RelativeLayout.ALIGN_BASELINE,ID_TEXT1);
        textParam3.addRule(RelativeLayout.RIGHT_OF, ID_TEXT1);
        mTextView3.setGravity(GRAGVITY_CENTER);
        mTextView3.setLayoutParams(textParam3);
        mTextView3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize2);
        mTextView3.setTextColor(textColor1 != null ? textColor1 : ColorStateList.valueOf(0xFF000000));
        mTextView3.setText(textStr3);
        mLayout2.addView(mTextView3);
        invalidate();
    }

    private void initView(Context context, AttributeSet attrs) {

        mTextView1 = new CheckedTextView(context);
        mTextView1.setId(ID_TEXT1);

        mTextView2 = new CheckedTextView(context);
        mTextView2.setId(ID_TEXT2);

        mTextView3 =new CheckedTextView(context);
        mTextView3.setId(ID_TEXT3);
        mMainLayout =new MyRelativeLayout(context);
        mLayout1 = new RelativeLayout(context);
        mLayout2 =new RelativeLayout(context);
        mLayout2.setId(ID_LAYOUT);
        addView(mMainLayout);
        LayoutParams mainparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mainparams.addRule(MyRelativeLayout.CENTER_IN_PARENT);
        mMainLayout.setLayoutParams(mainparams);
        LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLayout1.setLayoutParams(params1);
        LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mLayout2.setLayoutParams(params2);
        mLayout1.addView(mLayout2);
        mMainLayout.addView(mLayout1);
    }
    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     * @hide
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    @Override
    public void setChecked(boolean checked) {
        Log.e("MINE", "setChecked:" + checked);
        if (mChecked != checked) {
            mChecked = checked;
            setSelected(checked);
            refreshDrawableState();
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setSelected(mChecked);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setSelected(selected);
        }
    }

    public int getInternalR(String v1, String v2) {
        int titleStyle = 0;
        try {
            Class clasz = Class.forName("com.android.internal.R$" + v1);//styleable
            Field field = clasz.getDeclaredField(v2);
            field.setAccessible(true);
            titleStyle = (Integer) field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Log.w("MINE", "getInternalR:" + titleStyle);
        return titleStyle;
    }

    public int[] getInternalRS(String v1, String v2) {
        int[] textAppearanceStyleArr = new int[0];
        try {
            Class clasz = Class.forName("com.android.internal.R$" + v1);//styleable
            Field field = clasz.getDeclaredField(v2);
            field.setAccessible(true);
            textAppearanceStyleArr = (int[]) field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Log.w("MINE", "getInternalRS:" + textAppearanceStyleArr);
        return textAppearanceStyleArr;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(View buttonView, boolean isChecked);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public boolean performClick() {
        toggle();

        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }

        return handled;
    }

    @Override
    public void toggle() {
        if (!isChecked()) {
            setChecked(!mChecked);
        }
    }
}

