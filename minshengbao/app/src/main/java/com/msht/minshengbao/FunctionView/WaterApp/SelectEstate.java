package com.msht.minshengbao.FunctionView.WaterApp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.EstateAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.MoveSelectAddress.PoiSearchAdapter;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.ILoadMoreCallback;
import com.msht.minshengbao.ViewUI.PullRefresh.LoadMoreListView;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectEstate extends BaseActivity implements View.OnClickListener, TextWatcher {
    private ImageView iv_back;
    private LoadMoreListView search_data;
    private TextView tv_cancel;
    private EditText autotext;
    private int      page=1;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private EstateAdapter mAdapter;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private CustomDialog customDialog;
    Handler communityHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    if (customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String message = object.optString("message");
                        JSONObject jsonObject =object.optJSONObject("data");
                        if(Results.equals("success")) {
                            boolean lastPage=jsonObject.optBoolean("lastPage");
                            if (lastPage){
                                search_data.loadComplete(false);
                            }
                            JSONArray json=jsonObject.optJSONArray("list");
                            mList.clear();
                            initAccount(json);
                        }else {
                            ToastUtil.ToastText(context,message);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    ToastUtil.ToastText(context,msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    private void initAccount(JSONArray json) {
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
        search_data.setAdapter(mAdapter);
        doSearchQuery("");
        search_data.setLoadMoreListener(new ILoadMoreCallback() {
            @Override
            public void loadMore() {
                page++;
                doSearchQuery("");
            }
        });
        search_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        iv_back = (ImageView) findViewById(R.id.iv_back);
        autotext =(EditText) findViewById(R.id.et_search);
        tv_cancel=(TextView)findViewById(R.id.id_cancel);
        search_data=(LoadMoreListView)findViewById(R.id.id_search_data);
        tv_cancel.setEnabled(false);
        tv_cancel.setOnClickListener(this);
        autotext.addTextChangedListener(this);
        autotext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tv_cancel.setEnabled(true);
                tv_cancel.setVisibility(View.VISIBLE);
                return false;
            }
        });
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.id_cancel:
                tv_cancel.setVisibility(View.GONE);
                autotext.setText("");
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
        String validateURL= UrlUtil.SearchEstate_Url;
        String pageNo=String.valueOf(page);
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("keyword",keyWord);
        textParams.put("pageNo",pageNo);
        textParams.put("pageSize","50");
        HttpUrlconnectionUtil.executepost(validateURL, textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                communityHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                communityHandler.sendMessage(msg);
            }
        });
    }
}
