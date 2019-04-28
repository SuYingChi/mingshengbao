package com.msht.minshengbao.functionActivity.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.ViewUI.Dialog.PublicNoticeDialog;
import com.msht.minshengbao.ViewUI.widget.CustomToast;
import com.msht.minshengbao.androidShop.event.GoShopMainEvent;
import com.msht.minshengbao.base.BaseFragment;
import com.msht.minshengbao.adapter.RepairOrderListAdapter;
import com.msht.minshengbao.events.UpdateDataEvent;
import com.msht.minshengbao.functionActivity.repairService.MyOrderWorkDetailActivity;
import com.msht.minshengbao.functionActivity.repairService.RepairEvaluateActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

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
 * Created by hong on 2017/3/14.
 */

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/8/2  
 */
public class OrderListFragment extends BaseFragment  {
    private RepairOrderListAdapter mAdapter;
    private XRecyclerView mRecyclerView;
    private View layoutNoData;
    private int status =0;
    private String userId,password;
    private String orderId ;
    private String orderNo;
    private String type;
    private String title;
    private String categoryDesc;
    private String finishTime;
    private String parentCategoryName;
    private String amount;
    private int pageIndex=0;
    private int refreshType;
    private Activity mActivity;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    public static OrderListFragment getInstanse(int position){
        OrderListFragment orderListFragment = new OrderListFragment();
        switch (position){
            case 0:
                orderListFragment.status=0;
                break;
            case 1:
                orderListFragment.status=1;
                break;
            case 2:
                orderListFragment.status=2;
                break;
            case 3:
                orderListFragment.status=5;
                break;
            case 4:
                orderListFragment.status=4;
                break;
            default:
                break;
        }
        return orderListFragment;
    }
    @Override
    public View initView() {
        if(mRootView==null){
            mRootView= LayoutInflater.from(mContext).inflate(R.layout.fragment_orderlist,null,false);
        }
        mActivity = getActivity();
        customDialog=new CustomDialog(getActivity(), "正在加载");
        userId= SharedPreferencesUtil.getUserId(mActivity, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(mActivity, SharedPreferencesUtil.Password,"");
        EventBus.getDefault().register(this);
        initMyView(mRootView);
        return mRootView;
    }
    private void initMyView(View mRootView) {
        layoutNoData =mRootView.findViewById(R.id.id_re_nodata);
        mRecyclerView=(XRecyclerView)mRootView.findViewById(R.id.id_order_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter=new RepairOrderListAdapter(mActivity,orderList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
        mAdapter.setOnItemSelectListener(new RepairOrderListAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelectClick(View view, int thisPosition) {
                String orderId = orderList.get(thisPosition).get("id");
                String orderNo=orderList.get(thisPosition).get("orderNo");
                String type=orderList.get(thisPosition).get("type");
                String title=orderList.get(thisPosition).get("title");
                String categoryDesc=orderList.get(thisPosition).get("categoryDesc");
                String finishTime=orderList.get(thisPosition).get("time");
                String parentCategoryName=orderList.get(thisPosition).get("parent_category_name");
                String amount=orderList.get(thisPosition).get("amount");
                Intent intent = new Intent(mActivity, RepairEvaluateActivity.class);
                intent.putExtra("sendType","1");
                intent.putExtra("id",orderId);
                intent.putExtra("orderNo",orderNo);
                intent.putExtra("type",type);
                intent.putExtra("title",title);
                intent.putExtra("parentCategory",parentCategoryName);
                intent.putExtra("categoryDesc",categoryDesc);
                intent.putExtra("finishTime",finishTime);
                intent.putExtra("realAmount",amount);
                startActivityForResult(intent, 2);
            }
        });
        mAdapter.setClickCallBack(new RepairOrderListAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int positions) {
                 orderId = orderList.get(positions).get("id");
                 orderNo=orderList.get(positions).get("orderNo");
                 type=orderList.get(positions).get("type");
                 title=orderList.get(positions).get("title");
                 finishTime=orderList.get(positions).get("time");
                 parentCategoryName=orderList.get(positions).get("parent_category_name");
                 amount=orderList.get(positions).get("amount");
                 categoryDesc=orderList.get(positions).get("categoryDesc");
                 String cid=orderList.get(positions).get("cid");
                 String parentCode=orderList.get(positions).get("parent_category_code");
                 Intent intent = new Intent(mActivity, MyOrderWorkDetailActivity.class);
                 intent.putExtra("cid",cid);
                 intent.putExtra("id", orderId);
                 intent.putExtra("pos", positions);
                 intent.putExtra("categoryDesc",categoryDesc);
                 intent.putExtra("parentCode",parentCode);
                 startActivityForResult(intent, 4);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==0x002||resultCode==0x004||resultCode==0x005||resultCode==0x006) {
           /* orderList.clear();
            mAdapter.notifyDataSetChanged();
            initOrderList(1);*/
        }
        if (resultCode==0x006){
            onEvaluationDialog();
        }
    }
    private void onEvaluationDialog() {
        if (mActivity!=null&&!mActivity.isFinishing()){
            new PublicNoticeDialog(mActivity).builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .setLineViewVisibility(false)
                    .setTitleText("提示")
                    .setMessageContentText("您有新的订单待评价哦！")
                    .setButtonText("去评价")
                    .setOnPositiveClickListener(new PublicNoticeDialog.OnPositiveClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(mActivity, RepairEvaluateActivity.class);
                            intent.putExtra("sendType","1");
                            intent.putExtra("id",orderId);
                            intent.putExtra("orderNo",orderNo);
                            intent.putExtra("type",type);
                            intent.putExtra("title",title);
                            intent.putExtra("parentCategory",parentCategoryName);
                            intent.putExtra("categoryDesc",categoryDesc);
                            intent.putExtra("finishTime",finishTime);
                            intent.putExtra("realAmount",amount);
                            startActivityForResult(intent, 2);
                        }
                    }).show();

        }
    }

    @Override
    public void initData() {
        customDialog.show();
        orderList.clear();
        initOrderList(1);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshType=0;
                initOrderList(1);
            }
            @Override
            public void onLoadMore() {
                refreshType=1;
                initOrderList(pageIndex + 1);
            }
        });
    }
    private void initOrderList(int i) {
        pageIndex =i;
        String validateURL = UrlUtil.maintainservise_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(i);
        String statuses=String.valueOf(status);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("status",statuses);
        textParams.put("page",pageNum);
        textParams.put("size","16");
        OkHttpRequestManager.getInstance(mActivity.getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
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
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
                CustomToast.showWarningLong(data.toString());
            }
        });
      //  OkHttpRequestUtil.getInstance(mActivity.getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }

    private void onAnalysisData(String s ) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("error");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                JSONArray jsonArray=object.optJSONArray("data");
                if (pageIndex<=1){
                    orderList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                onReceiveOrderData(jsonArray);
            }else {
                onFailure(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private static class RequestHandler extends Handler{
        private WeakReference<OrderListFragment> mWeakReference;

        public RequestHandler(OrderListFragment orderListFragment) {
            mWeakReference = new WeakReference<OrderListFragment>(orderListFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final OrderListFragment reference =mWeakReference.get();
            // the referenced object has been cleared
            if (reference == null||reference.isDetached()) {
                return;
            }
            if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                reference.customDialog.dismiss();
            }
            if (reference.refreshType==0){
                reference.mRecyclerView.refreshComplete();
            }else if (reference.refreshType==1){
                reference.mRecyclerView.loadMoreComplete();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONArray jsonArray=object.optJSONArray("data");
                            if (reference.pageIndex<=1){
                                reference.orderList.clear();
                            }
                            reference.onReceiveOrderData(jsonArray);
                        }else {
                            reference.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.mContext,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void onReceiveOrderData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id=jsonObject.optString("id");
                String cid=jsonObject.optString("cid");
                String parentCategoryName=jsonObject.optString("parent_category_name");
                String parentCategoryCode=jsonObject.optString("parent_category_code");
                String categoryName=jsonObject.optString("category_name");
                String categoryDesc=jsonObject.optString("category_desc");
                String categoryCode=jsonObject.optString("category_code");
                String orderNo=jsonObject.optString("orderNo");
                String title = jsonObject.getString("title");
                String type = jsonObject.getString("type");
                String status = jsonObject.getString("status");
                String statusDesc=jsonObject.optString("statusDesc");
                String amount=jsonObject.optString("amount");
                String time = jsonObject.getString("time");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("orderNo",orderNo);
                map.put("cid",cid);
                map.put("parent_category_name",parentCategoryName);
                map.put("parent_category_code",parentCategoryCode);
                map.put("category_name",categoryName);
                map.put("category_code",categoryCode);
                map.put("categoryDesc",categoryDesc);
                map.put("type", type);
                map.put("title", title);
                map.put("status", status);
                map.put("statusDesc", statusDesc);
                map.put("amount",amount);
                map.put("time",time);
                orderList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (orderList.size()<1){
            mAdapter.notifyDataSetChanged();
            layoutNoData.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }else {
            layoutNoData.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(mContext)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateDataEvent messageEvent) {
        if (messageEvent.getMessage()){
            initOrderList(1);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        OkHttpRequestManager.getInstance(mActivity.getApplicationContext()).requestCancel(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
