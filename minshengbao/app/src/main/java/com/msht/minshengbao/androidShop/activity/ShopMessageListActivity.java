package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.lxy.dexlibs.ComplexRecyclerViewAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.MsgUserListAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.ShopChatUserBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IDeleteMsgUserItemView;
import com.msht.minshengbao.androidShop.viewInterface.IGetChatUserListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopMessageListActivity extends ShopBaseActivity implements OnRefreshListener, IGetChatUserListView, IDeleteMsgUserItemView {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    public int position;
    List<ShopChatUserBean> dataList = new ArrayList<ShopChatUserBean>();
    private MsgUserListAdapter adapter;

    @Override
    protected void setLayout() {
        setContentView(R.layout.shop_msg_list);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MsgUserListAdapter(this, dataList);
        rcl.setLayoutManager(new LinearLayoutManager(this));
        adapter.addBtn(R.layout.delete_btn_layout, new ComplexRecyclerViewAdapter.OnItemBtnClickListener() {
            @Override
            public void onItemBtnClickListener(int pson, ComplexRecyclerViewAdapter.ComplexViewHolder holder, Object bean) {
                ShopPresenter.deleteMsgUserItem(ShopMessageListActivity.this, dataList.get(pson).getU_id());
            }
        });

        adapter.setOnItemClickListener(new ComplexRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pson, ComplexRecyclerViewAdapter.ComplexViewHolder holder, Object bean) {
                ShopMessageListActivity.this.position = pson;
                Intent intent = new Intent(ShopMessageListActivity.this, ShopkefuActivity.class);
                intent.putExtra("t_id", dataList.get(pson).getU_id());
                startActivity(intent);
            }
        });
        rcl.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShopPresenter.getChatUserList(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ShopPresenter.getChatUserList(this);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).navigationBarColor(R.color.black).navigationBarWithKitkatEnable(false).init();
        ImmersionBar.setTitleBar(this, mToolbar);
    }

    @Override
    public void onGetChatUserListSuccess(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        try {
            JSONObject list = new JSONObject(s).optJSONObject("datas").optJSONObject("list");
        List<String> memberIdList = JsonUtil.getJsonObjectKeyList(list);
        dataList.clear();
        for (String memberId : memberIdList) {
            ShopChatUserBean shopChatUserBean = JsonUtil.toBean(list.optJSONObject(memberId).toString(), ShopChatUserBean.class);
            dataList.add(shopChatUserBean);
        }
        adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeleteMsgUserItemSuccess(String s) {
        PopUtil.showAutoDissHookDialog(this,"删除聊天记录",100);
        dataList.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }
}
