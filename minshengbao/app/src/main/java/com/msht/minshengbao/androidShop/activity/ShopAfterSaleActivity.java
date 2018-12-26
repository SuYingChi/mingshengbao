package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.RefundFormBean;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.IRefundFormView;

import butterknife.BindView;

public class ShopAfterSaleActivity extends ShopBaseActivity implements IRefundFormView {
    private String orderId;
    private String goodId;
    @BindView(R.id.store)
    TextView store;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.rlt_tuikuan)
    RelativeLayout rlt_tuikuan;
    @BindView(R.id.rlt_tuihuo)
    RelativeLayout rlt_tuihuo;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    @Override
    protected void setLayout() {
        setContentView(R.layout.after_sale_activity);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).navigationBarColor(R.color.black).navigationBarWithKitkatEnable(false).init();
        ImmersionBar.setTitleBar(this, mToolbar);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        orderId= intent.getStringExtra("orderId");
        goodId = intent.getStringExtra("goodId");

    }

    @Override
    protected void onResume() {
        super.onResume();
        ShopPresenter.getRefundForm(this);
    }

    @Override
    public void onGetRefundFormSuccess(String s) {
        final RefundFormBean refundFormBean = JsonUtil.toBean(s,RefundFormBean.class);
        if (refundFormBean != null) {
            store.setText(refundFormBean.getDatas().getOrder().getStore_name());
            GlideUtil.loadByImageView(this,iv,refundFormBean.getDatas().getGoods().getGoods_img_360());
            price.setText(StringUtil.getPriceSpannable12String(this,refundFormBean.getDatas().getGoods().getGoods_price(),R.style.big_money,R.style.big_money));
            num.setText(String.format("X%s", refundFormBean.getDatas().getGoods().getGoods_num()));
            if(refundFormBean.getDatas().getGoods().getGoods_spec()!=null) {
                desc.setText(refundFormBean.getDatas().getGoods().getGoods_spec().toString());
            }
            tvName.setText(refundFormBean.getDatas().getGoods().getGoods_name());
        }
        rlt_tuikuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent =new Intent(ShopAfterSaleActivity.this,RefundFormMoneyActivity.class);
               intent.putExtra("data",refundFormBean);
              startActivity(intent);
            }
        });
        rlt_tuihuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ShopAfterSaleActivity.this,RefundGoodActivity.class);
                intent.putExtra("data",refundFormBean);
                startActivity(intent);
            }
        });
    }

    @Override
    public String getOrderid() {
        return orderId;
    }

    @Override
    public String getRecGoodsid() {
        return goodId;
    }
}
