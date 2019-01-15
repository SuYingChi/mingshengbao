package com.msht.minshengbao.androidShop.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * <p>
 * 自定义ImageSpan 解决图片显示不居中问题
 */
public class MyImageSpan extends ImageSpan {

    public MyImageSpan(Drawable drawable) {
        super(drawable);
    }

    public MyImageSpan(Context context, int id) {
        super(context, id);
    }

    public MyImageSpan(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        Drawable drawable = getDrawable();
        int transY = (y + fm.descent + y + fm.ascent) / 2
                - drawable.getBounds().bottom / 2;
        canvas.save();
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
