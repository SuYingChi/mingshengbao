package com.msht.minshengbao.FunctionActivity.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.WaterIncomeAdapter;
import com.msht.minshengbao.Base.BaseFragment;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaterIncomeFra extends BaseFragment {

    private String  userAccount;
    private String    type="2";
    private View layoutNoData;
    private View layoutData;
    private XListView mListView;
    private int pageIndex=0;
    private int pageNo   = 1;
    private int refreshType=0;
    private CustomDialog customDialog;
    private final String mPageName = "余额明细";
    private WaterIncomeAdapter adapter;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private ArrayList<HashMap<String, String>> incomeList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    public static WaterIncomeFra getinstance(int position) {
        WaterIncomeFra waterIncomeFra = new WaterIncomeFra();
        switch (position){
            case 0:
                waterIncomeFra.type="2";
                break;
            case 1:
                waterIncomeFra.type="1";
                break;
            default:
                break;
        }
        return waterIncomeFra ;
    }

    public WaterIncomeFra() {}
    private static class RequestHandler extends Handler{
        private WeakReference<WaterIncomeFra> mWeakReference;
        public RequestHandler(WaterIncomeFra reference) {
            mWeakReference=new WeakReference<WaterIncomeFra>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            final WaterIncomeFra reference=mWeakReference.get();
            if (reference==null||reference.isDetached()){
                return;
            }
            if (reference.customDialog.isShowing()&&reference.customDialog!=null){
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("message");
                        reference.jsonObject =object.optJSONObject("data");
                        boolean firstPage=reference.jsonObject.optBoolean("firstPage");
                        boolean lastPage=reference.jsonObject.optBoolean("lastPage");
                        reference.jsonArray=reference.jsonObject.optJSONArray("list");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            if (reference.refreshType==0){
                                reference.mListView.stopRefresh(true);
                            }else if (reference.refreshType==1){
                                reference.mListView.stopLoadMore();
                            }
                            if (lastPage){
                                reference.mListView.setPullLoadEnable(false);
                            }else {
                                reference.mListView.setPullLoadEnable(true);
                            }
                            if(reference.jsonArray.length()>0){
                                if (reference.pageNo==1){
                                    reference.incomeList.clear();
                                }
                            }
                            reference.onReceiveIncomeData();
                        }else {
                            reference.mListView.stopLoadMore();
                            reference.mListView.stopRefresh(false);
                            reference.showNotify(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    reference.mListView.stopLoadMore();
                    reference.mListView.stopRefresh(false);
                    reference.showNotify(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onReceiveIncomeData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String id=json.optString("id");
                String type = json.getString("type");
                String payType= json.getString("payType");
                String amount = json.getString("amount");
                String payFee =json.optString("payFee");
                String giveFee=json.optString("giveFee");
                String saleWaterQuantity=json.optString("saleWaterQuantity");
                String payTime=json.optString("payTime");
                String payTypeName=json.optString("payTypeName");
                String bucketNum=json.optString("bucketNum");
                String bucketSpec=json.optString("bucketSpec");
                String deliveryFlag=json.optString("deliveryFlag");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("type", type);
                map.put("payType", payType);
                map.put("payTypeName",payTypeName);
                map.put("amount", amount);
                map.put("payFee",payFee);
                map.put("giveFee",giveFee);
                map.put("saleWaterQuantity",saleWaterQuantity);
                map.put("payTime",payTime);
                map.put("bucketNum",bucketNum);
                map.put("bucketSpec",bucketSpec);
                map.put("deliveryFlag",deliveryFlag);
                incomeList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (incomeList.size()!=0&&incomeList!=null){
            layoutData.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }else {
            adapter.notifyDataSetChanged();
            layoutData.setVisibility(View.GONE);
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }
    private void showNotify(String error) {
        new PromptDialog.Builder(getActivity())
                .setTitle(R.string.my_dialog_title)
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
    public View initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_water_income, null, false);
        Activity mActivity = getActivity();
        customDialog=new CustomDialog(mActivity,"正在加载");
        userAccount= SharedPreferencesUtil.getUserName(mActivity, SharedPreferencesUtil.UserName,"");
        initMyView(mRootView);
        return mRootView;
    }
    private void initMyView(View mRootView) {
        layoutData =mRootView.findViewById(R.id.id_layout_data);
        layoutNoData =mRootView.findViewById(R.id.id_nodata_view);
        TextView mText=(TextView)mRootView.findViewById(R.id.id_tv_nodata);
        mText.setText("亲，您还没有收支数据");
        mListView=(XListView)mRootView.findViewById(R.id.id_income_view);
        mListView.setPullLoadEnable(true);
        adapter = new WaterIncomeAdapter(getActivity(),incomeList);
        mListView.setAdapter(adapter);
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
        String validateURL = UrlUtil.WaterOrder_ListUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("account",userAccount);
        textParams.put("type",type);
        textParams.put("pageNo",pageNum);
        textParams.put("pageSize","16");
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    @Override
    public void initData() {
        customDialog.show();
        loadData(1);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customDialog.isShowing()&&customDialog!=null){
            customDialog.dismiss();
        }
    }
}
