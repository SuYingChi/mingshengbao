package com.msht.minshengbao.androidShop.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.widget.PopupMenu;
import com.msht.minshengbao.androidShop.Fragment.DetailFragment;
import com.msht.minshengbao.androidShop.Fragment.GoodEvaluationFragment;
import com.msht.minshengbao.androidShop.Fragment.GoodFragment;
import com.msht.minshengbao.androidShop.adapter.BaseLazyFragmentPagerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.customerview.AddCarOrBuyGoodDialog;
import com.msht.minshengbao.androidShop.customerview.NoScrollViewPager;
import com.msht.minshengbao.androidShop.event.GoShopMainEvent;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.viewInterface.GoodDetailActivityListener;
import com.msht.minshengbao.androidShop.viewInterface.IWarnMessageDetailView;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopGoodDetailActivity extends ShopBaseActivity implements GoodDetailActivityListener, IWarnMessageDetailView {
    private String type;
    private String data;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.vp)
    NoScrollViewPager vp;
    @BindView(R.id.ll_kefu)
    LinearLayout llask;
    @BindView(R.id.ll_car)
    LinearLayout llcar;
    @BindView(R.id.add_car)
    RelativeLayout rltAddCar;
    @BindView(R.id.pay)
    RelativeLayout pay;
    @BindView(R.id.car_num)
    TextView tvCarNum;
    private List<ShopBaseLazyFragment> list = new ArrayList<ShopBaseLazyFragment>();
    private final String[] titles = {"商品", "详情", "评价"};
    private GoodFragment f0;
    private DetailFragment f1;
    private GoodEvaluationFragment f2;
    private AddCarOrBuyGoodDialog addCarOrBuyGoodDialog;
    private Intent carListIntent;
    private float toolbarAlpha;
    private Drawable currentLeftDrawblw;
    private Drawable currentRightDrawblw;

    @Override
    protected void setLayout() {
        setContentView(R.layout.shop_good_detail);
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this).titleBar(R.id.toolbar).keyboardEnable(true).init();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        data = intent.getStringExtra("goodsid");
        for (String title : titles) {
            tab.addTab(tab.newTab().setText(title));
        }
        tab.getTabAt(0).select();
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        vp.setCurrentItem(0);
                          mToolbar.setAlpha(toolbarAlpha);
                        break;
                    case 1:
                        vp.setCurrentItem(1);
                          mToolbar.setAlpha(1);

                        break;
                    case 2:
                        vp.setCurrentItem(2);
                          mToolbar.setAlpha(1);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        f0 = new GoodFragment();
        Bundle bun = new Bundle();
        bun.putString("goodsid", data);
        bun.putString("type", type);
        f0.setArguments(bun);
        f1 = new DetailFragment();
        f1.setArguments(bun);
        f2 = new GoodEvaluationFragment();
        f2.setArguments(bun);
        list.add(f0);
        list.add(f1);
        list.add(f2);
        vp.setAdapter(new BaseLazyFragmentPagerAdapter(getSupportFragmentManager(), list));
        vp.setCurrentItem(0);
        mToolbar.setAlpha(0);
        vp.setOffscreenPageLimit(3);
        vp.setNoScroll(true);
        vp.setPageTransformer(true, null);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initNoNetworkLayout();
        carListIntent = new Intent(this, ShopCarActivity.class);
        mToolbar.setAlpha(1);
        RelativeLayout.LayoutParams blp = (RelativeLayout.LayoutParams) back.getLayoutParams();
        blp.setMargins(0,ImmersionBar.getStatusBarHeight(this),0,0);
        back.setLayoutParams(blp);
        RelativeLayout.LayoutParams mlp = (RelativeLayout.LayoutParams) menu.getLayoutParams();
        mlp.setMargins(0,ImmersionBar.getStatusBarHeight(this),0,0);
        menu.setLayoutParams(mlp);
        currentLeftDrawblw = getResources().getDrawable(R.drawable.back2x);
        currentRightDrawblw = getResources().getDrawable(R.drawable.menu);
        String msgid = getIntent().getIntExtra("msgid", 0) + "";
        if(!msgid.equals("0")) {
            ShopPresenter.getMessageDetail(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""), msgid);
        }
    }


    @OnClick({R.id.back, R.id.menu, R.id.ll_kefu, R.id.ll_car, R.id.add_car, R.id.pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.menu:
                clickMenu();
                break;
            case R.id.ll_kefu:
                askKefu();
                break;
            case R.id.ll_car:
                goCarListActivity();
                break;
            case R.id.pay:
                showAddCarDialog();
                break;
            case R.id.add_car:
                showAddCarDialog();
            default:
                break;
        }
    }

    private void showAddCarDialog() {
        if (!isFinishing() && addCarOrBuyGoodDialog == null) {
            addCarOrBuyGoodDialog = new AddCarOrBuyGoodDialog(this, f0);
            addCarOrBuyGoodDialog.show();
        } else if (!isFinishing() && !addCarOrBuyGoodDialog.isShowing()) {
            addCarOrBuyGoodDialog.show();
        }
    }

    private void goCarListActivity() {
        if (TextUtils.isEmpty(getKey())) {
            startActivity(new Intent(this,NoLoginCarActivity.class));
        }else if(Integer.valueOf(tvCarNum.getText().toString())<=0){
            startActivity(new Intent(this,NoCarActivity.class));
        }
        else {
            startActivity(carListIntent);
        }
    }


    private void askKefu() {
        if(!TextUtils.isEmpty(getKey())) {
            Intent intent = new Intent(this, ShopkefuActivity.class);
            intent.putExtra("t_id", f0.getTid());
            startActivity(intent);
        }else {
            startActivity(new Intent(this,LoginActivity.class));
        }
    }

    private void clickMenu() {
        String[] abs = new String[]{"消息", "首页"};
        PopupMenu mPopupMenu = new PopupMenu(this, abs, R.layout.shop_good_detail_popup_menu);
        // 设置弹出菜单弹出的位置
        mPopupMenu.showLocation(R.id.menu, getResources().getDimensionPixelSize(R.dimen.margin_width_70), getResources().getDimensionPixelSize(R.dimen.margin_width10));
        // 设置回调监听，获取点击事件
        mPopupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, int position) {
                if (position == 0) {
                    goMessage();
                } else if (position == 1) {
                    goShopHome();
                }
            }
        });
    }

    private void goShopHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("index",1);
        //EventBus.getDefault().postSticky(new GoShopMainEvent());
        startActivity(intent);
    }

    private void goMessage() {
        if(TextUtils.isEmpty(getKey())){
            startActivity(new Intent(this, LoginActivity.class));
        }else {
            startActivity(new Intent(this, TotalMessageListActivity.class));
        }
    }

    @Override
    public void onScrollChange1() {
        mToolbar.setVisibility(View.VISIBLE);
         mToolbar.setAlpha(1);
        toolbarAlpha = 1;
        back.setVisibility(View.VISIBLE);
        menu.setVisibility(View.VISIBLE);
        if(currentLeftDrawblw!=getResources().getDrawable(R.drawable.back2x)) {
            back.setImageDrawable(getResources().getDrawable(R.drawable.back2x));
            currentLeftDrawblw=getResources().getDrawable(R.drawable.back2x);
        }
        if(currentRightDrawblw!=getResources().getDrawable(R.drawable.menu)) {
            menu.setImageDrawable(getResources().getDrawable(R.drawable.menu));
            currentRightDrawblw=getResources().getDrawable(R.drawable.menu);
        }
    }

    @Override
    public void onScrollChange2(int y, int bannerHeight) {
        float alpha = (float) y / bannerHeight;
        LogUtils.e("alpha=== " + alpha + "      bannerHeight==" + bannerHeight + "      y==" + y);
        mToolbar.setAlpha(alpha);
        toolbarAlpha = alpha;
        back.setVisibility(View.VISIBLE);
        menu.setVisibility(View.VISIBLE);
        if(currentLeftDrawblw!=getResources().getDrawable(R.drawable.back2x)) {
            back.setImageDrawable(getResources().getDrawable(R.drawable.back2x));
            currentLeftDrawblw=getResources().getDrawable(R.drawable.back2x);
        }
        if(currentRightDrawblw!=getResources().getDrawable(R.drawable.menu)) {
            menu.setImageDrawable(getResources().getDrawable(R.drawable.menu));
            currentRightDrawblw=getResources().getDrawable(R.drawable.menu);
        }
    }

    @Override
    public void onScrollChange3() {
        mToolbar.setAlpha(1);
        toolbarAlpha = 1;
        back.setVisibility(View.VISIBLE);
        menu.setVisibility(View.VISIBLE);
        if(currentLeftDrawblw!=getResources().getDrawable(R.drawable.back2x)) {
            back.setImageDrawable(getResources().getDrawable(R.drawable.back2x));
            currentLeftDrawblw=getResources().getDrawable(R.drawable.back2x);
        }
        if(currentRightDrawblw!=getResources().getDrawable(R.drawable.menu)) {
            menu.setImageDrawable(getResources().getDrawable(R.drawable.menu));
            currentRightDrawblw=getResources().getDrawable(R.drawable.menu);
        }
    }

    @Override
    public void onScrollChange4() {
        mToolbar.setVisibility(View.INVISIBLE);
        back.setVisibility(View.VISIBLE);
        menu.setVisibility(View.VISIBLE);
        if(currentLeftDrawblw!=getResources().getDrawable(R.drawable.shop_good_detail_left_img)) {
            back.setImageDrawable(getResources().getDrawable(R.drawable.shop_good_detail_left_img));
            currentLeftDrawblw=getResources().getDrawable(R.drawable.shop_good_detail_left_img);
        }
        if(currentRightDrawblw!=getResources().getDrawable(R.drawable.shop_good_detail_right_img)) {
            menu.setImageDrawable(getResources().getDrawable(R.drawable.shop_good_detail_right_img));
            currentRightDrawblw=getResources().getDrawable(R.drawable.shop_good_detail_right_img);
        }
    }

    @Override
    public void onScrollChange5() {
        mToolbar.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        menu.setVisibility(View.VISIBLE);
        if(currentLeftDrawblw!=getResources().getDrawable(R.drawable.back2x)) {
            back.setImageDrawable(getResources().getDrawable(R.drawable.back2x));
            currentLeftDrawblw=getResources().getDrawable(R.drawable.back2x);
        }
        if(currentRightDrawblw!=getResources().getDrawable(R.drawable.menu)) {
            menu.setImageDrawable(getResources().getDrawable(R.drawable.menu));
            currentRightDrawblw=getResources().getDrawable(R.drawable.menu);
        }
    }

    @Override
    public void hasAddedCar(String totalAddedCarNum) {
        tvCarNum.setVisibility(View.VISIBLE);
        tvCarNum.setText(totalAddedCarNum);
    }

    @Override
    public void noAddedCar() {
        tvCarNum.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStorageChange(int goodsStorage) {
        if (goodsStorage <= 0) {
            rltAddCar.setBackgroundColor(getResources().getColor(R.color.shop_grey));
            pay.setBackgroundColor(getResources().getColor(R.color.shop_grey));
            rltAddCar.setEnabled(false);
            rltAddCar.setClickable(false);
            pay.setEnabled(false);
            pay.setClickable(false);
        } else {
            rltAddCar.setBackgroundColor(getResources().getColor(R.color.yellow_text));
            pay.setBackgroundColor(getResources().getColor(R.color.msb_color));
            rltAddCar.setEnabled(true);
            rltAddCar.setClickable(true);
            pay.setEnabled(true);
            pay.setClickable(true);
        }
    }

    @Override
    public void onGetGoodDetailSuccess() {
        if (!isFinishing() && addCarOrBuyGoodDialog != null&& addCarOrBuyGoodDialog.isShowing()) {
            addCarOrBuyGoodDialog.refreshData();
        }else {
            addCarOrBuyGoodDialog = new AddCarOrBuyGoodDialog(this, f0);
        }
    }

    @Override
    public void goEveluateFragment() {
        tab.getTabAt(2).select();
    }

    @Override
    public void showBottomDialog() {
        showAddCarDialog();
    }


    //打开别的页面后再跳回主页时回调
    @Override
    protected void onRestart() {
        super.onRestart();
        f0.refreshCarList();
    }

    @Override
    public void onGetDetailSuccess(String s) {

    }
}
