package com.msht.minshengbao.functionActivity.repairService;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.base.BaseActivity;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/9/2  
 */
public class EnlargePicActivity extends BaseActivity {
    private PhotoViewAttacher mAttacher;
    private ImageView ivPic, deleteImg,backimg;
    private ArrayList<String> imgPaths;
    private int position=0;
    private TextView tvNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_pic);
        context=this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imgPaths = bundle.getStringArrayList("imgPaths");
            position=bundle.getInt("position");
        }
        initHeader();
        initView();
        intEvent();
    }

    private void initHeader() {
        View mViewStatusBarPlace = findViewById(R.id.id_status_view);
        ViewGroup.LayoutParams params = mViewStatusBarPlace.getLayoutParams();
        params.height = StatusBarCompat.getStatusBarHeight(this);
        mViewStatusBarPlace.setLayoutParams(params);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mViewStatusBarPlace.setVisibility(View.GONE);
        }
    }

    private void initView() {
        backimg=(ImageView)findViewById(R.id.id_back);
        tvNum =(TextView)findViewById(R.id.id_text);
        deleteImg =(ImageView)findViewById(R.id.id_delete_img);
        ivPic=(ImageView)findViewById(R.id.iv_pic);
        mAttacher = new PhotoViewAttacher(ivPic);
        tvNum.setText((position+1)+"/"+imgPaths.size());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.icon_stub);
        Glide.with(this).asBitmap().load(imgPaths.get(position)).apply(requestOptions).into(new SimpleTarget<Bitmap>(){
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                ivPic.setImageBitmap(resource);
                mAttacher.update();
            }
        });
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                // hideOrShowToolBar();
            }
            @Override
            public void onOutsidePhotoTap() {
                // hideOrShowToolBar();
            }
        });
    }
    private void intEvent() {
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAttacher.cleanup();
    }
}
