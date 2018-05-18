package com.msht.minshengbao.FunctionView.Public;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.FunctionView.Myview.ShareMenuActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectVoucher extends BaseActivity {
    private ListView mListView;
    private Button btn_share;
    private RelativeLayout Rnodata,Rshare;
    private NotUseAdapter   mAdapter;
    private String userId,password,pay_amount,category="2";
    private JSONArray jsonArray;//数据解析
    private JSONObject jsonObject;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private static final int ADDRESS_CODE=13;
    private ArrayList<HashMap<String, String>> voucherList = new ArrayList<HashMap<String, String>>();
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        jsonObject =object.optJSONObject("data");
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
        String coupon_flag=jsonObject.optString("coupon_flag");
        jsonArray=jsonObject.optJSONArray("coupon_list");
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int ID = jsonObject.optInt("id");
                String id=Integer.toString(ID);
                String name=jsonObject.optString("name");
                String scope = jsonObject.getString("scope");
                String amount = jsonObject.getString("amount");
                String use_limit = jsonObject.getString("use_limit");
                String start_date = jsonObject.getString("start_date");
                String end_date= jsonObject.getString("end_date");
                String remainder_days="";
                if (jsonObject.has("remainder_days")){
                    remainder_days=jsonObject.optString("remainder_days");
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name",name);
                map.put("scope", scope);
                map.put("amount", amount);
                map.put("use_limit", use_limit);
                map.put("start_date",start_date);
                map.put("end_date", end_date);
                map.put("remainder_days",remainder_days);
                voucherList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (voucherList.size()==0){
            Rnodata.setVisibility(View.VISIBLE);
            if (category.equals("1")){
                Rshare.setVisibility(View.VISIBLE);
            }else {
                Rshare.setVisibility(View.GONE);
            }
        }else {
            Rnodata.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            if (coupon_flag.equals("1")){
                Rshare.setVisibility(View.GONE);
            }else {
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String vouid = voucherList.get(position).get("id");
                        String amount = voucherList.get(position).get("amount");
                        Intent name = new Intent();
                        name.putExtra("vouid", vouid);
                        name.putExtra("amount", amount);
                        setResult(3, name);
                        finish();
                    }
                });
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_voucher);
        context=this;
        setCommonHeader("选择代金券");
        userId = SharedPreferencesUtil.getUserId(context, SharedPreferencesUtil.UserId, "");
        password = SharedPreferencesUtil.getPassword(context, SharedPreferencesUtil.Password, "");
        Intent data=getIntent();
        pay_amount=data.getStringExtra("pay_amount");
        category=data.getStringExtra("category");
        initfindViewByid();
        mAdapter=new NotUseAdapter(context);
        mListView.setAdapter(mAdapter);
        initData();
        initEvent();
    }
    private void initData() {
        String validateURL = UrlUtil.Voucher_CanuseUrl;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("category",category);
        textParams.put("pay_amount",pay_amount);
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
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
    private void initfindViewByid() {
        mListView=(ListView)findViewById(R.id.id_view_voucher);
        Rnodata=(RelativeLayout)findViewById(R.id.id_re_nodata);
        Rshare=(RelativeLayout)findViewById(R.id.id_re_share);
        btn_share=(Button)findViewById(R.id.id_btn_share);
    }
    private void initEvent() {
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ShareMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private class NotUseAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        public NotUseAdapter(Context context) {
            super();
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return voucherList.size();
        }
        @Override
        public Object getItem(int position) {
            return voucherList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            final int thisposition=position;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_dicount_coupon, null);
                holder.cn_name=(TextView)convertView.findViewById(R.id.id_title_name);
                holder.cn_time=(TextView)convertView.findViewById(R.id.id_time);
                holder.cn_scope=(TextView) convertView.findViewById(R.id.id_scope);
                holder.cn_amount=(TextView) convertView.findViewById(R.id.id_amount);
                holder.cn_use_limit=(TextView) convertView.findViewById(R.id.id_use_limit);
                holder.btn_use=(Button)convertView.findViewById(R.id.id_btn_use);

                // holder.cn_start_date=(TextView) convertView.findViewById(R.id.id_start_date);
                holder.cn_end_date=(TextView) convertView.findViewById(R.id.id_end_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String remainder_days=voucherList.get(position).get("remainder_days");
            if (!remainder_days.equals("")){
                holder.cn_time.setVisibility(View.VISIBLE);
                holder.cn_time.setText("剩"+remainder_days+"天");
            }else {
                holder.cn_time.setVisibility(View.GONE);
            }
            holder.cn_name.setText(voucherList.get(position).get("name"));
            String limit_use="买满"+voucherList.get(position).get("use_limit")+"元可用";
            holder.cn_scope.setText(voucherList.get(position).get("scope"));
            holder.cn_amount.setText("¥"+voucherList.get(position).get("amount"));
            holder.cn_use_limit.setText(limit_use);
           // holder.cn_start_date.setText(voucherList.get(position).get("start_date"));
            holder.cn_end_date.setText(voucherList.get(position).get("end_date"));
            holder.btn_use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vouid = voucherList.get(thisposition).get("id");
                    String amount = voucherList.get(thisposition).get("amount");
                    Intent name = new Intent();
                    name.putExtra("vouid", vouid);
                    name.putExtra("amount", amount);
                    setResult(3, name);
                    finish();
                }
            });
            return convertView;
        }
    }
    class ViewHolder {
        TextView  cn_name;
        public TextView  cn_scope;
        public TextView  cn_amount;
        public TextView  cn_use_limit;
        public TextView  cn_end_date;
        TextView  cn_time;
        Button    btn_use;
    }
}
