package com.msht.minshengbao.functionActivity.WaterApp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.Utils.AppPackageUtil;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.DateSheetDialog;
import com.msht.minshengbao.adapter.PublicViewAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.ViewUI.ViewPagerIndicator;
import com.msht.minshengbao.adapter.WaterIncomeAdapter;
import com.msht.minshengbao.events.DateEvent;
import com.msht.minshengbao.events.LocationEvent;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterBalanceDetailActivity extends BaseActivity {
    private TextView tvDateString;
    private String mDateString="";
    private int  type=0;
    private View layoutNoData;
    private XRecyclerView mRecyclerView;
    private int pageIndex=0;
    private int refreshType=0;
    private CustomDialog customDialog;
    private WaterIncomeAdapter adapter;
    private ArrayList<HashMap<String, String>> incomeList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_balance_detail);
        context=this;
        setCommonHeader("余额明细(水宝)");
        customDialog=new CustomDialog(context,"正在加载");
        //initView();
        initFindView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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
                getBalanceData(type,1);
            }
            @Override
            public void onLoadMore() {
                refreshType=1;
                getBalanceData(type,pageIndex + 1);
            }
        });
        initData();
    }

    private void initData() {
        customDialog.show();
        getBalanceData(0,1);
    }

    private void initFindView() {
        mRecyclerView=(XRecyclerView)findViewById(R.id.id_income_view);
        layoutNoData =findViewById(R.id.id_noData_view);
        tvDateString=(TextView)findViewById(R.id.id_date_time);
        TextView mText=(TextView)findViewById(R.id.id_tv_nodata);
        mText.setText("亲，您还没有数据");
        RadioGroup radioGroup=(RadioGroup)findViewById(R.id.id_group);
        String todayString="选择日期";
        tvDateString.setText(todayString);
        findViewById(R.id.id_select_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectDate();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.id_all_radio:
                        getBalanceData(0,1);
                        break;
                    case R.id.id_consumption_radio:
                        getBalanceData(2,1);
                        break;
                    case R.id.id_recharge_radio:
                        getBalanceData(1,1);
                        break;
                    default:
                        getBalanceData(0,1);
                        break;
                }
            }
        });
    }
    private void getBalanceData(int i, int i1) {
        type=i;
        pageIndex =i1;
        String validateURL = UrlUtil.WATER_BALANCE_DETAIL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageIndex);
        textParams.put("account",VariableUtil.waterAccount);
        textParams.put("date",mDateString);
        textParams.put("type",String.valueOf(type));
        textParams.put("pageNo",pageNum);
        textParams.put("pageSize","16");
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog.isShowing()&&customDialog!=null){
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
                if (customDialog.isShowing()&&customDialog!=null){
                    customDialog.dismiss();
                }
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
                ToastUtil.ToastText(context,data.toString());
            }
        });
    }
    private void onAnalysisData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("message");
            JSONObject jsonObject =object.optJSONObject("data");
            boolean firstPage=jsonObject.optBoolean("firstPage");
            boolean lastPage=jsonObject.optBoolean("lastPage");
            JSONArray jsonArray=jsonObject.optJSONArray("list");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                if (lastPage){
                    mRecyclerView.setLoadingMoreEnabled(false);
                }else {
                    mRecyclerView.setLoadingMoreEnabled(true);
                }
                if (pageIndex==1) {
                    incomeList.clear();
                }
                onReceiveIncomeData(jsonArray);
            }else {
                ToastUtil.ToastText(context,error);
            }
        }catch (Exception e){
            e.printStackTrace();
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
            layoutNoData.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }else {
            adapter.notifyDataSetChanged();
            layoutNoData.setVisibility(View.VISIBLE);
        }
    }
    private void onSelectDate() {
        new DateSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setOnSheetButtonOneClickListener(new DateSheetDialog.OnSheetButtonOneClickListener() {
                    @Override
                    public void onClick(String text) {
                        tvDateString.setText(DateUtils.getStringDate(text,"yyyy年MM月dd日","MM月dd日"));
                        mDateString=DateUtils.getStringDate(text,"yyyy年MM月dd日","yyyy-MM-dd");
                        getBalanceData(type,1);

                    }
                }).show();
    }
    private int getControlViewWidth(View view){
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return AppPackageUtil.getScreenWidth(context)-view.getMeasuredWidth();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog.isShowing()&&customDialog!=null){
            customDialog.dismiss();
        }
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }
}
