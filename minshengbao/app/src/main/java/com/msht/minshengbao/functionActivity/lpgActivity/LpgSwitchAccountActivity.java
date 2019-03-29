package com.msht.minshengbao.functionActivity.lpgActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.msht.minshengbao.adapter.LpgOrderListAdapter;
import com.msht.minshengbao.adapter.LpgUserListAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;
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
 * @date 2018/7/17
 */
public class LpgSwitchAccountActivity extends BaseActivity {
    private int pageIndex=0;
    private String   userName;
    private LoadMoreListView moreListView;
    private LpgUserListAdapter mAdapter;
    private static final String PAGE_NAME="账号切换(lpg)";
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private static final int BINDING_SUCCESS_CODE=1;
    private  int requestCode=0;
    private CustomDialog customDialog;
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<LpgSwitchAccountActivity> mWeakReference;
        public RequestHandler(LpgSwitchAccountActivity activity) {
            mWeakReference = new WeakReference<LpgSwitchAccountActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgSwitchAccountActivity activity=mWeakReference.get();
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
                        String msgError = object.optString("msg");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode==0){
                                activity.onReceiveData(object);
                            }else if (activity.requestCode==1){
                               activity.onSwitchResult();
                            }else if (activity.requestCode==2){
                                activity.mList.clear();
                                activity.mAdapter.notifyDataSetChanged();
                                activity.onDeleteSuccess(msgError);
                            }
                        }else {
                            activity.onFailure(msgError);
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
    private void onDeleteSuccess(String msgError) {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(msgError)
                .setButton1("我知道了", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        initUserListData();
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onSwitchResult() {
        setResult(2);
        finish();
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

    private void onReceiveData(JSONObject jsonObject) {
        boolean isEndPage=jsonObject.optBoolean("isEndPage");
        boolean isStartPage=jsonObject.optBoolean("isStartPage");
        JSONArray jsonArray=jsonObject.optJSONArray("userVos");
        if (isEndPage){
            moreListView.loadComplete(false);
        }else {
            moreListView.loadComplete(true);
        }
        if (jsonArray.length()>0&&isStartPage){
            mList.clear();
            mAdapter.notifyDataSetChanged();
        }
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String id=json.optString("id");
                String addressName=json.optString("addressName");
                String mobile=json.optString("mobile");
                String isLastLogin=json.optString("isLastLogin");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("addressName", addressName);
                map.put("mobile", mobile);
                map.put("isLastLogin",isLastLogin);
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
        setContentView(R.layout.activity_lpg_switch_account);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        setCommonHeader("切换账户");
        userName = SharedPreferencesUtil.getUserName(context, SharedPreferencesUtil.UserName, "");
        final TextView rightText=(TextView)findViewById(R.id.id_tv_rightText);
        moreListView=(LoadMoreListView)findViewById(R.id.id_user_list);
        mAdapter=new LpgUserListAdapter(context,mList);
        moreListView.setAdapter(mAdapter);
        rightText.setVisibility(View.VISIBLE);
        rightText.setText("编辑");
        rightText.setTag(0);
        findViewById(R.id.id_btn_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,BindingAccountActivity.class);
                intent.putExtra("flag",BINDING_SUCCESS_CODE);
                startActivityForResult(intent,BINDING_SUCCESS_CODE);
            }
        });
        moreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lpgUserId=mList.get(position).get("id");
                onSwitchUser(lpgUserId);
            }
        });
        moreListView.setLoadMoreListener(new ILoadMoreCallback() {
            @Override
            public void loadMore() {
                loadData(pageIndex + 1);
            }
        });
        mAdapter.setOnItemSelectListener(new LpgOrderListAdapter.OnItemSelectListener() {
            @Override
            public void onSelectItemClick(View view, int thisPosition) {
                String lpgUserId=mList.get(thisPosition).get("id");
                onWhetherDelete(lpgUserId);
            }
        });
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
        initUserListData();
    }

    private void onWhetherDelete(final String lpgUserId) {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("请确认是否删除账户")
                .setButton1("残忍删除", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        onDeleteUser(lpgUserId);
                        dialog.dismiss();
                    }
                })
                .setButton2("算了", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }
    private void onDeleteUser(String lpgUserId) {
        customDialog.show();
        requestCode=2;
        String requestUrl= UrlUtil.LPG_DELETE_USER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("msbMobile",userName);
        textParams.put("userId",lpgUserId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onSwitchUser(String lpgUserId) {
        customDialog.show();
        requestCode=1;
        String requestUrl= UrlUtil.LPG_SWITCH_USER_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("msbMobile",userName);
        textParams.put("userId",lpgUserId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }

    private void initUserListData() {
        customDialog.show();
        loadData(1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==BINDING_SUCCESS_CODE){
            setResult(2);
            initUserListData();
        }
    }
    private void loadData(int i) {
        requestCode=0;
        pageIndex =i;
        String pageNum=String.valueOf(i);
        String pageSize="16";
        String requestUrl= UrlUtil.LPG_ALL_BINDING_USER;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("msbMobile",userName);
        textParams.put("pageNum",pageNum);
        textParams.put("pageSize",pageSize);
        OkHttpRequestUtil.getInstance(context).requestAsyn(requestUrl, OkHttpRequestUtil.TYPE_GET,textParams,requestHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
