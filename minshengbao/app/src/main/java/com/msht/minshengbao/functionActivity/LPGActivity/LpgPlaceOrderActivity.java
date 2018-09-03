package com.msht.minshengbao.functionActivity.LPGActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
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
 * lpg订单下单
 * @author hong
 * @date 2018/7/4  
 */
public class LpgPlaceOrderActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTextPrice1;
    private TextView mTextPrice2;
    private TextView mTextPrice3;
    private TextView mTextPledge1;
    private TextView mTextPledge2;
    private TextView mTextPledge3;
    private TextView mTextTotal;
    private Button   mButtonNum1;
    private Button   mButtonNum2;
    private Button   mButtonNum3;
    private ButtonM  mButtonNext;
    private double   doublePrice1=1;
    private double   doublePrice2=1;
    private double   doublePrice3=1;
    private double   doublePledge1=1;
    private double   doublePledge2=1;
    private double   doublePledge3=1;
    private double   weightFiveTotal;
    private double   weightFifteenTotal;
    private double   weightFiftyTotal;
    private String   mTotalAmount;
    private int      mBottleNum1=0;
    private int      mBottleNum2=0;
    private int      mBottleNum3=0;
    private int      mBottleTotalCount;
    private String   siteId;
    private static final String PAGE_NAME="下单(LPG)";
    private CustomDialog customDialog;
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<LpgPlaceOrderActivity> mWeakReference;
        public RequestHandler(LpgPlaceOrderActivity activity) {
            mWeakReference = new WeakReference<LpgPlaceOrderActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgPlaceOrderActivity activity=mWeakReference.get();
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
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONObject objectInfo = object.optJSONObject("data");
                            activity.onReceiveData(objectInfo);

                        }else {
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onReceiveData(JSONObject object) {
        JSONObject depositPriceObject=object.optJSONObject("depositPrice");
        JSONObject gasPriceObject=object.optJSONObject("gasPrice");
        doublePrice1=gasPriceObject.optDouble("fivePrice");
        doublePrice2=gasPriceObject.optDouble("fifteenPrice");
        doublePrice3=gasPriceObject.optDouble("fiftyPrice");
        doublePledge1=depositPriceObject.optDouble("fiveDepositPrice");
        doublePledge2=depositPriceObject.optDouble("fifteenDepositPrice");
        doublePledge3=depositPriceObject.optDouble("fiftyDepositPrice");
        String priceText1="¥"+String.valueOf(doublePrice1);;
        String priceText2="¥"+String.valueOf(doublePrice2);;
        String priceText3="¥"+String.valueOf(doublePrice3);
        String pledgeText1=String.valueOf(doublePledge1)+"元/瓶";
        String pledgeText2=String.valueOf(doublePledge2)+"元/瓶";
        String pledgeText3=String.valueOf(doublePledge3)+"元/瓶";
        mTextPrice1.setText(priceText1);
        mTextPrice2.setText(priceText2);
        mTextPrice3.setText(priceText3);
        mTextPledge1.setText(pledgeText1);
        mTextPledge2.setText(pledgeText2);
        mTextPledge3.setText(pledgeText3);
        onOperationResult();
        mButtonNext.setEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_place_order);
        setCommonHeader("下单");
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        siteId=getIntent().getStringExtra("siteId");
        initFindViewId();
        onGetPriceData();
        onOperationResult();
    }
    private void onGetPriceData() {
        customDialog.show();
        String requestUrl= UrlUtil.LPG_QUERY_GAS_DEPOSIT_PRICE;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("siteId",siteId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,textParams,requestHandler);
    }
    private void initFindViewId() {
        mTextPrice1=(TextView)findViewById(R.id.id_tv_amount);
        mTextPrice2=(TextView)findViewById(R.id.id_tv_amount2);
        mTextPrice3=(TextView)findViewById(R.id.id_tv_amount3);
        mTextPledge1=(TextView)findViewById(R.id.id_tv_pledge1);
        mTextPledge2=(TextView)findViewById(R.id.id_tv_pledge2);
        mTextPledge3=(TextView)findViewById(R.id.id_tv_pledge3);
        mTextTotal=(TextView)findViewById(R.id.id_total_amount);

        mButtonNum1=(Button)findViewById(R.id.id_tv_num1);
        mButtonNum2=(Button)findViewById(R.id.id_tv_num2);
        mButtonNum3=(Button)findViewById(R.id.id_tv_num3);
        mButtonNext=(ButtonM)findViewById(R.id.id_btn_next);
        findViewById(R.id.id_add_btn1).setOnClickListener(this);
        findViewById(R.id.id_add_btn2).setOnClickListener(this);
        findViewById(R.id.id_add_btn3).setOnClickListener(this);
        findViewById(R.id.id_subtract_btn1).setOnClickListener(this);
        findViewById(R.id.id_subtract_btn2).setOnClickListener(this);
        findViewById(R.id.id_subtract_btn3).setOnClickListener(this);
        findViewById(R.id.id_look_detail).setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mButtonNext.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_add_btn1:
                mBottleNum1++;
                onOperationResult();
                break;
            case R.id.id_add_btn2:
                mBottleNum2++;
                onOperationResult();
                break;
            case R.id.id_add_btn3:
                mBottleNum3++;
                onOperationResult();
                break;
            case R.id.id_subtract_btn1:
                if (mBottleNum1>0){
                    mBottleNum1--;
                }
                onOperationResult();
                break;
            case R.id.id_subtract_btn2:
                if (mBottleNum2>0){
                    mBottleNum2--;
                }
                onOperationResult();
                break;
            case R.id.id_subtract_btn3:
                if (mBottleNum3>0){
                    mBottleNum3--;
                }
                onOperationResult();
                break;
            case R.id.id_btn_next:
                if (mBottleTotalCount>0){
                    onNextActivity();
                }else {
                    ToastUtil.ToastText(context,"请您选择购买瓶装气数量");
                }
                break;
            case R.id.id_look_detail:
                startHtmlWebView();
                break;
                default:
                    break;
        }
    }

    private void startHtmlWebView() {
        String url=UrlUtil.LPG_DISCOUNT_TABLE;
        Intent intent=new Intent(context, HtmlPageActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","折价说明");
        startActivity(intent);
    }
    private void onNextActivity() {
        Intent intent=new Intent(context,LpgOrderGasActivity.class);
        intent.putExtra("weightFiveNum",mBottleNum1);
        intent.putExtra("weightFifteenNum",mBottleNum2);
        intent.putExtra("weightFiftyNum",mBottleNum3);
        startActivity(intent);
    }
    private void onOperationResult(){
         mBottleTotalCount=mBottleNum1+mBottleNum2+mBottleNum3;
         weightFiveTotal=mBottleNum1*doublePrice1;
         weightFifteenTotal=mBottleNum2*doublePrice2;
         weightFiftyTotal=mBottleNum3*doublePrice3;
         double totalValue=weightFiveTotal+weightFifteenTotal+weightFiftyTotal;
         totalValue= VariableUtil.twoDecinmal2(totalValue);
         mTotalAmount="¥"+String.valueOf(totalValue);
         mButtonNum1.setText(String.valueOf(mBottleNum1));
         mButtonNum2.setText(String.valueOf(mBottleNum2));
         mButtonNum3.setText(String.valueOf(mBottleNum3));
         mTextTotal.setText(String.valueOf(mBottleTotalCount));
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
