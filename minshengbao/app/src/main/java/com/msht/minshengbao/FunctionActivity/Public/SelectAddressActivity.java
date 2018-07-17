package com.msht.minshengbao.FunctionActivity.Public;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.FunctionActivity.MyActivity.AddAddressActivity;
import com.msht.minshengbao.FunctionActivity.MyActivity.AddressManageActivity;
import com.msht.minshengbao.FunctionActivity.MyActivity.ModifyAddress;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/7/2  
 */
public class SelectAddressActivity extends BaseActivity {
    private ListView mListView;
    private AddressAdapter mAdapter;
    private View layoutView;
    private View layoutBtnNew;
    private TextView tvRightText;
    private String userId;
    private String password;
    private String id;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> addrList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<SelectAddressActivity> mWeakReference;
        public RequestHandler(SelectAddressActivity activity) {
            mWeakReference=new WeakReference<SelectAddressActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SelectAddressActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonArray =object.optJSONArray("data");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            if (activity.jsonArray.length() == 0) {
                                activity.layoutView.setVisibility(View.VISIBLE);
                            } else {
                                activity.onReceiveAddressData();
                                activity.layoutView.setVisibility(View.GONE);
                            }
                        }else {
                            activity.displayDialog(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendrequestUtil.FAILURE:
                    if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                        activity.customDialog.dismiss();
                    }
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
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
    private void onReceiveAddressData() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String cityId=jsonObject.getString("city_id");
                String address=jsonObject.getString("address");
                String name=jsonObject.optString("name");
                String phone=jsonObject.optString("phone");
                String longitude = jsonObject.getString("longitude");
                String latitude = jsonObject.getString("latitude");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("city_id", cityId);
                map.put("address", address);
                map.put("name",name);
                map.put("phone",phone);
                map.put("longitude", longitude);
                map.put("latitude", latitude);
                addrList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (addrList.size()!=0){
            mAdapter.notifyDataSetChanged();
            findViewById(R.id.id_re_nodata).setVisibility(View.GONE);
            findViewById(R.id.line).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.id_re_nodata).setVisibility(View.GONE);
            findViewById(R.id.line).setVisibility(View.GONE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        context=this;
        setCommonHeader("选择地址");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initHeader();
        initView();
        mAdapter=new AddressAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mAddress=addrList.get(position).get("address");
                String name=addrList.get(position).get("name");
                String phone=addrList.get(position).get("phone");
                Intent intent=new Intent();
                intent.putExtra("mAddress",mAddress);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                setResult(1,intent);
                finish();
            }
        });
        initData();
        initEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==0x001||resultCode==0x002){
                    addrList.clear();
                    initData();
                }
                break;
            default:
                break;
        }
    }
    private void initEvent() {
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AddressManageActivity.class);
                startActivityForResult(intent,1);
            }
        });
        layoutBtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AddAddressActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }
    private void initView() {
        layoutView =findViewById(R.id.id_re_nodata);
        layoutBtnNew =findViewById(R.id.id_re_newaddress);
        mListView=(ListView)findViewById(R.id.id_address_view);
    }
    private void initHeader() {
        tvRightText =(TextView) findViewById(R.id.id_tv_rightText);
        tvRightText.setVisibility(View.VISIBLE);
        tvRightText.setText("管理");
    }
    private void initData() {
        customDialog.show();
        String validateURL = UrlUtil.AddressManage_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        SendrequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }

    private class AddressAdapter extends BaseAdapter {

        private Context mContext = null;
        public AddressAdapter(Context context) {
            this.mContext = context;
        }
        @Override
        public int getCount() {
            if (addrList!=null){
                return addrList.size();
            }else {
                return 0;
            }
        }
        @Override
        public Object getItem(int position) {
            if (addrList!=null){
                return addrList.get(position);
            }else {
                return null;
            }
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int thisposition=position;
            Holder holder;
            if(convertView==null){
                holder = new Holder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address_manage, null);
                holder.cnName =(TextView)convertView.findViewById(R.id.id_tv_name);
                holder.cnPhone =(TextView)convertView.findViewById(R.id.id_tv_phone);
                holder.cnAddress = (TextView) convertView.findViewById(R.id.id_tv_address);
                holder.imgEdit =(ImageView)convertView.findViewById(R.id.id_edit_img);
                holder.editLayout =convertView.findViewById(R.id.id_edit_layout);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            holder.cnPhone.setText(addrList.get(position).get("phone"));
            holder.cnName.setText(addrList.get(position).get("name"));
            holder.cnAddress.setText(addrList.get(position).get("address"));
            holder.editLayout.setVisibility(View.GONE);
            holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String address=addrList.get(thisposition).get("address");
                    String id = addrList.get(thisposition).get("id");
                    String cityId=addrList.get(thisposition).get("city_id");
                    String longitude = addrList.get(thisposition).get("longitude");
                    String latitude =addrList.get(thisposition).get("latitude");
                    Intent intent=new Intent(mContext,ModifyAddress.class);
                    intent.putExtra("id",id);
                    intent.putExtra("address",address);
                    intent.putExtra("city_id",cityId);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("latitude",latitude);
                    startActivityForResult(intent,1);
                }
            });
            return convertView;
        }
        class Holder{
            View editLayout;
            ImageView imgEdit;
            TextView cnAddress;
            TextView cnName;
            TextView cnPhone;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
