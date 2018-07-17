package com.msht.minshengbao.FunctionActivity.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.msht.minshengbao.Adapter.LpgDepositOrderListAdapter;
import com.msht.minshengbao.Base.BaseFragment;
import com.msht.minshengbao.FunctionActivity.LPGActivity.LpgOrderDetailActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

import org.json.JSONArray;
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


    private String lpgUserId;
    private String orderStatus;
    private View layoutNoData;
    private XListView mListView;
    private int pageIndex=0;
    private int pageNo   = 1;
    private int refreshType=0;
    private final String mPageName ="lpg订单";
    private LpgDepositOrderListAdapter mAdapter;
    private Activity mActivity ;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private static final int CALL_BACK_CODE=0;
    private static final int PAY_SUCCESS_CODE=0x001;
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
                break;
            case 1:

                break;
            case 2:

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
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("msg");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
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

        layoutNoData =mRootView.findViewById(R.id.id_not_data_view);
        TextView mText=(TextView)mRootView.findViewById(R.id.id_tv_nodata);
        mText.setText("亲，您还没有订单数据");
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
                Intent intent=new Intent(mActivity, LpgOrderDetailActivity.class);
                intent.putExtra("orderId",orderId);
                startActivityForResult(intent,PAY_SUCCESS_CODE);
            }
        });
        mAdapter.setOnItemSelectCancelListener(new LpgDepositOrderListAdapter.OnItemSelectCancelListener() {
            @Override
            public void onSelectItemClick(View view, int thisPosition) {

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
        String orderType="1";
        String requestUrl = UrlUtil.LPG_GET_ALL_ORDER;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",lpgUserId);
        textParams.put("orderType",orderType);
        textParams.put("orderStatus",orderStatus);
        textParams.put("pageNum",pageNum);
        textParams.put("pageSize","16");
        OkHttpRequestManager.getInstance(mActivity).requestAsyn(requestUrl,OkHttpRequestManager.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    public void initData() {
        customDialog.show();
        loadData(1);
    }
}
