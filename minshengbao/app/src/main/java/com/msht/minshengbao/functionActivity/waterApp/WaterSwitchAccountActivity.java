package com.msht.minshengbao.functionActivity.waterApp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.msht.minshengbao.custom.widget.CustomToast;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.DateUtils;
import com.msht.minshengbao.Utils.SecretKeyUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.custom.Dialog.CustomDialog;
import com.msht.minshengbao.custom.Dialog.PromptDialog;
import com.msht.minshengbao.adapter.WaterUserAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterSwitchAccountActivity extends BaseActivity {
    private WaterUserAdapter mAdapter;
    private String account;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private  int requestCode=0;
    private CustomDialog customDialog;
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<WaterSwitchAccountActivity> mWeakReference;
        public RequestHandler(WaterSwitchAccountActivity activity) {
            mWeakReference = new WeakReference<WaterSwitchAccountActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterSwitchAccountActivity activity=mWeakReference.get();
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
                        String msgError = object.optString("message");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                activity.onReceiveData(object);
                            }else if (activity.requestCode==1){
                                activity.onSwitchResult();
                            }else if (activity.requestCode==2){
                                activity.setResult(1);
                                activity.mList.clear();
                                activity.mAdapter.notifyDataSetChanged();
                                activity.onDeleteSuccess(msgError);
                            }
                        }else {
                            CustomToast.showWarningLong(msgError);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    CustomToast.showErrorLong(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onDeleteSuccess(String msgError) {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(msgError)
                .setButton1("我知道了", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        initData();
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onSwitchResult() {
        setResult(1);
        finish();
    }

    private void onReceiveData(JSONObject object) {
        JSONArray jsonArray=object.optJSONArray("data");
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String account=json.optString("account");
                String isDefault=json.optString("isDefault");
                String bindTime=json.optString("bindTime");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("account", account);
                map.put("isDefault", isDefault);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_switch_account);
        context=this;
        mPageName="账户切换(水宝)";
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        initHearer();
        initFindViewId();
        initData();
        mAdapter.setClickCallBack(new WaterUserAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                String switchPhone=mList.get(pos).get("account");
                requestCode=1;
                String requestUrl= UrlUtil.WATER_SWITCH_ACCOUNT;
                switchAccount(switchPhone,requestUrl);
            }
        });
        mAdapter.setDeleteItemClickBack(new WaterUserAdapter.ItemDeleteClickBack() {
            @Override
            public void onItemDeleteClick(int pos) {
                String switchPhone=mList.get(pos).get("account");
                requestCode=2;
                String requestUrl= UrlUtil.WATER_UNBIND_ACCOUNT;
                switchAccount(switchPhone,requestUrl);
            }
        });

    }

    private void switchAccount(String switchPhone, String requestUrl) {
        long time= DateUtils.getCurTimeLong();
        String pattern="yyyy-MM-dd HH:mm:ss";
        String openTime=DateUtils.getDateToString(time,pattern);
        String jsonResult="";
        JSONObject object=new JSONObject();
        try{
            object.put("account",account);
            object.put("phone",switchPhone);
            object.put("time",openTime);
            jsonResult=object.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("account",account);
        treeMap.put("phone",switchPhone);
        treeMap.put("time",openTime);
        String extParams= SecretKeyUtil.getKeyextParams(jsonResult);
        String sign= SecretKeyUtil.getKeySign(treeMap);
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("extParams",extParams);
        textParams.put("sign",sign);
        customDialog.show();
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantUtil.VALUE1:
                if (resultCode==ConstantUtil.VALUE1){
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                    initData();
                }else if(resultCode==ConstantUtil.VALUE2){
                    setResult(1);
                    finish();
                }
                break;
                default:
                    break;
        }

    }
    private void initHearer() {
        final TextView rightText=(TextView)findViewById(R.id.id_tv_rightText);
        rightText.setVisibility(View.VISIBLE);
        rightText.setText("编辑");
        rightText.setTag(0);
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag=(Integer)v.getTag();
                switch (tag){
                    case 0:
                        VariableUtil.deleteFlag =1;
                        mAdapter.notifyDataSetChanged();
                        v.setTag(1);
                        rightText.setText("完成");
                        break;
                    case 1:
                        VariableUtil.deleteFlag =0;
                        mAdapter.notifyDataSetChanged();
                        v.setTag(0);
                        rightText.setText("编辑");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initData() {
        customDialog.show();
        requestCode=0;
        String requestUrl= UrlUtil.WATER_BIND_ACCOUNT_LIST;
        HashMap<String, String> textParams = new HashMap<String, String>(2);
        textParams.put("account",account);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void initFindViewId() {
        XRecyclerView mRecyclerView=(XRecyclerView)findViewById(R.id.id_user_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter=new WaterUserAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);
        findViewById(R.id.id_btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,WaterBindUserActivity.class);
                startActivityForResult(intent,1);
            }
        });

    }
}
