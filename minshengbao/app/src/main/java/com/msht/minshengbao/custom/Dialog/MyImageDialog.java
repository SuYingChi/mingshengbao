package com.msht.minshengbao.custom.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.msht.minshengbao.R;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class MyImageDialog {
  //  private PhotoViewAttacher photoViewAttacher;
    private RequestOptions requestOptions;
   // private ImageView mImageView;
    private PhotoView mImageView;
    private int resId;
    private Context context;
    private Dialog dialog;
    private OnPositiveClickListener callClickListener;
    private OnCancelClickListener cancelClickListener;
    public interface OnPositiveClickListener {
        /**
         *  回调返回数据
         * @param v v
         */
        void onClick(View v);
    }
    public MyImageDialog setOnPositiveClickListener(OnPositiveClickListener listener){
        this.callClickListener=listener;
        return this;
    }

    public interface OnCancelClickListener {
        /**
         *  回调返回数据
         * @param v v
         */
        void onCancelClick(View v);
    }
    public MyImageDialog setCancelClickListener(OnCancelClickListener listener){
        this.cancelClickListener=listener;
        return this;
    }
    public MyImageDialog(Context context,int resId) {
        this.context = context;
        this.resId=resId;
        requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.icon_stub);
    }

    public MyImageDialog builder() {
        // 获取Dialog布局
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_image_view,null);
       // mImageView=(ImageView)view.findViewById(R.id.id_image);
        mImageView=(PhotoView) view.findViewById(R.id.id_image);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (resId!=0){
            mImageView.setImageResource(resId);
        }
       /* photoViewAttacher = new PhotoViewAttacher(mImageView);
        photoViewAttacher.update();*/
        dialog = new Dialog(context, R.style.PromptDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (windowManager!=null){
            DisplayMetrics displayMetrics=new DisplayMetrics();
            if (windowManager.getDefaultDisplay()!=null){
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            }
            if (dialogWindow!=null){
                dialogWindow.setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.x = 0;
                lp.y = 0;
                dialogWindow.setAttributes(lp);
            }
        }
        return this;
    }
    public MyImageDialog setImageResource(int resId){
        mImageView.setImageResource(resId);
       // photoViewAttacher.update();
        return this;
    }
    public MyImageDialog setImageUrl(String resId){
        Glide.with(context).asBitmap().load(resId).apply(requestOptions).into(new SimpleTarget<Bitmap>(){
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                mImageView.setImageBitmap(resource);
               // photoViewAttacher.update();
            }
        });
        return this;
    }
    public  MyImageDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }
    public MyImageDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }
    public void show() {
        dialog.show();
    }
}
