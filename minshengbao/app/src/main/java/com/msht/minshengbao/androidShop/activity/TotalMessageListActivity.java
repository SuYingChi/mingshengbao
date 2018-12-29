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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lxy.dexlibs.ComplexRecyclerViewAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.androidShop.adapter.MsgUserListAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.MessagePreviewBean;
import com.msht.minshengbao.androidShop.shopBean.ShopChatUserBean;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IDeleteMsgUserItemView;
import com.msht.minshengbao.androidShop.viewInterface.IGetChatUserListView;
import com.msht.minshengbao.androidShop.viewInterface.IMessagePreView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TotalMessageListActivity extends ShopBaseActivity implements OnRefreshListener, IGetChatUserListView, IDeleteMsgUserItemView, IMessagePreView {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.rlt_warn)
    RelativeLayout rltWarn;
    @BindView(R.id.warntv)
    TextView tvWarn;
    @BindView(R.id.warntime)
    TextView warnTime;
    @BindView(R.id.warnnum)
    TextView warnnum;
    @BindView(R.id.rlt_gas)
    RelativeLayout rltGas;
    @BindView(R.id.gastv)
    TextView tvgas;
    @BindView(R.id.gastime)
    TextView gasTime;
    @BindView(R.id.gasnum)
    TextView gasnum;
    @BindView(R.id.rlt_route)
    RelativeLayout rltRoute;
    @BindView(R.id.routetv)
    TextView tvroute;
    @BindView(R.id.routetime)
    TextView routetime;
    @BindView(R.id.routenum)
    TextView routenum;
    @BindView(R.id.rlt_youhui)
    RelativeLayout rltYouhui;
    @BindView(R.id.youhuitv)
    TextView tvyouhui;
    @BindView(R.id.youhuitime)
    TextView youhuitime;
    @BindView(R.id.youhuinum)
    TextView youhuinum;
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
                ShopPresenter.deleteMsgUserItem(TotalMessageListActivity.this, dataList.get(pson).getU_id());
            }
        });

        adapter.setOnItemClickListener(new ComplexRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pson, ComplexRecyclerViewAdapter.ComplexViewHolder holder, Object bean) {
                TotalMessageListActivity.this.position = pson;
                Intent intent = new Intent(TotalMessageListActivity.this, ShopkefuActivity.class);
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
        ShopPresenter.getMessagePreview(this, SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, ""), SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, ""));
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
        PopUtil.showAutoDissHookDialog(this, "删除聊天记录", 100);
        dataList.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onGetMessagePreviewSuccess(MessagePreviewBean s) {
        List<MessagePreviewBean.DataBean> list = s.getData();
        ArrayList<Integer> types = new ArrayList<Integer>();
        for (final MessagePreviewBean.DataBean bean : list) {
            String content = bean.getContent();
            int id = bean.getId();
            String time = bean.getTime();
            int num = bean.getUnread_num();
            final int type = bean.getType();
            types.add(type);
            switch (type) {
                //燃气服务
                case 1:
                    tvgas.setText(content);
                    gasTime.setText(time);
                    if(num==0){
                        gasnum.setVisibility(View.INVISIBLE);
                    }else {
                        gasnum.setText(num+"");
                        gasnum.setVisibility(View.VISIBLE);
                    }
                    rltGas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =  new Intent(TotalMessageListActivity.this,MessageListActivity.class);
                            intent.putExtra("type",type);
                            startActivity(intent);
                        }
                    });
                    break;
                //紧急通知
                case 2:
                    tvWarn.setText(content);
                    warnTime.setText(time);
                    if(num==0){
                        warnnum.setVisibility(View.INVISIBLE);
                    }else {
                        warnnum.setText(num+"");
                        warnnum.setVisibility(View.VISIBLE);
                    }
                    rltWarn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           Intent intent =  new Intent(TotalMessageListActivity.this,MessageListActivity.class);
                            intent.putExtra("type",type);
                           startActivity(intent);
                        }
                    });
                    break;
                //物流助手
                case 3:
                    tvroute.setText(content);
                    routetime.setText(time);
                    if(num==0){
                        routenum.setVisibility(View.INVISIBLE);
                    }else {
                        routenum.setText(num+"");
                        routenum.setVisibility(View.VISIBLE);
                    }
                    rltRoute.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =  new Intent(TotalMessageListActivity.this,MessageListActivity.class);
                            intent.putExtra("type",type);
                            startActivity(intent);
                        }
                    });
                    break;
                //优惠促销
                case 4:
                    tvyouhui.setText(content);
                    youhuitime.setText(time);
                    if(num==0){
                        youhuinum.setVisibility(View.INVISIBLE);
                    }else {
                        youhuinum.setText(num+"");
                        youhuinum.setVisibility(View.VISIBLE);
                    }
                    rltYouhui.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent =  new Intent(TotalMessageListActivity.this,MessageListActivity.class);
                            intent.putExtra("type",type);
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
        if(types.contains(1)){
            rltGas.setVisibility(View.VISIBLE);
        }else {
            rltGas.setVisibility(View.GONE);
        }
        if(types.contains(2)){
            rltWarn.setVisibility(View.VISIBLE);
        }else {
            rltWarn.setVisibility(View.GONE);
        }
        if(types.contains(3)){
            rltRoute.setVisibility(View.VISIBLE);
        }else {
            rltRoute.setVisibility(View.GONE);
        }
        if(types.contains(4)){
            rltYouhui.setVisibility(View.VISIBLE);
        }else {
            rltYouhui.setVisibility(View.GONE);
        }
    }
}
