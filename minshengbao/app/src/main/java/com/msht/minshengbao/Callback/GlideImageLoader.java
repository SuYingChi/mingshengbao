package com.msht.minshengbao.Callback;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.ViewUI.widget.BannerLayout;

/**
 * Created by Administrator on 2016/12/21 0021.
 */

public class GlideImageLoader implements BannerLayout.ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(context).load(path).apply(requestOptions).into(imageView);
    }
}
