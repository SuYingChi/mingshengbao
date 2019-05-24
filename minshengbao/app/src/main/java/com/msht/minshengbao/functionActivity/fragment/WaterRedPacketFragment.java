package com.msht.minshengbao.functionActivity.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.base.BaseFragment;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.adapter.RedPacketAdapter;
import com.msht.minshengbao.functionActivity.publicModule.QrCodeScanActivity;
import com.msht.minshengbao.functionActivity.waterApp.WaterBalanceActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * @author hong
 */
public class WaterRedPacketFragment extends BaseFragment {


    private View layoutNoData;
    private XRecyclerView mRecyclerView;
    private int pageIndex=0;
    private int refreshType=0;
    private RedPacketAdapter mAdapter;
    private CustomDialog customDialog;
    private String  userAccount;
    private String status="0";
    private  Activity mActivity;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    public static WaterRedPacketFragment getInstance(int position) {
        WaterRedPacketFragment waterIncomeFra = new WaterRedPacketFragment();
        switch (position){
            case 0:
                waterIncomeFra.status="0";
                break;
            case 1:
                waterIncomeFra.status="1";
                break;
            case 2:
                waterIncomeFra.status="2";
                break;
            default:
                break;
        }
        return waterIncomeFra ;
    }
    public WaterRedPacketFragment() {
        // Required empty public constructor
    }

    @Override
    public View initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_water_red_packet, null, false);
        mActivity = getActivity();
        customDialog=new CustomDialog(mActivity,"正在加载");
        userAccount= SharedPreferencesUtil.getUserName(mContext, SharedPreferencesUtil.UserName,"");
        initMyView(mRootView);
        return mRootView;
    }
    private void initMyView(View mRootView) {
        layoutNoData =mRootView.findViewById(R.id.id_noData_view);
        final TextView mText=(TextView)mRootView.findViewById(R.id.id_tv_nodata);
        mText.setText("亲，您还没有红包券");
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView=(XRecyclerView)mRootView.findViewById(R.id.id_redPacket_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter=new RedPacketAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshType=0;
                loadData(1);
            }
            @Override
            public void onLoadMore() {
                refreshType=1;
                loadData(pageIndex+1);
            }
        });
        mAdapter.setButtonCallBack(new RedPacketAdapter.ItemClickButtonCallBack() {
            @Override
            public void onItemButtonClick(int pos) {
                String scope=mList.get(pos).get("scope");
                if (scope.equals(ConstantUtil.VALUE_ONE)){
                    Intent intent=new Intent(mActivity,WaterBalanceActivity.class);
                    startActivity(intent);
                    mActivity.finish();
                }else {
                    requestPermission();
                }
            }
        });
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                scanQrCode();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtil.ToastText(mActivity,"没有权限您将无法进行扫描操作！");
                            }
                        }).start();
            }else {
                scanQrCode();
            }
        }else {
            scanQrCode();
        }
    }

    private void scanQrCode() {
        Intent intent =new Intent(mActivity, QrCodeScanActivity.class);
        startActivityForResult(intent,1);
        mActivity.finish();
    }
    @Override
    public void initData() {
        customDialog.show();
        loadData(1);
    }
    private void loadData(int i) {
        pageIndex =i;
        String validateURL = UrlUtil.WATER_COUPON_LIST;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("account",VariableUtil.waterAccount);
        textParams.put("status",status);
        textParams.put("pageNo",String.valueOf(pageIndex));
        textParams.put("pageSize","16");
        OkHttpRequestManager.getInstance(mContext.getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog.isShowing()&&customDialog!=null){
                    customDialog.dismiss();
                }
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
                onAnalysisData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
                if (customDialog.isShowing()&&customDialog!=null){
                    customDialog.dismiss();
                }
                ToastUtil.ToastText(mContext,data.toString());
            }
        });
    }
    private void onAnalysisData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("message");
            JSONObject jsonObject =object.optJSONObject("data");
            boolean lastPage=jsonObject.optBoolean("lastPage");
            JSONArray jsonArray=jsonObject.optJSONArray("list");
            if (lastPage){
                mRecyclerView.setLoadingMoreEnabled(false);
            }else {
                mRecyclerView.setLoadingMoreEnabled(true);
            }
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                if(jsonArray.length()>0){
                    if (pageIndex==1){
                        mList.clear();
                    }
                }
                onReceiveData(jsonArray);
            }else {
                ToastUtil.ToastText(mContext,error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onReceiveData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String code = json.getString("code");
                String name=json.optString("name");
                String status= json.getString("status");
                String scope = json.getString("scope");
                String conditionType =json.optString("conditionType");
                String conditionAmount=json.optString("conditionAmount");
                String discountAmount=json.optString("discountAmount");
                String description=json.optString("description");
                String days=json.optString("days");
                String usageTime="使用时间"+json.optString("usageTime");
                String endTime="有效期至"+json.optString("endTime");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("code", code);
                map.put("name", name);
                map.put("status",status);
                map.put("scope",scope);
                map.put("conditionType",conditionType);
                map.put("conditionAmount",conditionAmount);
                map.put("discountAmount",discountAmount);
                map.put("description",description);
                map.put("days",days);
                map.put("endTime",endTime);
                map.put("usageTime",usageTime);
                map.put("isVisible","1");
                map.put("type","");
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList.size()!=0&&mList!=null){
            layoutNoData.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }else {
            mAdapter.notifyDataSetChanged();
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customDialog.isShowing()&&customDialog!=null){
            customDialog.dismiss();
        }
        OkHttpRequestManager.getInstance(mContext.getApplicationContext()).requestCancel(mContext);
    }
}
