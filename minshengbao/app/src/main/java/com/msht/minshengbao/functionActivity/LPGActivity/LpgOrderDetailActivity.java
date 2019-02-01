package com.msht.minshengbao.functionActivity.LPGActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.ButtonUI.ButtonM;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class LpgOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvAddress;
    private TextView tvName;
    private TextView tvMobile;
    private TextView tvAppointmentTime;
    private TextView tvElevator;
    private TextView tvSite;
    private TextView tvStatus;
    private TextView tvOrderNo;
    private TextView tvCreateDate;
    private TextView tvFiveWeightNum;
    private TextView tvFifteenWeightNum;
    private TextView tvFiftyWeightNum;
    private TextView tvFiveWeightTotal;
    private TextView tvFifteenWeightTotal;
    private TextView tvFiftyWeightTotal;
    private TextView tvFiveWeightPledgeTotal;
    private TextView tvFifteenWeightPledgeTotal;
    private TextView tvFiftyWeightPledgeTotal;
    private TextView tvGasTotalAmount;
    private TextView tvDepositAmount;
    private TextView tvRetrieveAmount;
    private TextView tvTransportationAmount;
    private TextView tvRealAmount;
    private TextView tvDeliveryText;
    private ButtonM  btnCancel,btnPayFee;
    private View     layoutDelivery;
    private View     layoutPayAmount;
    private View     fiveWeightGasView;
    private View     fifteenWeightGasView;
    private View     fiftyWeightGasView;
    private View     fiveWeightPledgeView;
    private View     fifteenWeightPledgeView;
    private View     fiftyWeightPledgeView;

    private String orderId;
    private String realAmount;
    private int requestCode=0;
    private static final int PAY_SUCCESS_CODE=0x001;
    private static final int PAY_CANCEL_CODE=0X002;
    private static final String PAGE_NAME="订单详情(LPG)";
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);

    private static class RequestHandler extends Handler {
        private WeakReference<LpgOrderDetailActivity> mWeakReference;
        public RequestHandler(LpgOrderDetailActivity activity) {
            mWeakReference = new WeakReference<LpgOrderDetailActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgOrderDetailActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("msg");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                JSONObject jsonObject =object.optJSONObject("data");
                                activity.onReceiveOrderData(jsonObject);
                            }else if (activity.requestCode==1){
                                activity.onCancelSuccess();
                            }
                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.onFailure(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onCancelSuccess() {
        new PromptDialog.Builder(this)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("订单已取消")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        setResult(0x002);
                        finish();
                    }
                }).show();
    }
    private void onFailure(String error) {

        new PromptDialog.Builder(this)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onReceiveOrderData(JSONObject jsonObject) {
        String addressName=jsonObject.optString("addressName");
        String siteName=jsonObject.optString("siteName");
        String buyer=jsonObject.optString("buyer");
        String mobile=jsonObject.optString("mobile");
        String retrieveAmount="¥"+jsonObject.optString("retrieveAmount");
        int isElevator=jsonObject.optInt("isElevator");
        String deliveryTime=jsonObject.optString("deliveryTime");
        realAmount=jsonObject.optString("realAmount");
        int orderStatus=jsonObject.optInt("orderStatus");

        int fiveBottleCount=jsonObject.optInt("fiveBottleCount");
        int fifteenBottleCount=jsonObject.optInt("fifteenBottleCount");
        int fiftyBottleCount=jsonObject.optInt("fiftyBottleCount");

        double payFiveAmount=jsonObject.optDouble("payFiveAmount");
        double payFifteenAmount=jsonObject.optDouble("payFifteenAmount");
        double payFiftyAmount=jsonObject.optDouble("payFiftyAmount");

        double fiveDepositFee=jsonObject.optDouble("fiveDepositFee");
        double fifteenDepositFee=jsonObject.optDouble("fifteenDepositFee");
        double fiftyDepositFee=jsonObject.optDouble("fiftyDepositFee");

        String depositAmount=jsonObject.optString("depositAmount");
        String deliveryAmount=jsonObject.optString("deliveryAmount");
        String orderId=jsonObject.optString("orderId");
        String createDate=jsonObject.optString("createDate");
        double totalAmount=payFiveAmount+payFifteenAmount+payFiftyAmount;
        totalAmount=VariableUtil.twoDecinmal2(totalAmount);
        String gasTotalAmountText="¥"+String.valueOf(totalAmount);
        String realAmountText="¥"+realAmount;
        String depositAmountText="¥"+depositAmount;
        String deliveryTimeText="预约时间："+deliveryTime;
        String fiveBottleCountText="x"+fiveBottleCount;
        String fifteenBottleCountText="x"+fifteenBottleCount;
        String fiftyBottleCountText="x"+fiftyBottleCount;
        String fiveDepositFeeText="¥"+fiveDepositFee;
        String fifteenDepositFeeText="¥"+fifteenDepositFee;
        String fiftyDepositFeeText="¥"+fiftyDepositFee;
        String payFiveAmountText="¥"+String.valueOf(payFiveAmount);
        String payFifteenAmountText="¥"+String.valueOf(payFifteenAmount);
        String payFiftyAmountText="¥"+String.valueOf(payFiftyAmount);
        String deliveryAmountText="¥"+deliveryAmount;
        tvOrderNo.setText(orderId);
        tvAddress.setText(addressName);
        tvCreateDate.setText(createDate);
        tvAppointmentTime.setText(deliveryTimeText);
        tvMobile.setText(mobile);
        tvName.setText(buyer);
        tvSite.setText(siteName);
        tvRetrieveAmount.setText(retrieveAmount);
        tvDepositAmount.setText(depositAmountText);
        tvFiveWeightNum.setText(fiveBottleCountText);
        tvFifteenWeightNum.setText(fifteenBottleCountText);
        tvFiftyWeightNum.setText(fiftyBottleCountText);
        tvFiveWeightTotal.setText(payFiveAmountText);
        tvFifteenWeightTotal.setText(payFifteenAmountText);
        tvFiftyWeightTotal.setText(payFiftyAmountText);
        tvFiveWeightPledgeTotal.setText(fiveDepositFeeText);
        tvFifteenWeightPledgeTotal.setText(fifteenDepositFeeText);
        tvFiftyWeightPledgeTotal.setText(fiftyDepositFeeText);
        tvGasTotalAmount.setText(gasTotalAmountText);
        tvTransportationAmount.setText(deliveryAmountText);
        tvDepositAmount.setText(depositAmountText);
        tvRealAmount.setText(realAmountText);
        if (fiveBottleCount==0){
           fiveWeightGasView.setVisibility(View.GONE);
        }else {
            fiveWeightGasView.setVisibility(View.VISIBLE);
        }
        if (fifteenBottleCount==0){
            fifteenWeightGasView.setVisibility(View.GONE);
        }else {
            fifteenWeightGasView.setVisibility(View.VISIBLE);
        }
        if (fifteenBottleCount==0){
            fiftyWeightGasView.setVisibility(View.GONE);
        }else {
            fiftyWeightGasView.setVisibility(View.VISIBLE);
        }
        if (fiveDepositFee==0){
            fiveWeightPledgeView.setVisibility(View.GONE);
        }else {
            fiveWeightPledgeView.setVisibility(View.VISIBLE);
        }
        if (fifteenDepositFee==0){
            fifteenWeightPledgeView.setVisibility(View.GONE);
        }else {
            fifteenWeightPledgeView.setVisibility(View.VISIBLE);
        }
        if (fiftyDepositFee==0){
            fiftyWeightPledgeView.setVisibility(View.GONE);
        }else {
            fiftyWeightPledgeView.setVisibility(View.VISIBLE);
        }
        if (isElevator==0){
            tvElevator.setText("无电梯");
        }else {
            tvElevator.setText("有电梯");
        }
        switch (orderStatus){
            case 0:
                tvDeliveryText.setText("您的订单已送达，请完成支付...");
                tvStatus.setText("待支付");
                btnCancel.setVisibility(View.GONE);
                layoutPayAmount.setVisibility(View.VISIBLE);
                layoutDelivery.setVisibility(View.GONE);
                break;
            case 1:
                tvStatus.setText("待发货");
                btnCancel.setVisibility(View.VISIBLE);
                layoutPayAmount.setVisibility(View.GONE);
                layoutDelivery.setVisibility(View.GONE);
                break;
            case 2:
                tvDeliveryText.setText("您的订单正在配送途中，请您准备查收...");
                tvStatus.setText("已发货");
                btnPayFee.setVisibility(View.GONE);
                layoutPayAmount.setVisibility(View.GONE);
                layoutDelivery.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvDeliveryText.setText("感谢您使用瓶装气业务，期待您再次使用！");
                tvStatus.setText("已完成");
                btnPayFee.setVisibility(View.GONE);
                layoutPayAmount.setVisibility(View.GONE);
                layoutDelivery.setVisibility(View.VISIBLE);
                break;
            case 4:
                tvStatus.setText("已退款");
                btnPayFee.setVisibility(View.GONE);
                layoutPayAmount.setVisibility(View.GONE);
                layoutDelivery.setVisibility(View.VISIBLE);
                break;
            case 5:
                tvStatus.setText("已取消");
                btnPayFee.setVisibility(View.GONE);
                layoutPayAmount.setVisibility(View.GONE);
                layoutDelivery.setVisibility(View.GONE);
                break;
            case 6:
                tvDeliveryText.setText("您的订单正在配送途中，请您准备查收...");
                tvStatus.setText("待收货");
                btnPayFee.setVisibility(View.GONE);
                layoutPayAmount.setVisibility(View.GONE);
                layoutDelivery.setVisibility(View.VISIBLE);
                break;
            case 7:
                tvDeliveryText.setText("您的订单已送达，请完成支付...");
                tvStatus.setText("已收货");
                btnPayFee.setVisibility(View.GONE);
                layoutPayAmount.setVisibility(View.GONE);
                layoutDelivery.setVisibility(View.VISIBLE);
                break;
                default:
                    break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_order_detail);
        setCommonHeader("订单详情");
        context=this;
        Intent data=getIntent();
        if (data!=null){
            orderId=data.getStringExtra("orderId");
        }
        customDialog=new CustomDialog(this, "正在加载");
        initFindViewId();
        initOrderData();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case PAY_SUCCESS_CODE:
                setResult(PAY_SUCCESS_CODE);
                finish();
                break;
            case PAY_CANCEL_CODE:
                break;
                default:
                    break;
        }
    }
    private void initFindViewId() {
        tvOrderNo=(TextView)findViewById(R.id.id_tv_orderNo);
        tvCreateDate=(TextView)findViewById(R.id.id_place_time);
        tvAddress = (TextView) findViewById(R.id.id_tv_address);
        tvElevator=(TextView)findViewById(R.id.id_tv_elevator);
        tvName = (TextView) findViewById(R.id.id_tv_username);
        tvMobile = (TextView) findViewById(R.id.id_tv_mobile);
        tvAppointmentTime = (TextView) findViewById(R.id.id_delivery_time);
        tvSite = (TextView) findViewById(R.id.id_tv_siteName);
        tvStatus = (TextView) findViewById(R.id.id_tv_status);
        tvFiveWeightNum = (TextView) findViewById(R.id.id_bottle_num1);
        tvFifteenWeightNum = (TextView) findViewById(R.id.id_bottle_num2);
        tvFiftyWeightNum = (TextView) findViewById(R.id.id_bottle_num3);
        tvFiveWeightTotal=(TextView)findViewById(R.id.id_five_amount) ;
        tvFifteenWeightTotal=(TextView)findViewById(R.id.id_fifteen_amount);
        tvGasTotalAmount= (TextView) findViewById(R.id.id_gas_total_amount);
        tvFiftyWeightTotal=(TextView)findViewById(R.id.id_fifty_amount);
        tvFiveWeightPledgeTotal=(TextView)findViewById(R.id.id_pledge_amount1);
        tvFifteenWeightPledgeTotal=(TextView)findViewById(R.id.id_pledge_amount2);
        tvFiftyWeightPledgeTotal=(TextView)findViewById(R.id.id_pledge_amount3);
        tvDepositAmount= (TextView) findViewById(R.id.id_pledge_amount);
        tvTransportationAmount= (TextView) findViewById(R.id.id_transportation_amount);
        tvRetrieveAmount=(TextView)findViewById(R.id.id_retrieveAmount);
        tvRealAmount= (TextView) findViewById(R.id.id_real_amount);
        tvDeliveryText=(TextView)findViewById(R.id.id_delivery_status);
        btnCancel=(ButtonM)findViewById(R.id.id_btn_cancel);
        btnPayFee=(ButtonM)findViewById(R.id.id_btn_pay);
        layoutDelivery=findViewById(R.id.id_layout_delivery);
        layoutPayAmount=findViewById(R.id.id_layout_pay);
        fiveWeightGasView=findViewById(R.id.id_fiveWeightGas_view);
        fifteenWeightGasView=findViewById(R.id.id_fifteenWeightGas_view);
        fiftyWeightGasView=findViewById(R.id.id_fiftyWeightGas_view);
        fiveWeightPledgeView=findViewById(R.id.id_fiveWeightPledge_view);
        fifteenWeightPledgeView=findViewById(R.id.id_fifteenWeightPledge_view);
        fiftyWeightPledgeView=findViewById(R.id.id_fiftyWeightPledge_view);
        findViewById(R.id.id_layout_replace_bottle).setOnClickListener(this);
        findViewById(R.id.id_transportation_img).setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnPayFee.setOnClickListener(this);
        layoutDelivery.setOnClickListener(this);
    }
    private void initOrderData() {
        requestCode=0;
        String orderType="1";
        customDialog.show();
        String validateURL= UrlUtil.LPG_QUERY_ORDER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("id",orderId);
        textParams.put("orderType",orderType);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_cancel:
                onCancelOrder();
                break;
            case R.id.id_btn_pay:
                onPayOrderFee();
                break;
            case R.id.id_layout_replace_bottle:
                goReplaceBottleInformation();
                break;
            case R.id.id_layout_delivery:
                goDeliveryInformation();
                break;
            case R.id.id_transportation_img:
                startHtmlWebView();
                break;
                default:
                    break;
        }
    }
    private void startHtmlWebView() {
        String url=UrlUtil.LPG_TRANSPORTATION_EXPENSE;
        Intent intent=new Intent(context, HtmlPageActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","运费说明");
        startActivity(intent);
    }
    private void goDeliveryInformation() {
        Intent intent=new Intent(context, LpgDeliveryInformationActivity.class);
        intent.putExtra("orderId",orderId);
        startActivity(intent);
    }
    private void goReplaceBottleInformation() {
        Intent intent=new Intent(context, LpgReplaceBottleListActivity.class);
        intent.putExtra("orderId",orderId);
        startActivityForResult(intent,PAY_SUCCESS_CODE);
    }
    private void onPayOrderFee() {
        Intent intent=new Intent(context, LpgPayOrderActivity.class);
        intent.putExtra("realAmount",realAmount);
        intent.putExtra("orderId",orderId);
        startActivityForResult(intent,PAY_SUCCESS_CODE);
    }
    private void onCancelOrder() {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("请确认是否取消订单")
                .setButton1("残忍取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        requestService();
                        dialog.dismiss();
                    }
                })
                .setButton2("算了", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void requestService() {
        customDialog.show();
        requestCode=1;
        String validateURL= UrlUtil.LPG_FAIL_ORDER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("id",orderId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
