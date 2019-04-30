package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.GlideUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IAddCollectStoreView;
import com.msht.minshengbao.androidShop.viewInterface.IDelCollectStoreView;
import com.msht.minshengbao.androidShop.viewInterface.IShopStoreJingle;
import com.msht.minshengbao.androidShop.viewInterface.OnDissmissLisenter;
import com.msht.minshengbao.functionActivity.myActivity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class ShopStoreJingle extends ShopBaseActivity implements IShopStoreJingle, IDelCollectStoreView, IAddCollectStoreView {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.collect)
    ImageView collect;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.leixin)
    TextView leixing;
    @BindView(R.id.companyname)
    TextView compantname;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.storeleixin)
    TextView tvStoreLeixing;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String storeid;
    private boolean is_favorate;

    @Override
    protected void setLayout() {
        setContentView(R.layout.shop_store_jingle);
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
        storeid = getIntent().getStringExtra("id");
        mToolbar.setPadding(0, ImmersionBar.getStatusBarHeight(this),0,0);
        ShopPresenter.getStoreJingle(this);
    }

    @Override
    public void onDeleteStoreCollect(String s) {
        collect.setImageDrawable(getResources().getDrawable(R.drawable.store_uncollect));
        is_favorate=false;
        PopUtil.showAutoDissHookDialog(this, "取消收藏店铺成功", 0);
    }

    @Override
    public void onAddStoreCollect(String s) {
        collect.setImageDrawable(getResources().getDrawable(R.drawable.store_collect));
        is_favorate=true;
        PopUtil.showAutoDissHookDialog(this, "取消收藏店铺成功", 0);
    }

    @Override
    public String getStoreId() {
        return storeid;
    }

    @Override
    public void onGetStoreJingleClass(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject store_info = obj.optJSONObject("datas").optJSONObject("store_info");
            String store_avatar = store_info.optString("store_avatar");
            GlideUtil.loadRemoteCircleImg(this,iv,store_avatar);
            String store_name = store_info.optString("store_name");
            name.setText(store_name);
            String sc_name = store_info.optString("sc_name");
            leixing.setText(sc_name);
            String store_company_name = store_info.optString("store_company_name");
            compantname.setText(store_company_name);
            String area_info = store_info.optString("area_info");
            location.setText(area_info);
            String store_time_text = store_info.optString("store_time_text");
            time.setText(store_time_text);
            if(TextUtils.isEmpty(store_info.optString("grade_name"))||TextUtils.equals(store_info.optString("grade_name"),"null")){
                if(store_info.optBoolean("is_own_shop")){
                    tvStoreLeixing.setText("平台自营");
                }else{
                    tvStoreLeixing.setText("普通店铺");
                }
            }else {
                tvStoreLeixing.setText(store_info.optString("grade_name"));
            }
            if(store_info.optBoolean("is_favorate")){
                collect.setImageDrawable(getResources().getDrawable(R.drawable.store_collect));
                is_favorate= true;
            }else {
                collect.setImageDrawable(getResources().getDrawable(R.drawable.store_uncollect));
                is_favorate= false;
            }
            collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(getKey())){
                    if(is_favorate){
                        ShopPresenter.delCollectStore(ShopStoreJingle.this);
                    }else {
                        ShopPresenter.addCollectStore(ShopStoreJingle.this);
                    }
                } else {
                      startActivity(new Intent(ShopStoreJingle.this, LoginActivity.class));
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}