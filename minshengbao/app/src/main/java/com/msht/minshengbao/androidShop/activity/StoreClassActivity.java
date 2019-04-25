package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.androidShop.adapter.StoreClassAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.StoreClassBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.viewInterface.IStoreClassView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StoreClassActivity extends ShopBaseActivity implements IStoreClassView, StoreClassAdapter.StoreClassAdapterInterface {
    private String storeId;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.tvSearchD)
    EditText et;
    List<StoreClassBean> list = new ArrayList<StoreClassBean>();
    private StoreClassAdapter adapter;

    @Override
    protected void setLayout() {
        setContentView(R.layout.store_class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeId = getIntent().getStringExtra("id");
        mToolbar.setPadding(0, StatusBarCompat.getStatusBarHeight(this),0,0);
        LinearLayoutManager lm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(lm);
        adapter = new StoreClassAdapter(this, R.layout.item_store_class, list);
        adapter.setStoreClassAdapterInterface(this);
        rcl.setAdapter(adapter);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchKeyWord = et.getText().toString();
                    MyApplication.getInstance().addSearchHis(searchKeyWord);
                    Intent intent = new Intent(StoreClassActivity.this,StoreSearchGoodListActivity.class);
                    intent.putExtra("storeid",storeId);
                    intent.putExtra("keyword",searchKeyWord);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        ShopPresenter.getStoreClass(this);
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public void onGetStoreClass(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject datas = obj.optJSONObject("datas");
            JSONArray store_goods_class = datas.optJSONArray("store_goods_class");
             StoreClassBean storeClassbean = null;
            for(int i=0;i<store_goods_class.length();i++){
                JSONObject bean = store_goods_class.optJSONObject(i);
                if(bean.optString("level").equals("1")){
                    storeClassbean = new StoreClassBean();
                    storeClassbean.setTopBean(JsonUtil.toBean(bean.toString(),StoreClassBean.ChildStoreClassBean.class));
                    list.add(storeClassbean);
                }else if(bean.optString("level").equals("2")){
                    if (storeClassbean != null) {
                        if(storeClassbean.getChildList()==null) {
                            List<StoreClassBean.ChildStoreClassBean> childlist = new ArrayList<>();
                            childlist.add(JsonUtil.toBean(bean.toString(), StoreClassBean.ChildStoreClassBean.class));
                            storeClassbean.setChildList(childlist);
                        }else {
                           storeClassbean.getChildList().add(JsonUtil.toBean(bean.toString(), StoreClassBean.ChildStoreClassBean.class));
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCLick(String id) {
        Intent intent = new Intent(this, StoreSearchGoodListActivity.class);
        intent.putExtra("stc_id",id);
        intent.putExtra("storeid",storeId);
        startActivity(intent);
    }
}
