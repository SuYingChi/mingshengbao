package com.msht.minshengbao.androidShop.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.msht.minshengbao.androidShop.adapter.ShopOrderGoodListAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.BuyStep3PayListBean;
import com.msht.minshengbao.androidShop.shopBean.ShopOrderDetailBean;
import com.msht.minshengbao.androidShop.util.DrawbleUtil;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.PermissionUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;
import com.msht.minshengbao.androidShop.util.StringUtil;
import com.msht.minshengbao.androidShop.viewInterface.IBuyStep3GetPayListView;
import com.msht.minshengbao.androidShop.viewInterface.ICancelOrderView;
import com.msht.minshengbao.androidShop.viewInterface.IOrderQrCodeView;
import com.msht.minshengbao.androidShop.viewInterface.IReceivedOrderView;
import com.msht.minshengbao.androidShop.viewInterface.IShopOrderDetailView;
import com.yanzhenjie.permission.Permission;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopOrdersDetailActivity extends ShopBaseActivity implements IShopOrderDetailView, ShopOrderGoodListAdapter.ClickAfterSaleListener, ICancelOrderView, IReceivedOrderView, IBuyStep3GetPayListView, IOrderQrCodeView {

    private String orderId;
    @BindView(R.id.ll_1)
    LinearLayout llHead;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.phone_num)
    TextView tvPhone;
    @BindView(R.id.location)
    TextView tvLocation;
    @BindView(R.id.pay)
    TextView tvPay;
    @BindView(R.id.state_desc)
    TextView tvState;
    @BindView(R.id.tip)
    TextView tvTip;
    @BindView(R.id.inv_info)
    TextView tvInvo;
    @BindView(R.id.store)
    TextView tvStore;
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.message)
    TextView tvMesssage;
    @BindView(R.id.delivery)
    TextView tvDelivery;
    List<ShopOrderDetailBean.DatasBean.OrderInfoBean.GoodsListBean> list = new ArrayList<ShopOrderDetailBean.DatasBean.OrderInfoBean.GoodsListBean>();
    ShopOrderGoodListAdapter adapter;
    @BindView(R.id.good_num)
    TextView tvNum;
    @BindView(R.id.total)
    TextView tvTotal;
    @BindView(R.id.kefu)
    LinearLayout llkefu;
    @BindView(R.id.call)
    LinearLayout llcall;
    @BindView(R.id.order_num)
    TextView tvOrderNum;
    @BindView(R.id.creat_time)
    TextView tvCreatTime;
    @BindView(R.id.ll_creat)
    LinearLayout llCreat;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.payment_time)
    TextView tvPayTime;
    @BindView(R.id.ll_shipping)
    LinearLayout llShipping;
    @BindView(R.id.shipping_time)
    TextView tvShippingTime;
    @BindView(R.id.ll_finish)
    LinearLayout llFinish;
    @BindView(R.id.finnshed_time)
    TextView tvFinishTime;
    @BindView(R.id.btn_container)
    LinearLayout llbtns;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    @BindView(R.id.ll_zengping)
    LinearLayout llzengping;
    @BindView(R.id.zengpingName)
    TextView tvZengping;
    @BindView(R.id.youhui)
    TextView tvYouhui;
    @BindView(R.id.ll_inv)
    LinearLayout llinv;

    private String memberId;
    private List<ShopOrderDetailBean.DatasBean.OrderInfoBean.ZengpinListBean> zengpinglist;
    private String store_phone;

    @Override
    protected void setLayout() {
        setContentView(R.layout.shop_order_detail);
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
        orderId = getIntent().getStringExtra("data");
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        llm.setAutoMeasureEnabled(true);
        rcl.setLayoutManager(llm);
        rcl.setNestedScrollingEnabled(false);
        rcl.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ShopOrderGoodListAdapter(this);
        adapter.setDatas(list);
        adapter.setClickAfterSaleListener(this);
        rcl.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShopPresenter.getOrderDetail(this);
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void onGetDetailSuccess(String s) {
        ShopOrderDetailBean bean = JsonUtil.toBean(s, ShopOrderDetailBean.class);
        if (bean != null) {
            llbtns.removeAllViews();
            LinearLayout.LayoutParams llprams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            llprams.weight=1;
            llbtns.addView(new View(this), llprams);
            final ShopOrderDetailBean.DatasBean.OrderInfoBean orderInfo = bean.getDatas().getOrder_info();
            String state = orderInfo.getOrder_state();
            memberId = orderInfo.getStore_member_id();
            switch (state) {
                case "0":
                    llHead.setBackgroundResource(R.drawable.shop_order_detail_close);
                    break;
                case "10":
                    llHead.setBackgroundResource(R.drawable.shop_order_detail_wait_pay);
                    TextView tvPay = new TextView(this);
                    LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    paramas.gravity = Gravity.CENTER_VERTICAL;
                    paramas.rightMargin = (int) this.getResources().getDimension(R.dimen.margin_Modules);
                    tvPay.setLayoutParams(paramas);
                    tvPay.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_pay));
                    tvPay.setText("支付订单");
                    tvPay.setTextColor(this.getResources().getColor(R.color.white));
                    tvPay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    tvPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShopPresenter.buyStep3(ShopOrdersDetailActivity.this, orderInfo.getPay_sn(),orderId);
                        }
                    });
                    llbtns.addView(tvPay);
                    break;
                case "20":
                    llHead.setBackgroundResource(R.drawable.shop_order_detail_wait_send);
                    break;
                case "30":
                    llHead.setBackgroundResource(R.drawable.shop_order_detail_sended);
                    break;
                case "40":
                    llHead.setBackgroundResource(R.drawable.shop_order_detail_finish);
                    break;
                case "24":
                    llHead.setBackgroundResource(R.drawable.shop_order_detail_close);
                    break;
                case "7":
                    llHead.setBackgroundResource(R.drawable.shop_order_detail_close);
                    break;
                default:
                    break;
            }
            tvName.setText(orderInfo.getReciver_name());
            tvPhone.setText(orderInfo.getReciver_phone());
            tvLocation.setText(orderInfo.getReciver_addr());
            tvPay.setText(orderInfo.getPayment_name());
            tvState.setText(orderInfo.getState_desc());
            if(TextUtils.isEmpty(orderInfo.getInvoice())){
                tvTip.setVisibility(View.GONE);
            }else {
                tvTip.setText(orderInfo.getOrder_tips());
            }
            if(TextUtils.isEmpty(orderInfo.getInvoice())){
                tvInvo.setVisibility(View.GONE);
                llinv.setVisibility(View.GONE);
            }else {
                tvInvo.setVisibility(View.VISIBLE);
                llinv.setVisibility(View.VISIBLE);
                tvInvo.setText(orderInfo.getInvoice());
            }
            tvStore.setText(orderInfo.getStore_name());
            list.clear();
            list.addAll(orderInfo.getGoods_list());
            adapter.notifyDataSetChanged();
            tvMesssage.setText(orderInfo.getOrder_message());
            tvDelivery.setText(StringUtil.getPriceSpannable12String(this, orderInfo.getShipping_fee(), R.style.big_money, R.style.big_money));
            int num = 0;
            for (ShopOrderDetailBean.DatasBean.OrderInfoBean.GoodsListBean b : orderInfo.getGoods_list()) {
                num += Integer.valueOf(b.getGoods_num());
            }
            tvNum.setText(String.format("%d 件商品", num));
            tvTotal.setText(StringUtil.getPriceSpannable12String(this,orderInfo.getReal_pay_amount(),R.style.big_money,R.style.big_money));
            llkefu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoKefu();
                }
            });
            llcall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderInfo.getStore_phone() == null || orderInfo.getStore_phone().toString().equals("")) {
                        PopUtil.showComfirmDialog(ShopOrdersDetailActivity.this,"","该商家没有提供联系电话","","知道了",null,null,true);
                    } else {
                        call(orderInfo.getStore_phone().toString());
                    }
                }
            });
            tvOrderNum.setText(orderInfo.getOrder_sn());

            if (orderInfo.getAdd_time().equals("")) {
                llCreat.setVisibility(View.GONE);
            } else {
                tvCreatTime.setText(orderInfo.getAdd_time());
            }
            if (orderInfo.getPayment_time().equals("")) {
                llPay.setVisibility(View.GONE);
            } else {
                tvPayTime.setText(orderInfo.getPayment_time());
            }
            if (orderInfo.getShipping_time().equals("")) {
                llShipping.setVisibility(View.GONE);
            } else {
                tvShippingTime.setText(orderInfo.getShipping_time());
            }
            if (orderInfo.getFinnshed_time().equals("")) {
                llFinish.setVisibility(View.GONE);
            } else {
                tvFinishTime.setText(orderInfo.getFinnshed_time());
            }
            if (bean.getDatas().getOrder_info().getZengpin_list().size() > 0) {
                llzengping.setVisibility(View.VISIBLE);
                zengpinglist = bean.getDatas().getOrder_info().getZengpin_list();
                StringBuilder sb = new StringBuilder();
                for (ShopOrderDetailBean.DatasBean.OrderInfoBean.ZengpinListBean zengping : zengpinglist) {
                    sb.append(zengping.getGoods_name() + "x" + zengping.getGoods_num());
                }
                tvZengping.setText("贈品  " + sb.toString());
            } else {
                llzengping.setVisibility(View.GONE);
            }
            if (bean.getDatas().getOrder_info().getPromotion().size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (List<String> p : bean.getDatas().getOrder_info().getPromotion()) {
                    for (String pp : p) {
                        sb.append(pp);
                    }
                }
                tvYouhui.setText(sb.toString());
            }
            /*     是否显示取消订单按钮 true/false*/
            if (orderInfo.isIf_buyer_cancel()) {
                TextView tvCancel = new TextView(this);
                LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramas.gravity = Gravity.CENTER_VERTICAL;
                paramas.rightMargin = (int) this.getResources().getDimension(R.dimen.margin_Modules);
                tvCancel.setLayoutParams(paramas);
                tvCancel.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_cancle));
                tvCancel.setText("取消订单");
                tvCancel.setTextColor(this.getResources().getColor(R.color.black));
                tvCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopPresenter.cancelOrder(ShopOrdersDetailActivity.this, orderId);
                    }
                });
                llbtns.addView(tvCancel);

            }

            if (orderInfo.isIf_deliver()) {
                TextView tvGoodRoute = new TextView(this);
                LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramas.gravity = Gravity.CENTER_VERTICAL;
                paramas.rightMargin = (int) this.getResources().getDimension(R.dimen.margin_Modules);
                tvGoodRoute.setLayoutParams(paramas);
                tvGoodRoute.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_cancle));
                tvGoodRoute.setText("物流查询");
                tvGoodRoute.setTextColor(this.getResources().getColor(R.color.black));
                tvGoodRoute.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tvGoodRoute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopOrdersDetailActivity.this, ShopOrderRouteActivity.class);
                        intent.putExtra("id", orderId);
                        startActivity(intent);
                    }
                });
                llbtns.addView(tvGoodRoute);
            }
            if (orderInfo.isIf_receive()) {
                TextView tvReceive = new TextView(this);
                LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramas.gravity = Gravity.CENTER_VERTICAL;
                paramas.rightMargin = (int) this.getResources().getDimension(R.dimen.margin_Modules);
                tvReceive.setLayoutParams(paramas);
                tvReceive.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_cancle));
                tvReceive.setText("确认收货");
                tvReceive.setTextColor(this.getResources().getColor(R.color.black));
                tvReceive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tvReceive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopPresenter.receivedOrder(ShopOrdersDetailActivity.this, orderId);
                    }
                });
                llbtns.addView(tvReceive);
            }
            if (orderInfo.isIf_delivery_receive()) {
                TextView tvGetGoodSelf = new TextView(this);
                LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramas.gravity = Gravity.CENTER_VERTICAL;
                paramas.rightMargin = (int) this.getResources().getDimension(R.dimen.margin_Modules);
                tvGetGoodSelf.setLayoutParams(paramas);
                tvGetGoodSelf.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_cancle));
                tvGetGoodSelf.setText("确认提货");
                tvGetGoodSelf.setTextColor(this.getResources().getColor(R.color.black));
                tvGetGoodSelf.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tvGetGoodSelf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                llbtns.addView(tvGetGoodSelf);
            }/*     if_evaluation 是否显示评价按钮 true/false*/
            if (orderInfo.isIf_evaluation()) {
                TextView tvEvaluation = new TextView(this);
                LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramas.gravity = Gravity.CENTER_VERTICAL;
                paramas.rightMargin = (int) this.getResources().getDimension(R.dimen.margin_Modules);
                tvEvaluation.setLayoutParams(paramas);
                tvEvaluation.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_cancle));
                tvEvaluation.setText("评价订单");
                tvEvaluation.setTextColor(this.getResources().getColor(R.color.black));
                tvEvaluation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvEvaluation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopOrdersDetailActivity.this, ShopOrderEveluateActivity.class);
                        intent.putExtra("id", orderId);
                        startActivity(intent);
                    }
                });
                llbtns.addView(tvEvaluation);
            }   /*    if_lock 是否显示锁定中状态 true/false*/
            if (orderInfo.isIf_lock()) {
                TextView tvLock = new TextView(this);
                LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramas.gravity = Gravity.CENTER_VERTICAL;
                paramas.rightMargin = (int) this.getResources().getDimension(R.dimen.margin_Modules);
                tvLock.setLayoutParams(paramas);
                tvLock.setText("退货/退款中...");
                tvLock.setTextColor(this.getResources().getColor(R.color.black));
                tvLock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                llbtns.addView(tvLock);
            }/*  if_refund_cancel 是否显示退款按钮 true/false*/
            if (orderInfo.isIf_refund_cancel()) {
                TextView tvRefund = new TextView(this);
                LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramas.gravity = Gravity.CENTER_VERTICAL;
                paramas.rightMargin = (int) this.getResources().getDimension(R.dimen.margin_Modules);
                tvRefund.setLayoutParams(paramas);
                tvRefund.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_cancle));
                tvRefund.setText("申请退款");
                tvRefund.setTextColor(this.getResources().getColor(R.color.black));
                tvRefund.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tvRefund.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopOrdersDetailActivity.this, RefundALLActivity.class);
                        intent.putExtra("data", orderId);
                        startActivity(intent);
                    }
                });
                llbtns.addView(tvRefund);
            }/*if_evaluation_again 是否显示追加评价按钮 true/false*/
            if (orderInfo.isIf_evaluation_again()) {
                TextView tvAddEvaluation = new TextView(this);
                LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramas.gravity = Gravity.CENTER_VERTICAL;
                paramas.rightMargin = (int) this.getResources().getDimension(R.dimen.margin_Modules);
                tvAddEvaluation.setLayoutParams(paramas);
                tvAddEvaluation.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_cancle));
                tvAddEvaluation.setText("追加评价");
                tvAddEvaluation.setTextColor(this.getResources().getColor(R.color.black));
                tvAddEvaluation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tvAddEvaluation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShopOrdersDetailActivity.this, ShopOrderAddEveluateActivity.class);
                        intent.putExtra("id", orderId);
                        startActivity(intent);
                    }
                });
                llbtns.addView(tvAddEvaluation);
            }
            if ((orderInfo.getOrder_state().equals("20") || orderInfo.isIf_receive()) && !orderInfo.isIf_lock()) {
                TextView tvQrReceive = new TextView(this);
                LinearLayout.LayoutParams paramas = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramas.gravity = Gravity.CENTER_VERTICAL;
                paramas.rightMargin = (int) this.getResources().getDimension(R.dimen.margin_Modules);
                tvQrReceive.setLayoutParams(paramas);
                tvQrReceive.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_refund));
                tvQrReceive.setText("提货二维码");
                tvQrReceive.setTextColor(getResources().getColor(R.color.msb_color));
                tvQrReceive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                tvQrReceive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopPresenter.getReceiveQrCodeImage(ShopOrdersDetailActivity.this, orderId);
                    }
                });
                llbtns.addView(tvQrReceive);
            }
            if (llbtns.getChildCount() == 1) {
                llbtns.setVisibility(View.GONE);
            }
        }
    }


    @SuppressLint("MissingPermission")
    private void call(final String store_phone) {
        this.store_phone = store_phone;
        if (!TextUtils.isEmpty(store_phone)) {
            if (Build.VERSION.SDK_INT >= 23) {
             /*   if (ContextCompat.checkSelfPermission(ShopOrdersDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
           */        /* AndPermission.with(ShopOrdersDetailActivity.this)
                            .requestCode(CALL_PERMISSIONS_REQUEST)
                            .permission(
                                    Manifest.permission.CALL_PHONE)
                            .send();*/
           PermissionUtils.requestPermissions(ShopOrdersDetailActivity.this, new PermissionUtils.PermissionRequestFinishListener() {
               @Override
               public void onPermissionRequestSuccess(List<String> permissions) {
                   Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + store_phone));
                   startActivity(intent);
               }
           },Permission.CALL_PHONE);
               /* } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + store_phone));
                    startActivity(intent);
                }*/
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + store_phone));
                startActivity(intent);
            }

        }
    }

    private void gotoKefu() {
        Intent intent = new Intent(this, ShopkefuActivity.class);
        intent.putExtra("t_id", memberId);
        startActivity(intent);
    }

    @Override
    public void onGotoAfterSale(int position) {
        Intent intent = new Intent(this, ShopAfterSaleActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("goodId", list.get(position).getRec_id());
        startActivity(intent);
    }

    @Override
    public void onGotoGoodDetail(int position) {
      /*  Map map = new HashMap<String,String>();
        map.put("type","goods");
        map.put("data",list.get(position).getGoods_id());
        doNotAdClick(map);*/
        String goodsId = list.get(position).getGoods_id();
        onShopItemViewClick("goods", goodsId);
    }

    @Override
    public void onCancelOrderSuccess(String s) {
        Intent intent = new Intent(this, ShopSuccessActivity.class);
        intent.putExtra("id", orderId);
        intent.putExtra("state", "cancel");
        startActivity(intent);
        finish();
    }

    @Override
    public void onReceiveOrderSuccess(String s, String orderId) {
        Intent intent = new Intent(this, ShopSuccessActivity.class);
        intent.putExtra("id", orderId);
        intent.putExtra("state", "receive");
        startActivity(intent);
        finish();
    }

    @Override
    public void onBuyStep3(String s,String orderId) {
        Intent intent = new Intent(this, ShopPayOrderActivity.class);
        BuyStep3PayListBean buyStep3bean = JsonUtil.toBean(s, BuyStep3PayListBean.class);
        intent.putExtra("buyStep3", buyStep3bean);
        intent.putExtra("pdPassword", "");
        intent.putExtra("orderId", orderId);
        startActivity(intent);
    }

    @Override
    public void onGetOrderQrCodeSuccess(String s) {
        try {
            String image_url = new JSONObject(s).getJSONObject("datas").optString("image_url");
            LayoutInflater inflaterDl = LayoutInflater.from(this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(
                    R.layout.dialog_qr_receive, null);
            RecyclerHolder holder = new RecyclerHolder(this, layout);
            final AlertDialog dialog = new AlertDialog.Builder(this, R.style.full_screen_dialog).create();
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
                            if (ContextCompat.checkSelfPermission(ShopOrdersDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                               /* AndPermission.with(ShopOrdersDetailActivity.this)
                                        .requestCode(MY_PERMISSIONS_REQUEST)
                                        .permission(
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .send();*/
                                PermissionUtils.requestPermissions(ShopOrdersDetailActivity.this, new PermissionUtils.PermissionRequestFinishListener() {
                                    @Override
                                    public void onPermissionRequestSuccess(List<String> permissions) {
                                        Bitmap bitmap = DrawbleUtil.drawableToBitmap(qrCodeImage);
                                        if (DrawbleUtil.saveImageToGallery(ShopOrdersDetailActivity.this, bitmap) != null) {
                                            PopUtil.showAutoDissHookDialog(ShopOrdersDetailActivity.this, "已保存到本地相册", 200);
                                        }
                                    }
                                }, Permission.WRITE_EXTERNAL_STORAGE);
                            } else {
                                Bitmap bitmap = DrawbleUtil.drawableToBitmap(qrCodeImage);
                                if (DrawbleUtil.saveImageToGallery(ShopOrdersDetailActivity.this, bitmap) != null) {
                                    PopUtil.showAutoDissHookDialog(ShopOrdersDetailActivity.this, "已保存到本地相册", 200);
                                }
                            }
                        } else {
                            Bitmap bitmap = DrawbleUtil.drawableToBitmap(qrCodeImage);
                            if (DrawbleUtil.saveImageToGallery(ShopOrdersDetailActivity.this, bitmap) != null) {
                                PopUtil.showAutoDissHookDialog(ShopOrdersDetailActivity.this, "已保存到本地相册", 200);
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

 /*   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }*/

    private static final int MY_PERMISSIONS_REQUEST = 200;
    private Drawable qrCodeImage;
    private int CALL_PERMISSIONS_REQUEST = 300;
/*    private PermissionListener listener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == MY_PERMISSIONS_REQUEST) {
                Bitmap bitmap = DrawbleUtil.drawableToBitmap(qrCodeImage);
                if (DrawbleUtil.saveImageToGallery(ShopOrdersDetailActivity.this, bitmap) != null) {
                    PopUtil.showAutoDissHookDialog(ShopOrdersDetailActivity.this, "已保存到本地相册", 200);
                }
            } else if (requestCode == CALL_PERMISSIONS_REQUEST) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + store_phone));
                startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode) {
            ToastUtil.ToastText(ShopOrdersDetailActivity.this, "没有权限");
        }
    };*/
}
