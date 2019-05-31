package com.msht.minshengbao.functionActivity.waterApp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Utils.MathUtil;
import com.msht.minshengbao.Utils.TypeConvertUtil;
import com.msht.minshengbao.custom.ButtonUI.ButtonM;
import com.msht.minshengbao.custom.Dialog.WaterRedPacketDialog;
import com.msht.minshengbao.custom.widget.CustomToast;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.widget.MyNoScrollGridView;
import com.msht.minshengbao.custom.widget.MyScrollview;
import com.msht.minshengbao.custom.widget.VerticalSwipeRefreshLayout;
import com.msht.minshengbao.adapter.WaterMealAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterBalanceActivity extends BaseActivity implements View.OnClickListener, MyScrollview.ScrollViewListener {
    private MyNoScrollGridView mGridView;
    private TextView tvTotalAmount;
    private TextView tvGiveAmount;
    private TextView tvMassFlowName;
    private TextView tvLimitDate;
    private String amount;
    private String realAmount;
    private String giveFee;
    private String packId;
    private String childType;
    private String couponCode;
    private int bgHeight;
    private View    headBgLayout;
    private ButtonM btnPay;
    private TextView tvRealAmount;
    private TextView tvDiscount;
    private TextView tvDescribeText;
    private View     couponLayout;
    private Button   btnPurchase;
    private View     layoutPay;
    private View     layoutMassFlow;
    private MyScrollview myScrollview;
    private View  layoutNavigation;
    private WaterMealAdapter waterMealAdapter;
    private VerticalSwipeRefreshLayout mSwipeRefresh;
    private ArrayList<HashMap<String, String>> mListType = new ArrayList<HashMap<String, String>>();
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private final BalanceHandler balanceHandler=new BalanceHandler(this);


    private static class BalanceHandler extends Handler {
        private WeakReference< WaterBalanceActivity> mWeakReference;
         BalanceHandler( WaterBalanceActivity activity) {
            mWeakReference=new WeakReference< WaterBalanceActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final  WaterBalanceActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }else {
                if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                    activity.customDialog.dismiss();
                }
                switch (msg.what) {
                    case SendRequestUtil.SUCCESS:
                        try {
                            JSONObject object = new JSONObject(msg.obj.toString());
                            String results=object.optString("result");
                            String message = object.optString("message");
                            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                                activity.mSwipeRefresh.setRefreshing(false);
                                JSONObject json =object.optJSONObject("data");
                                activity.onReceiveAccountData(json);
                            }else {
                                CustomToast.showWarningLong(message);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case SendRequestUtil.FAILURE:
                        activity.mSwipeRefresh.setRefreshing(false);
                        ToastUtil.ToastText(activity.context,msg.obj.toString());
                        break;
                    default:
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }
    private void onReceiveAccountData(JSONObject json) {
        String type=json.optString("type");
        String accounts=json.optString("account");
        String payAmount=json.optString("payBalance");
        String giveAmount="(含赠送"+json.optString("giveBalance")+")";
        String massFlowName="大流量水卡("+json.optString("descType")+")·"+json.optString("waterQuantity")+"升";
        String validateDays="有效期"+json.optString("validateDays")+"天";
        int isFlow=json.optInt("isFlow");
        double payBalance=json.optDouble("payBalance");
        double giveBalance=json.optDouble("giveBalance");
        double totalBalance=payBalance+giveBalance;
        totalBalance=MathUtil.getDoubleDecimal(totalBalance,2);
        String totalAmount=String.valueOf(totalBalance);
        VariableUtil.waterAccount=accounts;
        tvGiveAmount.setText(giveAmount);
        tvTotalAmount.setText(totalAmount);
        if (isFlow==1){
            btnPurchase.setVisibility(View.GONE);
            tvMassFlowName.setText(massFlowName);
            tvLimitDate.setText(validateDays);
            layoutMassFlow.setEnabled(false);
        }else {
            tvMassFlowName.setText("民生水宝大流量水卡");
            btnPurchase.setVisibility(View.VISIBLE);
            tvLimitDate.setText("买卡用水更划算");
            layoutMassFlow.setEnabled(true);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_balance);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        mPageName="我的余额(水宝)";
        setCommonHeader(mPageName);
        initView();
        initRefresh();
        initSlideListener();
        waterMealAdapter=new WaterMealAdapter(context, mListType);
        mGridView.setAdapter(waterMealAdapter);
        initData();
        initRechargeData();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VariableUtil.MealPos=i;
                btnPay.setEnabled(true);
                waterMealAdapter.notifyDataSetChanged();
                amount= mListType.get(i).get("amount");
                giveFee =mListType.get(i).get("giveFee");
                packId=mListType.get(i).get("id");
                childType="0";
                couponCode ="";
                realAmount=amount;
                tvRealAmount.setText(realAmount);
                layoutPay.setVisibility(View.VISIBLE);
                onGetCoupons(amount);
            }
        });
    }

    private void initSlideListener() {
        ViewTreeObserver vto = headBgLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headBgLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                bgHeight = headBgLayout.getHeight()-20;
                initSetListener();
            }
        });
    }

    private void initSetListener() {
        myScrollview.setScrollViewListener(this);
    }
    @Override
    public void onScrollChanged(MyScrollview scrollView, int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            //设置标题的背景颜色
            layoutNavigation.setBackgroundColor(Color.argb(0, 0, 255, 0));
            //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
        } else if ( t <= bgHeight) {
            float scale = (float) t / bgHeight;
            float alpha = (255 * scale);
            layoutNavigation.setBackgroundColor(Color.argb((int) alpha, 27, 141, 224));
        } else {
            //滑动到banner下面设置普通颜色
            layoutNavigation.setBackgroundResource(R.drawable.shape_change_blue_bg);

        }
    }

    private void onGetCoupons(String amount) {
        if (customDialog!=null&&!customDialog.isShowing()){
            customDialog.show();;
        }
        String validateURL = UrlUtil.WATER_GET_COUPON;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("account",VariableUtil.waterAccount);
        textParams.put("type","0");
        textParams.put("amount",amount);
        textParams.put("pageNo","1");
        textParams.put("pageSize","16");
        OkHttpRequestManager.getInstance(context.getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                onCouponsData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                CustomToast.showWarningLong(data.toString());
            }
        });
    }

    private void onCouponsData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("message");
            JSONObject jsonObject =object.optJSONObject("data");
            boolean lastPage=jsonObject.optBoolean("lastPage");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                JSONArray jsonArray=jsonObject.optJSONArray("list");
                if (jsonArray!=null&&jsonArray.length()>0){
                    JSONObject json = jsonArray.getJSONObject(0);
                    couponCode = json.getString("code");
                    String discountAmount=json.optString("discountAmount");
                    realAmount=String.valueOf(TypeConvertUtil.convertToDouble(amount,0)-TypeConvertUtil.convertToDouble(discountAmount,0));
                    String amountText="实付"+realAmount+"元";
                    String discountText="已优惠"+discountAmount+"元";
                    tvRealAmount.setText(amountText);
                    String describe="充值红包满"+amount+"减"+discountAmount;
                    tvDescribeText.setText(describe);
                    tvDiscount.setVisibility(View.VISIBLE);
                    tvDiscount.setText(discountText);
                    couponLayout.setEnabled(true);
                }else {
                    couponCode ="";
                    realAmount=amount;
                    String amountText="实付"+realAmount+"元";
                    tvRealAmount.setText(amountText);
                    couponLayout.setEnabled(false);
                    tvDescribeText.setText("无优惠券可用");
                    tvDiscount.setVisibility(View.GONE);
                    String discountText="已优惠0.0元";
                    tvDiscount.setText(discountText);
                }
            }else {
                CustomToast.showWarningLong(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initRechargeData() {
        String validateURL= UrlUtil.WATER_RECHARGE_MEAL;
        HashMap<String, String> textParams = new HashMap<String, String>(2);
        textParams.put("type","1");
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                try {
                    JSONObject object = new JSONObject(data.toString());
                    String results=object.optString("result");
                    String message = object.optString("message");
                    if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                        JSONArray jsonArray =object.optJSONArray("data");
                        saveData(jsonArray);
                    }else {
                       ToastUtil.ToastText(context, message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void responseReqFailed(Object data) {
                ToastUtil.ToastText(context, data.toString());
            }
        });

    }
    private void saveData(JSONArray jsonArray) {
        VariableUtil.MealPos=-1;
        mListType.clear();
        waterMealAdapter.notifyDataSetChanged();
        layoutPay.setVisibility(View.GONE);
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String id = json.getString("id");
                String type=json.getString("type");
                String amount=json.getString("amount");
                String waterQuantity=json.optString("waterQuantity");
                String activityId="";
                String title="";
                String activityType="";
                String scope="";
                String giveFee="0";
                if (!json.isNull("activity")){
                    JSONObject object=json.getJSONObject("activity");
                    activityId=object.optString("id");
                    title=object.optString("title");
                    activityType=object.optString("type");
                    scope=object.optString("scope");
                    giveFee=object.optString("giveFee");
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("type",type);
                map.put("amount",amount);
                map.put("waterQuantity",waterQuantity);
                map.put("activityId",activityId);
                map.put("title",title);
                map.put("activityType",activityType);
                map.put("scoped",scope);
                map.put("giveFee",giveFee);
                mListType.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mListType.size()!=0){
            waterMealAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.VALUE2:
                if (resultCode==ConstantUtil.VALUE1){
                    layoutPay.setVisibility(View.GONE);
                    VariableUtil.MealPos=-1;
                    waterMealAdapter.notifyDataSetChanged();
                    initData();
                    setResult(1);
                }
                break;
                case ConstantUtil.VALUE1:
                    if (resultCode==ConstantUtil.VALUE2){
                        layoutPay.setVisibility(View.GONE);
                        VariableUtil.MealPos=-1;
                        waterMealAdapter.notifyDataSetChanged();
                        initData();
                        setResult(1);
                    }
                    break;
                default:
                    break;
        }
    }
    private void initRefresh() {
        mSwipeRefresh.setProgressViewEndTarget(false,100);
        mSwipeRefresh.setProgressViewOffset(false,2,20);
        mSwipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefresh.setEnabled(true);
        mSwipeRefresh.setProgressBackgroundColorSchemeResource(R.color.transparent_Orange);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                initRechargeData();
            }
        });
    }
    private void initData() {
        customDialog.show();
        String validateURL= UrlUtil.WATER_ACCOUNT_URL;
        Map<String, String> textParams = new HashMap<String, String>(1);
        textParams.put("account",VariableUtil.waterAccount);
        textParams.put("type","0");
        SendRequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }
    private void initView() {
        layoutNavigation=findViewById(R.id.id_re_layout);
        layoutNavigation.setBackgroundResource(R.color.transparent);
        myScrollview = (MyScrollview)findViewById(R.id.id_scrollview);
        mSwipeRefresh=(VerticalSwipeRefreshLayout)findViewById(R.id.id_swipe_refresh);
        rightImg=(ImageView)findViewById(R.id.id_right_img);
        mGridView=(MyNoScrollGridView)findViewById(R.id.id_recharge_view);
        tvGiveAmount =(TextView)findViewById(R.id.id_give_fee);
        tvTotalAmount =(TextView)findViewById(R.id.id_total_amount);
        tvMassFlowName=(TextView)findViewById(R.id.id_card_type);
        tvLimitDate=(TextView)findViewById(R.id.id_limit_date);
        btnPurchase=(Button)findViewById(R.id.id_btn_purchase);
        findViewById(R.id.id_tv_detail).setOnClickListener(this);
        findViewById(R.id.id_forward_img).setOnClickListener(this);
        layoutMassFlow=findViewById(R.id.id_mass_flow_layout);
        layoutMassFlow.setOnClickListener(this);
        layoutMassFlow.setEnabled(false);
        headBgLayout=findViewById(R.id.id_head_bg);
        tvRealAmount=(TextView)findViewById(R.id.id_real_amount) ;
        tvDiscount=(TextView)findViewById(R.id.id_discount_text) ;
        tvDescribeText=(TextView)findViewById(R.id.id_coupon_describe) ;
        btnPay=(ButtonM)findViewById(R.id.id_btn_pay) ;
        btnPay.setEnabled(false);
        couponLayout=findViewById(R.id.id_coupon_layout);
        layoutPay=findViewById(R.id.id_pay_layout);
        btnPurchase.setOnClickListener(this);
        layoutPay.setVisibility(View.GONE);
        couponLayout.setOnClickListener(this);
        btnPay.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_forward_img:
                break;
            case R.id.id_tv_detail:
                onRecharge();
                break;
            case R.id.id_btn_purchase:
                onMassFlowCard();
                break;
            case R.id.id_coupon_layout:
                onCouponDialog();
                break;
            case R.id.id_btn_pay:
                onRechargePay();
                break;
                default:
                    break;
        }
    }

    private void onRechargePay() {
        Intent intent=new Intent(context,WaterPayRechargeActivity.class);
        intent.putExtra("amount",amount);
        intent.putExtra("realAmount",realAmount);
        intent.putExtra("giveFee",giveFee);
        intent.putExtra("packId",packId);
        intent.putExtra("childType",childType);
        intent.putExtra("couponCode",couponCode);
        startActivityForResult(intent,1);
    }

    private void onCouponDialog() {

        new WaterRedPacketDialog(context,amount).builder()
                .setCanceledOnTouchOutside(true)
                .setOnSheetButtonOneClickListener(new WaterRedPacketDialog.OnSheetButtonOneClickListener() {
                    @Override
                    public void onSelectClick(String code, String discountAmount) {
                        couponCode=code;
                        realAmount=String.valueOf(TypeConvertUtil.convertToDouble(amount,0)-TypeConvertUtil.convertToDouble(discountAmount,0));
                        String amountText="实付"+realAmount+"元";
                        String discountText="已优惠"+discountAmount+"元";
                        tvRealAmount.setText(amountText);
                        if (TypeConvertUtil.convertToDouble(discountAmount,0)==0){
                            String describe="未使用红包优惠券";
                            tvDescribeText.setText(describe);
                            tvDiscount.setVisibility(View.GONE);
                        }else {
                            String describe="充值红包满"+amount+"减"+discountAmount;
                            tvDescribeText.setText(describe);
                            tvDiscount.setVisibility(View.VISIBLE);
                            tvDiscount.setText(discountText);
                        }
                    }
                })
                .show();
    }

    private void onMassFlowCard() {
        Intent intent=new Intent(context,MassFlowWaterCardPurchaseActivity.class);
        startActivityForResult(intent,1);
    }

    private void onRecharge() {
        Intent intent=new Intent(context,WaterBalanceDetailActivity.class);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }
}
