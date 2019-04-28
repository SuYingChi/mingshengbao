package com.msht.minshengbao.functionActivity.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.adapter.WaterIncomeAdapter;
import com.msht.minshengbao.base.BaseFragment;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.events.UpdateDataEvent;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/6/10 
 */
public class WaterIncomeFra extends BaseFragment {

    private String  userAccount;
    private String  mDateString="";
    private String  type="2";
    private View layoutNoData;
    private View layoutData;
    private XRecyclerView mRecyclerView;
    private int pageIndex=0;
    private int refreshType=0;
    private CustomDialog customDialog;
    private WaterIncomeAdapter adapter;
    private ArrayList<HashMap<String, String>> incomeList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    public static WaterIncomeFra getInstance(int position) {
        WaterIncomeFra waterIncomeFra = new WaterIncomeFra();
        switch (position){
            case 0:
                waterIncomeFra.type="0";
                break;
            case 1:
                waterIncomeFra.type="2";
                break;
            case 2:
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
            }else {
                if (reference.customDialog.isShowing()&&reference.customDialog!=null){
                    reference.customDialog.dismiss();
                }
            }
            if (reference.refreshType==0){
                reference.mRecyclerView.refreshComplete();
            }else if (reference.refreshType==1){
                reference.mRecyclerView.loadMoreComplete();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        Log.d("textParams=",msg.obj.toString());
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("message");
                        JSONObject jsonObject =object.optJSONObject("data");
                        boolean firstPage=jsonObject.optBoolean("firstPage");
                        boolean lastPage=jsonObject.optBoolean("lastPage");
                        JSONArray jsonArray=jsonObject.optJSONArray("list");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (lastPage){
                                reference.mRecyclerView.setLoadingMoreEnabled(false);
                            }else {
                                reference.mRecyclerView.setLoadingMoreEnabled(true);
                            }
                            if (reference.pageIndex==1) {
                                reference.incomeList.clear();
                            }
                            reference.onReceiveIncomeData(jsonArray);
                        }else {
                            reference.showNotify(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    reference.showNotify(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onReceiveIncomeData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String type = json.getString("type");
                String childType=json.optString("childType");
                String payType= json.getString("payType");
                String amount = json.getString("amount");
                String orderNo =json.optString("orderNo");
                String payTypeName=json.optString("payTypeName");
                String createTime=json.optString("createTime");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("type", type);
                map.put("payType", payType);
                map.put("childType",childType);
                map.put("payTypeName",payTypeName);
                map.put("amount", amount);
                map.put("orderNo",orderNo);
                map.put("createTime",createTime);
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
        new PromptDialog.Builder(mContext)
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
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        customDialog=new CustomDialog(mActivity,"正在加载");
        userAccount= SharedPreferencesUtil.getUserName(mContext, SharedPreferencesUtil.UserName,"");
        initMyView(mRootView);
        return mRootView;
    }
    private void initMyView(View mRootView) {
        layoutData =mRootView.findViewById(R.id.id_layout_data);
        layoutNoData =mRootView.findViewById(R.id.id_nodata_view);
        TextView mText=(TextView)mRootView.findViewById(R.id.id_tv_nodata);
        mText.setText("亲，您还没有数据");
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView=(XRecyclerView)mRootView.findViewById(R.id.id_income_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        adapter=new WaterIncomeAdapter (incomeList);
        mRecyclerView.setAdapter(adapter);
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
                loadData(pageIndex + 1);
            }
        });
    }
    private void loadData(int i) {
        pageIndex =i;
        String validateURL = UrlUtil.WATER_BALANCE_DETAIL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageIndex);
        textParams.put("account",VariableUtil.waterAccount);
        textParams.put("date",VariableUtil.mDateString);
        textParams.put("type",type);
        textParams.put("pageNo",pageNum);
        textParams.put("pageSize","16");
        OkHttpRequestUtil.getInstance(mContext.getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
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
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
