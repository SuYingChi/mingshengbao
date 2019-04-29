package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonObject;
import com.gyf.barlibrary.OSUtils;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.widget.PopupMenu;
import com.msht.minshengbao.adapter.ViewPagerAdapter;
import com.msht.minshengbao.androidShop.adapter.ShopStoreViewPagerAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.event.VerticalOffset;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.Imagebean;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.viewInterface.IStoreView;
import com.msht.minshengbao.functionActivity.MainActivity;
import com.msht.minshengbao.functionActivity.myActivity.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class ShopStoreMainActivity extends ShopBaseActivity implements IStoreView {
    private String storeId;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
   @BindView(R.id.head_layout)
    RelativeLayout headlayout;
   @BindView(R.id.storeleixin)
    TextView tvStoreLeixing;
   @BindView(R.id.storename)
   TextView tvStoreName;
   @BindView(R.id.collect)
    ImageView ivCollect;
   @BindView(R.id.app_bar_layout)
   AppBarLayout appBarLayout;
    @BindView(R.id.main_vp_container)
    ViewPager main_vp_container;
    @BindView(R.id.toolbar_tab)
    TabLayout tabLayout;
    @BindView(R.id.storefenlei)
    TextView tvStorefenlei;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.kefu)
    TextView tvKefu;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.storejieshao)
    TextView storejieshao;
    private String memberId;

    @Override
    protected void setLayout() {
     setContentView(R.layout.shop_store_main);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        storeId = getIntent().getStringExtra("id");
        int tabIndex = getIntent().getIntExtra("tabindex", 0);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("verticalOffset","====="+verticalOffset);
                EventBus.getDefault().post(new VerticalOffset(verticalOffset) );
            }
        });
        ShopStoreViewPagerAdapter vpAdapter = new ShopStoreViewPagerAdapter(getSupportFragmentManager(),storeId);
        main_vp_container.setAdapter(vpAdapter);
        main_vp_container.setOffscreenPageLimit(4);
        main_vp_container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener
                (tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener
                (main_vp_container));
        ShopPresenter.getStoreInfo(this);
        tvStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent inten =new Intent(ShopStoreMainActivity.this,ShopStoreJingle.class);
               inten.putExtra("id",storeId);
               startActivity(inten);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] abs = new String[]{"消息","购物车", "首页"};
                PopupMenu mPopupMenu = new PopupMenu(ShopStoreMainActivity.this, abs, R.layout.store_main_menu);
                // 设置弹出菜单弹出的位置
                mPopupMenu.showLocation(R.id.menu, getResources().getDimensionPixelSize(R.dimen.margin_width_70), -getResources().getDimensionPixelSize(R.dimen.margin_15));
                // 设置回调监听，获取点击事件
                mPopupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
                    @Override
                    public void onClick(PopupMenu.MENUITEM item, int position) {
                        if (position == 0) {
                            if(TextUtils.isEmpty(getKey())){
                                startActivity(new Intent(ShopStoreMainActivity.this, LoginActivity.class));
                            }else {
                                startActivity(new Intent(ShopStoreMainActivity.this, TotalMessageListActivity.class));
                            }
                        } else if (position == 1) {
                            if(TextUtils.isEmpty(getKey())){
                                startActivity(new Intent(ShopStoreMainActivity.this, LoginActivity.class));
                            }else {
                                Intent intent = new Intent(ShopStoreMainActivity.this, ShopCarActivity.class);
                                //EventBus.getDefault().postSticky(new GoShopMainEvent());
                                startActivity(intent);
                            }
                        }else if(position==2){
                            Intent intent = new Intent(ShopStoreMainActivity.this, MainActivity.class);
                            intent.putExtra("index",1);
                            //EventBus.getDefault().postSticky(new GoShopMainEvent());
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        tvKefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(getKey())) {
                    Intent intent = new Intent(ShopStoreMainActivity.this, ShopkefuActivity.class);
                    intent.putExtra("t_id", memberId);
                    startActivity(intent);
                }else {
                    startActivity(new Intent(ShopStoreMainActivity.this,LoginActivity.class));
                }
            }
        });
    }

    @Override
    public String getStoreId() {
        return storeId;
    }
    @Override
    public void onGetStoreInfoSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject datas = obj.optJSONObject("datas");
            JSONObject storeInfo = datas.optJSONObject("store_info");
            memberId = storeInfo.optString("member_id");
            storeInfo.optString("store_avatar");
            if(TextUtils.isEmpty(storeInfo.optString("grade_name"))||TextUtils.equals(storeInfo.optString("grade_name"),"null")){
                if(storeInfo.optBoolean("is_own_shop")){
                    tvStoreLeixing.setText("平台自营");
                }else{
                    tvStoreLeixing.setText("普通店铺");
                }
            }else {
                tvStoreLeixing.setText(storeInfo.optString("grade_name"));
            }
            tvStoreName.setText(storeInfo.optString("store_name"));
            if(TextUtils.isEmpty(storeInfo.optString("mb_title_img"))){
                headlayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.store_head));
            }else {
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(R.drawable.icon_stub)
                        .error(R.drawable.icon_stub)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .priority(Priority.HIGH);
                Glide.with(this)
                        .load(storeInfo.optString("mb_title_img"))
                        .thumbnail(0.5f)
                        .apply(options)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                headlayout.setBackgroundDrawable(resource);
                            }
                        });
            }
            if(storeInfo.optBoolean("is_favorate")){
               ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.store_collect));
            }else {
                ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.store_uncollect));
            }
            tvStorefenlei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShopStoreMainActivity.this, StoreClassActivity.class);
                    intent.putExtra("id",storeId);
                    startActivity(intent);

                }
            });
            storejieshao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShopStoreMainActivity.this, ShopStoreJingle.class);
                    intent.putExtra("id",storeId);
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
