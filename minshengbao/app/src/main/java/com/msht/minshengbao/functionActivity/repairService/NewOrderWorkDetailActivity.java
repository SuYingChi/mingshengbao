package com.msht.minshengbao.functionActivity.repairService;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.CallPhoneUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.GsonImpl;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.adapter.RepairAdditionalInfoAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.widget.CustomToast;
import com.msht.minshengbao.events.UpdateDataEvent;
import com.msht.minshengbao.functionActivity.publicModule.SelectVoucherActivity;
import com.msht.minshengbao.functionActivity.repairService.LookEvaluateActivity;
import com.msht.minshengbao.functionActivity.repairService.MasterDetailActivity;
import com.msht.minshengbao.functionActivity.repairService.MyOrderWorkDetailActivity;
import com.msht.minshengbao.functionActivity.repairService.RefundApplyActivity;
import com.msht.minshengbao.functionActivity.repairService.RepairEvaluateActivity;
import com.msht.minshengbao.functionActivity.repairService.RepairPaymentActivity;
import com.msht.minshengbao.functionActivity.repairService.RepeatFixActivity;
import com.msht.minshengbao.functionActivity.repairService.WarrantyCardActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class NewOrderWorkDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView typeImg, forwardImg;
    private ImageView evaluateImg;
    private ImageView downwardImg;
    private TextView  tvTitle, tvType, tvOrderNo, tvPhone;
    private TextView  tvCreateTime, tvAppointTime;
    private TextView  tvAddress, tvRemarkInfo, tvCancelInfo;
    private TextView  tvMasterName, tvPayAmount,tvOrderUserName;
    private TextView  tvDetectFee, tvMaterialFee, tvServeFee;
    private TextView  tvTotalAmount, tvMustPay, tvTotalCoupon;
    private TextView  tvUseCoupon, tvPayWay, reasonTitle;
    private TextView  tvStatus, tvGuaranteeDay;
    private TextView  tvEstimateAmount;
    private TextView  tvRefundAmount;
    private Button    btnCancel, btnEvaluate;
    private Button    btnReFix,btnRefund;
    private View layoutExpense, layoutVoucher, layoutPayFee;
    private View layoutRepairCancel, layoutForward, layoutPhone;
    private View layoutPayWay, layoutCoupon, viewExpense;
    private View layoutRealAmount, layoutEvaluate;
    private View layoutMaster, layoutCostDetail, layoutButton, layoutFixCard;
    private View viewFixCard;
    private View viewCoupon, viewPayAmount;
    private View estimateLayout;
    private View layoutCategoryButton;
    private View layoutCategory;
    private View layoutRefund;
    private String userId,password,id,cid;
    private String orderId, address,type;
    private String phone,title,orderNo, couponId="0";
    private String repairmanId, parentCategory;
    private String repairmanName, repairmanPhone, guaranteeDay;
    private String serveTime,amount, realAmount;
    private String payMethod, finishTime, evaluateScore, evaluateInfo;
    private String realMoney;
    private String phoneNo;
    private String parentCode;
    private String categoryDesc;
    private String additionalInfo;
    private String estimateAmount;
    private int    requestType=0;
    private RepairAdditionalInfoAdapter mAdditionalAdapter;
    private ArrayList<HashMap<String ,String>> additionalList=new ArrayList <HashMap<String ,String>>();
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_work_detail);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        cid=data.getStringExtra("cid");
        id=data.getStringExtra("id");
        parentCode=data.getStringExtra("parentCode");
        categoryDesc=data.getStringExtra("categoryDesc");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initFindViewId();
        initSetCodeImage(parentCode);
        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.id_category_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdditionalAdapter=new RepairAdditionalInfoAdapter(context,additionalList);
        mRecyclerView.setAdapter(mAdditionalAdapter);
        initData();
        initEvent();
    }
    private void initSetCodeImage(String parentCode) {
        tvTitle.setText(categoryDesc);
        switch (parentCode) {
            case ConstantUtil.SANITARY_WARE:
                typeImg.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.HOUSEHOLD_CLEAN:
                typeImg.setImageResource(R.drawable.home_appliance_clean_xh);
                break;
            case ConstantUtil.HOUSEHOLD_REPAIR:
                typeImg.setImageResource(R.drawable.home_otherfix_xh);
                // holder.serviceIMG.setImageResource(R.drawable.home_appliance_fix_xh);
                break;
            case ConstantUtil.OTHER_REPAIR:
                typeImg.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.HOUSEKEEPING_CLEAN:
                typeImg.setImageResource(R.drawable.housekeeping_clean_xh);
                break;
            default:
                typeImg.setImageResource(R.drawable.home_appliance_fix_xh);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0x001:
                //评价成功
                if (resultCode==2){
                    setResult(0x004);
                    requestType=0;
                    requestService();
                }else if (resultCode==3){
                    couponId=data.getStringExtra("voucherId");
                    String disAmount=data.getStringExtra("amount");
                    tvUseCoupon.setText(disAmount+"元");
                    tvUseCoupon.setTextColor(0xff555555);
                    double couponAmount =Double.parseDouble(disAmount);
                    double doubleAmount=Double.parseDouble(amount);
                    double doubleRealAmount=doubleAmount- couponAmount;
                    NumberFormat format=new DecimalFormat("0.##");
                    realMoney =format.format(doubleRealAmount);
                    tvMustPay.setText(realMoney);
                    tvPayAmount.setText("¥"+ realMoney);
                }
                break;
            case 0x004:
                if (resultCode==0x004){
                    setResult(0x004);
                    finish();
                }
                break;
            case 0x005:
                if (resultCode==0x005){
                    setResult(0x006);
                    finish();
                }
            default:
                break;
        }
    }
    private void initFindViewId() {

        typeImg =(ImageView)findViewById(R.id.id_img_type);
        downwardImg=(ImageView)findViewById(R.id.id_downward_img) ;
        forwardImg =(ImageView)findViewById(R.id.id_img_forward2);
        evaluateImg =(ImageView)findViewById(R.id.id_evaluate_status);
        btnCancel =(Button)findViewById(R.id.id_cancel_order);
        btnEvaluate =(Button)findViewById(R.id.id_evaluate_order);
        tvStatus =(TextView) findViewById(R.id.id_tv_status);
        tvTitle =(TextView)findViewById(R.id.id_tv_title);
        tvEstimateAmount=(TextView)findViewById(R.id.id_estimate_price) ;
        tvType =(TextView)findViewById(R.id.id_tv_type);
        tvAppointTime =(TextView)findViewById(R.id.id_tv_appoint_time);
        tvOrderNo =(TextView)findViewById(R.id.id_orderNo);
        tvOrderUserName=(TextView)findViewById(R.id.id_order_userName) ;
        tvPhone =(TextView)findViewById(R.id.id_phone);
        tvCreateTime =(TextView)findViewById(R.id.id_create_time);
        tvAddress =(TextView)findViewById(R.id.id_tv_address);
        tvRemarkInfo =(TextView)findViewById(R.id.id_tv_remark);
        tvCancelInfo =(TextView)findViewById(R.id.id_repair_man_cancel_info);
        reasonTitle =(TextView)findViewById(R.id.text3);
        tvMasterName =(TextView)findViewById(R.id.id_master);
        tvDetectFee =(TextView)findViewById(R.id.id_detect_fee);
        tvMaterialFee =(TextView)findViewById(R.id.id_material_fee);
        tvServeFee =(TextView)findViewById(R.id.id_serve_fee);
        tvTotalAmount =(TextView)findViewById(R.id.id_total_amount);
        tvMustPay =(TextView)findViewById(R.id.id_real_amount);
        tvTotalCoupon =(TextView)findViewById(R.id.id_coupon_amount);
        tvUseCoupon =(TextView)findViewById(R.id.id_use_coupon);
        tvPayWay =(TextView)findViewById(R.id.id_tv_payway);
        tvPayAmount =(TextView)findViewById(R.id.id_pay_amount);
        tvRefundAmount=(TextView)findViewById(R.id.id_refund_amount);
        tvGuaranteeDay =(TextView)findViewById(R.id.id_tv_date);
        layoutFixCard =findViewById(R.id.id_re_fixCard);
        layoutButton =findViewById(R.id.id_re_button);
        layoutPayFee =findViewById(R.id.id_re_pay);
        estimateLayout=findViewById(R.id.id_estimate_layout);
        btnReFix =(Button)findViewById(R.id.id_btn_refix);
        btnRefund=(Button)findViewById(R.id.id_btn_refund);
        layoutCategory=findViewById(R.id.id_category_layout);
        layoutCategoryButton=findViewById(R.id.id_category_button);
        layoutVoucher =findViewById(R.id.id_re_voucher);
        layoutExpense =findViewById(R.id.id_re_expanse);
        layoutPhone =findViewById(R.id.id_re_phone);
        layoutRepairCancel =findViewById(R.id.id_relayout_cancel);
        layoutMaster =findViewById(R.id.id_master_info);
        layoutForward =findViewById(R.id.id_relauout_master);
        layoutPayWay =findViewById(R.id.id_re_payway);
        layoutCoupon =findViewById(R.id.id_re_coupon);
        layoutRealAmount =findViewById(R.id.id_re_realamount);
        layoutEvaluate =findViewById(R.id.id_li_evaluate);
        layoutCostDetail =findViewById(R.id.id_order_expense);
        viewCoupon =findViewById(R.id.id_li_coupon);
        viewExpense =findViewById(R.id.id_li_expense);
        viewPayAmount =findViewById(R.id.id_li_pay);
        viewFixCard =findViewById(R.id.id_li_fixcard);
        layoutRefund=findViewById(R.id.id_re_refund);
        btnReFix.setOnClickListener(this);
        btnRefund.setOnClickListener(this);
    }
    private void initData() {
        requestType=0;
        requestService();
    }
    private void initEvent() {
        btnCancel.setOnClickListener(this);
        layoutPayFee.setOnClickListener(this);
        btnEvaluate.setOnClickListener(this);
        layoutForward.setOnClickListener(this);
        layoutPhone.setOnClickListener(this);
        layoutEvaluate.setOnClickListener(this);
        layoutVoucher.setOnClickListener(this);
        layoutFixCard.setOnClickListener(this);
        layoutExpense.setTag(0);
        layoutCategoryButton.setTag(0);
        layoutExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag=(Integer)v.getTag();
                switch (tag){
                    case 0:
                        forwardImg.setImageResource(R.drawable.downward_m);
                        viewExpense.setVisibility(View.VISIBLE);
                        v.setTag(1);
                        break;
                    case 1:
                        forwardImg.setImageResource(R.drawable.forward_m);
                        viewExpense.setVisibility(View.GONE);
                        v.setTag(0);
                        break;
                    default:
                        break;
                }
            }
        });
        layoutCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag=(Integer)view.getTag();
                switch (tag){
                    case 0:
                        layoutCategory.setVisibility(View.VISIBLE);
                        downwardImg.setRotation(180);
                        view.setTag(1);
                        break;
                    case 1:
                        layoutCategory.setVisibility(View.GONE);
                        downwardImg.setRotation(0);
                        view.setTag(0);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_evaluate_order:
                onEvaluate();
                break;
            case R.id.id_cancel_order:
                onCancelOrder();
                break;
            case R.id.id_re_pay:
                onPayOrder();
                break;
            case R.id.id_relauout_master:
                onMasterInfo();
                break;
            case R.id.id_li_evaluate:
                onLookEvaluate();
                break;
            case R.id.id_re_phone:
                callToMaster();
                break;
            case R.id.id_re_voucher:
                getVoucher();
                break;
            case R.id.id_btn_refix:
                repeatFix();
                break;
            case R.id.id_btn_refund:
                onRefund();
                break;
            case R.id.id_re_fixCard:
                onFixCard();
                break;
            default:
                break;
        }
    }

    private void onRequestLimit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.CALL_PHONE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                CallPhoneUtil.onCallPhone(context,repairmanPhone);
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                CustomToast.showWarningLong("请您允许使用拨打电话权限!");
                            }
                        }).start();
            }else {
                CallPhoneUtil.onCallPhone(context,repairmanPhone);
            }
        }else {
            CallPhoneUtil.onCallPhone(context,repairmanPhone);
        }
    }

    private void onFixCard() {
        Intent intent=new Intent(context,WarrantyCardActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("orderNo",orderNo);
        intent.putExtra("guaranteeDay", guaranteeDay);
        intent.putExtra("masterName", repairmanName);
        intent.putExtra("serveTime", serveTime);
        startActivity(intent);
    }
    private void repeatFix() {
        Intent repeat=new Intent(context,RepeatFixActivity.class);
        repeat.putExtra("reid",cid);
        repeat.putExtra("id",orderId);
        repeat.putExtra("orderNo",orderNo);
        repeat.putExtra("type",type);
        repeat.putExtra("title",title);
        repeat.putExtra("categoryDesc",categoryDesc);
        repeat.putExtra("additionalInfo",additionalInfo);
        repeat.putExtra("parentCategory", parentCategory);
        repeat.putExtra("finishTime", finishTime);
        repeat.putExtra("phone",phone);
        repeat.putExtra("address",address);
        repeat.putExtra("parentCode",parentCode);
        startActivityForResult(repeat,0x004);
    }
    private void onRefund() {
        Intent fund=new Intent(context,RefundApplyActivity.class);
        fund.putExtra("id",orderId);
        fund.putExtra("orderNo",orderNo);
        fund.putExtra("type",type);
        fund.putExtra("title",title);
        fund.putExtra("categoryDesc",categoryDesc);
        fund.putExtra("parentCategory", parentCategory);
        fund.putExtra("additionalInfo",additionalInfo);
        fund.putExtra("parentCode",parentCode);
        fund.putExtra("finishTime", finishTime);
        fund.putExtra("realAmount", realAmount);
        startActivityForResult(fund,0x004);
    }
    private void getVoucher() {
        Intent voucher=new Intent(context, SelectVoucherActivity.class);
        voucher.putExtra("payAmount",amount);
        voucher.putExtra("category","1");
        startActivityForResult(voucher,1);
    }
    private void callToMaster() {
        new PromptDialog.Builder(this)
                .setTitle("师傅电话")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(repairmanPhone)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("呼叫", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        onRequestLimit();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void onMasterInfo() {
        Intent master=new Intent(context,MasterDetailActivity.class);
        master.putExtra("masterId", repairmanId);
        startActivity(master);
    }
    private void onPayOrder() {
        Intent pay=new Intent(context,RepairPaymentActivity.class);
        pay.putExtra("realMoney", realMoney);
        pay.putExtra("orderNo",orderNo);
        pay.putExtra("orderId",orderId);
        pay.putExtra("couponId",couponId);
        startActivityForResult(pay,0x005);
    }
    private void onLookEvaluate() {
        Intent evaluate=new Intent(context,LookEvaluateActivity.class);
        evaluate.putExtra("id",orderId);
        evaluate.putExtra("orderNo",orderNo);
        evaluate.putExtra("type",type);
        evaluate.putExtra("title",title);
        evaluate.putExtra("finishTime", finishTime);
        evaluate.putExtra("parentCategory", parentCategory);
        evaluate.putExtra("categoryDesc",categoryDesc);
        evaluate.putExtra("realAmount", realAmount);
        evaluate.putExtra("evaluateScore", evaluateScore);
        evaluate.putExtra("evaluateInfo", evaluateInfo);
        evaluate.putExtra("parentCode",parentCode);
        startActivity(evaluate);
    }
    private void onCancelOrder() {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("请确认是否取消工单")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        requestType=1;
                        requestService();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void requestService() {
        customDialog.show();
        String validateURL ="";
        if ( requestType==0){
            validateURL = UrlUtil.RepairOrder_detailUrl;
        }else if ( requestType==1){
            validateURL =UrlUtil.RepairOrder_cancelUrl;
        }
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                onAnalysisData(data.toString());
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

    private void onAnalysisData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("error");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                if (requestType==0){
                    JSONObject  jsonObject =object.optJSONObject("data");
                    onReceiveData(jsonObject);
                }else if (requestType==1){
                    success();
                }
            }else {
                CustomToast.showErrorLong(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onEvaluate() {
        Intent evaluate=new Intent(context,RepairEvaluateActivity.class);
        evaluate.putExtra("sendType","1");
        evaluate.putExtra("id",orderId);
        evaluate.putExtra("orderNo",orderNo);
        evaluate.putExtra("type",type);
        evaluate.putExtra("title",title);
        evaluate.putExtra("categoryDesc",categoryDesc);
        evaluate.putExtra("parentCategory", parentCategory);
        evaluate.putExtra("finishTime", finishTime);
        evaluate.putExtra("realAmount", realAmount);
        startActivityForResult(evaluate,1);
    }
    private void success() {
        setResult(0x001);
        EventBus.getDefault().post(new UpdateDataEvent(true));
        finish();
    }
    private void onReceiveData(JSONObject jsonObject) {
        orderId= jsonObject.optString("id");
        type= jsonObject.optString("type");
        String status= jsonObject.optString("status");
        String statusInfo = jsonObject.optString("status_info");
        String info= jsonObject.optString("info");
        String orderUserName=jsonObject.optString("username");
        address= jsonObject.optString("address");
        tvOrderUserName.setText(orderUserName);
        phone= jsonObject.optString("phone");
        title= jsonObject.optString("title");
        categoryDesc= jsonObject.optString("category_desc");
        additionalInfo=jsonObject.optString("additional_info");
        parentCategory = jsonObject.optString("parent_category");
        String appointTime= jsonObject.optString("appoint_time");
        orderNo= jsonObject.optString("orderNo");
        String createTime = jsonObject.optString("createTime");
        estimateAmount= jsonObject.optString("estimated_price");
        tvStatus.setText(statusInfo);
        tvAddress.setText(address);
        tvPhone.setText(phone);
        tvTitle.setText(categoryDesc);
        tvAppointTime.setText(appointTime);
        tvOrderNo.setText(orderNo);
        tvCreateTime.setText(createTime);
        tvType.setText(parentCategory);
        if (TextUtils.isEmpty(info)){
            tvRemarkInfo.setVisibility(View.GONE);
        }else {
            tvRemarkInfo.setText(info);
        }
        if (!TextUtils.isEmpty(additionalInfo)){
            layoutCategoryButton.setVisibility(View.VISIBLE);
        }else {
            layoutCategoryButton.setVisibility(View.GONE);
        }
        onSetStatusView(jsonObject,status);
        onSetGuaranteeStopDayView(jsonObject);
        onEstimateView(estimateAmount);
        additionalList.clear();
        additionalList.addAll(GsonImpl.getAdditionalList(jsonObject.optString("additional_info")));
        mAdditionalAdapter.notifyDataSetChanged();
    }
    private void onSetStatusView(JSONObject jsonObject, String status) {
        switch (status){
            case ConstantUtil.VALUE_ONE:
                layoutRefund.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                layoutButton.setVisibility(View.GONE);
                layoutFixCard.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_TWO:
                layoutRefund.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                layoutButton.setVisibility(View.GONE);
                layoutFixCard.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_THREE:
                layoutRefund.setVisibility(View.GONE);
                layoutButton.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                layoutFixCard.setVisibility(View.GONE);
                onJsonMaster(jsonObject);
                break;
            case ConstantUtil.VALUE_FOUR:
                layoutRefund.setVisibility(View.GONE);
                String repairManCancelInfo=jsonObject.optString("repair_man_cancel_info");
                layoutRepairCancel.setVisibility(View.VISIBLE);
                reasonTitle.setText("转单提示:");
                tvCancelInfo.setText(repairManCancelInfo);
                btnCancel.setVisibility(View.VISIBLE);
                layoutButton.setVisibility(View.GONE);
                layoutFixCard.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_FIVE:
                layoutRefund.setVisibility(View.GONE);
                layoutButton.setVisibility(View.GONE);
                layoutFixCard.setVisibility(View.GONE);
                onJsonMaster(jsonObject);
                break;
            case ConstantUtil.VALUE_SIX:
                layoutRefund.setVisibility(View.GONE);
                onJsonMaster(jsonObject);
                onStatusSix(jsonObject);
                break;
            case ConstantUtil.VALUE_SEVER:
                onStatusSeven(jsonObject);
                break;
            case ConstantUtil.VALUE_EIGHT:
                if (jsonObject.has("evaluate_score")){
                    layoutEvaluate.setVisibility(View.VISIBLE);
                    evaluateScore =jsonObject.optString("evaluate_score");
                    evaluateInfo =jsonObject.optString("evaluate_info");
                    onEvaluateImg(evaluateScore);
                }else {
                    layoutEvaluate.setVisibility(View.GONE);
                }
                layoutButton.setVisibility(View.VISIBLE);
                btnEvaluate.setVisibility(View.GONE);
                layoutRefund.setVisibility(View.GONE);
                finishInfo(jsonObject);
                break;
            case ConstantUtil.VALUE_NINE:
                onEstimateView(estimateAmount);
                String refuseReason=jsonObject.optString("refuse_reason");
                layoutRepairCancel.setVisibility(View.VISIBLE);
                viewPayAmount.setVisibility(View.GONE);
                layoutRefund.setVisibility(View.GONE);
                tvCancelInfo.setText(refuseReason);
                reasonTitle.setText("拒单提示:");
                break;
            case ConstantUtil.VALUE_TEN:
                if (jsonObject.has("evaluate_score")){
                    layoutEvaluate.setVisibility(View.VISIBLE);
                    evaluateScore =jsonObject.optString("evaluate_score");
                    evaluateInfo =jsonObject.optString("evaluate_info");
                    onEvaluateImg(evaluateScore);
                }else {
                    layoutEvaluate.setVisibility(View.GONE);
                }
                tvRefundAmount.setText(jsonObject.optString("refund_amount"));
                layoutButton.setVisibility(View.GONE);
                layoutRefund.setVisibility(View.VISIBLE);
                finishInfo(jsonObject);
                break;
            case ConstantUtil.VALUE_ELEVEN:
                layoutRefund.setVisibility(View.GONE);
                layoutButton.setVisibility(View.GONE);
                layoutFixCard.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_TWELVE:
                if (jsonObject.has("evaluate_score")){
                    layoutEvaluate.setVisibility(View.VISIBLE);
                    evaluateScore =jsonObject.optString("evaluate_score");
                    evaluateInfo =jsonObject.optString("evaluate_info");
                    onEvaluateImg(evaluateScore);
                }else {
                    layoutEvaluate.setVisibility(View.GONE);
                }
                layoutButton.setVisibility(View.VISIBLE);
                btnEvaluate.setVisibility(View.GONE);
                layoutRefund.setVisibility(View.GONE);
                finishInfo(jsonObject);
                break;
            case ConstantUtil.VALUE_FOURTEEN:
                layoutRefund.setVisibility(View.GONE);
                onStatusFourTeen(jsonObject);
                break;
            default:
                break;
        }
    }
    private void onEstimateView(String estimateAmount){
        if (TextUtils.isEmpty(estimateAmount)||estimateAmount.equals(ConstantUtil.NULL_VALUE)
                ||estimateAmount.equals(ConstantUtil.VALUE_ZERO2)||estimateAmount.equals(ConstantUtil.VALUE_ZERO1)){
            estimateLayout.setVisibility(View.GONE);
        }else {
            estimateLayout.setVisibility(View.VISIBLE);
            estimateAmount=estimateAmount+"元";
        }
        tvEstimateAmount.setText(estimateAmount);
    }
    private void onEvaluateImg(String evaluateScore) {
        switch (evaluateScore){
            case ConstantUtil.VALUE_ONE:
                evaluateImg.setImageResource(R.drawable.star_one_h);
                break;
            case ConstantUtil.VALUE_TWO:
                evaluateImg.setImageResource(R.drawable.star_two_h);
                break;
            case ConstantUtil.VALUE_THREE:
                evaluateImg.setImageResource(R.drawable.star_three_h);
                break;
            case ConstantUtil.VALUE_FOUR:
                evaluateImg.setImageResource(R.drawable.star_four_h);
                break;
            case ConstantUtil.VALUE_FIVE:
                evaluateImg.setImageResource(R.drawable.star_five_h);
                break;
            default:
                evaluateImg.setImageResource(R.drawable.star_four_h);
                break;
        }
    }

    private void onStatusFourTeen(JSONObject jsonObject) {
        String repairManCancelInfo=jsonObject.optString("repair_man_cancel_info");
        reasonTitle.setText("改单原因：");
        layoutRepairCancel.setVisibility(View.VISIBLE);
        tvCancelInfo.setText(repairManCancelInfo);
        btnCancel.setVisibility(View.VISIBLE);
        layoutButton.setVisibility(View.GONE);
        layoutFixCard.setVisibility(View.GONE);
    }
    private void onStatusSix(JSONObject jsonObject) {
        layoutButton.setVisibility(View.GONE);
        serveTime =jsonObject.optString("serve_time");
        String couponFlag=jsonObject.optString("coupon_flag");
        guaranteeDay =jsonObject.optString("guarantee_day");
        repairmanId =jsonObject.optString("repairman_id");
        repairmanName =jsonObject.optString("repairman_name");
        JSONArray jsonDiscount=jsonObject.optJSONArray("coupon");
        viewPayAmount.setVisibility(View.VISIBLE);
        onSetCouponFlag(couponFlag,jsonDiscount.length());
        viewFixCard.setVisibility(View.VISIBLE);
        layoutMaster.setVisibility(View.VISIBLE);
        viewCoupon.setVisibility(View.VISIBLE);
        tvGuaranteeDay.setText(guaranteeDay);
        onShowFee(jsonObject);
        realMoney =amount;
        tvMustPay.setText(amount);
        tvPayAmount.setText("¥"+ realMoney);
        layoutRealAmount.setVisibility(View.VISIBLE);
    }
    private void onStatusSeven(JSONObject jsonObject) {
        layoutButton.setVisibility(View.VISIBLE);
        serveTime =jsonObject.optString("serve_time");
        realAmount =jsonObject.optString("real_amount");
        payMethod =jsonObject.optString("pay_method");
        finishTime =jsonObject.optString("finish_time");
        guaranteeDay =jsonObject.optString("guarantee_day");
        repairmanId =jsonObject.optString("repairman_id");
        repairmanName =jsonObject.optString("repairman_name");
        String couponAmount=jsonObject.optString("coupon_amount");
        viewFixCard.setVisibility(View.VISIBLE);
        layoutPayWay.setVisibility(View.VISIBLE);
        layoutCoupon.setVisibility(View.VISIBLE);
        layoutRealAmount.setVisibility(View.VISIBLE);
        tvGuaranteeDay.setText(guaranteeDay);
        onShowFee(jsonObject);
        tvMustPay.setText(realAmount);
        if (TextUtils.isEmpty(couponAmount)){
            tvTotalCoupon.setText(ConstantUtil.VALUE_ZERO2);
        }else {
            tvTotalCoupon.setText(couponAmount);
        }
        tvPayWay.setText(payMethod);
        btnEvaluate.setVisibility(View.VISIBLE);
        viewPayAmount.setVisibility(View.GONE);
    }

    private void onSetGuaranteeStopDayView(JSONObject jsonObject) {
        if (jsonObject.has("guarantee_stop_day")){
            String guaranteeStopDay=jsonObject.optString("guarantee_stop_day");
            String curDate= DateUtils.getCurDate("yyyy-MM-dd");
            if (DateUtils.isDateBigger(curDate,guaranteeStopDay)){
                btnReFix.setVisibility(View.VISIBLE);
                btnRefund.setVisibility(View.VISIBLE);
            }else {
                btnReFix.setVisibility(View.GONE);
                btnRefund.setVisibility(View.GONE);
            }
        }else {
            btnReFix.setVisibility(View.GONE);
            btnRefund.setVisibility(View.GONE);
        }
    }
    private void onSetCouponFlag(String couponFlag, int length) {
        if (couponFlag.equals(ConstantUtil.VALUE_ONE)){
            layoutVoucher.setEnabled(false);
            tvUseCoupon.setText("不可用");
            tvUseCoupon.setTextColor(0xFFF96331);
        }else {
            layoutVoucher.setEnabled(true);
            if (length!=0){
                tvUseCoupon.setText("有券，点击使用");
                tvUseCoupon.setTextColor(0xFFF96331);
            }else {
                tvUseCoupon.setText("无券，去分享获取");
                tvUseCoupon.setTextColor(0xFFF96331);
            }
        }
    }
    private void finishInfo(JSONObject jsonObject) {
        serveTime =jsonObject.optString("serve_time");
        realAmount =jsonObject.optString("real_amount");
        payMethod =jsonObject.optString("pay_method");
        guaranteeDay =jsonObject.optString("guarantee_day");
        repairmanId =jsonObject.optString("repairman_id");
        repairmanName =jsonObject.optString("repairman_name");
        String couponAmount=jsonObject.optString("coupon_amount");
        tvPayWay.setText(payMethod);
        tvGuaranteeDay.setText(guaranteeDay);
        viewFixCard.setVisibility(View.VISIBLE);
        layoutPayWay.setVisibility(View.VISIBLE);
        layoutCoupon.setVisibility(View.VISIBLE);
        layoutRealAmount.setVisibility(View.VISIBLE);
        onShowFee(jsonObject);
        tvMustPay.setText(realAmount);
        if (TextUtils.isEmpty(couponAmount)){
            tvTotalCoupon.setText(ConstantUtil.VALUE_ZERO2);
        }else {
            tvTotalCoupon.setText(couponAmount);
        }
        finishTime =jsonObject.optString("finish_time");
        viewPayAmount.setVisibility(View.GONE);
    }
    private void onShowFee(JSONObject jsonObject) {
        amount=jsonObject.optString("amount");
        String detectFee=jsonObject.optString("detect_fee");
        String materialFee=jsonObject.optString("material_fee");
        String serveFee=jsonObject.optString("serve_fee");
        layoutCostDetail.setVisibility(View.VISIBLE);
        tvTotalAmount.setText(amount);
        tvDetectFee.setText(detectFee);
        tvMaterialFee.setText(materialFee);
        tvServeFee.setText(serveFee);
    }
    private void onJsonMaster(JSONObject jsonObject) {
        String distributeTime =jsonObject.optString("distribute_time");
        repairmanId =jsonObject.optString("repairman_id");
        repairmanName =jsonObject.optString("repairman_name");
        repairmanPhone =jsonObject.optString("repairman_phone");
        layoutMaster.setVisibility(View.VISIBLE);
        tvMasterName.setText(repairmanName);

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