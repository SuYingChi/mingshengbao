package com.msht.minshengbao.functionActivity.WaterApp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.msht.minshengbao.ViewUI.widget.VerticalSwipeRefreshLayout;
import com.msht.minshengbao.adapter.LpgMyBottleAdapter;
import com.msht.minshengbao.adapter.WaterBalanceAdapter;
import com.msht.minshengbao.adapter.WaterOrderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterBalanceActivity extends BaseActivity implements View.OnClickListener {
    private String   account="";
    private TextView tvTotalAmount;
    private TextView tvGiveAmount;
    private View layoutNoData;
    private int      pageNo=1;
    private int pageIndex=0;
    private LoadMoreListView mListView;
    private WaterBalanceAdapter mAdapter;
    private VerticalSwipeRefreshLayout mSwipeRefresh;
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();

    private final BalanceHandler balanceHandler=new BalanceHandler(this);
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class BalanceHandler extends Handler {
        private WeakReference< WaterBalanceActivity> mWeakReference;
        public BalanceHandler( WaterBalanceActivity activity) {
            mWeakReference=new WeakReference< WaterBalanceActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final  WaterBalanceActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:

                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String message = object.optString("message");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.mSwipeRefresh.setRefreshing(false);
                            JSONObject json =object.optJSONObject("data");
                            activity.onReceiveAccountData(json);
                        }else {
                            ToastUtil.ToastText(activity.context,message);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.mSwipeRefresh.setRefreshing(false);
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class RequestHandler extends Handler{
        private WeakReference< WaterBalanceActivity> mWeakReference;
        public RequestHandler( WaterBalanceActivity activity) {
            mWeakReference=new WeakReference< WaterBalanceActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final  WaterBalanceActivity activity =mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {

                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("message");
                        JSONObject jsonObject =object.optJSONObject("data");
                        boolean firstPage=jsonObject.optBoolean("firstPage");
                        boolean lastPage=jsonObject.optBoolean("lastPage");
                        JSONArray jsonArray=jsonObject.optJSONArray("list");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.mSwipeRefresh.setRefreshing(false);
                            if (lastPage){
                                activity.mListView.loadComplete(false);
                            }else {
                                activity.mListView.loadComplete(true);
                            }
                            if(jsonArray.length()>0){
                                if (activity.pageNo==1){
                                    activity.mList.clear();
                                }
                            }
                            activity.onReceiveIncomeData(jsonArray);
                        }else {
                            ToastUtil.ToastText(activity.context,error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.mAdapter.notifyDataSetChanged();
                    activity.mSwipeRefresh.setRefreshing(false);
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
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
    private void onReceiveAccountData(JSONObject json) {
        String type=json.optString("type");
        String accounts=json.optString("account");
        String payAmount=json.optString("payBalance");
        String giveAmount="(含赠送"+json.optString("giveBalance")+")";
        double payBalance=json.optDouble("payBalance");
        double giveBalance=json.optDouble("giveBalance");
        double totalBalance=payBalance+giveBalance;
        totalBalance= VariableUtil.twoDecinmal2(totalBalance);
        String totalAmount=String.valueOf(totalBalance);
        VariableUtil.waterAccount=accounts;
        tvGiveAmount.setText(giveAmount);
        tvTotalAmount.setText(totalAmount);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_balance);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        mPageName="我的余额(水宝)";
        setCommonHeader(mPageName);
        account= SharedPreferencesUtil.getUserName(context, SharedPreferencesUtil.UserName,"");
        initView();
        initRefresh();
        mAdapter=new WaterBalanceAdapter(context,mList);
        mListView.setAdapter(mAdapter);
        initData();
        initOrderData(1);
        mListView.setLoadMoreListener(new ILoadMoreCallback() {
            @Override
            public void loadMore() {
                initOrderData(pageIndex + 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.VALUE2:
                if (resultCode==ConstantUtil.VALUE1){
                    initData();
                    initOrderData(1);
                    setResult(1);
                }
                break;
                default:
                    break;
        }
    }

    private void initRefresh() {
        mSwipeRefresh.setProgressViewEndTarget(false,100);
        mSwipeRefresh.setProgressViewOffset(false,2,20);
        mSwipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
        mSwipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefresh.setEnabled(true);
        mSwipeRefresh.setProgressBackgroundColorSchemeResource(R.color.transparent_Orange);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                initOrderData(1);
            }
        });
        /** SwipeRefreshLayout 与ListView滑动冲突  **/
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) { }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(mListView != null && mListView.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = mListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = mListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefresh.setEnabled(enable);
            }
        });
    }

    private void initOrderData(int i) {
        pageIndex =i;
        pageNo=i;
        String validateURL = UrlUtil.WATER_BALANCE_DETAIL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("account",VariableUtil.waterAccount);
        textParams.put("pageNo",pageNum);
        textParams.put("pageSize","16");
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initData() {
        customDialog.show();
        String validateURL= UrlUtil.WATER_ACCOUNT_URL;
        Map<String, String> textParams = new HashMap<String, String>(1);
        textParams.put("account",VariableUtil.waterAccount);
        textParams.put("type","0");
        SendRequestUtil.postDataFromService(validateURL,textParams,balanceHandler);
    }
    private void initView() {
        View view=findViewById(R.id.id_re_layout);
        view.setBackgroundResource(R.drawable.shape_change_blue_bg);
        mSwipeRefresh=(VerticalSwipeRefreshLayout)findViewById(R.id.id_swipe_refresh);
        layoutNoData=findViewById(R.id.id_nodata_view);
        rightImg=(ImageView)findViewById(R.id.id_right_img);
        View layoutAccountHeader=getLayoutInflater().inflate(R.layout.layout_water_balance_hearder,null);
        mListView=(LoadMoreListView) findViewById(R.id.id_order_list);
        mListView.addHeaderView(layoutAccountHeader);
        tvGiveAmount =(TextView)layoutAccountHeader.findViewById(R.id.id_give_fee);
        tvTotalAmount =(TextView)layoutAccountHeader.findViewById(R.id.id_total_amount);
        layoutAccountHeader.findViewById(R.id.id_tv_detail).setOnClickListener(this);
        layoutAccountHeader.findViewById(R.id.id_forward_img).setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_forward_img:
                break;
            case R.id.id_tv_detail:
                onRecharge();
                break;
                default:
                    break;
        }

    }

    private void onRecharge() {
        Intent intent=new Intent(context,WaterRechargeActivity.class);
        startActivityForResult(intent,2);
    }
}
