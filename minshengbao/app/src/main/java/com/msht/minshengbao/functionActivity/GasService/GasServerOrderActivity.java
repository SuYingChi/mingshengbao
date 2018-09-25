package com.msht.minshengbao.functionActivity.GasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.GasServerAdapter;
import com.msht.minshengbao.Base.BaseActivity;
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
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/9/23  
 */
public class GasServerOrderActivity extends BaseActivity {
    private XListView mListView;
    private View layoutNoData;
    private GasServerAdapter mAdapter;
    private JSONArray jsonArray;
    private int pageNo=1;
    private int pageIndex=0;
    private String  userId,password;
    private int refreshType;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> serviceList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<GasServerOrderActivity> mWeakReference;
        public RequestHandler(GasServerOrderActivity activity) {
            mWeakReference = new WeakReference<GasServerOrderActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasServerOrderActivity activity=mWeakReference.get();
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
                        String result=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonArray =object.optJSONArray("data");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.refreshType==0){
                                activity.mListView.stopRefresh(true);
                            }else if (activity.refreshType==1){
                                activity.mListView.stopLoadMore();
                            }
                            if(activity.jsonArray.length()>0){
                                if (activity.pageNo==1){
                                    activity.serviceList.clear();
                                }
                            }
                            activity.onReceiveData();
                        }else {
                            activity.mListView.stopRefresh(false);
                            activity.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.mListView.stopRefresh(false);
                    ToastUtil.ToastText(activity.context, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onReceiveData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.optString("id");
                String type = jsonObject.getString("type");
                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                String time = jsonObject.getString("time");
                String status = jsonObject.getString("status");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("type", type);
                map.put("title", title);
                map.put("description", description);
                map.put("time",time);
                map.put("status", status);
                serviceList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (serviceList.size()==0){
            layoutNoData.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else {
            layoutNoData.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_server_order);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader("燃气服务");
        userId = SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password, "");
        initView();
        mAdapter = new GasServerAdapter(context, serviceList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int positions=position-1;
                String ids= serviceList.get(positions).get("id");
                Intent server=new Intent(context, GasServiceDetailActivity.class);
                server.putExtra("id", ids);
                startActivityForResult(server, ConstantUtil.REQUEST_CODE_ONE);
            }
        });
        initData();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==0x001||resultCode==0x002){
                    serviceList.clear();
                    mAdapter.notifyDataSetChanged();
                    customDialog.show();
                    loadData(1);
                }
                break;
                default:
                    break;
        }
    }
    private void initView() {
        mListView=(XListView)findViewById(R.id.id_gasserver_view);
        mListView.setPullLoadEnable(true);
        layoutNoData =findViewById(R.id.id_nodata_layout);
        TextView tvNoData =(TextView)findViewById(R.id.id_tv_nodata);
        tvNoData.setText("当前没有燃气订单信息");
    }
    private void initData() {
        customDialog.show();
        loadData(1);
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
        String validateURL = UrlUtil.AllMyservice_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type","0");
        textParams.put("page",pageNum);
        textParams.put("size","16");
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
