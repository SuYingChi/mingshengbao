package com.msht.minshengbao.androidShop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.viewInterface.IPingTuanDetailView;

public class PingtuanDetail extends ShopBaseActivity implements IPingTuanDetailView {
    private String pingtuanid;
    private String buyerid;

    @Override
    protected void setLayout() {
     setContentView(R.layout.pintuan);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pingtuanid =  getIntent().getStringExtra("pingtuanid");
        buyerid =  getIntent().getStringExtra("buyer_id");
        ShopPresenter.getPingTuanDetail(this);
    }

    @Override
    public void onGetPingTuanDetail(String s) {

    }

    @Override
    public String getPingTuanId() {
        return pingtuanid;
    }

    @Override
    public String getBuyerId() {
        return buyerid;
    }
}
