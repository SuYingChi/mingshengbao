package com.msht.minshengbao.functionActivity.Public;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.functionActivity.MyActivity.ShareMenuActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
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
import java.util.Map;

public class SelectVoucherActivity extends BaseActivity {
    private ListView mListView;
    private Button   btnShare;
    private RelativeLayout  layoutNoData, layoutShare;
    private NotUseAdapter   mAdapter;
    private String userId,password,payAmount,category="2";
    private JSONObject jsonObject;
    private ArrayList<HashMap<String, String>> voucherList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<SelectVoucherActivity> mWeakReference;
        public RequestHandler(SelectVoucherActivity activity) {
            mWeakReference = new WeakReference<SelectVoucherActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SelectVoucherActivity activity =mWeakReference.get();
            if (activity == null||activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("error");
                        activity.jsonObject =object.optJSONObject("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onGetVoucherData();
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
    private void onGetVoucherData() {
        String couponFlag=jsonObject.optString("coupon_flag");
        JSONArray jsonArray=jsonObject.optJSONArray("coupon_list");
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int iD = jsonObject.optInt("id");
                String id=Integer.toString(iD);
                String name=jsonObject.optString("name");
                String scope = jsonObject.getString("scope");
                String amount = jsonObject.getString("amount");
                String useLimit = jsonObject.getString("use_limit");
                String startDate = jsonObject.getString("start_date");
                String endDate= jsonObject.getString("end_date");
                String remainderDays="";
                if (jsonObject.has("remainder_days")){
                    remainderDays=jsonObject.optString("remainder_days");
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("name",name);
                map.put("scope", scope);
                map.put("amount", amount);
                map.put("use_limit", useLimit);
                map.put("start_date",startDate);
                map.put("end_date", endDate);
                map.put("remainder_days",remainderDays);
                voucherList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (voucherList.size()==0){
            layoutNoData.setVisibility(View.VISIBLE);
            if (category.equals(VariableUtil.VALUE_ONE)){
                layoutShare.setVisibility(View.VISIBLE);
            }else {
                layoutShare.setVisibility(View.GONE);
            }
        }else {
            layoutNoData.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            if (couponFlag.equals(VariableUtil.VALUE_ONE)){
                layoutShare.setVisibility(View.GONE);
            }else {
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String vouId = voucherList.get(position).get("id");
                        String amount = voucherList.get(position).get("amount");
                        Intent name = new Intent();
                        name.putExtra("voucherId", vouId);
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
        payAmount=data.getStringExtra("payAmount");
        category=data.getStringExtra("category");
        initFindViewById();
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
        textParams.put("pay_amount",payAmount);
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private void initFindViewById() {
        mListView=(ListView)findViewById(R.id.id_view_voucher);
        layoutNoData =(RelativeLayout)findViewById(R.id.id_re_nodata);
        layoutShare =(RelativeLayout)findViewById(R.id.id_re_share);
        btnShare =(Button)findViewById(R.id.id_btn_share);
    }
    private void initEvent() {
        btnShare.setOnClickListener(new View.OnClickListener() {
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
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_dicount_coupon, null);
                holder.cnName =(TextView)convertView.findViewById(R.id.id_title_name);
                holder.cnTime =(TextView)convertView.findViewById(R.id.id_time);
                holder.cnScope =(TextView) convertView.findViewById(R.id.id_scope);
                holder.cnAmount =(TextView) convertView.findViewById(R.id.id_amount);
                holder.cnUseLimit =(TextView) convertView.findViewById(R.id.id_use_limit);
                holder.cnEndDate =(TextView) convertView.findViewById(R.id.id_end_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String remainderDay=voucherList.get(position).get("remainder_days");
            if ((!TextUtils.isEmpty(remainderDay))&&(!remainderDay.equals(ConstantUtil.VALUE_ZERO))){
                remainderDay="剩"+remainderDay+"天";
                holder.cnTime.setVisibility(View.VISIBLE);
                holder.cnTime.setText(remainderDay);
            }else {
                holder.cnTime.setVisibility(View.GONE);
            }
            holder.cnName.setText(voucherList.get(position).get("name"));
            String limitUse="买满"+voucherList.get(position).get("use_limit")+"元可用";
            holder.cnScope.setText(voucherList.get(position).get("scope"));
            holder.cnAmount.setText("¥"+voucherList.get(position).get("amount"));
            holder.cnUseLimit.setText(limitUse);
            holder.cnEndDate.setText(voucherList.get(position).get("end_date"));
            return convertView;
        }
    }
    class ViewHolder {
        TextView cnName;
        TextView cnScope;
        TextView cnAmount;
        TextView cnUseLimit;
        TextView cnEndDate;
        TextView cnTime;
    }
}
