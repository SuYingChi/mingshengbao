package com.msht.minshengbao.androidShop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.GoodEvaluationChildAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadAndFootRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.ShopReturnDetailGoodListAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.customerview.ImageListPagerDialog;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.ReturnGoodDetailBean;
import com.msht.minshengbao.androidShop.shopBean.ReturnGoodDetailBean2;
import com.msht.minshengbao.androidShop.shopBean.ReturnGoodsListBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.IRefundGoodDetailView;
import com.msht.minshengbao.androidShop.viewInterface.ImageListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ReturnGoodDetailActivity extends ShopBaseActivity implements IRefundGoodDetailView, ImageListView {
    private String returnGoodId;
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
    @BindView(R.id.pd_amount)
    TextView pd_amount;
    @BindView(R.id.rcb_amount)
    TextView rcb_amount;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    @BindView(R.id.rcl2)
    RecyclerView rcl2;
    @BindView(R.id.tvpz)
    TextView tvpz;
    @BindView(R.id.llll)
    LinearLayout llll;
    private List<ReturnGoodsListBean> goodList = new ArrayList<ReturnGoodsListBean>();
    private ShopReturnDetailGoodListAdapter adapter;
    private List<String> imageList;
    private int selectedIamgePosition;

    @Override
    protected void setLayout() {
        setContentView(R.layout.return_good_detail);
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
        returnGoodId = getIntent().getStringExtra("data");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rcl.setNestedScrollingEnabled(false);
        rcl.setLayoutManager(linearLayoutManager);
        rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ShopReturnDetailGoodListAdapter(this);
        adapter.setDatas(goodList);
        adapter.setOnItemClickListener(new MyHaveHeadAndFootRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
              /*  Map map = new HashMap<String,String>();
                map.put("type","goods");
                map.put("data",goodList.get(position).getGoodId());
                doNotAdClick(map);*/
                String goodsId = goodList.get(position).getGoodId();
                onShopItemViewClick("goods",goodsId);

            }
        });
        rcl.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ShopPresenter.getRefundGoodDetail(this);
    }

    @Override
    public String getReturnGood_id() {
        return returnGoodId;
    }

    @Override
    public void onGetRefundGoodDetailSuccess(String s) {
        ReturnGoodDetailBean returnGoodDetailBean = JsonUtil.toBean(s, ReturnGoodDetailBean.class);
        if (returnGoodDetailBean != null) {
            List<ReturnGoodDetailBean.DatasBean.GoodsListBean> goods = returnGoodDetailBean.getDatas().getGoods_list();
            for (ReturnGoodDetailBean.DatasBean.GoodsListBean good : goods) {
                goodList.add(new ReturnGoodsListBean(good.getGoods_image(), good.getGoods_name(), good.getGoods_spec(), good.getGoods_price(), good.getGoods_num(),good.getGoods_id()));
            }
            ReturnGoodDetailBean.DatasBean.ReturnInfoBean refund = returnGoodDetailBean.getDatas().getReturn_info();
            amount.setText(StringUtil.getPriceSpannable12String(this, refund.getRefund_amount(), R.style.big_money, R.style.big_money));
            reason.setText(refund.getReason_info());
            detail.setText(refund.getBuyer_message());
            amount2.setText(StringUtil.getPriceSpannable12String(this, refund.getRefund_amount(), R.style.big_money, R.style.big_money));
            adapter.notifyDataSetChanged();
            int total = 0;
            for (ReturnGoodsListBean b : goodList) {
                total += Integer.valueOf(b.getGoods_num());
            }
            num.setText(total + "");
            sn.setText(refund.getRefund_sn());
            state.setText(refund.getSeller_state());
            seller_message.setText(refund.getSeller_message());
            caiwu_state.setText(refund.getAdmin_state());
            ReturnGoodDetailBean.DatasBean.DetailArrayBean detailArray = returnGoodDetailBean.getDatas().getDetail_array();
            llll.setVisibility(View.VISIBLE);
            pay_method.setText(detailArray.getRefund_code());
            online_refund.setText(detailArray.getPay_amount());
            pd_amount.setText(detailArray.getPd_amount());
            rcb_amount.setText(detailArray.getRcb_amount());
            imageList = returnGoodDetailBean.getDatas().getPic_list();
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
                        ImageListPagerDialog imageListDialog = new ImageListPagerDialog(ReturnGoodDetailActivity.this, ReturnGoodDetailActivity.this, childposition);
                        imageListDialog.show();
                    }
                });
                rcl2.setAdapter(ad);
            }
        } else {
            ReturnGoodDetailBean2 returnGoodDetailBean2 = JsonUtil.toBean(s, ReturnGoodDetailBean2.class);
            llll.setVisibility(View.GONE);
            if (returnGoodDetailBean2 != null) {
                List<ReturnGoodDetailBean2.DatasBean.GoodsListBean> goods = returnGoodDetailBean2.getDatas().getGoods_list();
                for (ReturnGoodDetailBean2.DatasBean.GoodsListBean good : goods) {
                    goodList.add(new ReturnGoodsListBean(good.getGoods_image(), good.getGoods_name(), good.getGoods_spec(), good.getGoods_price(), good.getGoods_num(), good.getGoods_id()));
                }
                ReturnGoodDetailBean2.DatasBean.ReturnInfoBean refund = returnGoodDetailBean2.getDatas().getReturn_info();
                amount.setText(StringUtil.getPriceSpannable12String(this, refund.getRefund_amount(), R.style.big_money, R.style.big_money));
                reason.setText(refund.getReason_info());
                detail.setText(refund.getBuyer_message());
                amount2.setText(StringUtil.getPriceSpannable12String(this, refund.getRefund_amount(), R.style.big_money, R.style.big_money));
                adapter.notifyDataSetChanged();
                int total = 0;
                for (ReturnGoodsListBean b : goodList) {
                    total += Integer.valueOf(b.getGoods_num());
                }
                num.setText(total + "");
                sn.setText(refund.getRefund_sn());
                state.setText(refund.getSeller_state());
                seller_message.setText(refund.getSeller_message());
                caiwu_state.setText(refund.getAdmin_state());
                imageList = returnGoodDetailBean2.getDatas().getPic_list();
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
                            ImageListPagerDialog imageListDialog = new ImageListPagerDialog(ReturnGoodDetailActivity.this, ReturnGoodDetailActivity.this, childposition);
                            imageListDialog.show();
                        }
                    });
                    rcl2.setAdapter(ad);
                }
            }
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
