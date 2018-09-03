package com.msht.minshengbao.functionActivity.WaterApp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.adapter.WaterOrderAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.SecretKeyUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.Dialog.WaterDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WaterOrderListActivity extends BaseActivity {
    private View layoutNoData;
    private XListView mListView;
    private int pageIndex=0;
    private int pageNo   = 1;
    private WaterOrderAdapter mAdapter;
    private String    account="";
    private CustomDialog customDialog;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private int refreshType=0;
    private static final int  REQUEST_CALL_PHONE=2;
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private final CancelHandler cancelHandler=new CancelHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<WaterOrderListActivity> mWeakReference;
        public RequestHandler(WaterOrderListActivity activity) {
            mWeakReference=new WeakReference<WaterOrderListActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterOrderListActivity activity=mWeakReference.get();
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
                        String error = object.optString("message");
                        JSONObject jsonObject =object.optJSONObject("data");
                        boolean firstPage=jsonObject.optBoolean("firstPage");
                        boolean lastPage=jsonObject.optBoolean("lastPage");
                        JSONArray jsonArray=jsonObject.optJSONArray("list");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.refreshType==0){
                                activity.mListView.stopRefresh(true);
                            }else if (activity.refreshType==1){
                                activity.mListView.stopLoadMore();
                            }
                            if (lastPage){
                                activity.mListView.setPullLoadEnable(false);
                            }else {
                                activity.mListView.setPullLoadEnable(true);
                            }
                            if(jsonArray.length()>0){
                                if (activity.pageNo==1){
                                    activity.orderList.clear();
                                }
                            }
                            activity.onReceiveOrderData(jsonArray);
                        }else {
                            ToastUtil.ToastText(activity.context,error);
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
    private static class CancelHandler extends Handler{
        private WeakReference<WaterOrderListActivity> mWeakReference;

        public CancelHandler(WaterOrderListActivity activity) {
            mWeakReference=new WeakReference<WaterOrderListActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final WaterOrderListActivity activity=mWeakReference.get();
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
                        String Results=object.optString("result");
                        String message = object.optString("message");
                        if(Results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.showDialog("提示","订单已取消");
                        }else {
                            ToastUtil.ToastText(activity.context,message);
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
    private void showDialog(String s, String title) {
        new PromptDialog.Builder(this)
                .setTitle(title)
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        setResult(0x001);
                        initOrderData();
                    }
                }).show();
    }

    private void onReceiveOrderData(JSONArray jsonArray) {
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
            mAdapter.notifyDataSetChanged();
            layoutNoData.setVisibility(View.GONE);
        }else {
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_order_list);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        setCommonHeader("配送订单");
        initView();
        mAdapter=new WaterOrderAdapter(context,orderList);
        mListView.setAdapter(mAdapter);
        initOrderData();
        mAdapter.setOnItemSelectCancelListener(new WaterOrderAdapter.OnItemSelectCancelListener() {
            @Override
            public void ItemSelectCancelClick(View view, int thisposition) {
                String orderNo=orderList.get(thisposition).get("orderNo");
                DialogTip(orderNo);
            }
        });
        mAdapter.setOnItemSelectPhoneListener(new WaterOrderAdapter.OnItemSelectPhoneListener() {
            @Override
            public void ItemSelectPhoneClick(View view, int thisposition) {
                String phone=orderList.get(thisposition).get("deliveryPhone");
                requestPermission(phone);
            }
        });
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                refreshType=0;
                loadData(1);
            }
            @Override
            public void onLoadMore() {
                refreshType=1;
                loadData(pageIndex + 1);
            }
        });
    }

    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String type="3";
        String validateURL = UrlUtil.WATER_ORDER_LIST_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("account",account);
        textParams.put("type",type);
        textParams.put("page","1");
        textParams.put("pageSize","20");
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private void requestPermission(final String phone) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
                MPermissionUtils.requestPermissionsResult(this, REQUEST_CALL_PHONE, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int Code) {
                        callphone(phone);
                    }
                    @Override
                    public void onPermissionDenied(int Code) {
                        Toast.makeText(context,"没有权限您将无法进行扫描操作！",Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                callphone(phone);
            }
        }else {
            callphone(phone);
        }
    }

    private void callphone(String phone) {
        try{
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }
    private void DialogTip(final String orderNo) {

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
                Cancelrequest(orderNo);
            }
        });
        waterDialog.show();
    }

    private void Cancelrequest(String orderNo) {
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String operTime=DateUtils.getDateToString(time,pattern);
        String jsonresult="";
        JSONObject object=new JSONObject();
        try{
            object.put("operAccount",account);
            object.put("userType","1");
            object.put("operType","4");
            object.put("orderNo",orderNo);
            object.put("operTime",operTime);
            jsonresult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("operAccount",account);
        treeMap.put("userType","1");
        treeMap.put("operType","4");
        treeMap.put("orderNo",orderNo);
        treeMap.put("operTime",operTime);
        String extParams= SecretKeyUtil.getKeyextParams(jsonresult);
        String sign= SecretKeyUtil.getKeySign(treeMap);
        customDialog.show();
        String validateURL= UrlUtil.WATER_CANCEL_URL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        SendRequestUtil.postDataFromService(validateURL,textParams,cancelHandler);
    }

    private void initOrderData() {
        customDialog.show();
        loadData(1);
    }
    private void initView() {
        TextView mText=(TextView)findViewById(R.id.id_tv_nodata);
        mText.setText("亲，您还没有订单数据");
        mListView=(XListView)findViewById(R.id.id_more_list);
        mListView.setPullLoadEnable(true);
        layoutNoData =findViewById(R.id.id_nodata_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
