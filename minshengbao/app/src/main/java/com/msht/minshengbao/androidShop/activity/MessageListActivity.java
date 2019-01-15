package com.msht.minshengbao.androidShop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxy.dexlibs.ComplexRecyclerViewAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.androidShop.adapter.MessageListAdapter2;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.WarnBean;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IDeleteMessageItemView;
import com.msht.minshengbao.androidShop.viewInterface.IWarnListView;
import com.msht.minshengbao.androidShop.viewInterface.OnDissmissLisenter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MessageListActivity extends ShopBaseActivity implements OnRefreshLoadMoreListener, OnRefreshListener, IWarnListView, IDeleteMessageItemView {
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.back)
    ImageView ivback;
    @BindView(R.id.title)
    TextView tvtitle;
    private List<WarnBean.DataBean> dataList = new ArrayList<WarnBean.DataBean>();
    /*   private MessageListAdapter adapter;*/
    private MessageListAdapter2 adapter;
    private int page = 1;
    private String type;
    private int lastPage = -1;
    private boolean onRestart = false;
    private int deletepson;


    @Override
    protected void setLayout() {
        setContentView(R.layout.warn_list);
    }

    @Override
    protected void initImmersionBar() {

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
        type = getIntent().getIntExtra("type", 0) + "";
        rcl.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        if (type.equals("1")) {
            tvtitle.setText("燃气服务");
            //  adapter = new MessageListAdapter(this, R.layout.item_warn);
            adapter = new MessageListAdapter2(this, dataList, 1);
            adapter.addBtn(R.layout.delete_btn_message, new ComplexRecyclerViewAdapter.OnItemBtnClickListener() {
                @Override
                public void onItemBtnClickListener(int pson, ComplexRecyclerViewAdapter.ComplexViewHolder holder, Object bean) {
                    deletepson = pson;
                    ShopPresenter.deleteMessageItem(MessageListActivity.this, SharedPreferencesUtil.getUserId(MessageListActivity.this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(MessageListActivity.this, SharedPreferencesUtil.Password, ""), dataList.get(pson).getId() + "");
                }
            });
        } else if (type.equals("2")) {
            tvtitle.setText("紧急通知");
            //    adapter = new MessageListAdapter(this, R.layout.item_warn);
            adapter = new MessageListAdapter2(this, dataList, 2);
        } else if (type.equals("3")) {
            tvtitle.setText("物流助手");
            //   adapter = new MessageListAdapter(this, R.layout.item_wuliu);
            adapter = new MessageListAdapter2(this, dataList, 3);
        } else {
            tvtitle.setText("优惠促销");
            // adapter = new MessageListAdapter(this, R.layout.item_youhui);
            adapter = new MessageListAdapter2(this, dataList, 4);
        }
      /*  adapter.setOnItemClickListener(new MyHaveHeadViewRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (type.equals("2")) {
                    Intent intent = new Intent(MessageListActivity.this, WarnMessageDetailActivity.class);
                    intent.putExtra("id", dataList.get(position).getId() + "");
                    startActivity(intent);
                } else if (type.equals("3")) {
                    Intent intent = new Intent(MessageListActivity.this, ShopOrderRouteActivity.class);
                    intent.putExtra("id", position + "");
                    startActivity(intent);
                }
            }
        });*/
        // adapter.setDatas(dataList);
        adapter.setOnItemClickListener(new ComplexRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int i, ComplexRecyclerViewAdapter.ComplexViewHolder complexViewHolder, Object o) {
                if (type.equals("2")) {
                    Intent intent = new Intent(MessageListActivity.this, WarnMessageDetailActivity.class);
                    intent.putExtra("id", dataList.get(i).getId() + "");
                    startActivity(intent);
                } else if (type.equals("3")) {
                    try {
                        JSONObject obj = new JSONObject(dataList.get(i).getContent());
                        Intent intent = new Intent(MessageListActivity.this, ShopOrderRouteActivity.class);
                        intent.putExtra("id", obj.optString("order_id"));
                        intent.putExtra("msgid", dataList.get(i).getId());
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (type.equals("4")) {
                    try {
                        JSONObject obj = new JSONObject(dataList.get(i).getContent());
                        Intent intent = new Intent(MessageListActivity.this, ShopKeywordListActivity.class);
                        intent.putExtra("keyword", obj.optString("log_type_v"));
                        intent.putExtra("msgid", dataList.get(i).getId());
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        rcl.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        page = 1;
        ShopPresenter.getMessageList(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""), type);

    }

    @Override
    protected void onStop() {
        super.onStop();
        lastPage = page;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onRestart = true;
        page = 1;
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        ShopPresenter.getMessageList(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""), type);

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        ShopPresenter.getMessageList(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""), type);

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        refreshLayout.setNoMoreData(false);
        refreshLayout.setEnableAutoLoadMore(true);
        ShopPresenter.getMessageList(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""), type);

    }

    @Override
    public String getPage() {
        return page + "";
    }

    @Override
    public void onGetWarnListSuccess(WarnBean bean) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (!onRestart) {
            if (page == 1) {
                dataList.clear();
                dataList.addAll(bean.getData());
                adapter.notifyDataSetChanged();
            } else if (bean.getData().size() != 0) {
                dataList.addAll(bean.getData());
                adapter.notifyDataSetChanged();

            }
        } else {
            if (page == 1) {
                dataList.clear();
                dataList.addAll(bean.getData());
                if (page<lastPage) {
                    page++;
                    ShopPresenter.getMessageList(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""), type);
                }else {
                    adapter.notifyDataSetChanged();
                    onRestart = false;
                }
            } else if (page < lastPage && bean.getData().size() != 0) {
                dataList.addAll(bean.getData());
                page++;
                ShopPresenter.getMessageList(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""), type);
            } else if (page == lastPage && bean.getData().size() != 0) {
                dataList.addAll(bean.getData());
                adapter.notifyDataSetChanged();
                onRestart = false;
            }
        }
    }

    @Override
    public void onDeleteMsgItemSuccess(String s) {
        PopUtil.showAutoDissHookDialog(this, "删除成功", 0, new OnDissmissLisenter() {
            @Override
            public void onDissmiss() {
                dataList.remove(deletepson);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
