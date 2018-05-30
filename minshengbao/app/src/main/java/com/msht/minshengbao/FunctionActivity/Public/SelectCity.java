package com.msht.minshengbao.FunctionActivity.Public;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.SelectCityAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectCity extends BaseActivity {
    private ListView mListView;
    private SelectCityAdapter  mAdapter;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private JSONArray jsonArray;//数据解析
    private ArrayList<HashMap<String, String>> cityList = new ArrayList<HashMap<String, String>>();
    Handler CityHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonArray =object.optJSONArray("data");
                        if(Results.equals("success")) {
                            initShow();
                        }else {
                            failure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
    private void failure(String error) {
        new PromptDialog.Builder(this)
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
    private void initShow() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String flag = jsonObject.getString("flag");
                if (flag.equals("1")){
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", id);
                    map.put("name", name);
                    map.put("flag", flag);
                    cityList.add(map);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (cityList.size()!=0){
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        context=this;
        setCommonHeader("选择城市");
        mListView=(ListView)findViewById(R.id.id_city_view);
        mAdapter=new SelectCityAdapter(context,cityList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VariableUtil.citypos=position;
                String mCity=cityList.get(position).get("name");
                String flag=cityList.get(position).get("flag");
                String Id=cityList.get(position).get("id");
                Intent name=new Intent();
                name.putExtra("mCity",mCity);
                name.putExtra("Id",Id);
                name.putExtra("flag",flag);
                setResult(2, name);
                finish();
            }
        });
        requestserver();
    }
    private void requestserver() {
        String cityurl= UrlUtil.SelectCity_Url;
        SendrequestUtil.executeGet(cityurl, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                CityHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                CityHandler.sendMessage(msg);

            }
        });
    }
}
