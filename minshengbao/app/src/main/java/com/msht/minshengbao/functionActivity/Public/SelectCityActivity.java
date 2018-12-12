package com.msht.minshengbao.functionActivity.Public;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.adapter.SelectCityAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
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
import java.util.List;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/8/17  
 */
public class SelectCityActivity extends BaseActivity {

    private SelectCityAdapter  mAdapter;
    private JSONArray jsonArray;
    private ArrayList<HashMap<String, String>> cityList = new ArrayList<HashMap<String, String>>();
    private final CityHandler cityHandler=new CityHandler(this);
    private static class CityHandler extends Handler{
        private WeakReference<SelectCityActivity> mWeakReference;
        private CityHandler(SelectCityActivity activity) {
            mWeakReference=new WeakReference<SelectCityActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SelectCityActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onReceiveCityData();
                        }else {
                            activity.onFailure(error);
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
                if (flag.equals(VariableUtil.VALUE_ONE)){
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
        ListView mListView=(ListView)findViewById(R.id.id_city_view);
        mAdapter=new SelectCityAdapter(context,cityList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VariableUtil.cityPos =position;
                String mCity=cityList.get(position).get("name");
                String flag=cityList.get(position).get("flag");
                String cityId=cityList.get(position).get("id");
                Intent name=new Intent();
                name.putExtra("mCity",mCity);
                name.putExtra("Id",cityId);
                name.putExtra("flag",flag);
                setResult(2, name);
                finish();
            }
        });
        requestServer();
    }
    private void requestServer() {
        String cityUrl= UrlUtil.SelectCity_Url;
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(cityUrl, OkHttpRequestUtil.TYPE_GET,null,cityHandler);
    }
}
