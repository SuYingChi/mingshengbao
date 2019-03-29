package com.msht.minshengbao.functionActivity.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.msht.minshengbao.adapter.LpgOrderListAdapter;
import com.msht.minshengbao.base.BaseFragment;
import com.msht.minshengbao.functionActivity.lpgActivity.LpgPayOrderActivity;
import com.msht.minshengbao.functionActivity.lpgActivity.LpgOrderDetailActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.umeng.analytics.MobclickAgent;

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
 * lpg订单
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/11  
 */
public class LpgOrderListFragment extends BaseFragment {

    private String lpgUserId;
    private String orderStatus;
    private View layoutNoData;
    private XListView mListView;
    private int pageIndex=0;
    private int refreshType=0;
    private final String mPageName ="lpg订单";
    private LpgOrderListAdapter mAdapter;
    private Activity mActivity ;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private static final int CALL_BACK_CODE=0;
    private static final int PAY_SUCCESS_CODE=0x001;
    private static final int ORDER_CANCEL_CODE=0X002;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    public static LpgOrderListFragment getInstance(int position) {
        LpgOrderListFragment lpgOrderListFragment = new LpgOrderListFragment();
        switch (position){
            case 0:
                lpgOrderListFragment.orderStatus="";
                break;
            case 1:
                lpgOrderListFragment.orderStatus="1";
                break;
            case 2:
                lpgOrderListFragment.orderStatus="2";
                break;
            case 3:
                lpgOrderListFragment.orderStatus="0";
                break;
            case 4:
                lpgOrderListFragment.orderStatus="3";
                break;
            default:
                break;
        }
        return lpgOrderListFragment ;
    }
    public LpgOrderListFragment() {}
    private static class RequestHandler extends Handler {
        private WeakReference<LpgOrderListFragment> mWeakReference;
        public RequestHandler(LpgOrderListFragment reference) {
            mWeakReference=new WeakReference<LpgOrderListFragment>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            final LpgOrderListFragment reference=mWeakReference.get();
            if (reference==null||reference.isDetached()){
                return;
            }
            if (reference.customDialog.isShowing()&&reference.customDialog!=null){
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("msg");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            boolean firstPage=object.optBoolean("isStartPage");
                            boolean lastPage=object.optBoolean("isEndPage");
                            reference.jsonArray=object.optJSONArray("orderList");
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
                                if (firstPage){
                                    reference.orderList.clear();
                                }
                            }
                            reference.onReceiveOrderData();
                        }else {
                            reference.mListView.stopLoadMore();
                            reference.mListView.stopRefresh(false);
                            reference.showNotify(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
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
    private void showNotify(String error) {
        if (mContext!=null){
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
    }
    private void onReceiveOrderData() {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String orderId=json.optString("orderId");
                String code = json.getString("code");
                String fiveBottleCount= json.getString("fiveBottleCount");
                String fifteenBottleCount = json.getString("fifteenBottleCount");
                String fiftyBottleCount =json.optString("fiftyBottleCount");
                String realAmount=json.optString("realAmount");
                String orderStatus=json.optString("orderStatus");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("orderId", orderId);
                map.put("code", code);
                map.put("orderType","1");
                map.put("fiveBottleCount", fiveBottleCount);
                map.put("fifteenBottleCount",fifteenBottleCount);
                map.put("fiftyBottleCount", fiftyBottleCount);
                map.put("realAmount",realAmount);
                map.put("orderStatus",orderStatus);
                orderList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (orderList.size()!=0&&orderList!=null){
            mAdapter.notifyDataSetChanged();
            layoutNoData.setVisibility(View.GONE);
        }else {
            mAdapter.notifyDataSetChanged();
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public View initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_lpg_order_list, null, false);
        mActivity = getActivity();
        customDialog=new CustomDialog(mActivity,"正在加载");
        lpgUserId=SharedPreferencesUtil.getStringData(mActivity, SharedPreferencesUtil.LPG_USER_ID,"");
        initMyView(mRootView);
        return mRootView;
    }
    private void initMyView(View mRootView) {

        layoutNoData =mRootView.findViewById(R.id.id_not_data_view);
        TextView mText=(TextView)mRootView.findViewById(R.id.id_tv_nodata);
        mText.setText("亲，您还没有订单数据");
        mListView=(XListView)mRootView.findViewById(R.id.id_order_list);
        mListView.setPullLoadEnable(true);
        mAdapter = new LpgOrderListAdapter(mActivity ,orderList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos=position-1;
                String realAmount=orderList.get(pos).get("realAmount");
                String orderId=orderList.get(pos).get("orderId");
                Intent intent=new Intent(mActivity, LpgOrderDetailActivity.class);
                intent.putExtra("orderId",orderId);
                startActivityForResult(intent,PAY_SUCCESS_CODE);
            }
        });
        mAdapter.setOnItemSelectListener(new LpgOrderListAdapter.OnItemSelectListener() {
            @Override
            public void onSelectItemClick(View view, int thisPosition) {
                String realAmount=orderList.get(thisPosition).get("realAmount");
                String orderId=orderList.get(thisPosition).get("orderId");
                Intent intent=new Intent(mActivity, LpgPayOrderActivity.class);
                intent.putExtra("realAmount",realAmount);
                intent.putExtra("orderId",orderId);
                startActivityForResult(intent,PAY_SUCCESS_CODE);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==PAY_SUCCESS_CODE||resultCode == ORDER_CANCEL_CODE ) {
            orderList.clear();
            mAdapter.notifyDataSetChanged();
            loadData(1);
        }
    }
    private void loadData(int i) {
        pageIndex =i;
        String orderType="1";
        String requestUrl = UrlUtil.LPG_GET_ALL_ORDER;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(i);
        textParams.put("userId",lpgUserId);
        textParams.put("orderType",orderType);
        textParams.put("orderStatus",orderStatus);
        textParams.put("pageNum",pageNum);
        textParams.put("pageSize","16");
        OkHttpRequestUtil.getInstance(mActivity.getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }

    @Override
    public void initData() {
        customDialog.show();
        loadData(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
