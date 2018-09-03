package com.msht.minshengbao.functionActivity.WaterApp;

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
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.adapter.WaterOrderAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.HtmlPageActivity;
import com.msht.minshengbao.functionActivity.Public.QrCodeScanActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.SecretKeyUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.Dialog.WaterDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.ViewUI.widget.VerticalSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WaterHomeActivity extends BaseActivity implements View.OnClickListener {

    private String    account="";
    private TextView tvTotalAmount;
    private TextView tvPayAmount;
    private TextView tvGiveAmount;
    private int pageNo=1;
    private String   phone="";
    private ListViewForScrollView mListView;
    private WaterOrderAdapter mAdapter;
    private int requestType=0;
    private VerticalSwipeRefreshLayout mSwipeRefresh;
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    private static final int    REQUEST_CALL_PHONE=2;
    private static  final int MY_CAMERA_REQUEST=1;
    private CustomDialog customDialog;
    private final BalanceHandler balanceHandler=new BalanceHandler(this);
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class BalanceHandler extends Handler{
        private WeakReference<WaterHomeActivity> mWeakReference;
        public BalanceHandler(WaterHomeActivity activity) {
            mWeakReference=new WeakReference<WaterHomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final WaterHomeActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String message = object.optString("message");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestType==0){
                                activity.mSwipeRefresh.setRefreshing(false);
                                JSONObject json =object.optJSONObject("data");
                                activity.onReceiveAccountData(json);
                            }else if (activity.requestType==1){
                                activity.onShowDialog("提示","订单已取消");
                            }
                        }else {
                            ToastUtil.ToastText(activity.context,message);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.mSwipeRefresh.setRefreshing(false);
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class RequestHandler extends Handler{
        private WeakReference<WaterHomeActivity> mWeakReference;
        public RequestHandler(WaterHomeActivity activity) {
            mWeakReference=new WeakReference<WaterHomeActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterHomeActivity activity =mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    if (activity.customDialog.isShowing()&&activity.customDialog!=null){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("message");
                        JSONObject jsonObject =object.optJSONObject("data");
                        boolean firstPage=jsonObject.optBoolean("firstPage");
                        boolean lastPage=jsonObject.optBoolean("lastPage");
                        JSONArray jsonArray=jsonObject.optJSONArray("list");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.mSwipeRefresh.setRefreshing(false);
                            if(jsonArray.length()>0){
                                if (activity.pageNo==1){
                                    activity.orderList.clear();
                                }
                            }
                            activity.onReceiveOrderListData(jsonArray);
                        }else {
                            ToastUtil.ToastText(activity.context,error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    activity.mSwipeRefresh.setRefreshing(false);
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onReceiveOrderListData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String id=json.optString("id");
                String type = json.getString("type");
                String orderNo= json.getString("orderNo");
                String status = json.getString("status");
                String amount =json.optString("amount");
                String waterNum=json.optString("waterNum");
                String bucketSpec=json.optString("bucketSpec");
                String bucketNum=json.optString("bucketNum");
                String bucketFlag=json.optString("bucketFlag");
                String applyDeliveryTime=json.optString("applyDeliveryTime");
                String address=json.optString("address");
                String deliveryPhone=json.optString("deliveryPhone");
                String deliveryFlag=json.optString("deliveryFlag");
                String doorplate=json.optString("doorplate");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("type", type);
                map.put("orderNo", orderNo);
                map.put("status",status);
                map.put("amount",amount);
                map.put("waterNum",waterNum);
                map.put("bucketSpec",bucketSpec);
                map.put("bucketNum",bucketNum);
                map.put("bucketFlag",bucketFlag);
                map.put("applyDeliveryTime",applyDeliveryTime);
                map.put("deliveryPhone",deliveryPhone);
                map.put("address",address);
                map.put("doorplate",doorplate);
                orderList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (orderList.size()!=0&&orderList!=null){
           // layout_data.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }else {
           // layout_data.setVisibility(View.GONE);
           // layout_nodata.setVisibility(View.VISIBLE);
        }
    }
    private void onReceiveAccountData(JSONObject json) {
        String id=json.optString("id");
        String type=json.optString("type");
        String accounts=json.optString("account");
        String payAmount=json.optString("payBalance");
        String giveAmount=json.optString("giveBalance");
        double payBalance=json.optDouble("payBalance");
        double giveBalance=json.optDouble("giveBalance");
        double totalBalance=payBalance+giveBalance;
        totalBalance= VariableUtil.twoDecinmal2(totalBalance);
        String totalAmount=String.valueOf(totalBalance);
        tvGiveAmount.setText(giveAmount);
        tvPayAmount.setText(payAmount);
        tvTotalAmount.setText(totalAmount);
    }
    private void onShowDialog(String title, String s) {
        new PromptDialog.Builder(context)
                .setTitle(title)
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        initData();
                        initOrderData();
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_home);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        setCommonHeader("直饮水账户");
        initView();
        initRefresh();
        initData();
        mAdapter=new WaterOrderAdapter(context,orderList);
        mListView.setAdapter(mAdapter);
        initOrderData();
        mAdapter.setOnItemSelectCancelListener(new WaterOrderAdapter.OnItemSelectCancelListener() {
            @Override
            public void ItemSelectCancelClick(View view, int thisposition) {
                String orderNo=orderList.get(thisposition).get("orderNo");
                dialogTip(orderNo);
            }
        });
        mAdapter.setOnItemSelectPhoneListener(new WaterOrderAdapter.OnItemSelectPhoneListener() {
            @Override
            public void ItemSelectPhoneClick(View view, int thisposition) {
                phone=orderList.get(thisposition).get("deliveryPhone");
                callPhone();
            }
        });
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
                initOrderData();
            }
        });
    }

    private void callPhone() {
        new PromptDialog.Builder(this)
                .setTitle("师傅电话")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(phone)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {dialog.dismiss();}})
                .setButton2("呼叫", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(callIntent);
                            } else {
                                ActivityCompat.requestPermissions(WaterHomeActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                            }
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phone));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callIntent);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void initOrderData() {
        String type="3";
        String validateURL = UrlUtil.WATER_ORDER_LIST_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("account",account);
        textParams.put("type",type);
        textParams.put("page","1");
        textParams.put("pageSize","20");
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==0x001){
                    initOrderData();
                    initData();
                }
                break;
            case 2:
                if (resultCode==0x002){
                    initData();
                }
                break;
            default:
                break;
        }
    }
    private void initData() {
        requestType=0;
        customDialog.show();
        String validateURL= UrlUtil.WATER_ACCOUNT_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("account",account);
        SendRequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }
    private void initView() {
        mSwipeRefresh=(VerticalSwipeRefreshLayout)findViewById(R.id.id_swipe_refresh);
        rightImg=(ImageView)findViewById(R.id.id_right_img);
        findViewById(R.id.id_layout_more).setOnClickListener(this);
        findViewById(R.id.id_layout_place).setOnClickListener(this);
        findViewById(R.id.id_layout_recharge).setOnClickListener(this);
        findViewById(R.id.id_scan_layout).setOnClickListener(this);
        findViewById(R.id.id_tv_detail).setOnClickListener(this);
        findViewById(R.id.id_forward_img).setOnClickListener(this);
        mListView=(ListViewForScrollView)findViewById(R.id.id_order_list);
        tvGiveAmount =(TextView)findViewById(R.id.id_give_fee);
        tvPayAmount =(TextView)findViewById(R.id.id_pay_fee);
        tvTotalAmount =(TextView)findViewById(R.id.id_total_amount);
        rightImg.setImageResource(R.drawable.water_help_xh);
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_layout_place:
                placeOrder();
                break;
            case R.id.id_layout_recharge:
                rechargeAmount();
                break;
            case R.id.id_tv_detail:
                balanceDetail();
                break;
            case R.id.id_forward_img:
                balanceDetail();
                break;
            case R.id.id_scan_layout:
                requestPermission();
                break;
            case R.id.id_right_img:
                waterHelp();
                break;
            case R.id.id_layout_more:
                moreOrder();
                break;
            default:
                break;
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                MPermissionUtils.requestPermissionsResult(this, MY_CAMERA_REQUEST, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int Code) {
                        scanQrCode();
                    }
                    @Override
                    public void onPermissionDenied(int Code) {
                        ToastUtil.ToastText(context,"没有权限您将无法进行扫描操作！");
                    }
                });

            }else {
                scanQrCode();
            }
        }else {
            scanQrCode();
        }
    }

    private void moreOrder() {
        onShowDialog("民生宝","近期上线");
        /*Intent intent=new Intent(context,WaterOrderList.class);
        startActivityForResult(intent,1);*/
    }
    private void waterHelp() {
        String url="http://msbapp.cn/water_h5/sbtips.html";
        Intent intent =new Intent(context, HtmlPageActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","帮助");
        startActivity(intent);
    }
    private void scanQrCode() {
        Intent intent =new Intent(context, QrCodeScanActivity.class);
        startActivity(intent);
    }
    private void balanceDetail() {
        Intent intent =new Intent(context,WaterBalanceDetailActivity.class);
        startActivity(intent);
    }
    private void rechargeAmount() {
        Intent intent=new Intent(context,WaterRechargeActivity.class);
        startActivityForResult(intent,2);
    }
    private void placeOrder() {
        //onShowDialog("民生宝","近期上线");
        Intent intent=new Intent(context,WaterEquipmentMapActivity.class);
        startActivityForResult(intent,1);
    }
    private void dialogTip(final String orderNo) {
        final WaterDialog waterDialog=new WaterDialog(context);
        waterDialog.setImageIcon(R.drawable.water_cancel_xh);
        waterDialog.setTitleText("确认要取消订单吗？");
        waterDialog.setContentText1("取消后您的款项将返还至账户余额");
        waterDialog.setContentText2("提示：如配送人员取走您的水桶则需去小区业务取回");
        waterDialog.getBtnEnsure().setVisibility(View.GONE);
        waterDialog.setOnNegativeListener("返回",new View.OnClickListener() {
            @Override
            public void onClick(View v) {waterDialog.dismiss();}
        });
        waterDialog.setOnpositiveListener("残忍取消",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterDialog.dismiss();
                cancelRequest(orderNo);
            }
        });
        waterDialog.show();
    }
    private void cancelRequest(String orderNo) {
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String operTime=DateUtils.getDateToString(time,pattern);
        String jsonResult="";
        JSONObject object=new JSONObject();
        try{
            object.put("operAccount",account);
            object.put("userType","1");
            object.put("operType","4");
            object.put("orderNo",orderNo);
            object.put("operTime",operTime);
            jsonResult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("operAccount",account);
        treeMap.put("userType","1");
        treeMap.put("operType","4");
        treeMap.put("orderNo",orderNo);
        treeMap.put("operTime",operTime);
        String extParams= SecretKeyUtil.getKeyextParams(jsonResult);
        String sign= SecretKeyUtil.getKeySign(treeMap);
        requestType=1;
        customDialog.show();
        String validateURL= UrlUtil.WATER_CANCEL_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        SendRequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==REQUEST_CALL_PHONE){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                try{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
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

    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
