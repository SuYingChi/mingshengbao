package com.msht.minshengbao.functionActivity.waterApp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.TypeConvertUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.adapter.water.MassFlowWaterCardTypeAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.custom.ButtonUI.ButtonM;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.Dialog.WaterRedPacketDialog;
import com.msht.minshengbao.custom.widget.CustomToast;
import com.msht.minshengbao.functionActivity.publicModule.PaySuccessActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2019/5/24  
 */
public class MassFlowWaterCardPurchaseActivity extends BaseActivity {
    private String amount;
    private String realAmount;
    private String giveFee;
    private String packId;
    private String childType;
    private String couponCode;
    private ButtonM btnPay;
    private TextView tvRealAmount;
    private TextView tvDiscount;
    private TextView tvDescribeText;
    private View     couponLayout;
    private CustomDialog customDialog;
    private MassFlowWaterCardTypeAdapter massFlowWaterCardTypeAdapter;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_flow_water_card_purchase);
        context=this;
        setCommonHeader("大流量水卡");
        customDialog=new CustomDialog(this,"正在加载...");
        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.id_data_view);
        initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        VariableUtil.mPos=0;
        massFlowWaterCardTypeAdapter=new MassFlowWaterCardTypeAdapter(context,mList);
        mRecyclerView.setAdapter(massFlowWaterCardTypeAdapter);
        initWebView();
        initData();
        massFlowWaterCardTypeAdapter.setClickCallBack(new MassFlowWaterCardTypeAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                VariableUtil.mPos=pos;
                massFlowWaterCardTypeAdapter.notifyDataSetChanged();
                amount= mList.get(pos).get("amount");
                realAmount=amount;
                giveFee = mList.get(pos).get("giveFee");
                packId=mList.get(pos).get("id");
                couponCode ="";
                String activityType=mList.get(pos).get("activityType");
                if (!TextUtils.isEmpty(activityType)){
                    switch (activityType){
                        case ConstantUtil.VALUE_EIGHT:
                            childType="6";
                            break;
                        case ConstantUtil.VALUE_SEVER:
                            childType="5";
                            break;
                        default:
                            childType="0";
                            break;
                    }
                }else {
                    childType="0";
                }
                onGetCoupons(amount);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.VALUE1:
                if (resultCode==ConstantUtil.VALUE2){
                    setResult(0x002);
                    finish();
                }
                break;
                default:
                    break;
        }
    }

    private void initView() {
        tvRealAmount=(TextView)findViewById(R.id.id_real_amount) ;
        tvDiscount=(TextView)findViewById(R.id.id_discount_text) ;
        tvDescribeText=(TextView)findViewById(R.id.id_coupon_describe) ;
        btnPay=(ButtonM)findViewById(R.id.id_btn_pay) ;
        btnPay.setEnabled(false);
        couponLayout=findViewById(R.id.id_coupon_layout);
        couponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCouponDialog();
            }
        });
        findViewById(R.id.id_btn_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WaterPayRechargeActivity.class);
                intent.putExtra("amount",amount);
                intent.putExtra("realAmount",realAmount);
                intent.putExtra("giveFee",giveFee);
                intent.putExtra("packId",packId);
                intent.putExtra("childType",childType);
                intent.putExtra("couponCode",couponCode);
                startActivityForResult(intent,1);
            }
        });
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

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        String url=UrlUtil.WATER_NEED_KNOW_TREATY;
        WebView webView=(WebView)findViewById(R.id.id_webView) ;
        WebSettings settings= webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setUseWideViewPort(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.requestFocusFromTouch();
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    view.loadUrl(request.getUrl().toString());
                }else {
                    view.loadUrl(request.toString());
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }

    private void initData() {
        String validateURL= UrlUtil.WATER_RECHARGE_MEAL;
        HashMap<String, String> textParams = new HashMap<String, String>(2);
        textParams.put("type","1");
        textParams.put("childType","1");
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                try {
                    JSONObject object = new JSONObject(data.toString());
                    String results=object.optString("result");
                    String message = object.optString("message");
                    if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                        JSONArray jsonArray =object.optJSONArray("data");
                        onAnalysisData(jsonArray);
                    }else {
                        CustomToast.showErrorLong(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void responseReqFailed(Object data) {
                CustomToast.showWarningLong(data.toString());
            }
        });
    }
    private void onAnalysisData(JSONArray jsonArray) {
        mList.clear();
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
                String validateDays="0";
                if (!json.isNull("activity")){
                    JSONObject object=json.getJSONObject("activity");
                    activityId=object.optString("id");
                    title=object.optString("title");
                    activityType=object.optString("type");
                    scope=object.optString("scope");
                    giveFee=object.optString("giveFee");
                    validateDays=object.optString("validateDays");
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
                map.put("validateDays",validateDays);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        massFlowWaterCardTypeAdapter.notifyDataSetChanged();
        if (mList!=null&&mList.size()>0){
            btnPay.setEnabled(true);
            amount= mList.get(0).get("amount");
            giveFee = mList.get(0).get("giveFee");
            packId=mList.get(0).get("id");
            realAmount=amount;
            String activityType=mList.get(0).get("activityType");
            if (!TextUtils.isEmpty(activityType)){
                switch (activityType){
                    case ConstantUtil.VALUE_EIGHT:
                        childType="6";
                        break;
                    case ConstantUtil.VALUE_SEVER:
                        childType="5";
                        break;
                        default:
                            childType="0";
                            break;
                }
            }else {
                childType="0";
            }
            String amountText="实付"+realAmount+"元";
            tvRealAmount.setText(amountText);
            onGetCoupons(amount);
        }else {
            tvRealAmount.setText("当前无大流量水卡购买");
            btnPay.setEnabled(false);
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
                    couponCode = "";
                    realAmount=amount;
                    couponLayout.setEnabled(false);
                    tvDescribeText.setText("无优惠券可用");
                    tvDiscount.setVisibility(View.GONE);
                    String discountText="已优惠0.0元";
                    tvDiscount.setText(discountText);
                    String amountText="实付"+realAmount+"元";
                    tvRealAmount.setText(amountText);
                }
            }else {
                CustomToast.showWarningLong(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            new PromptDialog.Builder(context)
                    .setTitle(R.string.my_dialog_title)
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage(message)
                    .setButton1("我知道了", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            result.cancel();
                            dialog.dismiss();
                        }
                    }).show();
            return true;
        }
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
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
