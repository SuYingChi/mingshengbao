package com.msht.minshengbao.androidShop.viewInterface;

import android.graphics.Bitmap;

public interface IGetYanzhengCodeView extends IBaseView{
    void onGetYanzhengCode(Bitmap s);

    String getCodeKey();
}
