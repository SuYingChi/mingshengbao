package com.msht.minshengbao.ViewUI.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.DialogUtil;

import java.util.Calendar;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/11/26  
 */
public class SelectDateDialog extends Dialog{

    private Context context;
    public static final int VIEW_STYLE_NORMAL			= 0x00000001;
    public static final int VIEW_STYLE_TITLEBAR			= 0x00000002;
    public static final int VIEW_STYLE_TITLEBAR_SKYBLUE = 0x00000003;

    public static final int BUTTON_1 = 0x00000001;
    public static final int BUTTON_2 = 0x00000002;
    public static final int BUTTON_3 = 0x00000003;

    protected SelectDateDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    protected SelectDateDialog(Context context) {
        this(context, R.style.PromptDialogStyle);
    }

    protected SelectDateDialog(Context context, boolean cancelableOnTouchOutside) {
        this(context);
        this.setCanceledOnTouchOutside(cancelableOnTouchOutside);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int marginBorder = DialogUtil.dip2px(context, 30);
        /**屏幕自适应*/
       // params.width =  DialogUtil.getScreenWidth(context) - marginBorder * 2;
        window.setAttributes(params);
    }
    @SuppressLint({ "NewApi", "InflateParams" })
    public static class Builder{

        private SelectDateDialog dialog;
        private Context context;
        private CharSequence title;
       // private CharSequence message;
        private CharSequence button1Text;
        private CharSequence button2Text;
        private CharSequence button3Text;
        private int button1TextColor;
        private int button2TextColor;
        private int button3TextColor;
        private int titleColor;
        private int messageColor;
        private float button1Size;
        private float button2Size;
        private float button3Size;
        private float titleSize;
        private float messageSize;
        private ColorStateList titleColorStateList;
        private ColorStateList messageColorStateList;
        private ColorStateList button1ColorStateList;
        private ColorStateList button2ColorStateList;
        private ColorStateList button3ColorStateList;
        private int titlebarGravity;

        private Drawable icon;
        private boolean cancelable = true;
        private boolean	canceledOnTouchOutside;
        private View view;
        private int viewStyle;

        private OnClickListener button1Listener;
        private OnClickListener button2Listener;
        private OnClickListener button3Listener;

        private int button1Flag;
        private int button2Flag;
        private int button3Flag;

        public Builder(Context context, int theme) {
            dialog = new SelectDateDialog(context, theme);
            this.context = context;
            initData();
        }

        public Builder(Context context) {
            dialog = new SelectDateDialog(context);
            this.context = context;
            initData();
        }
        private void initData(){
            this.button1TextColor = Color.parseColor("#808080");
            this.button2TextColor = Color.parseColor("#808080");
            this.button3TextColor = Color.parseColor("#808080");
            this.messageColor = Color.parseColor("#696969");
            this.titleColor = Color.WHITE;

            this.button1Size = 16;
            this.button2Size = 16;
            this.button3Size = 16;
            this.messageSize = 20;
            this.titleSize = 18;

            this.titlebarGravity = Gravity.START;
        }

        public Context getContext(){
            return context;
        }

        public SelectDateDialog.Builder setTitleBarGravity(int titlebarGravity){
            this.titlebarGravity = titlebarGravity;
            return this;
        }

        public SelectDateDialog.Builder setTitle(CharSequence title){
            this.title = title;
            return this;
        }
        public SelectDateDialog.Builder setTitle(int titleResId){
            this.title = context.getResources().getString(titleResId);
            return this;
        }
        public SelectDateDialog.Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }
        public SelectDateDialog.Builder setTitleColor(ColorStateList titleColor) {
            this.titleColorStateList = titleColor;
            return this;
        }
        public SelectDateDialog.Builder setTitleSize(float titleSize) {
            this.titleSize = titleSize;
            return this;
        }
        public SelectDateDialog.Builder setIcon(Drawable icon){
            this.icon = icon;
            return this;
        }
        public SelectDateDialog.Builder setIcon(int iconResId){
            this.icon = context.getResources().getDrawable(iconResId);
            return this;
        }
        public SelectDateDialog.Builder setMessage(CharSequence message){
          //  this.message = message;
            return this;
        }
        public SelectDateDialog.Builder setMessage(int messageResId){
          //  this.message = context.getResources().getString(messageResId);
            return this;
        }
        public SelectDateDialog.Builder setMessageColor(int color){
           // this.messageColor = color;
            return this;
        }
        public SelectDateDialog.Builder setMessageColor(ColorStateList color){
          //  this.messageColorStateList = color;
            return this;
        }
        public SelectDateDialog.Builder setMessageSize(float size){
           // this.messageSize = size;
            return this;
        }
        public SelectDateDialog.Builder setButton1(CharSequence text, SelectDateDialog.OnClickListener listener){
            this.button1Text = text;
            this.button1Listener = listener;
            button1Flag = 1;
            return this;
        }
        public SelectDateDialog.Builder setButton1(int textId, SelectDateDialog.OnClickListener listener){
            this.button1Text = context.getResources().getString(textId);
            this.button1Listener = listener;
            button1Flag = 1;
            return this;
        }
        public SelectDateDialog.Builder setButton1TextColor(int color) {
            this.button1TextColor = color;
            return this;
        }
        public SelectDateDialog.Builder setButton1TextColor(ColorStateList color) {
            this.button1ColorStateList = color;
            return this;
        }

        public SelectDateDialog.Builder setButton1Size(float button1Size) {
            this.button1Size = button1Size;
            return this;
        }
        public SelectDateDialog.Builder setButton2(CharSequence text, SelectDateDialog.OnClickListener listener){
            this.button2Text = text;
            this.button2Listener = listener;
            button2Flag = 2;
            return this;
        }
        public SelectDateDialog.Builder setButton2(int textId, SelectDateDialog.OnClickListener listener){
            this.button2Text = context.getResources().getString(textId);
            this.button2Listener = listener;
            button2Flag = 2;
            return this;
        }
        public SelectDateDialog.Builder setButton2TextColor(int color) {
            this.button2TextColor = color;
            return this;
        }
        public SelectDateDialog.Builder setButton2TextColor(ColorStateList color) {
            this.button2ColorStateList = color;
            return this;
        }
        public SelectDateDialog.Builder setButton2Size(float button2Size) {
            this.button2Size = button2Size;
            return this;
        }
        public  SelectDateDialog.Builder setButton3(CharSequence text, OnClickListener listener){
            this.button3Text = text;
            this.button3Listener = listener;
            button3Flag = 4;
            return this;
        }
        public SelectDateDialog.Builder setButton3(int textId, OnClickListener listener){
            this.button3Text = context.getResources().getString(textId);
            this.button3Listener = listener;
            button3Flag = 4;
            return this;
        }
        public SelectDateDialog.Builder setButton3TextColor(int color) {
            this.button3TextColor = color;
            return this;
        }
        public SelectDateDialog.Builder setButton3TextColor(ColorStateList color) {
            this.button3ColorStateList = color;
            return this;
        }

        public SelectDateDialog.Builder setButton3Size(float button3Size) {
            this.button3Size = button3Size;
            return this;
        }
        public SelectDateDialog.Builder setCancelable(boolean cancelable){
            this.cancelable = cancelable;
            return this;
        }
        public SelectDateDialog.Builder setCanceledOnTouchOutside(boolean canceled){
            this.canceledOnTouchOutside = canceled;
            return this;
        }
        public SelectDateDialog.Builder setView(View view){
            this.view = view;
            return this;
        }
        /**
         * you can set view style, and need not set others,including message style,title style,etc.
         */
        public SelectDateDialog.Builder setViewStyle(int style){
            this.viewStyle = style;
            this.titleColor = Color.WHITE;
            this.titlebarGravity = Gravity.START;
            return this;
        }
        @SuppressLint("InflateParams")
        public SelectDateDialog create(){

            if(dialog == null){
                return null;
            }
            View mView = null;
            LinearLayout mTitleBar = null;
            TextView mTitle = null;
            TextView mMessage = null;
            TextView btnLeft = null;
            TextView btnCenter = null;
            TextView btnRight = null;
            LinearLayout addView = null;
            LinearLayout btnView = null;
            View btnDivider1 = null;
            View btnDivider2 = null;
            View msgBtnDivider = null;
            mView = LayoutInflater.from(context).inflate(R.layout.item_datepicker_view, null);
            mTitleBar = (LinearLayout) mView.findViewById(R.id.titlebar);
            mTitle = (TextView) mView.findViewById(R.id.title);
          //  mMessage = (TextView) mView.findViewById(R.id.message);
            addView = (LinearLayout) mView.findViewById(R.id.layout_addView);
            btnLeft = (TextView) mView.findViewById(R.id.button_left);
            btnCenter = (TextView) mView.findViewById(R.id.button_center);
            btnRight = (TextView) mView.findViewById(R.id.button_right);
            btnDivider1 = (View) mView.findViewById(R.id.btn_divider1);
            btnDivider2 = (View) mView.findViewById(R.id.btn_divider2);
            msgBtnDivider = (View) mView.findViewById(R.id.msg_btn_divider);
            btnView = (LinearLayout) mView.findViewById(R.id.btn_view);
            final DatePicker datePicker=(DatePicker)mView.findViewById(R.id.id_date_picker);
            if((title != null) || (icon != null)){
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(title);
                mTitle.setTextSize(titleSize);
                mTitle.setTextColor(titleColor);
                if(titleColorStateList != null){
                    mTitle.setTextColor(titleColorStateList);
                }
                mTitle.setCompoundDrawables(icon, null, null, null);
                mTitleBar.setGravity(titlebarGravity);
            }else{
                mTitle.setVisibility(View.GONE);
            }
            if(view != null){
                addView.removeAllViews();
                addView.addView(view);
                addView.setGravity(Gravity.CENTER);
            }

            int btnCountFlag = button1Flag + button2Flag + button3Flag;
            switch(btnCountFlag) {
                //one button
                case 1:
                case 5:
                    btnCenter.setVisibility(View.VISIBLE);
                    btnLeft.setVisibility(View.GONE);
                    btnRight.setVisibility(View.GONE);
                    btnCenter.setBackgroundResource(R.drawable.prompt_dialog_btn_single_selector);
                    if(button1Text != null){
                        btnCenter.setText(button1Text);
                        btnCenter.setTextSize(button1Size);
                        btnCenter.setTextColor(button1TextColor);
                        if(button1ColorStateList != null){
                            btnCenter.setTextColor(button1ColorStateList);
                        }
                        if(button1Listener != null){
                            btnCenter.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    button1Listener.onClick(dialog,BUTTON_1,"");
                                }
                            });
                        }
                    }
                    break;

                case 3:
                    //two button
                    btnLeft.setVisibility(View.VISIBLE);
                    btnRight.setVisibility(View.VISIBLE);
                    btnCenter.setVisibility(View.GONE);
                    btnDivider1.setVisibility(View.VISIBLE);
                    btnDivider2.setVisibility(View.GONE);

                    if(button1Text != null){
                        btnLeft.setText(button1Text);
                        btnLeft.setTextSize(button1Size);
                        btnLeft.setTextColor(button1TextColor);

                        if(button1ColorStateList != null){
                            btnLeft.setTextColor(button1ColorStateList);
                        }

                        if(button1Listener != null){
                            btnLeft.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    button1Listener.onClick(dialog,BUTTON_1,"");
                                }
                            });
                        }
                    }
                    if(button2Text != null){
                        btnRight.setText(button2Text);
                        btnRight.setTextSize(button2Size);
                        btnRight.setTextColor(button2TextColor);

                        if(button2ColorStateList != null){
                            btnRight.setTextColor(button2ColorStateList);
                        }
                        if(button2Listener != null){
                            btnRight.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    String appointmentDataText=datePicker.getYear() + "-"+ (datePicker.getMonth()+1) + "-"
                                            + datePicker.getDayOfMonth();
                                    button2Listener.onClick(dialog,BUTTON_2,appointmentDataText);
                                }
                            });
                        }
                    }
                    break;
                case 7:
                    //three button
                    btnLeft.setVisibility(View.VISIBLE);
                    btnCenter.setVisibility(View.VISIBLE);
                    btnRight.setVisibility(View.VISIBLE);
                    btnDivider1.setVisibility(View.VISIBLE);
                    btnDivider2.setVisibility(View.VISIBLE);
                    if(button1Text != null){
                        btnLeft.setText(button1Text);
                        btnLeft.setTextSize(button1Size);
                        btnLeft.setTextColor(button1TextColor);
                        if(button1ColorStateList != null){
                            btnLeft.setTextColor(button1ColorStateList);
                        }

                        if(button1Listener != null){
                            btnLeft.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    button1Listener.onClick(dialog,BUTTON_1,"");
                                }
                            });
                        }
                    }
                    if(button2Text != null){
                        btnCenter.setText(button2Text);
                        btnCenter.setText(button2Text);
                        btnCenter.setTextSize(button2Size);
                        btnCenter.setTextColor(button2TextColor);

                        if(button2ColorStateList != null){
                            btnCenter.setTextColor(button2ColorStateList);
                        }
                        if(button2Listener != null){
                            btnCenter.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    button2Listener.onClick(dialog,BUTTON_2,"");
                                }
                            });
                        }
                    }
                    if(button3Text != null){
                        btnRight.setText(button3Text);
                        btnRight.setTextSize(button3Size);
                        btnRight.setTextColor(button3TextColor);

                        if(button3ColorStateList != null){
                            btnRight.setTextColor(button3ColorStateList);
                        }

                        if(button3Listener != null){
                            btnRight.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    button3Listener.onClick(dialog,BUTTON_3,"");
                                }
                            });
                        }
                    }
                    break;
                default:
                    btnView.setVisibility(View.GONE);
                    msgBtnDivider.setVisibility(View.GONE);
                    break;
            }
            Calendar mCurrent=Calendar.getInstance();
            datePicker.init(mCurrent.get(Calendar.YEAR), mCurrent.get(Calendar.MONTH),  mCurrent.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {//滑动日期进行对日期的方位进行判断
                    Calendar mAfter=Calendar.getInstance();
                    Calendar mBefore=Calendar.getInstance();
                    mAfter.add(Calendar.DAY_OF_MONTH,4);
                    if (isDateAfter(view)) {
                        view.init(mAfter.get(Calendar.YEAR),mAfter.get(Calendar.MONTH),mAfter.get(Calendar.DAY_OF_MONTH), this);
                    }
                    if (isDateBefore(view)) {
                        view.init(mBefore.get(Calendar.YEAR), mBefore.get(Calendar.MONTH),mBefore.get(Calendar.DAY_OF_MONTH), this);
                    }
                }
            });

            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            dialog.setContentView(mView);
            return dialog;
        }
        public SelectDateDialog show(){
            create().show();
            return dialog;
        }
        private boolean isDateBefore(DatePicker tempView) {
            Calendar mCalendar = Calendar.getInstance();
            Calendar temCalendar = Calendar.getInstance();
            temCalendar.set(tempView.getYear(), tempView.getMonth(), tempView.getDayOfMonth(), 0, 0, 0);
            return temCalendar.before(mCalendar);
        }
        private boolean isDateAfter(DatePicker tempView) {
            Calendar mCalendar=Calendar.getInstance();
            mCalendar.add(Calendar.DAY_OF_MONTH,4);
            Calendar temCalendar=Calendar.getInstance();
            temCalendar.add(Calendar.DAY_OF_MONTH,4);
            temCalendar.set(tempView.getYear(),tempView.getMonth(),tempView.getDayOfMonth(),0,0,0);
            return temCalendar.after(mCalendar);
        }
    }
    public interface OnClickListener{
        /**
         * 点击回调
         * @param dialog
         * @param which
         * @param s
         */
        void onClick(Dialog dialog, int which,String s);
    }
}
