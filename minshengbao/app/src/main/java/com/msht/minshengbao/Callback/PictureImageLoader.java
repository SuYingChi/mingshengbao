package com.msht.minshengbao.Callback;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.ViewUI.widget.BannerLayout;

/**
 * Created by hong on 2017/12/5.
 */

public class PictureImageLoader implements BannerLayout.ImageLoader{
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(context).load(path).apply(requestOptions).into(imageView);
    }
}
