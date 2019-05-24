package com.msht.minshengbao.functionActivity.lpgActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.msht.minshengbao.adapter.LpgDeliveryInfoAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.CallPhoneUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.custom.CircleImageView;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.DeliveryInfoDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.custom.widget.ListViewForScrollView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/23  
 */
public class LpgDeliveryInformationActivity extends BaseActivity {
    private CircleImageView circleImageView;
    private TextView tvPersonnel;
    private TextView tvOrderNo;
    private TextView tvDeliverySite;
    private View     layoutTransferTrajectory;
    private String  lpgUserId;
    private String  orderId;
    private String  employeeMobile;
    private String  employeeName;
    private String  siteName;
    private String  totalSend;
    private String  employeeId;
    private String  sendForMeCount;
    private String  employeeHeadUrl;
    private static final String PAGE_NAME="配送信息";
    private static  final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    private LpgDeliveryInfoAdapter mAdapter;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private CustomDialog customDialog;
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<LpgDeliveryInformationActivity> mWeakReference;
        public RequestHandler(LpgDeliveryInformationActivity activity) {
            mWeakReference = new WeakReference<LpgDeliveryInformationActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgDeliveryInformationActivity activity=mWeakReference.get();
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
                            JSONArray jsonArray=object.optJSONArray("lists");
                            activity.onReceiveData(object);
                            activity.onReceiveFlowList(jsonArray);
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
        new PromptDialog.Builder(context)
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
        employeeName=object.optString("employeeName");
        siteName=object.optString("siteName");
        totalSend=object.optString("totalSend");
        String employeeCode=object.optString("employeeCode");
        employeeMobile=object.optString("employeeMobile");
        employeeId=object.optString("employeeId");
        boolean isSendEmp=object.optBoolean("isSendEmp");
        sendForMeCount=object.optString("sendForMeCount");
        employeeHeadUrl=object.optString("employeeHeadUrl");
        tvPersonnel.setText(employeeName);
        tvDeliverySite.setText(siteName);
        tvOrderNo.setText(employeeCode);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.potrait);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        Glide.with(context)
                .load(employeeHeadUrl)
                .apply(requestOptions).into(circleImageView);
    }
    /**
     *  钢瓶流转轨迹图
     * @param jsonArray 流转轨迹数据
     */
    private void onReceiveFlowList(JSONArray jsonArray) {
        if (jsonArray.length() > 0) {
            layoutTransferTrajectory.setVisibility(View.VISIBLE);
        } else {
            layoutTransferTrajectory.setVisibility(View.GONE);
        }
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int status = jsonObject.optInt("orderStatus");
                String orderId = jsonObject.optString("orderId");
                String createDate = jsonObject.optString("createDate");
                String describe="";
                if (status==0){
                    describe="您已提交了订单，请等待系统确认。";
                }else if (status==1){
                    describe="您的订单已被"+siteName+"网点受理，请耐心等待。";
                }else if (status==2){
                    describe="您的订单正在配送途中，请您准备签收(配送员："+employeeName+"，电话："+employeeMobile
                            +"),感谢您的耐心等待。";
                }else if (status==3){
                    describe="您的订单已签收，感谢您使用瓶装气业务，期待您的再次使用。";
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("describe", describe);
                map.put("createDate", createDate);
                mList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_delivery_information);
        context=this;
        setCommonHeader("配送信息");
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        if (data!=null){
            orderId=data.getStringExtra("orderId");
        }
        lpgUserId= SharedPreferencesUtil.getStringData(this, SharedPreferencesUtil.LPG_USER_ID,"");
        ListViewForScrollView mListView=(ListViewForScrollView)findViewById(R.id.id_delivery_information);
        tvDeliverySite=(TextView)findViewById(R.id.id_tv_site) ;
        tvOrderNo=(TextView)findViewById(R.id.id_tv_order) ;
        tvPersonnel=(TextView)findViewById(R.id.id_tv_personnel);
        circleImageView=(CircleImageView)findViewById(R.id.id_portrait_view) ;
        layoutTransferTrajectory=findViewById(R.id.id_layout_delivery);
        mAdapter=new LpgDeliveryInfoAdapter(context,mList);
        mListView.setAdapter(mAdapter);
        initDeliveryData();
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeliveryDialog();
            }
        });
    }
    private void onDeliveryDialog() {
        new DeliveryInfoDialog(context,employeeHeadUrl,employeeMobile).builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setEmployeeMoId(employeeId)
                .setEmployeeName(employeeName)
                .setSendForMeCount(sendForMeCount)
                .setSendTotal(totalSend)
                .setSiteName(siteName)
                .setOnCallPhoneClickListener(new DeliveryInfoDialog.OnCallPhoneClickListener() {
                    @Override
                    public void onClick(String phone) {
                        callPhone(phone);
                    }
                }).show();
    }

    private void callPhone(final String phone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                CallPhoneUtil.callPhone(context,phone);
            } else {
                MPermissionUtils.requestPermissionsResult(this, MY_PERMISSIONS_REQUEST_CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int code) {
                        if (code==MY_PERMISSIONS_REQUEST_CALL_PHONE){
                            CallPhoneUtil.callPhone(context,phone);
                        }
                    }
                    @Override
                    public void onPermissionDenied(int code) {
                        ToastUtil.ToastText(context,"没有权限您将无法进行相关操作！");
                    }
                });
            }
        }else {
            CallPhoneUtil.callPhone(context,phone);
        }
    }
    private void initDeliveryData() {
        customDialog.show();
        String requestUrl= UrlUtil.LPG_QUERY_ORDER_FLOW;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("id",orderId);
        textParams.put("userId",lpgUserId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,textParams,requestHandler);
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
