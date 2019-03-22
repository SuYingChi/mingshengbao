package com.msht.minshengbao.functionActivity.WaterApp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.ViewUI.widget.VerticalSwipeRefreshLayout;
import com.msht.minshengbao.adapter.WaterBalanceAdapter;
import com.msht.minshengbao.adapter.WaterMealAdapter;

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
    private MyNoScrollGridView mGridView;
    private TextView tvTotalAmount;
    private TextView tvGiveAmount;
    private WaterMealAdapter waterMealAdapter;
    private VerticalSwipeRefreshLayout mSwipeRefresh;
    private ArrayList<HashMap<String, String>> orderList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> mListType = new ArrayList<HashMap<String, String>>();
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private final BalanceHandler balanceHandler=new BalanceHandler(this);
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
            }else {
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
            }
            super.handleMessage(msg);
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
        initView();
        initRefresh();
        VariableUtil.MealPos=-1;
        waterMealAdapter=new WaterMealAdapter(context, mListType);
        mGridView.setAdapter(waterMealAdapter);
        initData();
        initRechargeData();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String amount= mListType.get(i).get("amount");
                String giveFee = mListType.get(i).get("giveFee");
                String packId=mListType.get(i).get("id");
                VariableUtil.MealPos=i;
                waterMealAdapter.notifyDataSetChanged();
                Intent intent=new Intent(context,WaterPayRechargeActivity.class);
                intent.putExtra("amount",amount);
                intent.putExtra("giveFee",giveFee);
                intent.putExtra("packId",packId);
                startActivityForResult(intent,1);
            }
        });
    }
    private void initRechargeData() {
        String validateURL= UrlUtil.WATER_RECHARGE_MEAL;
        HashMap<String, String> textParams = new HashMap<String, String>(2);
        textParams.put("type","1");
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                try {
                    JSONObject object = new JSONObject(data.toString());
                    String results=object.optString("result");
                    String message = object.optString("message");
                    if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                        JSONArray jsonArray =object.optJSONArray("data");
                        saveData(jsonArray);
                    }else {
                       ToastUtil.ToastText(context, message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void responseReqFailed(Object data) {
                ToastUtil.ToastText(context, data.toString());
            }
        });

    }
    private void saveData(JSONArray jsonArray) {
        mListType.clear();
        waterMealAdapter.notifyDataSetChanged();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String id = json.getString("id");
                String type=json.getString("type");
                String amount=json.getString("amount");
                String activityId="";
                String title="";
                String activityType="";
                String scope="";
                String giveFee="0";
                if (!json.isNull("activity")){
                    JSONObject object=json.getJSONObject("activity");
                    activityId=object.optString("id");
                    title=object.optString("title");
                    activityType=object.optString("type");
                    scope=object.optString("scope");
                    giveFee=object.optString("giveFee");
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("type",type);
                map.put("amount",amount);
                map.put("activityId",activityId);
                map.put("title",title);
                map.put("activityType",activityType);
                map.put("scoped",scope);
                map.put("giveFee",giveFee);
                mListType.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mListType.size()!=0){
            waterMealAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.VALUE2:
                if (resultCode==ConstantUtil.VALUE1){
                    initData();
                    setResult(1);
                }
                break;
                case ConstantUtil.VALUE1:
                    VariableUtil.MealPos=-1;
                    waterMealAdapter.notifyDataSetChanged();
                    if (resultCode==ConstantUtil.VALUE2){
                        initData();
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
                initRechargeData();
            }
        });
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
        rightImg=(ImageView)findViewById(R.id.id_right_img);
        mGridView=(MyNoScrollGridView)findViewById(R.id.id_recharge_view);
        tvGiveAmount =(TextView)findViewById(R.id.id_give_fee);
        tvTotalAmount =(TextView)findViewById(R.id.id_total_amount);
        findViewById(R.id.id_tv_detail).setOnClickListener(this);
        findViewById(R.id.id_forward_img).setOnClickListener(this);
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
        Intent intent=new Intent(context,WaterBalanceDetailActivity.class);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }
}
