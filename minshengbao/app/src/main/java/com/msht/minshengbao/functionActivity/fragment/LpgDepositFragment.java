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

import com.msht.minshengbao.adapter.LpgDepositOrderListAdapter;
import com.msht.minshengbao.Base.BaseFragment;
import com.msht.minshengbao.ViewUI.ButtonUI.ButtonM;
import com.msht.minshengbao.functionActivity.LPGActivity.LpgDepositOrderDetailActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.msht.minshengbao.functionActivity.LPGActivity.LpgDepositReturnActivity;
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
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/16  
 */
public class LpgDepositFragment extends BaseFragment {
    private TextView tvDepositCount;
    private TextView tvDepositTotalAmount;
    private String lpgUserId;
    private String orderStatus;
    private View layoutNoData;
    private XListView mListView;
    private int pageIndex=0;
    private int refreshType=0;
    private int requestCode=0;
    private final String mPageName ="退瓶订单";
    private LpgDepositOrderListAdapter mAdapter;
    private Activity mActivity ;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private static final int SEND_SUCCESS_CODE=0x001;
    private static final int ORDER_CANCEL_CODE=0X002;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    public LpgDepositFragment() {
        // Required empty public constructor
    }
    public static LpgDepositFragment getInstance(int position) {
        LpgDepositFragment lpgReturnBottleFragment = new LpgDepositFragment();
        switch (position){
            case 0:
                lpgReturnBottleFragment.orderStatus="";
                break;
            case 1:
                lpgReturnBottleFragment.orderStatus="6";
                break;
            case 2:
                lpgReturnBottleFragment.orderStatus="3";
                break;
            default:
                break;
        }
        return lpgReturnBottleFragment ;
    }
    private static class RequestHandler extends Handler {
        private WeakReference<LpgDepositFragment> mWeakReference;
        public RequestHandler(LpgDepositFragment reference) {
            mWeakReference=new WeakReference<LpgDepositFragment>(reference);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgDepositFragment reference=mWeakReference.get();
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
                            if (reference.requestCode==0){
                                boolean firstPage=object.optBoolean("isStartPage");
                                boolean lastPage=object.optBoolean("isEndPage");
                                reference.onDepositData(object);
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
                                        reference.mAdapter.notifyDataSetChanged();
                                    }
                                }
                                reference.onReceiveOrderData();
                            }else if (reference.requestCode==1){
                                reference.onCancelSuccess();
                            }
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

    private void onDepositData(JSONObject object) {
        String depositCount="当前共租用"+object.optString("depositCount")+"个钢瓶,";
        String depositTotalAmount=object.optString("depositTotalAmount");
        String depositTotalAmountText="¥"+depositTotalAmount;
        tvDepositTotalAmount.setText(depositTotalAmountText);
        tvDepositCount.setText(depositCount);

    }

    private void onCancelSuccess() {
        new PromptDialog.Builder(mContext)
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("订单已取消")
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        orderList.clear();
                        mAdapter.notifyDataSetChanged();
                        loadData(1);
                    }
                }).show();
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

    private void onReceiveOrderData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String orderId=json.optString("orderId");
                String code = json.getString("code");
                String fiveBottleCount= json.getString("reFiveBottleCount");
                String fifteenBottleCount = json.getString("reFifteenBottleCount");
                String fiftyBottleCount =json.optString("reFiftyBottleCount");
                String realAmount=json.optString("realAmount");
                String orderStatus=json.optString("orderStatus");
                String createDate=json.optString("createDate");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("orderId", orderId);
                map.put("code", code);
                map.put("orderType","0");
                map.put("fiveBottleCount", fiveBottleCount);
                map.put("fifteenBottleCount",fifteenBottleCount);
                map.put("fiftyBottleCount", fiftyBottleCount);
                map.put("realAmount",realAmount);
                map.put("orderStatus",orderStatus);
                map.put("createDate",createDate);
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
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_lpg_return_bottle, null, false);
        mActivity = getActivity();
        customDialog=new CustomDialog(mActivity,"正在加载");
        lpgUserId= SharedPreferencesUtil.getStringData(mActivity, SharedPreferencesUtil.LPG_USER_ID,"");
        initMyView(mRootView);
        return mRootView;
    }
    private void initMyView(View mRootView) {
        ButtonM btnApply=(ButtonM)mRootView.findViewById(R.id.id_btn_apply) ;
        layoutNoData =mRootView.findViewById(R.id.id_not_data_view);
        TextView mText=(TextView)mRootView.findViewById(R.id.id_tv_nodata);
        mText.setText("亲，您还没有订单数据");
        tvDepositCount=(TextView)mRootView.findViewById(R.id.id_total_count) ;
        tvDepositTotalAmount=(TextView)mRootView.findViewById(R.id.id_total_amount);
        mListView=(XListView)mRootView.findViewById(R.id.id_order_list);
        mListView.setPullLoadEnable(true);
        mAdapter = new LpgDepositOrderListAdapter(mActivity ,orderList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos=position-1;
                String realAmount=orderList.get(pos).get("realAmount");
                String orderId=orderList.get(pos).get("orderId");
                Intent intent=new Intent(mActivity, LpgDepositOrderDetailActivity.class);
                intent.putExtra("orderId",orderId);
                startActivityForResult(intent,ORDER_CANCEL_CODE);
            }
        });
        mAdapter.setOnItemSelectCancelListener(new LpgDepositOrderListAdapter.OnItemSelectCancelListener() {
            @Override
            public void onSelectItemClick(View view, int thisPosition) {
                String orderId=orderList.get(thisPosition).get("orderId");
                onCancelOrder(orderId);
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
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startReturnDeposit();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case SEND_SUCCESS_CODE:
                loadData(1);
                break;
            case ORDER_CANCEL_CODE:
                loadData(1);
                break;
                default:
                    break;
        }
    }

    private void startReturnDeposit() {
        Intent intent=new Intent(mContext, LpgDepositReturnActivity.class);
        startActivityForResult(intent,SEND_SUCCESS_CODE);
    }
    private void onCancelOrder(final String orderId) {
        new PromptDialog.Builder(mContext)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("请确认是否取消?")
                .setButton1("残忍取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        requestService(orderId);
                        dialog.dismiss();
                    }
                })
                .setButton2("算了", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) { dialog.dismiss(); }
                }).show();
    }
    private void requestService(String orderId) {
        customDialog.show();
        requestCode=1;
        String validateURL= UrlUtil.LPG_FAIL_ORDER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("id",orderId);
        OkHttpRequestUtil.getInstance(mActivity.getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void loadData(int i) {
        requestCode=0;
        pageIndex =i;
        String orderType="0";
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
