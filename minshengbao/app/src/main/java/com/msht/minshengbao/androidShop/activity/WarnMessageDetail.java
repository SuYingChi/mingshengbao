package com.msht.minshengbao.androidShop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;

public class WarnMessageDetail extends ShopBaseActivity{
    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.back)
    ImageView ivback;
    private String test;
    private RichText richText;

    @Override
    protected void setLayout() {
        setContentView(R.layout.warn_message);
    }

    @Override
    protected void initImmersionBar() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test = getIntent().getStringExtra("data");
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        richText = RichText.from(test).into(textView);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        richText.clear();
        richText = null;
    }
}
