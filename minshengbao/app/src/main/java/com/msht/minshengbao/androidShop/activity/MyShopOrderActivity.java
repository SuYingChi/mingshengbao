package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.Fragment.ShopOrdersFragement;
import com.msht.minshengbao.androidShop.Fragment.ShopRefundFragmnet;
import com.msht.minshengbao.androidShop.adapter.BaseLazyFragmentPagerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.customerview.NoScrollViewPager;
import com.msht.minshengbao.androidShop.event.OrderType;
import com.msht.minshengbao.androidShop.event.RefundType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyShopOrderActivity extends ShopBaseActivity {
    @BindView(R.id.vp)
    NoScrollViewPager vp;
    private List<ShopBaseLazyFragment> list = new ArrayList<ShopBaseLazyFragment>();
    private ShopOrdersFragement f2;
    private ShopRefundFragmnet f3;
    private int fragmentIndex = -1;
    private int refundindex = -1;
    private int orderstype = -1;


    @Override
    protected void setLayout() {
        setContentView(R.layout.my_shop_order);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (savedInstanceState != null) {
            int currentFragmentIndex = savedInstanceState.getInt("index", 0);
            int indexChilde = savedInstanceState.getInt("indexChild", 0);
            f2 = (ShopOrdersFragement) getSupportFragmentManager().getFragment(savedInstanceState, "f2");
            f3 = (ShopRefundFragmnet) getSupportFragmentManager().getFragment(savedInstanceState, "f3");
            if (currentFragmentIndex != this.fragmentIndex) {
                vp.setCurrentItem(currentFragmentIndex);
                this.fragmentIndex = currentFragmentIndex;
            }
            if (currentFragmentIndex == 0) {
                if (indexChilde != this.orderstype) {
                    this.orderstype = indexChilde;
                }
                f2.refreshCurrentTab(orderstype, false);
            } else {
                if (indexChilde != this.refundindex) {
                    this.refundindex = indexChilde;
                }
                f3.refreshCurrentTab(refundindex, false);
            }

        } else if (getIntent() != null) {
            f2 = new ShopOrdersFragement();
            f3 = new ShopRefundFragmnet();
            this.fragmentIndex = getIntent().getIntExtra("index", 0);
            if (fragmentIndex == 0) {
                this.orderstype = getIntent().getIntExtra("indexChild", 0);
                Bundle bundle = new Bundle();
                bundle.putInt("tab", orderstype);
                f2.setArguments(bundle);
            } else {
                this.refundindex = getIntent().getIntExtra("indexChild", 0);
                Bundle bundle = new Bundle();
                bundle.putInt("tab", refundindex);
                f3.setArguments(bundle);
            }
            list.add(f2);
            list.add(f3);
            vp.setAdapter(new BaseLazyFragmentPagerAdapter(getSupportFragmentManager(), list));
            vp.setOffscreenPageLimit(2);
            vp.setNoScroll(true);
            vp.setPageTransformer(true, null);
            vp.setCurrentItem(fragmentIndex);
            initNoNetworkLayout();
        }

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).navigationBarColor(R.color.black).navigationBarWithKitkatEnable(false).init();
    }

    //activity在被横竖屏切换时销毁activity时回调 onsaveinstancestate 保存数据到bundle里，传到在重新创建实例时的oncreat 和 onrestoreinstance时候的 bundle里
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", fragmentIndex);
        outState.putInt("indexChild", fragmentIndex == 0 ? orderstype : refundindex);
        getSupportFragmentManager().putFragment(outState, "f2", f2);
        getSupportFragmentManager().putFragment(outState, "f3", f3);
    }
   /*
    singletask模式的activity
    onPause
     onStop
    其他应用再发送Intent的话，执行顺序为：
    onNewIntent
    onRestart
     onStart
    onResume*/

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initNoNetworkLayout();
        if (getIntent() != null) {
            int currentFragmentIndex = getIntent().getIntExtra("index", 0);
            int indexChilde = getIntent().getIntExtra("indexChild", 0);
            if (currentFragmentIndex != this.fragmentIndex) {
                vp.setCurrentItem(currentFragmentIndex);
                this.fragmentIndex = currentFragmentIndex;
            }
            if (currentFragmentIndex == 0) {
                if (indexChilde != this.orderstype) {
                    this.orderstype = indexChilde;
                }
                f2.refreshCurrentTab(orderstype, false);
            } else {
                if (indexChilde != this.refundindex) {
                    this.refundindex = indexChilde;
                }
                f3.refreshCurrentTab(refundindex, false);
            }

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //刷新列表，当前页正在显示则重刷，不是则另选
        if (fragmentIndex == 0) {
            f2.refreshCurrentTab(orderstype, true);
        } else {
            f3.refreshCurrentTab(refundindex, true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefundType messageEvent) {
        refundindex = messageEvent.getRefundType();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OrderType messageEvent) {
        orderstype = messageEvent.getTabPosition();
    }
}
