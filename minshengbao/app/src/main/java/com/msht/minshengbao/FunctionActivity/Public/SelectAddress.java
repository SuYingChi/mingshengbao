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
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionActivity.MyActivity.AddAddressActivity;
import com.msht.minshengbao.FunctionActivity.MyActivity.AddressManageActivity;
import com.msht.minshengbao.FunctionActivity.MyActivity.ModifyAddress;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectAddress extends BaseActivity {
    private ListView mListView;
    private AddressAdapter mAdapter;
    private View Rview;
    private View Rbtnnew;
    private TextView tv_rightText;
    private String userId;
    private String password;
    private String Id;
    private boolean boolAction=false;
    private static final int SUCCESS=1;
    private static final int FAILURE=2;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> addrList = new ArrayList<HashMap<String, String>>();

    Handler requestHandler= new Handler() {
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
                            if (jsonArray.length() == 0) {
                                    Rview.setVisibility(View.VISIBLE);
                                } else {
                                    initShow();
                                    Rview.setVisibility(View.GONE);
                                }
                        }else {
                            displayDialog(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
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
    private void initShow() {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String city_id=jsonObject.getString("city_id");
                String address=jsonObject.getString("address");
                String name=jsonObject.optString("name");
                String phone=jsonObject.optString("phone");
                String longitude = jsonObject.getString("longitude");
                String latitude = jsonObject.getString("latitude");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("city_id", city_id);
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
        tv_rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AddressManageActivity.class);
                startActivityForResult(intent,1);
            }
        });
        Rbtnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AddAddressActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }
    private void initView() {
        Rview=findViewById(R.id.id_re_nodata);
        Rbtnnew=findViewById(R.id.id_re_newaddress);
        mListView=(ListView)findViewById(R.id.id_address_view);
    }
    private void initHeader() {
        tv_rightText=(TextView) findViewById(R.id.id_tv_rightText);
        tv_rightText.setVisibility(View.VISIBLE);
        tv_rightText.setText("管理");
    }
    private void initData() {
        customDialog.show();
        String validateURL = UrlUtil.AddressManage_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                requestHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            }
        });
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
                holder.cn_name=(TextView)convertView.findViewById(R.id.id_tv_name);
                holder.cn_phone=(TextView)convertView.findViewById(R.id.id_tv_phone);
                holder.cn_addre = (TextView) convertView.findViewById(R.id.id_tv_address);
                holder.img_edit=(ImageView)convertView.findViewById(R.id.id_edit_img);
                holder.edit_layout=convertView.findViewById(R.id.id_edit_layout);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            holder.cn_phone.setText(addrList.get(position).get("phone"));
            holder.cn_name.setText(addrList.get(position).get("name"));
            holder.cn_addre.setText(addrList.get(position).get("address"));
            holder.edit_layout.setVisibility(View.GONE);
            holder.img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String address=addrList.get(thisposition).get("address");
                    String id = addrList.get(thisposition).get("id");
                    String city_id=addrList.get(thisposition).get("city_id");
                    String longitude = addrList.get(thisposition).get("longitude");
                    String latitude =addrList.get(thisposition).get("latitude");
                    Intent intent=new Intent(mContext,ModifyAddress.class);
                    intent.putExtra("id",id);
                    intent.putExtra("address",address);
                    intent.putExtra("city_id",city_id);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("latitude",latitude);
                    startActivityForResult(intent,1);
                }
            });
            return convertView;
        }
        class Holder{
            View     edit_layout;
            ImageView   img_edit;
            TextView    cn_addre;
            TextView cn_name;
            TextView cn_phone;
        }
    }
}
