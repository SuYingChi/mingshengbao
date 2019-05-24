package com.msht.minshengbao.custom;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.R;


/**
 *
 * @author hei123
 * @date 11/11/2016
 * CopyRight @hei123
 * 图标+文字+箭头的按钮
 */

public class MyImageTextButton extends RelativeLayout {
    /**
     * 是否具有右边的图片
     */
    private ImageView leftImage;
    private ImageView rightImage;
    private TextView  leftText;

    public MyImageTextButton(Context context) {
        super(context);
        initView(context,null,0);
    }

    public MyImageTextButton(Context context, AttributeSet attrs) {
        super(context,attrs);
        initView(context,attrs,0);
    }

    public MyImageTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr);
       // initLayout();
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_image_text_button,null);
        leftImage=(ImageView)view.findViewById(R.id.id_left_img);
        leftText=(TextView)view.findViewById(R.id.id_left_text);
        rightImage=(ImageView)view.findViewById(R.id.id_right_img);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyImageTextButton);
        int leftDrawable=ta.getResourceId(R.styleable.MyImageTextButton_leftImage,defStyleAttr);
        int rightDrawable=ta.getResourceId(R.styleable.MyImageTextButton_rightImage,defStyleAttr);
        String leftTextString=ta.getString(R.styleable.MyImageTextButton_leftText);
        float  leftTextSize=ta.getDimensionPixelOffset(R.styleable.MyImageTextButton_leftTextSize,16);
        int    leftTextColor=ta.getColor(R.styleable.MyImageTextButton_leftTextColor,defStyleAttr);
        leftImage.setImageResource(leftDrawable);
        rightImage.setImageResource(rightDrawable);
        leftText.setText(leftTextString);
        leftText.setTextColor(leftTextColor);
        //leftText.setTextSize(leftTextSize);
        ta.recycle();
        addView(view,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

    }


    /**
     * 初始化元素的布局
     */
    private void initLayout() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = getRawSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        layoutParams.width = getRawSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        layoutParams.height = getRawSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        this.addView(leftImage, layoutParams);

    }



    /**
     * 设置左边图片的src
     *
     * @param resourceId
     */
    public void setImageLeftResource(int resourceId) {
        leftImage.setImageResource(resourceId);
    }

    /**
     * 设置左边图片的src
     *
     * @param drawable
     */
    public void setImageLeftResource(Drawable drawable) {
        leftImage.setImageDrawable(drawable);
    }

    /**
     * 设置右边图片的src
     *
     * @param resourceId
     */
    public void setImageRightResource(int resourceId) {
        rightImage.setImageResource(resourceId);
    }

    /**
     * 设置右边图片的src
     *
     * @param drawable
     */
    public void setImageRightResource(Drawable drawable) {
        rightImage.setImageDrawable(drawable);
    }

    /**
     * 设置文字内容和大小
     *
     * @param text
     * @param
     */
    public void setLeftTextAndSize(String text) {
        leftText.setText(text);
       // textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }

    public void setLeftTextColor(int textColor) {
        leftText.setTextColor(textColor);
    }

    public void setLeftTextSize(float textSize){
        leftText.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
    }
    public int getRawSize(int unit, float size) {
        Context c = getContext();
        Resources r;
        if (c == null){
            r = Resources.getSystem();
        }else{
            r = c.getResources();
        }
        return (int) TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }
    private int intSp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
    private float sp2px(float sp) {
        return  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}
