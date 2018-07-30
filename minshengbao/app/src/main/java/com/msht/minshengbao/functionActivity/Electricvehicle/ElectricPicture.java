package com.msht.minshengbao.FunctionActivity.Electricvehicle;

import android.os.Bundle;
import android.view.View;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.PictureImageLoader;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.widget.BannerLayout;

import java.util.ArrayList;

public class ElectricPicture extends BaseActivity{
    private View layout_header;
    private BannerLayout mBanner;
    private ArrayList<String> Imgurl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_picture);
        context=this;
        setCommonHeader("图片详情");
        Bundle bundle=getIntent().getBundleExtra("url");
        Imgurl=bundle.getStringArrayList("image");
        initView();
    }
    private void initView() {
        layout_header=findViewById(R.id.id_re_layout);
        layout_header.setBackgroundResource(R.color.black);
        mBanner=(BannerLayout)findViewById(R.id.id_banner);
        mBanner.setImageLoader(new PictureImageLoader());
        mBanner.setViewUrls(Imgurl);
    }
}
