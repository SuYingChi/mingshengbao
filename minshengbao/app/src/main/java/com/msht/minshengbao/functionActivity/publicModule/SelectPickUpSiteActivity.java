package com.msht.minshengbao.functionActivity.publicModule;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.BaseCallback;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.adapter.SelectAddressAdapter;
import com.msht.minshengbao.functionActivity.myActivity.ModifyAddressActivity;

import org.json.JSONArray;
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
public class SelectPickUpSiteActivity extends BaseActivity {

    private XRecyclerView mRecyclerView;
    private SelectAddressAdapter mAdapter;
    private String userId;
    private String password;
    private String id;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> addrList = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pick_up_site);
        context=this;
        setCommonHeader("选择地址");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        XRecyclerView  mRecyclerView=(XRecyclerView)findViewById(R.id.id_address_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter=new SelectAddressAdapter(addrList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh();
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);
        mAdapter.setClickCallBack(new SelectAddressAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int position) {
                String mAddress=addrList.get(position).get("address");
                String name=addrList.get(position).get("name");
                Intent intent=new Intent();
                intent.putExtra("mAddress",mAddress);
                intent.putExtra("name",name);
                setResult(1,intent);
                finish();
            }
        });
        mAdapter.setOnItemClickListener(new SelectAddressAdapter.OnItemClickListener() {
            @Override
            public void onItemSelectClick(int position) {
                String address=addrList.get(position).get("address");
                String id = addrList.get(position).get("id");
                Intent intent=new Intent(context,ModifyAddressActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("address",address);
                startActivityForResult(intent,1);
            }
        });
        initData();
    }

    private void initData() {
        customDialog.show();
        String validateURL = UrlUtil.SELF_DELIVERY_ADDRESS;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        OkHttpRequestManager.getInstance(getApplicationContext()).postRequestAsync(validateURL, OkHttpRequestManager.TYPE_POST_MULTIPART, textParams, new BaseCallback() {
            @Override
            public void responseRequestSuccess(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                Log.d("data=",data.toString());
                onAnalysisData(data.toString());
            }
            @Override
            public void responseReqFailed(Object data) {
                if (customDialog!=null&&customDialog.isShowing()){
                    customDialog.dismiss();
                }
                ToastUtil.ToastText(context,data.toString());
            }
        });
    }
    private void onAnalysisData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String results=object.optString("result");
            String error = object.optString("error");
            if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                addrList.clear();
                JSONArray jsonArray=object.optJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String address=jsonObject.getString("address");
                    String name=jsonObject.optString("title");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", id);
                    map.put("address", address);
                    map.put("phone","");
                    map.put("name",name);
                    addrList.add(map);
                }
                mAdapter.notifyDataSetChanged();
            }else {
                displayDialog(error);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void displayDialog(String string) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(string)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
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
