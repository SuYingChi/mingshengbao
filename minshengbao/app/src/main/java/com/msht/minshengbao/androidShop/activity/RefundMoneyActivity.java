package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.GoodEvaluationChildAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadAndFootRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.ShopRefundDetailGoodListAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.ImageListPagerDialog;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.RefundMoneyDetail;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.IRefundMoneyDetailView;
import com.msht.minshengbao.androidShop.viewInterface.ImageListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RefundMoneyActivity extends ShopBaseActivity implements IRefundMoneyDetailView, ImageListView {
    private String refund_id;
    @BindView(R.id.tv_amount)
    TextView amount;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.reason)
    TextView reason;
    @BindView(R.id.detail)
    TextView detail;
    @BindView(R.id.amount2)
    TextView amount2;
    @BindView(R.id.num)
    TextView num;
    @BindView(R.id.sn)
    TextView sn;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.seller_message)
    TextView seller_message;
    @BindView(R.id.caiwu_state)
    TextView caiwu_state;
    @BindView(R.id.pay_method)
    TextView pay_method;
    @BindView(R.id.online_refund)
    TextView online_refund;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    @BindView(R.id.rcl2)
    RecyclerView rcl2;
    @BindView(R.id.tvpz)
    TextView tvpz;
    private ShopRefundDetailGoodListAdapter adapter;
    private List<RefundMoneyDetail.DatasBean.GoodsListBean> goodList=new ArrayList<RefundMoneyDetail.DatasBean.GoodsListBean>();
    private List<String> imageList;
    private int selectedIamgePosition;

    @Override
    protected void setLayout() {
        setContentView(R.layout.refund_money_detail);
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
        refund_id = intent.getStringExtra("data");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rcl.setNestedScrollingEnabled(false);
        rcl.setLayoutManager(linearLayoutManager);
        rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ShopRefundDetailGoodListAdapter(this);
        adapter.setDatas(goodList);
        adapter.setOnItemClickListener(new MyHaveHeadAndFootRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
              /*  Map map = new HashMap<String,String>();
                map.put("type","goods");
                map.put("data",goodList.get(position).getGoods_id());
                doNotAdClick(map);*/
                String goodsId = goodList.get(position).getGoods_id();
                onShopItemViewClick("goods",goodsId);
            }
        });
        rcl.setAdapter(adapter);
        ShopPresenter.getRefundMoneyDetail(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public String getRefund_id() {
        return refund_id;
    }

    @Override
    public void onGetRefundMoneyDetailSuccess(String s) {
        RefundMoneyDetail bean = JsonUtil.toBean(s, RefundMoneyDetail.class);
        if(bean!=null) {
            RefundMoneyDetail.DatasBean.RefundBean refund = bean.getDatas().getRefund();
            amount.setText(StringUtil.getPriceSpannable12String(this, refund.getRefund_amount(), R.style.big_money, R.style.big_money));
            reason.setText(refund.getReason_info());
            detail.setText(refund.getBuyer_message());
            amount2.setText(StringUtil.getPriceSpannable12String(this, refund.getRefund_amount(), R.style.big_money, R.style.big_money));
           //如果在onresume里请求数据，这里list需要先清空在addall
            goodList.addAll(bean.getDatas().getGoods_list());
            adapter.notifyDataSetChanged();
            int total = 0;
            for (RefundMoneyDetail.DatasBean.GoodsListBean b : goodList) {
                total += Integer.valueOf(b.getGoods_num());
            }
            num.setText(total + "");
            sn.setText(refund.getRefund_sn());
            state.setText(refund.getSeller_state());
            seller_message.setText(refund.getSeller_message());
            caiwu_state.setText(refund.getAdmin_state());
            imageList = bean.getDatas().getPic_list();
            if (imageList.size() == 0) {
                tvpz.setVisibility(View.GONE);
                rcl2.setVisibility(View.GONE);
            } else {
                tvpz.setVisibility(View.VISIBLE);
                rcl2.setVisibility(View.VISIBLE);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
                rcl2.setLayoutManager(gridLayoutManager);
                gridLayoutManager.setAutoMeasureEnabled(true);
                rcl2.setNestedScrollingEnabled(false);
                GoodEvaluationChildAdapter ad = new GoodEvaluationChildAdapter(this);
                ad.setDatas(imageList);
                ad.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int childposition) {
                        selectedIamgePosition = childposition;
                        ImageListPagerDialog imageListDialog = new ImageListPagerDialog(RefundMoneyActivity.this, RefundMoneyActivity.this, childposition);
                        imageListDialog.show();
                    }
                });
                rcl2.setAdapter(ad);
            }
            pay_method.setText(bean.getDatas().getDetail_array().getRefund_code());
            online_refund.setText(bean.getDatas().getDetail_array().getPay_amount());
        }else {
            PopUtil.toastInBottom("数据转换异常");
        }
    }

    @Override
    public List<String> getImageList() {
        return imageList;
    }

    @Override
    public int getSelectedPosition() {
        return selectedIamgePosition;
    }
}
