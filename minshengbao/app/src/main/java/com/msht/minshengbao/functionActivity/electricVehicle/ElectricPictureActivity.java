package com.msht.minshengbao.functionActivity.electricVehicle;

import android.os.Bundle;
import android.view.View;

import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.Callback.PictureImageLoader;
import com.msht.minshengbao.R;
import com.msht.minshengbao.custom.widget.BannerLayout;

import java.util.ArrayList;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/10/18  
 */
public class ElectricPictureActivity extends BaseActivity{
    private ArrayList<String> imageUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_picture);
        context=this;
        mPageName="图片详情";
        setCommonHeader(mPageName);
        Bundle bundle=getIntent().getBundleExtra("url");
        imageUrl =bundle.getStringArrayList("image");
        initView();
    }
    private void initView() {
        View layoutHeader =findViewById(R.id.id_re_layout);
        layoutHeader.setBackgroundResource(R.color.black);
        BannerLayout mBanner=(BannerLayout)findViewById(R.id.id_banner);
        mBanner.setImageLoader(new PictureImageLoader());
        mBanner.setViewUrls(imageUrl);
    }
}
