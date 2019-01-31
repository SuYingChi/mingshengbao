package com.msht.minshengbao.functionActivity.GasService;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.AppShortCutUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.adapter.GetAddressAdapter;
import com.msht.minshengbao.androidShop.util.LogUtils;
import com.msht.minshengbao.functionActivity.MainActivity;

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
public class GasInternetRecodeListActivity extends BaseActivity {

    private String    userId;
    private String    password;
    private XRecyclerView mRecyclerView;
    private int pos=-1;
    private TextView tvNoData;
    private int refreshType;
    private JSONArray jsonArray;
    private GetAddressAdapter adapter;
    private int pageIndex=0;
    private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_internet_recode_list);
        context=this;
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        setCommonHeader("充值记录");
        tvNoData =(TextView)findViewById(R.id.id_tv_nodata);
        tvNoData.setText("当前没有客户号");
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView=(XRecyclerView )findViewById(R.id.id_pay_record_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        adapter=new GetAddressAdapter(context,recordList,pos);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);
        adapter.setClickCallBack(new GetAddressAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(View v, int position) {
                pos=position;
                adapter.notifyDataSetChanged();
                String customerNo=recordList.get(pos).get("customerNo");
                String address=recordList.get(pos).get("name");
                Intent name=new Intent(context, GasPayRecordActivity.class);
                name.putExtra("customerNo",customerNo);
                name.putExtra("address",address);
                name.putExtra("urlType","2");
                startActivity(name);
            }
        });
        adapter.setRadioButtonClickListener(new GetAddressAdapter.ItemRadioButtonClickListener() {
            @Override
            public void onRadioButtonClick(View v, int position) {
                adapter.notifyDataSetChanged();
                String customerNo=recordList.get(position).get("customerNo");
                String address=recordList.get(position).get("name");
                Intent name=new Intent(context, GasPayRecordActivity.class);
                name.putExtra("customerNo",customerNo);
                name.putExtra("address",address);
                name.putExtra("urlType","2");
                startActivity(name);
            }
        });
        initData();
    }
    private void initData() {
        loadData(1);
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
        pageIndex=i;
        String validateURL = UrlUtil.INTERNET_RECHARGE_RECODE_LIST;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
                onReceiveRecordData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (refreshType==0){
                    mRecyclerView.refreshComplete();
                }else if (refreshType==1){
                    mRecyclerView.loadMoreComplete();
                }
                ToastUtil.ToastText(context,data.toString());
            }
        });
    }
    private void onReceiveRecordData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("error");
            JSONArray jsonArray =object.optJSONArray("data");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                if (jsonArray.length()>0){
                    if (pageIndex==1){
                        recordList.clear();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String address = jsonObject.getString("address");
                        String customerNo = jsonObject.getString("customerNo");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", address);
                        map.put("customerNo", customerNo);
                        recordList.add(map);
                    }
                    if (recordList!=null&&recordList.size()!=0){
                        mRecyclerView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }else {
                        tvNoData.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                }
            }else {
                ToastUtil.ToastText(context,error);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpRequestManager.getInstance(getApplicationContext()).requestCancel(this);
    }
}
