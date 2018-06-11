package com.msht.minshengbao.Callback;

import android.graphics.Bitmap;

/**
 * Created by hong on 2017/2/17.
 */

public interface ResultImgListenner {
    void Success(Bitmap bitmap);
    void Failure(String string);
}
