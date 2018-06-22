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
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectCity extends BaseActivity {
    private ListView mListView;
    private SelectCityAdapter  mAdapter;
    private JSONArray jsonArray;
    private ArrayList<HashMap<String, String>> cityList = new ArrayList<HashMap<String, String>>();
    private final CityHandler cityHandler=new CityHandler(this);
    private static class CityHandler extends Handler{
        private WeakReference<SelectCity> mWeakReference;
        public CityHandler(SelectCity activity) {
            mWeakReference=new WeakReference<SelectCity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SelectCity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            activity.onReceiveCityData();
                        }else {
                            activity.onFailure(error);
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
    private void onFailure(String error) {
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
    private void onReceiveCityData() {
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
                VariableUtil.cityPos =position;
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
        String cityUrl= UrlUtil.SelectCity_Url;
        SendrequestUtil.getDataFromService(cityUrl,cityHandler);
    }
}
