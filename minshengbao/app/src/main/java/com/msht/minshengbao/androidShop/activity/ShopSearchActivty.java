package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.MyApplication;
import com.msht.minshengbao.R;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.androidShop.adapter.HotSearchAdapter;
import com.msht.minshengbao.androidShop.adapter.MoreGoodAdapter;
import com.msht.minshengbao.androidShop.adapter.MyHaveHeadViewRecyclerAdapter;
import com.msht.minshengbao.androidShop.adapter.ShopHisSearchAdapter;
import com.msht.minshengbao.androidShop.adapter.ShopHotSearchAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.util.AppUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.ShopSharePreferenceUtil;
import com.msht.minshengbao.androidShop.viewInterface.IShopSearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopSearchActivty extends ShopBaseActivity implements IShopSearchView {
    @BindView(R.id.tvSearchD)
    EditText et;
    @BindView(R.id.gv)
   /* MyNoScrollGridView gv;*/
    RecyclerView gv;
    @BindView(R.id.lv)
    ListViewForScrollView lv;
    @BindView(R.id.tv_his)
    TextView tvHis;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    @BindView(R.id.clear)
    ImageView tvClear;
/*    private ShopHotSearchAdapter hotAdpter;*/
    private ShopHisSearchAdapter hisAdapter;
    private LinearLayout footView;
    private List<String> list=new ArrayList<String>();
    private HotSearchAdapter ad;

    @Override
    protected void setLayout() {
        setContentView(R.layout.shop_search);
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
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                et.setHint("搜索商品，品牌");
            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchKeyWord = et.getText().toString();
                    MyApplication.getInstance().addSearchHis(searchKeyWord);
                    if(getIntent().getIntExtra("main",0)==1){
                        startSearchFormMain();
                    }else {
                        startSearch();
                    }
                    return true;
                }
                return false;
            }
        });
     /*
      //gridview 在item出现宽高长度不一的时候显示有截断
      hotAdpter= new ShopHotSearchAdapter(this);
        gv.setAdapter(hotAdpter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et.setText(hotAdpter.getmDatas().get(position));
                MyApplication.getInstance().addSearchHis(et.getText().toString());
                if(getIntent().getIntExtra("main",0)==1){
                    startSearchFormMain();
                }else {
                    startSearch();
                }
            }
        });*/
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gv.setNestedScrollingEnabled(false);
        gv.setLayoutManager(gridLayoutManager);
        ad = new HotSearchAdapter(this);
        ad.setDatas(list);
        ad.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                et.setText(list.get(position));
                MyApplication.getInstance().addSearchHis(et.getText().toString());
                if(getIntent().getIntExtra("main",0)==1){
                    startSearchFormMain();
                }else {
                    startSearch();
                }
            }
        });
        gv.setAdapter(ad);
        hisAdapter = new ShopHisSearchAdapter(this, new ShopHisSearchAdapter.DeleteItemListener() {
            @Override
            public void deleteItem(int position) {
                MyApplication.getInstance().getList().remove(position);
                if( MyApplication.getInstance().getList().size()==0){
                    tvHis.setVisibility(View.GONE);
                    footView.setVisibility(View.INVISIBLE);
                }else{
                    tvHis.setVisibility(View.VISIBLE);
                }
                hisAdapter.notifyDataSetChanged();
            }
        });
        footView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_clear_search_history, null);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().getList().clear();
                tvHis.setVisibility(View.GONE);
                footView.setVisibility(View.INVISIBLE);
                hisAdapter.notifyDataSetChanged();
            }
        });
        lv.addFooterView(footView);
        lv.setAdapter(hisAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et.setText( MyApplication.getInstance().getList().get(position));
                String searchKeyWord = et.getText().toString();
                MyApplication.getInstance().addSearchHis(searchKeyWord);
                if(getIntent().getIntExtra("main",0)==1){
                    startSearchFormMain();
                }else {
                startSearch();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TextUtils.isEmpty(getKey())) {
            ShopPresenter.getSearchSuccess(this);
        }else {
            ShopPresenter.getSearchSuccess(this,getKey());
        }
    }

    private void startSearch() {
        Intent intent = new Intent();
        intent.putExtra("keyword", et.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
    private void startSearchFormMain(){
        Intent intent = new Intent(ShopSearchActivty.this,ShopKeywordListActivity.class);
        intent.putExtra("keyword", et.getText().toString());
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                startSearch();
                break;
            default:
                break;
        }
    }


    @Override
    public void onGetDefaultSuccess(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("datas");
            String array = data.optString("list");
            List<String> mapListJson = JsonUtil.jsonArrayToList(array);
           /* hotAdpter.setmDatas(mapListJson);
            hotAdpter.notifyDataSetChanged();*/
           list.clear();
           list.addAll(mapListJson);
           ad.notifyDataSetChanged();
           /* String hisArray = data.optString("his_list");
            List<String> hisHist = JsonUtil.jsonArrayToList(hisArray);*/
            hisAdapter.setmDatas(MyApplication.getInstance().getList());
            if( MyApplication.getInstance().getList().size()==0){
                tvHis.setVisibility(View.GONE);
                footView.setVisibility(View.INVISIBLE);
            }else{
                tvHis.setVisibility(View.VISIBLE);
            }
            hisAdapter.notifyDataSetChanged();
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

}
