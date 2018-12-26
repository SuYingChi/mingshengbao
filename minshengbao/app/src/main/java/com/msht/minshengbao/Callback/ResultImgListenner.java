package com.msht.minshengbao.Callback;

import android.graphics.Bitmap;

/**
 *
 * @author hong
 * @date 2017/2/17
 */

public interface ResultImgListenner {
    void Success(Bitmap bitmap);
    void Failure(String string);
}
