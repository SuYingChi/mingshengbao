package com.msht.minshengbao.functionActivity.WaterApp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.adapter.EstateAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectEstateActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private ImageView ivBack;
    private LoadMoreListView searchData;
    private TextView tvCancel;
    private EditText automateText;
    private int      page=1;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private EstateAdapter mAdapter;
    private CustomDialog customDialog;
    private final CommunityHandler communityHandler=new CommunityHandler(this);
    private static class CommunityHandler extends Handler{
        private WeakReference<SelectEstateActivity>mWeakReference;
        public CommunityHandler(SelectEstateActivity activity) {
            mWeakReference=new WeakReference<SelectEstateActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SelectEstateActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String message = object.optString("message");
                        JSONObject jsonObject =object.optJSONObject("data");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            boolean lastPage=jsonObject.optBoolean("lastPage");
                            if (lastPage){
                                activity.searchData.loadComplete(false);
                            }
                            JSONArray json=jsonObject.optJSONArray("list");
                            activity.mList.clear();
                            activity.onReceiveAccountData(json);
                        }else {
                            ToastUtil.ToastText(activity.context,message);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onReceiveAccountData(JSONArray json) {
        try {
            for (int i = 0; i< json.length(); i++) {
                JSONObject jsonObject = json.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String address = jsonObject.getString("address");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name", name);
                map.put("address", address);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mList.size()!=0){
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_estate);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        initView();
        mAdapter=new EstateAdapter(this,mList);
        searchData.setAdapter(mAdapter);
        doSearchQuery("");
        searchData.setLoadMoreListener(new ILoadMoreCallback() {
            @Override
            public void loadMore() {
                page++;
                doSearchQuery("");
            }
        });
        searchData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name=mList.get(position).get("name");
                String Id=mList.get(position).get("id");
                String address=mList.get(position).get("address");
                Intent intent=new Intent();
                intent.putExtra("name",name);
                intent.putExtra("address",address);
                intent.putExtra("id",Id);
                setResult(0x001, intent);
                finish();
            }
        });
        mAdapter.setOnItemSelectListener(new EstateAdapter.OnItemSelectListener() {
            @Override
            public void ItemSelectClick(View view, int thisposition) {
                String name=mList.get(thisposition).get("name");
                String Id=mList.get(thisposition).get("id");
                String address=mList.get(thisposition).get("address");
                Intent intent=new Intent();
                intent.putExtra("name",name);
                intent.putExtra("address",address);
                intent.putExtra("id",Id);
                setResult(0x001, intent);
                finish();
            }
        });
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);;
        automateText =(EditText) findViewById(R.id.et_search);
        tvCancel =(TextView)findViewById(R.id.id_cancel);
        searchData =(LoadMoreListView)findViewById(R.id.id_search_data);
        tvCancel.setEnabled(false);
        tvCancel.setOnClickListener(this);
        automateText.addTextChangedListener(this);
        automateText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tvCancel.setEnabled(true);
                tvCancel.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.id_cancel:
                tvCancel.setVisibility(View.GONE);
                automateText.setText("");
                break;
            default:
                break;
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        page=1;
        doSearchQuery(s.toString().trim());
    }
    @Override
    public void afterTextChanged(Editable s) {}
    private void doSearchQuery(String keyWord) {
        String validateURL= UrlUtil.SEARCH_ESTATE_URL;
        String pageNo=String.valueOf(page);
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("keyword",keyWord);
        textParams.put("pageNo",pageNo);
        textParams.put("pageSize","50");
        SendrequestUtil.postDataFromService(validateURL,textParams,communityHandler);
    }
}
