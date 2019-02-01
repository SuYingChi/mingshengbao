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
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.ButtonUI.ButtonM;
import com.msht.minshengbao.ViewUI.Dialog.ActionSheetDialog;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/25  
 */
public class LpgDepositReturnActivity extends BaseActivity implements View.OnClickListener {
    private ButtonM  mButtonSend;
    private Button   mButtonNum1;
    private Button   mButtonNum2;
    private Button   mButtonNum3;
    private TextView tvFiveCount;
    private TextView tvFifteenCount;
    private TextView tvFiftyCount;
    private TextView tvDeliveryTime;
    private TextView tvElevator;
    private TextView tvAddress;
    private TextView tvDepositCount;
    private int   mPledgeMax1=0;
    private int   mPledgeMax2=0;
    private int   mPledgeMax3=0;
    private int   mBottleNum1=0;
    private int   mBottleNum2=0;
    private int   mBottleNum3=0;
    private int   totalCount=0;
    private int   requestCode=0;
    private String lpgUserName;
    private String lpgMobile;
    private String lpgUserId;
    private String lpgSex;
    private String userId;
    private String siteId;
    private String addressName;
    private String addressShort;
    private String longitude;
    private String latitude;
    private String isElevator;
    private String floors;
    private String unit;
    private String roomNum;
    private String city;
    private String area;
    private CustomDialog customDialog;
    private static final String PAGE_NAME="押金退还";
    private static final int SELECT_ADDRESS_CODE=1;
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<LpgDepositReturnActivity> mWeakReference;
        public RequestHandler(LpgDepositReturnActivity activity) {
            mWeakReference = new WeakReference<LpgDepositReturnActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgDepositReturnActivity activity=mWeakReference.get();
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
                                activity.onReceiveData(object);
                            }else if (activity.requestCode==1){
                                String orderId=object.optString("orderId");
                                activity.onSendSuccess();
                            }
                        }else {
                            if (activity.requestCode==0){
                                activity.mButtonSend.setEnabled(false);
                            }
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    if (activity.requestCode==0){
                        activity.mButtonSend.setEnabled(false);
                    }
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onSendSuccess() {
        new PromptDialog.Builder(this)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("订单已提交成功")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        setResult(0x001);
                        finish();
                    }
                }).show();
    }
    private void onReceiveData(JSONObject object) {
        mPledgeMax1=object.optInt("fiveDepositCount");
        mPledgeMax2=object.optInt("fifteenDepositCount");
        mPledgeMax3=object.optInt("fiveDepositCount");
        tvFiveCount.setText(String.valueOf(mPledgeMax1));
        tvFifteenCount.setText(String.valueOf(mPledgeMax2));
        tvFiftyCount.setText(String.valueOf(mPledgeMax3));
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_deposit_return);
        context=this;
        setCommonHeader(PAGE_NAME);
        customDialog=new CustomDialog(this, "正在加载");
        lpgUserId= SharedPreferencesUtil.getStringData(this, SharedPreferencesUtil.LPG_USER_ID,"");
        lpgMobile= SharedPreferencesUtil.getStringData(this, SharedPreferencesUtil.LPG_MOBILE,"");
        lpgUserName= SharedPreferencesUtil.getStringData(this, SharedPreferencesUtil.LPG_USER_NAME,"");
        lpgSex= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.LPG_SEX,"");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        initFindViewId();
        initDepositCountData();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECT_ADDRESS_CODE:
                if (resultCode==SELECT_ADDRESS_CODE){
                    if (data!=null){
                        mButtonSend.setEnabled(true);
                        siteId=data.getStringExtra("siteId");
                        addressName=data.getStringExtra("addressName");
                        addressShort=data.getStringExtra("addressShort");
                        longitude=data.getStringExtra("longitude");
                        latitude=data.getStringExtra("latitude");
                        floors=data.getStringExtra("floor");
                        isElevator=data.getStringExtra("isElevator");
                        unit=data.getStringExtra("unit");;
                        roomNum=data.getStringExtra("roomNum");
                        city=data.getStringExtra("city");
                        area=data.getStringExtra("area");
                        tvAddress.setText(addressName);
                        if (isElevator.equals(VariableUtil.VALUE_ONE)){
                            tvElevator.setText("有电梯");
                        }else {
                            tvElevator.setText("无电梯");
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
    private void initDepositCountData() {
        requestCode=0;
        customDialog.show();
        String requestUrl= UrlUtil.LPG_DEPOSIT_COUNT_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",lpgUserId);
        OkHttpRequestUtil.getInstance(context).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initFindViewId() {
        tvDeliveryTime=(TextView)findViewById(R.id.id_delivery_time);
        tvElevator=(TextView)findViewById(R.id.id_tv_elevator);
        tvAddress=(TextView)findViewById(R.id.id_tv_address);
        tvDepositCount=(TextView)findViewById(R.id.id_total_count);
        mButtonNum1=(Button)findViewById(R.id.id_tv_num1);
        mButtonNum2=(Button)findViewById(R.id.id_tv_num2);
        mButtonNum3=(Button)findViewById(R.id.id_tv_num3);
        mButtonSend=(ButtonM)findViewById(R.id.id_btn_send);
        tvFiveCount=(TextView)findViewById(R.id.id_tv_pledge1);
        tvFifteenCount=(TextView)findViewById(R.id.id_tv_pledge2);
        tvFiftyCount=(TextView)findViewById(R.id.id_tv_pledge3);
        TextView tvName=(TextView)findViewById(R.id.id_tv_username);
        TextView tvPhone=(TextView)findViewById(R.id.id_tv_mobile);
        mButtonSend.setEnabled(false);
        mButtonSend.setOnClickListener(this);
        ((TextView)findViewById(R.id.id_pledge_text1)).setText("数量:");
        ((TextView)findViewById(R.id.id_pledge_text2)).setText("数量:");
        ((TextView)findViewById(R.id.id_pledge_text3)).setText("数量:");
        findViewById(R.id.id_tv_amount).setVisibility(View.GONE);
        findViewById(R.id.id_tv_amount2).setVisibility(View.GONE);
        findViewById(R.id.id_tv_amount3).setVisibility(View.GONE);
        findViewById(R.id.id_add_btn1).setOnClickListener(this);
        findViewById(R.id.id_add_btn2).setOnClickListener(this);
        findViewById(R.id.id_add_btn3).setOnClickListener(this);
        findViewById(R.id.id_subtract_btn1).setOnClickListener(this);
        findViewById(R.id.id_subtract_btn2).setOnClickListener(this);
        findViewById(R.id.id_subtract_btn3).setOnClickListener(this);
        findViewById(R.id.id_select_address_layout).setOnClickListener(this);
        findViewById(R.id.id_select_time_layout).setOnClickListener(this);
        tvDeliveryTime.setText("立即上门");
        tvAddress.setHint("请您选择上门地址");
        tvName.setText(lpgUserName);
        tvPhone.setText(lpgMobile);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_add_btn1:
                if (mBottleNum1<mPledgeMax1){
                    mBottleNum1++;
                }
                onOperationResult();
                break;
            case R.id.id_add_btn2:
                if (mBottleNum2<mPledgeMax2){
                    mBottleNum2++;
                }
                onOperationResult();
                break;
            case R.id.id_add_btn3:
                if (mBottleNum3<mPledgeMax3){
                    mBottleNum3++;
                }
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
            case R.id.id_btn_send:
                if (totalCount>0){
                    onSendServer();
                }else {
                   ToastUtil.ToastText(context,"请添加钢瓶个数！");
                }
                break;
            case R.id.id_select_address_layout:
                onSelectAddress();
                break;
            case R.id.id_select_time_layout:
                onSelectTime();
                break;
            default:
                break;
        }
    }

    private void onSelectTime() {
        new ActionSheetDialog(context)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setOnSheetButtonOneClickListener(new ActionSheetDialog.OnSheetButtonOneClickListener() {
                    @Override
                    public void onClick(String text) {
                        tvDeliveryTime.setText(text);
                    }
                }).show();
    }
    private void onSelectAddress() {
        Intent intent=new Intent(context,LpgSelectAddressActivity.class);
        startActivityForResult(intent,SELECT_ADDRESS_CODE);
    }
    private void onSendServer() {
        requestCode=1;
        String orderType="0";
        String orderSource="1";
        String deliveryTime=tvDeliveryTime.getText().toString().trim();
        customDialog.show();
        String requestUrl= UrlUtil.LPG_CREATE_NEW_ORDER;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",lpgUserId);
        textParams.put("msbUserId",userId);
        textParams.put("orderType",orderType);
        textParams.put("orderSource",orderSource);
        textParams.put("deliveryTime",deliveryTime);
        textParams.put("mobile",lpgMobile);
        textParams.put("sex",lpgSex);
        textParams.put("reFiveBottleCount",String.valueOf(mBottleNum1));
        textParams.put("reFifteenBottleCount",String.valueOf(mBottleNum2));
        textParams.put("reFiftyBottleCount",String.valueOf(mBottleNum3));
        textParams.put("buyer",lpgUserName);
        textParams.put("addressName",addressName);
        textParams.put("addressShort",addressShort);
        textParams.put("longitude",longitude);
        textParams.put("latitude",latitude);
        textParams.put("isElevator",isElevator);
        textParams.put("floor",floors);
        textParams.put("unit",unit);
        textParams.put("roomNum",roomNum);
        textParams.put("city",city);
        textParams.put("area",area);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onOperationResult() {
        totalCount=mBottleNum1+mBottleNum2+mBottleNum3;
        String totalCountText=String.valueOf(totalCount);
        mButtonNum1.setText(String.valueOf(mBottleNum1));
        mButtonNum2.setText(String.valueOf(mBottleNum2));
        mButtonNum3.setText(String.valueOf(mBottleNum3));
        tvDepositCount.setText(totalCountText);
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
