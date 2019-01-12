package com.msht.minshengbao.functionActivity.repairService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.msht.minshengbao.adapter.InvoiceHistoryAdapter;
import com.msht.minshengbao.adapter.MyWorkOrderAdapter;
import com.msht.minshengbao.adapter.RepairOrderListAdapter;
import com.msht.minshengbao.functionActivity.fragment.OrderListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class RepairOrderListActivity extends BaseActivity {
    private XRecyclerView mRecyclerView;
    private View layoutNoData;
    private String status="0";
    private int pageIndex=0;
    private String  userId,password;
    private int refreshType=0;
    private RepairOrderListAdapter mAdapter;
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    private static class RequestHandler extends Handler {
        private WeakReference<RepairOrderListActivity> mWeakReference;

        public RequestHandler(RepairOrderListActivity activity) {
            mWeakReference = new WeakReference<RepairOrderListActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final RepairOrderListActivity activity =mWeakReference.get();
            // the referenced object has been cleared
            if (activity == null||activity.isFinishing()) {
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            if (activity.refreshType==0){
                activity.mRecyclerView.refreshComplete();
            }else if (activity.refreshType==1){
                activity.mRecyclerView.loadMoreComplete();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONArray jsonArray=object.optJSONArray("data");
                            if(jsonArray.length()>0){
                                if (activity.pageIndex<=1){
                                    activity.orderList.clear();
                                }
                            }
                            activity.onReceiveOrderData(jsonArray);
                        }else {
                            activity.onFailure(error);
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

    private void onFailure(String error) {
        new PromptDialog.Builder(context)
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

    private void onReceiveOrderData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id=jsonObject.optString("id");
                String cid=jsonObject.optString("cid");
                String parentCategoryName=jsonObject.optString("parent_category_name");
                String parentCategoryCode=jsonObject.optString("parent_category_code");
                String categoryName=jsonObject.optString("category_name");
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_order_list);
        context=this;
        setCommonHeader("维修订单");
        customDialog=new CustomDialog(this, "正在加载");
        status=getIntent().getStringExtra("status");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        layoutNoData =findViewById(R.id.id_re_nodata);
        mRecyclerView=(XRecyclerView)findViewById(R.id.id_order_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter=new RepairOrderListAdapter(context,orderList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(true);
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
        mAdapter.setOnItemSelectListener(new RepairOrderListAdapter.OnItemSelectListener() {
            @Override
            public void onItemSelectClick(View view, int thisPosition) {
                String orderId = orderList.get(thisPosition).get("id");
                String orderNo=orderList.get(thisPosition).get("orderNo");
                String type=orderList.get(thisPosition).get("type");
                String title=orderList.get(thisPosition).get("title");
                String finishTime=orderList.get(thisPosition).get("time");
                String parentCategoryName=orderList.get(thisPosition).get("parent_category_name");
                String amount=orderList.get(thisPosition).get("amount");
                Intent intent = new Intent(context, RepairEvaluateActivity.class);
                intent.putExtra("sendType","1");
                intent.putExtra("id",orderId);
                intent.putExtra("orderNo",orderNo);
                intent.putExtra("type",type);
                intent.putExtra("title",title);
                intent.putExtra("parentCategory",parentCategoryName);
                intent.putExtra("finishTime",finishTime);
                intent.putExtra("realAmount",amount);
                startActivityForResult(intent, 2);
            }
        });
        mAdapter.setClickCallBack(new RepairOrderListAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int positions) {
                String cid=orderList.get(positions).get("cid");
                String ids = orderList.get(positions).get("id");
                String parentCode=orderList.get(positions).get("parent_category_code");
                Intent intent = new Intent(context, MyOrderWorkDetailActivity.class);
                intent.putExtra("cid",cid);
                intent.putExtra("id", ids);
                intent.putExtra("pos", positions);
                intent.putExtra("parentCode",parentCode);
                startActivityForResult(intent, 4);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==0x004||resultCode==0x005||resultCode==0x006) {
            initOrderList(1);
        }
    }
    private void initOrderList(int i) {
        pageIndex =i;
        String validateURL = UrlUtil.INSURANCE_HISTORY_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(i);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("status",status);
        textParams.put("page",pageNum);
        textParams.put("size","16");
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
}
