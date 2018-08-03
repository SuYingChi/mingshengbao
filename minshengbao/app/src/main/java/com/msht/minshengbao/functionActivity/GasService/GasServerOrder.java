package com.msht.minshengbao.functionActivity.GasService;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.adapter.GasServerAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.PullRefresh.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GasServerOrder extends BaseActivity {
    private TextView tv_nodata;
    private XListView mListView;
    private View Lnodata;
    private GasServerAdapter mAdapter;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private JSONArray jsonArray;   //数据解析
    private int pageNo=1;
    private int pageIndex=0;
    private String  userId,password;
    private int refreshType;
    private static final int SEVERCE_CODE=1;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> seviceList = new ArrayList<HashMap<String, String>>();
    Handler allmyserveHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            if (refreshType==0){
                                mListView.stopRefresh(true);
                            }else if (refreshType==1){
                                mListView.stopLoadMore();
                            }
                            if(jsonArray.length()>0){
                                if (pageNo==1){
                                    seviceList.clear();
                                }
                            }
                            initShow();
                        }else {
                            mListView.stopRefresh(false);
                            faifure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    mListView.stopRefresh(false);
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private void initShow() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int ID = jsonObject.optInt("id");
                String id=Integer.toString(ID);
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
                seviceList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (seviceList.size()==0){
            Lnodata.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }else {
            Lnodata.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void faifure(String error) {
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
        mAdapter = new GasServerAdapter(context,seviceList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int positions=position-1;
                String ids=seviceList.get(positions).get("id");
                Intent servece=new Intent(context, GasservirceDetail.class);
                servece.putExtra("id", ids);
                startActivityForResult(servece, SEVERCE_CODE);
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
                    seviceList.clear();
                    mAdapter.notifyDataSetChanged();
                    customDialog.show();
                    loadData(1);
                }
                break;
        }
    }
    private void initView() {
        mListView=(XListView)findViewById(R.id.id_gasserver_view);
        mListView.setPullLoadEnable(true);
        Lnodata=findViewById(R.id.id_nodata_layout);
        tv_nodata=(TextView)findViewById(R.id.id_tv_nodata);
        tv_nodata.setText("当前没有燃气订单信息");
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
        Map<String, String> textParams = new HashMap<String, String>();
        String pageNum=String.valueOf(pageNo);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("type","0");
        textParams.put("page",pageNum);
        textParams.put("size","16");
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                allmyserveHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                allmyserveHandler.sendMessage(msg);
            }
        });
    }
}
