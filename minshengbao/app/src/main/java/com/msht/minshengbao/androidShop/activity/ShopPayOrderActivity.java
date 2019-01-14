package com.msht.minshengbao.androidShop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.adapter.ShopPayMethodsAdapter;
import com.msht.minshengbao.androidShop.baseActivity.ShopBaseActivity;
import com.msht.minshengbao.androidShop.presenter.ShopPresenter;
import com.msht.minshengbao.androidShop.shopBean.BuyStep3PayListBean;
import com.msht.minshengbao.androidShop.shopBean.NativePayMethodsBean;
import com.msht.minshengbao.androidShop.shopBean.NativePayMethodsBean2;
import com.msht.minshengbao.androidShop.util.JsonUtil;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.androidShop.util.PopUtil;
import com.msht.minshengbao.androidShop.viewInterface.IBuyStep4CreatChargeView;
import com.msht.minshengbao.androidShop.viewInterface.INativGetPayListView;
import com.pingplusplus.android.Pingpp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopPayOrderActivity extends ShopBaseActivity implements ShopPayMethodsAdapter.ShopPayAdapterListener, INativGetPayListView, IBuyStep4CreatChargeView {
    @BindView(R.id.rcl)
    RecyclerView rcl;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.toolbar2)
    Toolbar mToolbar;
    @BindView(R.id.back)
    ImageView ivback;
    List<NativePayMethodsBean2> payList = new ArrayList<NativePayMethodsBean2>();
    private ShopPayMethodsAdapter adapter;
    private BuyStep3PayListBean.DatasBean.PayInfoBean payinfo;
    private double payAmount;
    private NativePayMethodsBean2 selectedPayBean;
    private String pdPassword;
    private String charge;
    private String orderId;

    @Override
    protected void setLayout() {
        setContentView(R.layout.shop_select_pay);
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
        Intent intent = getIntent();
        BuyStep3PayListBean buyStep3Bean = (BuyStep3PayListBean) intent.getSerializableExtra("buyStep3");
        pdPassword = intent.getStringExtra("pdPassword");
        orderId = intent.getStringExtra("orderId");
        payinfo = buyStep3Bean.getDatas().getPay_info();
        payAmount = Double.valueOf(payinfo.getPay_amount()) - Double.valueOf(payinfo.getPayed_amount());
        tvAmount.setText(String.format("%s", payAmount));
        rcl.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new ShopPayMethodsAdapter(this, this);
        adapter.setFoot_layoutId(R.layout.item_pay_foot);
        adapter.setDatas(payList);
        rcl.setAdapter(adapter);
        ShopPresenter.getNativPayList(this);
    }


    @Override
    public void pay() {
        if (selectedPayBean == null) {
            PopUtil.showComfirmDialog(this, "", "请选择支付方式", "", "知道了", null, null, true);
        } else if (charge == null) {
            PopUtil.showComfirmDialog(this, "", "请等待charge重新生成", "", "知道了", null, null, true);
        } else {
            PopUtil.showComfirmDialog(this, null, "确认支付订单？", "取消", "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取到chargeid 但暂时不支付订单，订单转换为待付款
                    Intent intent = new Intent(ShopPayOrderActivity.this, ShopOrdersDetailActivity.class);
                    intent.putExtra("data", orderId);
                    startActivity(intent);
                    finish();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //确认支付 调起ping++完成支付
                    Pingpp.createPayment(ShopPayOrderActivity.this, charge);
                }
            }, false);
        }

    }

    @Override
    public void onCheckChange(boolean isChecked, int position) {
        selectedPayBean = payList.get(position);
        if (!selectedPayBean.isCheck() && isChecked) {
            for (int i = 0; i < payList.size() - 1; i++) {
                NativePayMethodsBean2 bean2 = payList.get(i);
                if (bean2.isCheck() && i != position) {
                    bean2.setCheck(false);
                    payList.set(i, bean2);
                    break;
                }
            }
            selectedPayBean.setCheck(true);
            payList.set(position, selectedPayBean);
            adapter.notifyDataSetChanged();
            charge = null;
            ShopPresenter.creatCharge(this);
        }
    }

    @Override
    public void onGetNativePayListSuccess(String s) {
        NativePayMethodsBean nativePay = JsonUtil.toBean(s, NativePayMethodsBean.class);
        for (NativePayMethodsBean.DataBean bean : nativePay.getData()) {
            NativePayMethodsBean2 be = new NativePayMethodsBean2(bean.getCode(), bean.getName(), bean.getChannel(), bean.getTips(), false);
            payList.add(be);
        }
        payList.add(null);
        adapter.notifyDataSetChanged();

    }

    @Override
    public String getPay_sn() {
        return payinfo.getPay_sn();
    }

    @Override
    public String getPayment_code() {
        return "msbapppay";
    }
  /*  //启动预付款时的密码
    @Override
    public String getPassword() {
        return pdPassword;
    }*/

    @Override
    public String getRcb_pay() {
        if (Double.valueOf(payinfo.getMember_available_rcb()) == 0) {
            return "0";
        } else return "1";

    }

    @Override
    public String getPd_pay() {
        if (Double.valueOf(payinfo.getMember_available_pd()) == 0) return "0";
        else return "1";
    }

    @Override
    public void onCreatChargedSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject data = obj.optJSONObject("data");
            charge = data.optString("charge");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPayAmount() {
        return payAmount + "";
    }

    @Override
    public String getChannel() {

        return selectedPayBean.getChannel() + "";

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                if (TextUtils.equals(result, "success")) {
                   /* Intent intent = new Intent(this, ShopOrdersDetailActivity.class);
                    intent.putExtra("data", payinfo.getPay_sn());
                    startActivity(intent);
                    finish();*/
                    Intent intent = new Intent(this, ShopSuccessActivity.class);
                    intent.putExtra("id", orderId);
                    intent.putExtra("state", "pay");
                    startActivity(intent);
                    finish();
                } else if (TextUtils.equals(result, "fail")) {
                    String errorMsg = data.getStringExtra("error_msg"); // 错误信息
                    String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                    if (errorMsg.equals("permission_denied")) {
                        PopUtil.toastInCenter("所需权限被禁止，请允许权限后重试");
                    }
                    LogUtils.e("result==" + result + "  error_msg==" + errorMsg + "   extra_msg==" + extraMsg);
                } else if (TextUtils.equals(result, "cancel")) {
                    Intent intent = new Intent(ShopPayOrderActivity.this, ShopOrdersDetailActivity.class);
                    intent.putExtra("data", orderId);
                    startActivity(intent);
                    finish();
                } else if (TextUtils.equals(result, "invalid")) {
                    PopUtil.toastInBottom("payment plugin not installed");
                }
            }
        }
    }
}
