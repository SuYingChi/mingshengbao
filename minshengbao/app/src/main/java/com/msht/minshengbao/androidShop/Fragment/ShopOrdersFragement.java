package com.msht.minshengbao.androidShop.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.androidShop.ShopConstants;
import com.msht.minshengbao.androidShop.activity.RefundALLActivity;
import com.msht.minshengbao.androidShop.activity.ShopOrderAddEveluateActivity;
import com.msht.minshengbao.androidShop.activity.ShopOrderEveluateActivity;
import com.msht.minshengbao.androidShop.activity.ShopOrderRouteActivity;
import com.msht.minshengbao.androidShop.activity.ShopOrdersDetailActivity;
import com.msht.minshengbao.androidShop.activity.ShopPayOrderActivity;
import com.msht.minshengbao.androidShop.activity.ShopSuccessActivity;
import com.msht.minshengbao.androidShop.adapter.ShopOrdersListAdapter;
import com.msht.minshengbao.androidShop.basefragment.ShopBaseLazyFragment;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.BuyStep3PayListBean;
import com.msht.minshengbao.androidShop.shopBean.MyExtendOrderGoodsBean;
import com.msht.minshengbao.androidShop.shopBean.OrdersItem;
import com.msht.minshengbao.androidShop.shopBean.OrderslistBean;
import com.msht.minshengbao.androidShop.shopBean.ZengpingBean;
import com.msht.minshengbao.androidShop.util.DrawbleUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.viewInterface.IBuyStep3GetPayListView;
import com.msht.minshengbao.androidShop.viewInterface.ICancelOrderView;
import com.msht.minshengbao.androidShop.viewInterface.IDeleteOrderView;
import com.msht.minshengbao.androidShop.viewInterface.IOrderQrCodeView;
import com.msht.minshengbao.androidShop.viewInterface.IReceivedOrderView;
import com.msht.minshengbao.androidShop.viewInterface.IShopOrdersView;

import butterknife.BindView;

import com.msht.minshengbao.functionActivity.MyActivity.LoginActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopOrdersFragement extends ShopBaseLazyFragment implements IShopOrdersView, OnRefreshListener, OnLoadMoreListener, ShopOrdersListAdapter.OrdersListListener, IDeleteOrderView, IReceivedOrderView, ICancelOrderView, IOrderQrCodeView, IBuyStep3GetPayListView {
    private static final int MY_PERMISSIONS_REQUEST = 200;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.iv_no_data)
    ImageView ivNoOrder;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    private ShopOrdersListAdapter adapter;
    private List<OrderslistBean> ordersList = new ArrayList<OrderslistBean>();
    private final String[] titles = {"全部", "待付款", "待发货", "待收货", "待评价"};
    private int pageNum = 1;
    private String url = ShopConstants.SHOP_ORDER_LIST + "&page=10&curpage=1";
    private String state_type = "";
    private Drawable qrCodeImage;
    private boolean isViewCreated = false;
    private int tabPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            tabPosition = bundle.getInt("tab", 0);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.shop_orders_fragment;
    }

    @Override
    protected void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcl.setLayoutManager(linearLayoutManager);
        rcl.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new ShopOrdersListAdapter(getContext());
        adapter.setDatas(ordersList);
        adapter.setlistener(this);
        rcl.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                switch (tabPosition) {
                    case 0:
                        state_type = "";
                        break;
                    case 1:
                        state_type = "state_new";
                        break;
                    case 2:
                        state_type = "state_pay";
                        break;
                    case 3:
                        state_type = "state_send";
                        break;
                    case 4:
                        state_type = "state_noeval";
                        break;
                    default:
                        break;
                }
                pageNum = 1;
                url = getOrdersUrl(pageNum);
                if (!getKey().equals("")) {
                    ShopPresenter.getShopOrdersList(ShopOrdersFragement.this, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        tabLayout.getTabAt(tabPosition).select();
        //tab为0的时候，没有触发ontabselected
        if (tabPosition == 0) {
            state_type = "";
            pageNum = 1;
            url = getOrdersUrl(pageNum);
            if (!getKey().equals("")) {
                ShopPresenter.getShopOrdersList(ShopOrdersFragement.this, true);
            }
        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), mToolbar);
        isViewCreated = true;
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        if (isViewCreated) {
            pageNum = 1;
            url = getOrdersUrl(pageNum);
            if (!getKey().equals("")) {
                ShopPresenter.getShopOrdersList(ShopOrdersFragement.this, true);
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getState_type() {
        return state_type;
    }

    @Override
    public void onGetShopOrdersList(String s) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        try {
            JSONObject obj = new JSONObject(s);
            int pageTotal = obj.optInt("page_total");
            JSONObject datas = obj.optJSONObject("datas");
            JSONArray order_group_list = datas.optJSONArray("order_group_list");
            if (pageTotal == 0) {
                ordersList.clear();
                refreshLayout.setEnableAutoLoadMore(true);
                refreshLayout.setNoMoreData(false);
                adapter.notifyDataSetChanged();
                ivNoOrder.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.VISIBLE);
                return;
            } else if (pageNum > pageTotal) {
                refreshLayout.setEnableAutoLoadMore(false);
                refreshLayout.finishLoadMoreWithNoMoreData();
                refreshLayout.setNoMoreData(true);
                return;
            } else if (pageNum == 1) {
                ordersList.clear();
            }
            ivNoOrder.setVisibility(View.INVISIBLE);
            tvNoData.setVisibility(View.INVISIBLE);
            refreshLayout.setEnableAutoLoadMore(true);
            refreshLayout.setNoMoreData(false);
            for (int i = 0; i < order_group_list.length(); i++) {
                JSONObject objj = order_group_list.optJSONObject(i);
                JSONArray order_list = objj.optJSONArray("order_list");
                String add_time = objj.optString("add_time");
                final String pay_sn = objj.optString("pay_sn");
                for (int ii = 0; ii < order_list.length(); ii++) {
                    JSONObject objjj = order_list.optJSONObject(ii);
                   /* OrdersItem ordersItem = JsonUtil.toBean(objjj.toString(), OrdersItem.class);*/
                    List<MyExtendOrderGoodsBean> ordersitem = new ArrayList<MyExtendOrderGoodsBean>();
                    JSONArray extend_order_goods = objjj.optJSONArray("extend_order_goods");
                    for(int j=0;j<extend_order_goods.length();j++){
                        JSONObject goodobj = extend_order_goods.optJSONObject(j);
                       ordersitem.add(new MyExtendOrderGoodsBean(goodobj.optString("goods_image_url"),goodobj.optString("goods_name"), TextUtils.isEmpty(goodobj.optString("goods_spec"))?"":goodobj.optString("goods_spec"),goodobj.optString("goods_price"),goodobj.optString("goods_num")));

                    }
                    JSONArray zengpinglist = objjj.getJSONArray("zengpin_list");
                    ArrayList<ZengpingBean> zplist = new ArrayList<ZengpingBean>();
                    for (int iii = 0; iii < zengpinglist.length(); iii++) {
                        zplist.add(JsonUtil.toBean(zengpinglist.optJSONObject(iii).toString(),ZengpingBean.class));
                    }
                    String store_name = objjj.optString("store_name");
                    String shipping_fee = objjj.optString("shipping_fee");
                    String order_state = objjj.optString("order_state");
                    String state_desc = objjj.optString("state_desc");
                    final String order_id = objjj.optString("order_id");
                    String pay_amount = objjj.optString("order_amount");
                    boolean if_cancel = objjj.optBoolean("if_cancel");
                    boolean if_refund_cancel = objjj.optBoolean("if_refund_cancel");
                    boolean if_receive = objjj.optBoolean("if_receive");
                    boolean if_lock = objjj.optBoolean("if_lock");
                    boolean if_deliver = objjj.optBoolean("if_deliver");
                    boolean if_evaluation = objjj.optBoolean("if_evaluation");
                    boolean if_delete = objjj.optBoolean("if_delete");
                    boolean if_delivery_receive = objjj.optBoolean("if_delivery_receive");
                    boolean if_evaluation_again = objjj.optBoolean("if_evaluation_again");
                    ArrayList<TextView> btnList = new ArrayList<TextView>();
                    /*    isIf_receive 是否显示取消订单按钮 true/false*/
                    if (if_cancel) {
                        TextView tvCancel = new TextView(getContext());
                        LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramas.gravity = Gravity.CENTER_VERTICAL;
                        paramas.rightMargin = (int) getContext().getResources().getDimension(R.dimen.margin_Modules);
                        tvCancel.setLayoutParams(paramas);
                        tvCancel.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_cancle));
                        tvCancel.setText("取消订单");
                        tvCancel.setTextColor(getContext().getResources().getColor(R.color.black));
                        tvCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShopPresenter.cancelOrder(ShopOrdersFragement.this, order_id);
                            }
                        });
                        btnList.add(tvCancel);
                        /*    isIf_receive 是否显示确认收货按钮 true/false*/
                    }
                    if (if_receive) {
                        TextView tvReceive = new TextView(getContext());
                        LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramas.gravity = Gravity.CENTER_VERTICAL;
                        paramas.rightMargin = (int) getContext().getResources().getDimension(R.dimen.margin_Modules);
                        tvReceive.setLayoutParams(paramas);
                        tvReceive.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_cancle));
                        tvReceive.setText("确认收货");
                        tvReceive.setTextColor(getContext().getResources().getColor(R.color.black));
                        tvReceive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tvReceive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShopPresenter.receivedOrder(ShopOrdersFragement.this, order_id);
                            }
                        });
                        btnList.add(tvReceive);
                        /*    isIf_delete 是否显示删除按钮 true/false*/
                    }
                    if (if_delete) {
                        TextView tvDelete = new TextView(getContext());
                        LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramas.gravity = Gravity.CENTER_VERTICAL;
                        paramas.rightMargin = (int) getContext().getResources().getDimension(R.dimen.margin_Modules);
                        tvDelete.setLayoutParams(paramas);
                        tvDelete.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_cancle));
                        tvDelete.setText("删除订单");
                        tvDelete.setTextColor(getContext().getResources().getColor(R.color.black));
                        tvDelete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tvDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShopPresenter.deleteOrder(ShopOrdersFragement.this, order_id);
                            }
                        });
                        btnList.add(tvDelete);

                    }
                    if (if_deliver) {
                        TextView tvGoodRoute = new TextView(getContext());
                        LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramas.gravity = Gravity.CENTER_VERTICAL;
                        paramas.rightMargin = (int) getContext().getResources().getDimension(R.dimen.margin_Modules);
                        tvGoodRoute.setLayoutParams(paramas);
                        tvGoodRoute.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_cancle));
                        tvGoodRoute.setText("物流查询");
                        tvGoodRoute.setTextColor(getContext().getResources().getColor(R.color.black));
                        tvGoodRoute.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tvGoodRoute.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ShopOrderRouteActivity.class);
                                intent.putExtra("id", order_id);
                                if (getActivity() != null) {
                                    getActivity().startActivity(intent);
                                }
                            }
                        });
                        btnList.add(tvGoodRoute);
                    }
                    if (if_delivery_receive) {
                        TextView tvGetGoodSelf = new TextView(getContext());
                        LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramas.gravity = Gravity.CENTER_VERTICAL;
                        paramas.rightMargin = (int) getContext().getResources().getDimension(R.dimen.margin_Modules);
                        tvGetGoodSelf.setLayoutParams(paramas);
                        tvGetGoodSelf.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_cancle));
                        tvGetGoodSelf.setText("确认提货");
                        tvGetGoodSelf.setTextColor(getContext().getResources().getColor(R.color.black));
                        tvGetGoodSelf.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        btnList.add(tvGetGoodSelf);
                    }
                    if (if_evaluation) {
                        TextView tvEvaluation = new TextView(getContext());
                        LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramas.gravity = Gravity.CENTER_VERTICAL;
                        paramas.rightMargin = (int) getContext().getResources().getDimension(R.dimen.margin_Modules);
                        tvEvaluation.setLayoutParams(paramas);
                        tvEvaluation.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_cancle));
                        tvEvaluation.setText("评价订单");
                        tvEvaluation.setTextColor(getContext().getResources().getColor(R.color.black));
                        tvEvaluation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tvEvaluation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ShopOrderEveluateActivity.class);
                                intent.putExtra("id", order_id);
                                if (getActivity() != null) {
                                    getActivity().startActivity(intent);
                                }
                            }
                        });
                        btnList.add(tvEvaluation);
                    }   /*    if_lock 是否显示锁定中状态 true/false*/
                    if (if_lock) {
                        TextView tvLock = new TextView(getContext());
                        LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramas.gravity = Gravity.CENTER_VERTICAL;
                        paramas.rightMargin = (int) getContext().getResources().getDimension(R.dimen.margin_Modules);
                        tvLock.setLayoutParams(paramas);
                        tvLock.setText("退货/退款中...");
                        tvLock.setTextColor(getContext().getResources().getColor(R.color.black));
                        tvLock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                        btnList.add(tvLock);
                    }
                    if ((order_state.equals("20") || if_receive) && !if_lock) {
                        TextView tvQrReceive = new TextView(getContext());
                        LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramas.gravity = Gravity.CENTER_VERTICAL;
                        paramas.rightMargin = (int) getContext().getResources().getDimension(R.dimen.margin_Modules);
                        tvQrReceive.setLayoutParams(paramas);
                        tvQrReceive.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_refund));
                        tvQrReceive.setText("提货二维码");
                        tvQrReceive.setTextColor(getContext().getResources().getColor(R.color.msb_color));
                        tvQrReceive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tvQrReceive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShopPresenter.getReceiveQrCodeImage(ShopOrdersFragement.this, order_id);
                            }
                        });
                        btnList.add(tvQrReceive);
                    }
                    if (if_refund_cancel) {
                        TextView tvRefund = new TextView(getContext());
                        LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramas.gravity = Gravity.CENTER_VERTICAL;
                        paramas.rightMargin = (int) getContext().getResources().getDimension(R.dimen.margin_Modules);
                        tvRefund.setLayoutParams(paramas);
                        tvRefund.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_cancle));
                        tvRefund.setText("申请退款");
                        tvRefund.setTextColor(getContext().getResources().getColor(R.color.black));
                        tvRefund.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tvRefund.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), RefundALLActivity.class);
                                intent.putExtra("data", order_id);
                                getActivity().startActivity(intent);
                            }
                        });
                        btnList.add(tvRefund);
                    }/*if_evaluation_again 是否显示追加评价按钮 true/false*/
                    if (if_evaluation_again) {
                        TextView tvAddEvaluation = new TextView(getContext());
                        LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramas.gravity = Gravity.CENTER_VERTICAL;
                        paramas.rightMargin = (int) getContext().getResources().getDimension(R.dimen.margin_Modules);
                        tvAddEvaluation.setLayoutParams(paramas);
                        tvAddEvaluation.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_cancle));
                        tvAddEvaluation.setText("追加评价");
                        tvAddEvaluation.setTextColor(getContext().getResources().getColor(R.color.black));
                        tvAddEvaluation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tvAddEvaluation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ShopOrderAddEveluateActivity.class);
                                intent.putExtra("id", order_id);
                                if (getActivity() != null) {
                                    getActivity().startActivity(intent);
                                }
                            }
                        });
                        btnList.add(tvAddEvaluation);
                    }
                    if (order_state.equals("10")) {
                        TextView tvPay = new TextView(getContext());
                        LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramas.gravity = Gravity.CENTER_VERTICAL;
                        paramas.rightMargin = (int) getContext().getResources().getDimension(R.dimen.margin_Modules);
                        tvPay.setLayoutParams(paramas);
                        tvPay.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.btn_pay));
                        tvPay.setText("付款");
                        tvPay.setTextColor(getContext().getResources().getColor(R.color.white));
                        tvPay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tvPay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShopPresenter.buyStep3(ShopOrdersFragement.this, pay_sn);
                            }
                        });
                        btnList.add(tvPay);
                    }
                    OrderslistBean orderslistBean = new OrderslistBean(ordersitem, pay_amount, add_time, pay_sn, store_name, shipping_fee, order_state, state_desc, btnList, order_id,zplist);
                    ordersList.add(orderslistBean);
                }
            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        url = getOrdersUrl(pageNum);
        if (!getKey().equals("")) {
            ShopPresenter.getShopOrdersList(ShopOrdersFragement.this, true);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        url = getOrdersUrl(pageNum);
        if (!getKey().equals("")) {
            ShopPresenter.getShopOrdersList(ShopOrdersFragement.this, true);
        }
    }

    @Override
    public void onError(String s) {
        super.onError(s);
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    private String getOrdersUrl(int curpage) {
        return ShopConstants.SHOP_ORDER_LIST + "&page=10&curpage=" + curpage;
    }


    @Override
    public void onGoDetail(String goodId) {
        Intent intent = new Intent(getContext(), ShopOrdersDetailActivity.class);
        intent.putExtra("data", goodId);
        Activity activity = getActivity();
        if (activity != null) {
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onDeleteOrderSuccess(final String orderId) {
        PopUtil.showAutoDissHookDialog(getContext(), "订单删除成功", 100);
        int position = 0;
        for (OrderslistBean bean : ordersList) {
            if (bean.getOrderId().equals(orderId)) {
                position = ordersList.indexOf(bean);
                break;
            }
        }
        ordersList.remove(position);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                if (ordersList.size() == 0) {
                    ivNoOrder.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        }, 1500);


    }


    @Override
    public void onReceiveOrderSuccess(String s, String orderId) {
        Intent intent = new Intent(getActivity(), ShopSuccessActivity.class);
        intent.putExtra("id", orderId);
        intent.putExtra("state", "receive");
        getActivity().startActivity(intent);

    }


    @Override
    public void onCancelOrderSuccess(String s) {
        PopUtil.showAutoDissHookDialog(getContext(), "取消订单", 100);
        pageNum = 1;
        url = getOrdersUrl(pageNum);
        if (!getKey().equals("")) {
            ShopPresenter.getShopOrdersList(ShopOrdersFragement.this, true);
        }
    }

    @Override
    public void onGetOrderQrCodeSuccess(String s) {
        try {
            String image_url = new JSONObject(s).getJSONObject("datas").optString("image_url");
            LayoutInflater inflaterDl = LayoutInflater.from(getContext());
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
                    R.layout.dialog_qr_receive, null);
            RecyclerHolder holder = new RecyclerHolder(getContext(), layout);
            final AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.full_screen_dialog).create();
            final ImageView iv = holder.getView(R.id.iv);
            holder.setImage(R.id.iv, image_url);
            Glide.with(this).load(image_url).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    qrCodeImage = resource;
                    iv.setImageDrawable(resource);
                }
            });

            holder.getView(R.id.save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (qrCodeImage != null) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                AndPermission.with(ShopOrdersFragement.this)
                                        .requestCode(MY_PERMISSIONS_REQUEST)
                                        .permission(
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .send();
                            } else {
                                Bitmap bitmap = DrawbleUtil.drawableToBitmap(qrCodeImage);
                                if (DrawbleUtil.saveImageToGallery(getContext(), bitmap) != null) {
                                    PopUtil.showAutoDissHookDialog(getContext(), "已保存到本地相册", 200);
                                }
                            }
                        } else {
                            Bitmap bitmap = DrawbleUtil.drawableToBitmap(qrCodeImage);
                            if (DrawbleUtil.saveImageToGallery(getContext(), bitmap) != null) {
                                PopUtil.showAutoDissHookDialog(getContext(), "已保存到本地相册", 200);
                            }
                        }

                    }

                }
            });
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            dialog.getWindow().setContentView(layout);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == MY_PERMISSIONS_REQUEST) {
                Bitmap bitmap = DrawbleUtil.drawableToBitmap(qrCodeImage);
                if (DrawbleUtil.saveImageToGallery(getContext(), bitmap) != null) {
                    PopUtil.showAutoDissHookDialog(getContext(), "已保存到本地相册", 200);
                }
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == MY_PERMISSIONS_REQUEST) {
                ToastUtil.ToastText(getContext(), "没有权限");
            }
        }
    };


    @Override
    public void onBuyStep3(String s) {
        Intent intent = new Intent(getActivity(), ShopPayOrderActivity.class);
        BuyStep3PayListBean buyStep3bean = JsonUtil.toBean(s, BuyStep3PayListBean.class);
        intent.putExtra("buyStep3", buyStep3bean);
        intent.putExtra("pdPassword", "");
        startActivity(intent);
    }

    public void refreshCurrentTab(int currentTab) {

        if (tabPosition != currentTab) {
            tabLayout.getTabAt(currentTab).select();
        }else {
            pageNum = 1;
            url = getOrdersUrl(pageNum);
            if (!getKey().equals("")) {
                ShopPresenter.getShopOrdersList(ShopOrdersFragement.this, true);
            }
        }
    }
}
