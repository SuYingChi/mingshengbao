package com.msht.minshengbao.FunctionActivity.repairService;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.FunctionActivity.Public.SelectVoucher;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.SendrequestUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Demo class
 *
 * @author hong
 * @date 2017/3/28
 */
public class MyorderworkDetail extends BaseActivity implements View.OnClickListener {
    private ImageView typeimg,forwardimg;
    private ImageView evaluateimg;
    private TextView  tv_title,tv_type,tv_orderNo,tv_phone;
    private TextView  tv_createtime,tv_appoint_time;
    private TextView  tv_address,tv_remarkinfo,tv_cancelinfo;
    private TextView  tv_mastername,tv_payamount;
    private TextView  tv_detect_fee,tv_material_fee,tv_serve_fee;
    private TextView  tv_totalamount,tv_mustpay,tv_totalcoupon;
    private TextView  tv_usecoupon,tv_payway,reason_title;
    private TextView  tv_status,tv_guarantee_day;
    private Button    btn_cancel,btn_evaluate;
    private Button    btnRefix,btnRefund;
    private View   Rexpense,Rvoucher,Rpayfee;
    private View   Repair_cancel,Rforwars,Rphone;
    private LinearLayout     Rpayway,Rcoupon,Lexpense;
    private LinearLayout     Realamount,Levaluate;
    private View Rmaster,Lcostdetail,Rbutton,Rfixcard;
    private View Lfixcard;
    private LinearLayout Lcoupon,Lpayamount;
    private String userId,password,id,cid;
    private String orderId,status,status_info,address,type,info;
    private String phone,title,orderNo,create_time,couponId="0";
    private String distribute_time,repairman_id,parent_cateogry;
    private String repairman_name,repairman_phone,guarantee_day;
    private String serve_time,amount,real_amount;
    private String pay_method,finish_time,evaluate_score,evaluate_info;
    private String realmoney;
    private String phoneNo;
    private double couponAmount;
    private int   requestCode=0;
    private final int  EVA_CODE=1;
    private JSONArray jsondicount;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    private ArrayList<HashMap<String, String>> dicountList = new ArrayList<HashMap<String, String>>();
    private final DetailHandler requestHandler = new DetailHandler(this);
    private static class DetailHandler extends Handler{
        private WeakReference<MyorderworkDetail> mWeakReference;
        public DetailHandler(MyorderworkDetail reference) {
            mWeakReference = new WeakReference<MyorderworkDetail>(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            final MyorderworkDetail reference =mWeakReference.get();
            if (reference == null||reference.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                        reference.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String Error = object.optString("error");
                        if(results.equals("success")) {
                            if (reference.requestCode==0){
                                reference.jsonObject =object.optJSONObject("data");
                                reference.initShow();
                            }else if (reference.requestCode==1){
                                reference.success();
                            }
                        }else {
                            reference.faifure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                        reference.customDialog.dismiss();
                    }
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
    private void initShow() {
        orderId=jsonObject.optString("id");
        type=jsonObject.optString("type");
        status=jsonObject.optString("status");
        status_info=jsonObject.optString("status_info");
        info=jsonObject.optString("info");
        address=jsonObject.optString("address");
        phone=jsonObject.optString("phone");
        title=jsonObject.optString("title");
        parent_cateogry=jsonObject.optString("parent_category");
        String appoint_time=jsonObject.optString("appoint_time");
        orderNo=jsonObject.optString("orderNo");
        create_time=jsonObject.optString("create_time");
        tv_status.setText(status_info);
        tv_address.setText(address);
        tv_phone.setText(phone);
        tv_title.setText(title);
        tv_appoint_time.setText(appoint_time);
        tv_orderNo.setText(orderNo);
        tv_createtime.setText(create_time);
        tv_type.setText(parent_cateogry);
        if (info.equals("")){
            tv_remarkinfo.setVisibility(View.GONE);
        }else {
            tv_remarkinfo.setText(info);
        }
        if (type.equals("1")){
            typeimg.setImageResource(R.drawable.home_sanitary_xh);
        }else if (type.equals("2")){
            typeimg.setImageResource(R.drawable.home_appliance_fix_xh);
        }else if (type.equals("3")){
            typeimg.setImageResource(R.drawable.home_lanterns_xh);
        }else if (type.equals("4")){
            typeimg.setImageResource(R.drawable.home_otherfix_xh);
        }else if (type.equals("48")){
            typeimg.setImageResource(R.drawable.home_appliance_clean_xh);
        }else {
            typeimg.setImageResource(R.drawable.home_appliance_clean_xh);
        }
        if (status.equals("1")){
            btn_cancel.setVisibility(View.VISIBLE);
            Rbutton.setVisibility(View.GONE);
            Rfixcard.setVisibility(View.GONE);
        }else if (status.equals("2")){
            btn_cancel.setVisibility(View.GONE);
            Rbutton.setVisibility(View.GONE);
            Rfixcard.setVisibility(View.GONE);
        }else if(status.equals("3")){
            Rbutton.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.VISIBLE);
            Rfixcard.setVisibility(View.GONE);
            jsonmaster();
        }else if (status.equals("4")){
            String repair_man_cancel_info=jsonObject.optString("repair_man_cancel_info");
            Repair_cancel.setVisibility(View.VISIBLE);
            tv_cancelinfo.setText(repair_man_cancel_info);
            btn_cancel.setVisibility(View.VISIBLE);
            Rbutton.setVisibility(View.GONE);
            Rfixcard.setVisibility(View.GONE);
        }else if (status.equals("5")){
            Rbutton.setVisibility(View.GONE);
            Rfixcard.setVisibility(View.GONE);
            jsonmaster();
        }else if (status.equals("6")){
            jsonmaster();
            Rbutton.setVisibility(View.GONE);
            serve_time=jsonObject.optString("serve_time");
            String coupon_flag=jsonObject.optString("coupon_flag");
            guarantee_day=jsonObject.optString("guarantee_day");
            repairman_id=jsonObject.optString("repairman_id");
            repairman_name=jsonObject.optString("repairman_name");
            jsondicount=jsonObject.optJSONArray("coupon");
            Lpayamount.setVisibility(View.VISIBLE);
            if (coupon_flag.equals("1")){
                Rvoucher.setEnabled(false);
                tv_usecoupon.setText("不可用");
                tv_usecoupon.setTextColor(0xFFF96331);
            }else {
                Rvoucher.setEnabled(true);
                if (jsondicount.length()!=0){
                    tv_usecoupon.setText("有券，点击使用");
                    tv_usecoupon.setTextColor(0xFFF96331);
                }else {
                    tv_usecoupon.setText("无券，去分享获取");
                    tv_usecoupon.setTextColor(0xFFF96331);
                }
            }
            Lfixcard.setVisibility(View.VISIBLE);
            Rmaster.setVisibility(View.VISIBLE);
            Lcoupon.setVisibility(View.VISIBLE);
            tv_guarantee_day.setText(guarantee_day);
            showfee();
            realmoney=amount;
            tv_mustpay.setText(amount);
            tv_payamount.setText("¥"+realmoney);
            Realamount.setVisibility(View.VISIBLE);
        }else if (status.equals("7")){
            Rbutton.setVisibility(View.VISIBLE);
            serve_time=jsonObject.optString("serve_time");
            real_amount=jsonObject.optString("real_amount");
            pay_method=jsonObject.optString("pay_method");
            finish_time=jsonObject.optString("finish_time");
            guarantee_day=jsonObject.optString("guarantee_day");
            repairman_id=jsonObject.optString("repairman_id");
            repairman_name=jsonObject.optString("repairman_name");
            String coupon_amount=jsonObject.optString("coupon_amount");
            Lfixcard.setVisibility(View.VISIBLE);
            Rpayway.setVisibility(View.VISIBLE);
            Rcoupon.setVisibility(View.VISIBLE);
            Realamount.setVisibility(View.VISIBLE);
            tv_guarantee_day.setText(guarantee_day);
            showfee();
            tv_mustpay.setText(real_amount);
            if (coupon_amount.equals("")){
                tv_totalcoupon.setText("0.00");
            }else {
                tv_totalcoupon.setText(coupon_amount);
            }
            tv_payway.setText(pay_method);
            btn_evaluate.setVisibility(View.VISIBLE);
            Lpayamount.setVisibility(View.GONE);
        }else if (status.equals("8")){
            if (jsonObject.has("evaluate_score")){
                Levaluate.setVisibility(View.VISIBLE);
                evaluate_score=jsonObject.optString("evaluate_score");
                evaluate_info=jsonObject.optString("evaluate_info");
            }else {
                Levaluate.setVisibility(View.GONE);
            }
            Rbutton.setVisibility(View.VISIBLE);
            finishInfo();
        }else if (status.equals("9")){
            String refuse_reason=jsonObject.optString("refuse_reason");
            Repair_cancel.setVisibility(View.VISIBLE);
            Lpayamount.setVisibility(View.GONE);
            tv_cancelinfo.setText(refuse_reason);
            reason_title.setText("拒单提示：");
        }else if (status.equals("10")){
            if (jsonObject.has("evaluate_score")){
                Levaluate.setVisibility(View.VISIBLE);
                evaluate_score=jsonObject.optString("evaluate_score");
                evaluate_info=jsonObject.optString("evaluate_info");
            }else {
                Levaluate.setVisibility(View.GONE);
            }
            Rbutton.setVisibility(View.GONE);
            finishInfo();
        }else if (status.equals("11")){
            Rbutton.setVisibility(View.GONE);
            Rfixcard.setVisibility(View.GONE);
           // jsonmaster();
        }else if(status.equals("14")){
            String repair_man_cancel_info=jsonObject.optString("repair_man_cancel_info");
            reason_title.setText("改单原因：");
            Repair_cancel.setVisibility(View.VISIBLE);
            tv_cancelinfo.setText(repair_man_cancel_info);
            btn_cancel.setVisibility(View.VISIBLE);
            Rbutton.setVisibility(View.GONE);
            Rfixcard.setVisibility(View.GONE);
        }
        if (jsonObject.has("guarantee_stop_day")){
            String guaranteeStopday=jsonObject.optString("guarantee_stop_day");
            String curDate= DateUtils.getCurDate("yyyy-MM-dd");
            if (DateUtils.isDateBigger(curDate,guaranteeStopday)){
                btnRefix.setVisibility(View.VISIBLE);
                btnRefund.setVisibility(View.VISIBLE);
            }else {
                btnRefix.setVisibility(View.GONE);
                btnRefund.setVisibility(View.GONE);
            }
        }else {
            btnRefix.setVisibility(View.GONE);
            btnRefund.setVisibility(View.GONE);
        }
    }
    private void finishInfo() {
        serve_time=jsonObject.optString("serve_time");
        real_amount=jsonObject.optString("real_amount");
        pay_method=jsonObject.optString("pay_method");
        guarantee_day=jsonObject.optString("guarantee_day");
        repairman_id=jsonObject.optString("repairman_id");
        repairman_name=jsonObject.optString("repairman_name");
        String coupon_amount=jsonObject.optString("coupon_amount");
        tv_payway.setText(pay_method);
        tv_guarantee_day.setText(guarantee_day);
        Lfixcard.setVisibility(View.VISIBLE);
        Rpayway.setVisibility(View.VISIBLE);
        Rcoupon.setVisibility(View.VISIBLE);
        Realamount.setVisibility(View.VISIBLE);
        showfee();
        tv_mustpay.setText(real_amount);
        if (coupon_amount.equals("")){
            tv_totalcoupon.setText("0.00");
        }else {
            tv_totalcoupon.setText(coupon_amount);
        }
        finish_time=jsonObject.optString("finish_time");
        Lpayamount.setVisibility(View.GONE);
        if (evaluate_info.equals("1")){
            evaluateimg.setImageResource(R.drawable.star_one_h);
        }else if (evaluate_score.equals("2")){
            evaluateimg.setImageResource(R.drawable.star_two_h);
        }else if (evaluate_score.equals("3")){
            evaluateimg.setImageResource(R.drawable.star_three_h);
        }else if (evaluate_score.equals("4")){
            evaluateimg.setImageResource(R.drawable.star_four_h);

        }else if (evaluate_score.equals("5")){
            evaluateimg.setImageResource(R.drawable.star_five_h);
        }
    }
    private void showfee() {
        amount=jsonObject.optString("amount");
        String detect_fee=jsonObject.optString("detect_fee");
        String material_fee=jsonObject.optString("material_fee");
        String serve_fee=jsonObject.optString("serve_fee");
        Lcostdetail.setVisibility(View.VISIBLE);
        tv_totalamount.setText(amount);
        tv_detect_fee.setText(detect_fee);
        tv_material_fee.setText(material_fee);
        tv_serve_fee.setText(serve_fee);
    }
    private void jsonmaster() {
        distribute_time=jsonObject.optString("distribute_time");
        repairman_id=jsonObject.optString("repairman_id");
        repairman_name=jsonObject.optString("repairman_name");
        repairman_phone=jsonObject.optString("repairman_phone");
        Rmaster.setVisibility(View.VISIBLE);
        tv_mastername.setText(repairman_name);

    }
    private void faifure(String error) {
        new PromptDialog.Builder(this)
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
        initfindview();
        initdata();
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
                    String diamount=data.getStringExtra("amount");
                    tv_usecoupon.setText(diamount+"元");
                    tv_usecoupon.setTextColor(0xff555555);
                    couponAmount =Double.parseDouble(diamount);
                    double doubleamount=Double.parseDouble(amount);
                    double  realamount=doubleamount- couponAmount;
                    NumberFormat format=new DecimalFormat("0.##");
                    realmoney=format.format(realamount);
                    tv_mustpay.setText(realmoney);
                    tv_payamount.setText("¥"+realmoney);
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
    private void initfindview() {
        typeimg=(ImageView)findViewById(R.id.id_img_type);
        forwardimg=(ImageView)findViewById(R.id.id_img_forward2);
        evaluateimg=(ImageView)findViewById(R.id.id_evaluate_status);
        btn_cancel=(Button)findViewById(R.id.id_cancel_order);
        btn_evaluate=(Button)findViewById(R.id.id_evaluate_order);
        tv_status=(TextView) findViewById(R.id.id_tv_status);
        tv_title=(TextView)findViewById(R.id.id_tv_title);
        tv_type=(TextView)findViewById(R.id.id_tv_type);
        tv_appoint_time=(TextView)findViewById(R.id.id_tv_appoint_time);
        tv_orderNo=(TextView)findViewById(R.id.id_orderNo);
        tv_phone=(TextView)findViewById(R.id.id_phone);
        tv_createtime=(TextView)findViewById(R.id.id_create_time);
        tv_address=(TextView)findViewById(R.id.id_tv_address);
        tv_remarkinfo=(TextView)findViewById(R.id.id_tv_remark);
        tv_cancelinfo=(TextView)findViewById(R.id.id_repair_man_cancel_info);
        reason_title=(TextView)findViewById(R.id.text3);
        tv_mastername=(TextView)findViewById(R.id.id_master);
        tv_detect_fee=(TextView)findViewById(R.id.id_detect_fee);
        tv_material_fee=(TextView)findViewById(R.id.id_material_fee);
        tv_serve_fee=(TextView)findViewById(R.id.id_serve_fee);
        tv_totalamount=(TextView)findViewById(R.id.id_total_amount);
        tv_mustpay=(TextView)findViewById(R.id.id_real_amount);
        tv_totalcoupon=(TextView)findViewById(R.id.id_coupon_amount);
        tv_usecoupon=(TextView)findViewById(R.id.id_use_coupon);
        tv_payway=(TextView)findViewById(R.id.id_tv_payway);
        tv_payamount=(TextView)findViewById(R.id.id_pay_amount);
        tv_guarantee_day=(TextView)findViewById(R.id.id_tv_date);
        Rfixcard=findViewById(R.id.id_re_fixcard);
        Rbutton=findViewById(R.id.id_re_button);
        Rpayfee=findViewById(R.id.id_re_pay);
        btnRefix=(Button)findViewById(R.id.id_btn_refix);
        btnRefund=(Button)findViewById(R.id.id_btn_refund);
        Rvoucher=findViewById(R.id.id_re_voucher);
        Rexpense=findViewById(R.id.id_re_expanse);
        Rphone=findViewById(R.id.id_re_phone);
        Repair_cancel=findViewById(R.id.id_relayout_cancel);
        Rmaster=findViewById(R.id.id_master_info);
        Rforwars=findViewById(R.id.id_relauout_master);
        Rpayway=(LinearLayout)findViewById(R.id.id_re_payway);
        Rcoupon=(LinearLayout)findViewById(R.id.id_re_coupon);
        Realamount=(LinearLayout)findViewById(R.id.id_re_realamount);
        Levaluate=(LinearLayout)findViewById(R.id.id_li_evaluate);
        Lcostdetail=findViewById(R.id.id_order_expense);
        Lcoupon=(LinearLayout)findViewById(R.id.id_li_coupon);
        Lexpense=(LinearLayout)findViewById(R.id.id_li_expense);
        Lpayamount=(LinearLayout)findViewById(R.id.id_li_pay);
        Lfixcard=findViewById(R.id.id_li_fixcard);
        btnRefix.setOnClickListener(this);
        btnRefund.setOnClickListener(this);
    }
    private void initdata() {
        customDialog.show();
        requestCode=0;
        requestService();
    }
    private void initEvent() {
        btn_cancel.setOnClickListener(this);
        Rpayfee.setOnClickListener(this);
        btn_evaluate.setOnClickListener(this);
        Rforwars.setOnClickListener(this);
        Rphone.setOnClickListener(this);
        Levaluate.setOnClickListener(this);
        Rvoucher.setOnClickListener(this);
        Rfixcard.setOnClickListener(this);
        Rexpense.setTag(0);
        Rexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag=(Integer)v.getTag();
                switch (tag){
                    case 0:
                        forwardimg.setImageResource(R.drawable.downward_m);
                        Lexpense.setVisibility(View.VISIBLE);
                        v.setTag(1);
                        break;
                    case 1:
                        forwardimg.setImageResource(R.drawable.forward_m);
                        Lexpense.setVisibility(View.GONE);
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
                goevaluate();
                break;
            case R.id.id_cancel_order:
                cancelorder();
                break;
            case R.id.id_re_pay:
                payorder();
                break;
            case R.id.id_relauout_master:
                masterinfo();
                break;
            case R.id.id_li_evaluate:
                lookevaluate();
                break;
            case R.id.id_re_phone:
                calltomaster();
                break;
            case R.id.id_re_voucher:
                getVoucher();
                break;
            case R.id.id_btn_refix:
                repeatFix();
                break;
            case R.id.id_btn_refund:
                reRund();
                break;
            case R.id.id_re_fixcard:
                fixcard();
                break;
            default:
                break;
        }
    }
    private void fixcard() {
        Intent intent=new Intent(context,Warrantycard.class);
        intent.putExtra("title",title);
        intent.putExtra("orderNo",orderNo);
        intent.putExtra("guarantee_day",guarantee_day);
        intent.putExtra("mastername",repairman_name);
        intent.putExtra("serve_time",serve_time);
        startActivity(intent);
    }
    private void repeatFix() {
        Intent repeat=new Intent(context,RepeatFix.class);
        repeat.putExtra("reid",cid);
        repeat.putExtra("id",orderId);
        repeat.putExtra("orderNo",orderNo);
        repeat.putExtra("type",type);
        repeat.putExtra("title",title);
        repeat.putExtra("parent_category",parent_cateogry);
        repeat.putExtra("finish_time",finish_time);
        repeat.putExtra("phone",phone);
        repeat.putExtra("address",address);
        startActivityForResult(repeat,0x004);
    }
    private void reRund() {
        Intent fund=new Intent(context,RefundApply.class);
        fund.putExtra("id",orderId);
        fund.putExtra("orderNo",orderNo);
        fund.putExtra("type",type);
        fund.putExtra("title",title);
        fund.putExtra("parent_category",parent_cateogry);
        fund.putExtra("finish_time",finish_time);
        fund.putExtra("real_amount",real_amount);
        startActivityForResult(fund,0x004);
    }
    private void getVoucher() {
        Intent voucher=new Intent(context, SelectVoucher.class);
        voucher.putExtra("pay_amount",amount);
        voucher.putExtra("category","1");
        startActivityForResult(voucher,1);
    }
    private void calltomaster() {
        new PromptDialog.Builder(this)
                .setTitle("客服热线")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage( repairman_phone)
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
                            if (MyorderworkDetail.this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" +  repairman_phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                phoneNo=repairman_phone;
                                ActivityCompat.requestPermissions(MyorderworkDetail.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + repairman_phone));
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
                Toast.makeText(this,"授权失败",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void masterinfo() {
        Intent master=new Intent(context,MasterDetail.class);
        master.putExtra("masterId",repairman_id);
        startActivity(master);
    }
    private void payorder() {
        Intent pay=new Intent(context,RepairPayment.class);
        pay.putExtra("realmoney",realmoney);
        pay.putExtra("orderNo",orderNo);
        pay.putExtra("orderId",orderId);
        pay.putExtra("couponId",couponId);
        startActivityForResult(pay,0x005);
    }
    private void lookevaluate() {
        Intent evaluate=new Intent(context,LookEvaluate.class);
        evaluate.putExtra("id",orderId);
        evaluate.putExtra("orderNo",orderNo);
        evaluate.putExtra("type",type);
        evaluate.putExtra("title",title);
        evaluate.putExtra("finish_time",finish_time);
        evaluate.putExtra("parent_category",parent_cateogry);
        evaluate.putExtra("real_amount",real_amount);
        evaluate.putExtra("evaluate_score",evaluate_score);
        evaluate.putExtra("evaluate_info",evaluate_info);
        startActivity(evaluate);
    }
    private void cancelorder() {
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
                        customDialog.show();
                        requestCode=1;
                        requestService();
                    }
                })
                .show();
    }
    private void requestService() {
        String validateURL ="";
        if (requestCode==0){
            validateURL = UrlUtil.RepairOrder_detailUrl;
        }else if (requestCode==1){
            validateURL =UrlUtil.RepairOrder_cancelUrl;
        }
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        SendrequestUtil.PostDataFromService(validateURL,textParams,requestHandler);
    }
    private void goevaluate() {
        Intent evaluate=new Intent(context,RepairEvaluate.class);
        evaluate.putExtra("send_type","1");
        evaluate.putExtra("id",orderId);
        evaluate.putExtra("orderNo",orderNo);
        evaluate.putExtra("type",type);
        evaluate.putExtra("title",title);
        evaluate.putExtra("parent_category",parent_cateogry);
        evaluate.putExtra("finish_time",finish_time);
        evaluate.putExtra("real_amount",real_amount);
        startActivityForResult(evaluate,1);
    }
    private void clearcoupon() {   //取消使用优惠
        tv_usecoupon.setText("0");
        realmoney=amount;
        tv_mustpay.setText(realmoney);
    }
}
