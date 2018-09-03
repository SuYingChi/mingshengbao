package com.msht.minshengbao.functionActivity.repairService;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.functionActivity.Public.SelectVoucherActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

/**
 * Demo class
 *
 * @author hong
 * @date 2017/3/28
 */
public class MyOrderWorkDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView typeImg, forwardImg;
    private ImageView evaluateImg;
    private TextView tvTitle, tvType, tvOrderNo, tvPhone;
    private TextView tvCreateTime, tvAppointTime;
    private TextView tvAddress, tvRemarkInfo, tvCancelInfo;
    private TextView tvMasterName, tvPayAmount;
    private TextView tvDetectFee, tvMaterialFee, tvServeFee;
    private TextView tvTotalAmount, tvMustPay, tvTotalCoupon;
    private TextView tvUseCoupon, tvPayWay, reasonTitle;
    private TextView tvStatus, tvGuaranteeDay;
    private Button btnCancel, btnEvaluate;
    private Button btnReFix,btnRefund;
    private View layoutExpense, layoutVoucher, layoutPayFee;
    private View layoutRepairCancel, layoutForward, layoutPhone;
    private View layoutPayWay, layoutCoupon, viewExpense;
    private View layoutRealAmount, layoutEvaluate;
    private View layoutMaster, layoutCostDetail, layoutButton, layoutFixCard;
    private View viewFixCard;
    private View viewCoupon, viewPayAmount;
    private String userId,password,id,cid;
    private String orderId, address,type;
    private String phone,title,orderNo, couponId="0";
    private String repairmanId, parentCateogry;
    private String repairmanName, repairmanPhone, guaranteeDay;
    private String serveTime,amount, realAmount;
    private String payMethod, finishTime, evaluateScore, evaluateInfo;
    private String realMoney;
    private String phoneNo;
    private int    requestCode=0;
    private static final int  EVA_CODE=1;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    private final DetailHandler requestHandler = new DetailHandler(this);
    private static class DetailHandler extends Handler{
        private WeakReference<MyOrderWorkDetailActivity> mWeakReference;
        public DetailHandler(MyOrderWorkDetailActivity reference) {
            mWeakReference = new WeakReference<MyOrderWorkDetailActivity>(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            final MyOrderWorkDetailActivity reference =mWeakReference.get();
            if (reference == null||reference.isFinishing()) {
                return;
            }
            if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (reference.requestCode==0){
                                reference.jsonObject =object.optJSONObject("data");
                                reference.onReceiveData();
                            }else if (reference.requestCode==1){
                                reference.success();
                            }
                        }else {
                            reference.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }
    private void success() {
        setResult(0x001);
        finish();
    }
    private void onReceiveData() {
        orderId=jsonObject.optString("id");
        type=jsonObject.optString("type");
        String status=jsonObject.optString("status");
        String statusInfo =jsonObject.optString("status_info");
        String info=jsonObject.optString("info");
        address=jsonObject.optString("address");
        phone=jsonObject.optString("phone");
        title=jsonObject.optString("title");
        parentCateogry =jsonObject.optString("parent_category");
        String appointTime=jsonObject.optString("appoint_time");
        orderNo=jsonObject.optString("orderNo");
        String createTime =jsonObject.optString("create_time");
        tvStatus.setText(statusInfo);
        tvAddress.setText(address);
        tvPhone.setText(phone);
        tvTitle.setText(title);
        tvAppointTime.setText(appointTime);
        tvOrderNo.setText(orderNo);
        tvCreateTime.setText(createTime);
        tvType.setText(parentCateogry);
        if (TextUtils.isEmpty(info)){
            tvRemarkInfo.setVisibility(View.GONE);
        }else {
            tvRemarkInfo.setText(info);
        }
        onSetTypeImage(type);
        onSetStatusView(status);
        onSetGuaranteeStopDayView();
    }
    private void onSetStatusView(String status) {
        switch (status){
            case ConstantUtil.VALUE_ONE:
                btnCancel.setVisibility(View.VISIBLE);
                layoutButton.setVisibility(View.GONE);
                layoutFixCard.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_TWO:
                btnCancel.setVisibility(View.GONE);
                layoutButton.setVisibility(View.GONE);
                layoutFixCard.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_THREE:
                layoutButton.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                layoutFixCard.setVisibility(View.GONE);
                onJsonMaster();
                break;
            case ConstantUtil.VALUE_FOUR:
                String repairManCancelInfo=jsonObject.optString("repair_man_cancel_info");
                layoutRepairCancel.setVisibility(View.VISIBLE);
                tvCancelInfo.setText(repairManCancelInfo);
                btnCancel.setVisibility(View.VISIBLE);
                layoutButton.setVisibility(View.GONE);
                layoutFixCard.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_FIVE:
                layoutButton.setVisibility(View.GONE);
                layoutFixCard.setVisibility(View.GONE);
                onJsonMaster();
                 break;
            case ConstantUtil.VALUE_SIX:
                onJsonMaster();
                onStatusSix();
                break;
            case ConstantUtil.VALUE_SEVER:
                onStatusSeven();
                break;
            case ConstantUtil.VALUE_EIGHT:
                if (jsonObject.has("evaluate_score")){
                    layoutEvaluate.setVisibility(View.VISIBLE);
                    evaluateScore =jsonObject.optString("evaluate_score");
                    evaluateInfo =jsonObject.optString("evaluate_info");
                }else {
                    layoutEvaluate.setVisibility(View.GONE);
                }
                layoutButton.setVisibility(View.VISIBLE);
                finishInfo();
                break;
            case ConstantUtil.VALUE_NINE:
                String refuseReason=jsonObject.optString("refuse_reason");
                layoutRepairCancel.setVisibility(View.VISIBLE);
                viewPayAmount.setVisibility(View.GONE);
                tvCancelInfo.setText(refuseReason);
                reasonTitle.setText("拒单提示：");
                break;
            case ConstantUtil.VALUE_TEN:
                if (jsonObject.has("evaluate_score")){
                    layoutEvaluate.setVisibility(View.VISIBLE);
                    evaluateScore =jsonObject.optString("evaluate_score");
                    evaluateInfo =jsonObject.optString("evaluate_info");
                }else {
                    layoutEvaluate.setVisibility(View.GONE);
                }
                layoutButton.setVisibility(View.GONE);
                finishInfo();
                break;
            case ConstantUtil.VALUE_ELEVEN:
                layoutButton.setVisibility(View.GONE);
                layoutFixCard.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_FOURTEEN:
                onStatusFourTeen();
                break;
                default:
                    break;

        }
    }
    private void onStatusFourTeen() {
        String repairManCancelInfo=jsonObject.optString("repair_man_cancel_info");
        reasonTitle.setText("改单原因：");
        layoutRepairCancel.setVisibility(View.VISIBLE);
        tvCancelInfo.setText(repairManCancelInfo);
        btnCancel.setVisibility(View.VISIBLE);
        layoutButton.setVisibility(View.GONE);
        layoutFixCard.setVisibility(View.GONE);
    }
    private void onStatusSix() {
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
        onShowFee();
        realMoney =amount;
        tvMustPay.setText(amount);
        tvPayAmount.setText("¥"+ realMoney);
        layoutRealAmount.setVisibility(View.VISIBLE);
    }
    private void onStatusSeven() {
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
        onShowFee();
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

    private void onSetGuaranteeStopDayView() {
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

    private void onSetTypeImage(String type) {
        switch (type){
            case ConstantUtil.VALUE_ONE:
                typeImg.setImageResource(R.drawable.home_sanitary_xh);
                break;
            case ConstantUtil.VALUE_TWO:
                typeImg.setImageResource(R.drawable.home_appliance_fix_xh);
                break;
            case ConstantUtil.VALUE_THREE:
                typeImg.setImageResource(R.drawable.home_lanterns_xh);
                break;
            case ConstantUtil.VALUE_FOUR:
                typeImg.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.VALUE_FORTY_EIGHT:
                typeImg.setImageResource(R.drawable.home_appliance_clean_xh);
                break;
            default:
                typeImg.setImageResource(R.drawable.home_appliance_clean_xh);
                break;
        }
    }

    private void finishInfo() {
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
        onShowFee();
        tvMustPay.setText(realAmount);
        if (TextUtils.isEmpty(couponAmount)){
            tvTotalCoupon.setText(ConstantUtil.VALUE_ZERO2);
        }else {
            tvTotalCoupon.setText(couponAmount);
        }
        finishTime =jsonObject.optString("finish_time");
        viewPayAmount.setVisibility(View.GONE);
        if (evaluateInfo.equals(ConstantUtil.VALUE_ONE)){
            evaluateImg.setImageResource(R.drawable.star_one_h);
        }else if (evaluateScore.equals(ConstantUtil.VALUE_TWO)){
            evaluateImg.setImageResource(R.drawable.star_two_h);
        }else if (evaluateScore.equals(ConstantUtil.VALUE_THREE)){
            evaluateImg.setImageResource(R.drawable.star_three_h);
        }else if (evaluateScore.equals(ConstantUtil.VALUE_FOUR)){
            evaluateImg.setImageResource(R.drawable.star_four_h);
        }else if (evaluateScore.equals(ConstantUtil.VALUE_FIVE)){
            evaluateImg.setImageResource(R.drawable.star_five_h);
        }
    }
    private void onShowFee() {
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
    private void onJsonMaster() {
        String distributeTime =jsonObject.optString("distribute_time");
        repairmanId =jsonObject.optString("repairman_id");
        repairmanName =jsonObject.optString("repairman_name");
        repairmanPhone =jsonObject.optString("repairman_phone");
        layoutMaster.setVisibility(View.VISIBLE);
        tvMasterName.setText(repairmanName);

    }
    private void onFailure(String error) {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorderwork_detail);
        setCommonHeader("维修订单详情");
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        cid=data.getStringExtra("cid");
        id=data.getStringExtra("id");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initFindViewId();
        initData();
        initEvent();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case EVA_CODE:
                //评价成功
                if (resultCode==2){
                    setResult(0x002);
                    finish();
                }else if (resultCode==3){
                    couponId=data.getStringExtra("vouid");
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
                }
                break;
            case 0x005:
                if (resultCode==0x005){
                    setResult(0x004);
                    finish();
                }
            default:
                break;
        }
    }
    private void initFindViewId() {
        typeImg =(ImageView)findViewById(R.id.id_img_type);
        forwardImg =(ImageView)findViewById(R.id.id_img_forward2);
        evaluateImg =(ImageView)findViewById(R.id.id_evaluate_status);
        btnCancel =(Button)findViewById(R.id.id_cancel_order);
        btnEvaluate =(Button)findViewById(R.id.id_evaluate_order);
        tvStatus =(TextView) findViewById(R.id.id_tv_status);
        tvTitle =(TextView)findViewById(R.id.id_tv_title);
        tvType =(TextView)findViewById(R.id.id_tv_type);
        tvAppointTime =(TextView)findViewById(R.id.id_tv_appoint_time);
        tvOrderNo =(TextView)findViewById(R.id.id_orderNo);
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
        tvGuaranteeDay =(TextView)findViewById(R.id.id_tv_date);
        layoutFixCard =findViewById(R.id.id_re_fixcard);
        layoutButton =findViewById(R.id.id_re_button);
        layoutPayFee =findViewById(R.id.id_re_pay);
        btnReFix =(Button)findViewById(R.id.id_btn_refix);
        btnRefund=(Button)findViewById(R.id.id_btn_refund);
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
        btnReFix.setOnClickListener(this);
        btnRefund.setOnClickListener(this);
    }
    private void initData() {
        customDialog.show();
        requestCode=0;
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
            case R.id.id_re_fixcard:
                onFixCard();
                break;
            default:
                break;
        }
    }
    private void onFixCard() {
        Intent intent=new Intent(context,WarrantyCardActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("orderNo",orderNo);
        intent.putExtra("guaranteeDay", guaranteeDay);
        intent.putExtra("mastername", repairmanName);
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
        repeat.putExtra("parent_category", parentCateogry);
        repeat.putExtra("finishTime", finishTime);
        repeat.putExtra("phone",phone);
        repeat.putExtra("address",address);
        startActivityForResult(repeat,0x004);
    }
    private void onRefund() {
        Intent fund=new Intent(context,RefundApplyActivity.class);
        fund.putExtra("id",orderId);
        fund.putExtra("orderNo",orderNo);
        fund.putExtra("type",type);
        fund.putExtra("title",title);
        fund.putExtra("parent_category", parentCateogry);
        fund.putExtra("finishTime", finishTime);
        fund.putExtra("realAmount", realAmount);
        startActivityForResult(fund,0x004);
    }
    private void getVoucher() {
        Intent voucher=new Intent(context, SelectVoucherActivity.class);
        voucher.putExtra("pay_amount",amount);
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (MyOrderWorkDetailActivity.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + repairmanPhone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                phoneNo= repairmanPhone;
                                ActivityCompat.requestPermissions(MyOrderWorkDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + repairmanPhone));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                        dialog.dismiss();


                    }
                })
                .show();
    }
    /*动态权限*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==MY_PERMISSIONS_REQUEST_CALL_PHONE){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                try{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phoneNo));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }else {
                ToastUtil.ToastText(context,"授权失败");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        evaluate.putExtra("parent_category", parentCateogry);
        evaluate.putExtra("realAmount", realAmount);
        evaluate.putExtra("evaluateScore", evaluateScore);
        evaluate.putExtra("evaluateInfo", evaluateInfo);
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
                        requestCode=1;
                        requestService();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void requestService() {
        customDialog.show();
        String validateURL ="";
        if (requestCode==0){
            validateURL = UrlUtil.RepairOrder_detailUrl;
        }else if (requestCode==1){
            validateURL =UrlUtil.RepairOrder_cancelUrl;
        }
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onEvaluate() {
        Intent evaluate=new Intent(context,RepairEvaluateActivity.class);
        evaluate.putExtra("send_type","1");
        evaluate.putExtra("id",orderId);
        evaluate.putExtra("orderNo",orderNo);
        evaluate.putExtra("type",type);
        evaluate.putExtra("title",title);
        evaluate.putExtra("parent_category", parentCateogry);
        evaluate.putExtra("finishTime", finishTime);
        evaluate.putExtra("realAmount", realAmount);
        startActivityForResult(evaluate,1);
    }
    private void onClearCoupon() {   //取消使用优惠
        tvUseCoupon.setText("0");
        realMoney =amount;
        tvMustPay.setText(realMoney);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
