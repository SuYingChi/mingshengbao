package com.msht.minshengbao.androidShop.customerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class DrawHookByAnimatorView extends View {

    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private float mProgress;

    //画圆
    private ValueAnimator mAnimatorCircle;
    //画勾
    private ValueAnimator mAnimatorHook;

    public DrawHookByAnimatorView(Context context) {
        super(context);
        init();
    }


    public DrawHookByAnimatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawHookByAnimatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.WHITE);
        canvas.drawPath(mPath,mPaint);
    }

    //画圆
    public void circleAnimation() {

        if (mAnimatorCircle != null || mAnimatorHook != null){
            mAnimatorCircle.cancel();
            mAnimatorHook.cancel();
        }

        mPath.reset();
        mAnimatorCircle = ValueAnimator.ofFloat(0, 360);
        mAnimatorCircle.setInterpolator(new LinearInterpolator());
        mAnimatorCircle.setDuration(1000);
        mAnimatorCircle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mPath.addArc(20, 20, getWidth() - 20, getHeight() - 20, 90, mProgress);
                }
                postInvalidate();
            }
        });
        mAnimatorCircle.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                hookAnimation();
            }
        });

        mAnimatorCircle.start();
    }

    //画钩
    public void hookAnimation() {

        mPath.moveTo(getWidth() / 4, getHeight() / 2);
        mAnimatorHook = ValueAnimator.ofFloat(0, 8);
        mAnimatorHook.setInterpolator(new LinearInterpolator());
        mAnimatorHook.setDuration(500);
        mAnimatorHook.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                if(progress < 5) {
                    mPath.rLineTo(progress, progress);
                }else{
                    mPath.rLineTo(progress, -progress);
                }
                postInvalidate();
            }
        });
        mAnimatorHook.start();
    }

}