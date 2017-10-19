package com.msht.minshengbao.FunctionView.fragmeht;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.CouponAdapter;
import com.msht.minshengbao.Base.BaseFragment;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.Myview.ShareMenuActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CouponFragment extends BaseFragment {
    private String userId;
    private String password;
    private  String status="1";
    private RelativeLayout Rnodata;
    private Button btn_share;
    private XListView mListview;
    private CouponAdapter mAdapter;
    private int pageNo=1;
    private int pageIndex=0;
    private int refreshType;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private JSONArray jsonArray;   //数据解析
    private final String mPageName = "优惠券";
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> couponList = new ArrayList<HashMap<String, String>>();
    public CouponFragment() {}
    public static CouponFragment getinstance(int position) {
        CouponFragment couponFragment = new CouponFragment();
        switch (position){
            case 0:
                couponFragment.status="1";
                break;
            case 1:
                couponFragment.status="2";
                break;
            case 2:
                couponFragment.status="3";
            default:
                break;
        }
        return couponFragment ;
    }
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            if (refreshType==0){
                                mListview.stopRefresh(true);
                            }else if (refreshType==1){
                                mListview.stopLoadMore();
                            }
                            if(jsonArray.length()>0){
                                if (pageNo==1){
                                    couponList.clear();
                                }
                            }
                            initShow();
                        }else {
                            faifure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(getActivity(), msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void initShow() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int ID = jsonObject.optInt("id");
                String id=Integer.toString(ID);
                String scope = jsonObject.getString("scope");
                String amount = jsonObject.getString("amount");
                String use_limit = jsonObject.getString("use_limit");
                String start_date = jsonObject.getString("start_date");
                String end_date= jsonObject.getString("end_date");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("scope", scope);
                map.put("amount", amount);
                map.put("use_limit", use_limit);
                map.put("start_date",start_date);
                map.put("end_date", end_date);
                map.put("type","1");
                couponList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (couponList.size()==0){
            if (status.equals("1")){
                Rnodata.setVisibility(View.VISIBLE);
            }else {
                Rnodata.setVisibility(View.GONE);
            }
        }else {
            Rnodata.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void faifure(String error) {
        new PromptDialog.Builder(getActivity())
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==1){
                    initData();
                }
                break;
            default:
                break;
        }
    }
    @Override
    public View initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_coupon, null, false);
        customDialog=new CustomDialog(getActivity(), "正在加载");
        userId= SharedPreferencesUtil.getUserId(getActivity(), SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(getActivity(), SharedPreferencesUtil.Password,"");
        initMyView(mRootView);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShareMenuActivity.class);
                startActivityForResult(intent,1);
            }
        });
        return mRootView;
    }

    private void initMyView(View mRootView) {
        Rnodata=(RelativeLayout)mRootView.findViewById(R.id.id_re_nodata);
        btn_share=(Button)mRootView.findViewById(R.id.id_btn_share);
        mListview=(XListView)mRootView.findViewById(R.id.id_dicount_mlistview);
        mListview.setPullLoadEnable(true);
        mAdapter=new CouponAdapter(getActivity(),couponList);
        mListview.setAdapter(mAdapter);
        mListview.setXListViewListener(new XListView.IXListViewListener() {
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
    @Override
    public void initData() {
        customDialog.show();
        loadData(1);
    }
    private void loadData(int i) {
        pageIndex =i;
        pageNo=i;
        String validateURL = UrlUtil.Counpon_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("status",status);
        textParams.put("page",pageNum);
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);

    };
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
}
