package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.GuessLikeAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.event.GoShopMainEvent;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.GuessLikeBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.IGuessLikeGoodListView;
import com.msht.minshengbao.functionActivity.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopSuccessActivity extends ShopBaseActivity implements IGuessLikeGoodListView {
    @BindView(R.id.back)
    ImageView ivBack;
    @BindView(R.id.order)
    TextView order;
    @BindView(R.id.return_home)
    TextView returnHome;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    private String orderId;
    private GuessLikeAdapter ad;
    private List<GuessLikeBean.DatasBean.GuessLikeListBean> datalist = new ArrayList<GuessLikeBean.DatasBean.GuessLikeListBean>();
    private String state;

    @Override
    protected void setLayout() {
        setContentView(R.layout.evelate_success);
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
        orderId = getIntent().getStringExtra("id");
        state = getIntent().getStringExtra("state");
        switch (state) {
            case "pay":
                iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_pay_success));
                tv.setText("付款成功");
                tvTitle.setText("付款成功");
                returnHome.setText("回到首页");
                returnHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, MainActivity.class);
                        intent.putExtra("index",1);
                        startActivity(intent);
                      //  EventBus.getDefault().postSticky(new GoShopMainEvent());
                        finish();
                    }
                });
                order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, ShopOrdersDetailActivity.class);
                        intent.putExtra("data", orderId);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case "eveluate":
                iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_evelate_success));
                tv.setText("评价成功");
                tvTitle.setText("评价成功");
                returnHome.setText("追加评价");
                returnHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, ShopOrderAddEveluateActivity.class);
                        intent.putExtra("id",orderId);
                        startActivity(intent);
                        finish();
                    }
                });
                order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, ShopOrdersDetailActivity.class);
                        intent.putExtra("data", orderId);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case "addeveluate":
                iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_evelate_success));
                tv.setText("追加评价成功");
                tvTitle.setText("追加评价成功");
                returnHome.setText("回到首页");
                returnHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, MainActivity.class);
                        intent.putExtra("index",1);
                        startActivity(intent);
                        //EventBus.getDefault().postSticky(new GoShopMainEvent());
                        finish();
                    }
                });
                order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, ShopOrdersDetailActivity.class);
                        intent.putExtra("data", orderId);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case "receive":
                iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_receive));
                tv.setText("确认收货");
                returnHome.setText("去评价");
                tvTitle.setText("确认收货");
                returnHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, ShopOrderEveluateActivity.class);
                        intent.putExtra("id",orderId);
                        startActivity(intent);

                        finish();
                    }
                });
                order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, ShopOrdersDetailActivity.class);
                        intent.putExtra("data", orderId);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case "cancel":
                iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_fail));
                tv.setText("取消订单");
                returnHome.setText("回到首页");
                tvTitle.setText("取消订单");
                returnHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, MainActivity.class);
                        intent.putExtra("index",1);
                        startActivity(intent);
                       // EventBus.getDefault().postSticky(new GoShopMainEvent());
                        finish();
                    }
                });
                order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, ShopOrdersDetailActivity.class);
                        intent.putExtra("data", orderId);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case "refund":
                iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_pay_success));
                tv.setText("订单退款");
                returnHome.setText("回到首页");
                tvTitle.setText("订单退款");
                returnHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, MainActivity.class);
                        intent.putExtra("index",1);
                        startActivity(intent);
                      //  EventBus.getDefault().postSticky(new GoShopMainEvent());
                        finish();
                    }
                });
                order.setText("我的退款");
                order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, MyShopOrderActivity.class);
                        intent.putExtra("index", 1);
                        intent.putExtra("indexChild", 0);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case "refundGood":
                iv.setImageDrawable(getResources().getDrawable(R.drawable.shop_pay_success));
                tv.setText("订单退货");
                returnHome.setText("回到首页");
                tvTitle.setText("退款退货");
                returnHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, MainActivity.class);
                        intent.putExtra("index",1);
                        startActivity(intent);
                       // EventBus.getDefault().postSticky(new GoShopMainEvent());
                        finish();
                    }
                });
                order.setText("我的退货");
                order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopSuccessActivity.this, MyShopOrderActivity.class);
                        intent.putExtra("index", 1);
                        intent.putExtra("indexChild", 1);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            default:
                break;
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcl.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rcl.setNestedScrollingEnabled(false);
        ad = new GuessLikeAdapter(this);
        ad.setDatas(datalist);
        ad.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int childposition) {
               /* HashMap<String, String> map = new HashMap<String, String>();
                String goodsId = datalist.get(childposition).getGoods_id();
                map.put("type", "goods");
                map.put("data", goodsId);
                doNotAdClick(map);*/

                String goodsId = datalist.get(childposition).getGoods_id();
                onShopItemViewClick("goods",goodsId);
            }
        });
        rcl.setAdapter(ad);
        ShopPresenter.guessLikeGoodList(this);
    }

    @Override
    public void onGetGuessLikeGoodSuccess(String s) {
        datalist.addAll(JsonUtil.toBean(s, GuessLikeBean.class).getDatas().getGuess_like_list());
        ad.notifyDataSetChanged();
    }
}
