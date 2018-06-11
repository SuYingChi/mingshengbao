package com.msht.minshengbao.FunctionActivity.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaterIncomeFra extends BaseFragment {

    private String  userAccount,password;
    private String    type="2";
    private Activity mActivity;
    private View layout_nodata;
    private View layout_data;
    private TextView mText;
    private XListView mListView;
    private int pageIndex=0;
    private int pageNo   = 1;
    private int refreshType=0;
    private CustomDialog customDialog;
    private final String mPageName = "余额明细";
    private WaterIncomeAdapter adapter;
    private final int SUCCESS   = 1;
    private final int FAILURE   = 0;
    private JSONArray jsonArray;   //数据解析
    private JSONObject jsonObject;
    private ArrayList<HashMap<String, String>> incomeList = new ArrayList<HashMap<String, String>>();
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
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    if (customDialog.isShowing()&&customDialog!=null){
                        customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("message");
                        jsonObject =object.optJSONObject("data");
                        boolean firstPage=jsonObject.optBoolean("firstPage");
                        boolean lastPage=jsonObject.optBoolean("lastPage");
                        jsonArray=jsonObject.optJSONArray("list");
                        if(Results.equals("success")) {
                            if (refreshType==0){
                                mListView.stopRefresh(true);
                            }else if (refreshType==1){
                                mListView.stopLoadMore();
                            }
                            if (lastPage){
                                mListView.setPullLoadEnable(false);
                            }else {
                                mListView.setPullLoadEnable(true);
                            }
                            if(jsonArray.length()>0){
                                if (pageNo==1){
                                    incomeList.clear();
                                }
                            }
                            initShow();
                        }else {
                            mListView.stopLoadMore();
                            mListView.stopRefresh(false);
                            showNotify(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    if (customDialog.isShowing()&&customDialog!=null){
                        customDialog.dismiss();
                    }
                    mListView.stopLoadMore();
                    mListView.stopRefresh(false);
                    showNotify(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    private void initShow() {
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
            layout_data.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }else {
            layout_data.setVisibility(View.GONE);
            layout_nodata.setVisibility(View.VISIBLE);
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
        mActivity = getActivity();
        customDialog=new CustomDialog(mActivity,"正在加载");
        userAccount= SharedPreferencesUtil.getUserName(mActivity, SharedPreferencesUtil.UserName,"");
        password=SharedPreferencesUtil.getPassword(mActivity, SharedPreferencesUtil.Password,"");
        initMyView(mRootView);
        return mRootView;
    }
    private void initMyView(View mRootView) {
        layout_data=mRootView.findViewById(R.id.id_layout_data);
        layout_nodata=mRootView.findViewById(R.id.id_nodata_view);
        mText=(TextView)mRootView.findViewById(R.id.id_tv_nodata);
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
        textParams.put("page",pageNum);
        textParams.put("pageSize","16");
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                requestHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            }
        });
    }
    @Override
    public void initData() {
        customDialog.show();
        loadData(1);
    }
}
