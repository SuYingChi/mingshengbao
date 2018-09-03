package com.msht.minshengbao.ViewUI.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;


import com.msht.minshengbao.R;

/**
 *
 * @author hong
 * @date 2018/3/7
 */

public class MyRadioButton extends RelativeLayout implements Checkable {

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
    private CheckedTextView  mTextView1;
    private CheckedTextView  mTextView2;
    private CheckedTextView  mTextView3;
    private ColorStateList   textColor1;
    private ColorStateList   textColor2;
    private int textSize1 =  16;
    private int textSize2 =  14;
    private String textStr1 ="";
    private String textStr2 ="";
    private String textStr3 ="";
    private IOnCheckChangedListener parent;

    public MyRadioButton(Context context) {
        super(context);
    }
    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
        initDrawable(context, attrs);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }
    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initView(Context context, AttributeSet attrs) {
        //textview
        mTextView1 = new CheckedTextView(context);
        mTextView1.setId(ID_TEXT1);
        //imageview
        mTextView2 = new CheckedTextView(context);
        mTextView2.setId(ID_TEXT2);

        mTextView3 =new CheckedTextView(context);
        mTextView3.setId(ID_TEXT3);
        mMainLayout =new RelativeLayout(context);
        mLayout1 = new RelativeLayout(context);
        mLayout2 =new RelativeLayout(context);
        mLayout2.setId(ID_LAYOUT);
        addView(mMainLayout);
        LayoutParams mainparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mainparams.addRule(RelativeLayout.CENTER_IN_PARENT);
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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
        switch (gravity) {
            case GRAGVITY_TOP:
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
                break;
            default:
                break;
        }
    }
    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
        if (mTextView1 != null) {
            mTextView1.setChecked(isChecked);
        }
        if (mTextView2!=null){
            mTextView2.setChecked(isChecked);
        }if (mTextView3!=null){
            mTextView3.setChecked(isChecked);
        }
        if (parent != null && isChecked) {
            parent.onCheckChanged(this);
        } else if(parent == null) {
           // LogUtil.log("parent is null");
        }
        /*if (isChecked != checked) {
           // refreshDrawableState();
        }*/
        refreshDrawableState();
    }
    @Override
    public boolean isChecked() {
        return isChecked;
    }
    @Override
    public void toggle() {
        if (!isChecked) {
            setChecked(!isChecked);
        }
       // setChecked(!isChecked);
       // setChecked(!isChecked);
       // isChecked = !isChecked;
        refreshDrawableState();
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
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setChecked(isChecked);
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setCheckable(true);
        info.setChecked(isChecked);
    }
    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.checked = isChecked();
        return ss;
    }
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }

    public void setOnCheckChangedListener(IOnCheckChangedListener parent) {
        this.parent = parent;
    }
    public interface IOnCheckChangedListener {
        public void onCheckChanged(MyRadioButton button);
    }

    static class SavedState extends BaseSavedState {
        boolean checked;
        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean)in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return "MyRadioButton.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
