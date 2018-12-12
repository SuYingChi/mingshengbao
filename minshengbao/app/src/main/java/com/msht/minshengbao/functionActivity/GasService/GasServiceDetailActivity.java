package com.msht.minshengbao.functionActivity.GasService;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.adapter.DetailServiceAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2016/9/18  
 */
public class GasServiceDetailActivity extends BaseActivity {
    private ImageView imgType;
    private Button btnStatus, btnCancel, btnEvaluate;
    private ListViewForScrollView mListView;
    private TextView mType;
    private DetailServiceAdapter adapter;
    private String userId,password,id;
    private int    requestCode =0;
    private String orderId;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<GasServiceDetailActivity> mWeakReference;
        public RequestHandler(GasServiceDetailActivity activity) {
            mWeakReference = new WeakReference<GasServiceDetailActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final GasServiceDetailActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestCode ==0){
                                activity.jsonObject =object.optJSONObject("data");
                                activity.onReceiveData();
                            }else if (activity.requestCode ==1){
                                activity.onSuccess();
                            }
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
    private void onSuccess() {
        setResult(0x001);
        finish();
    }
    private void onReceiveData() {
        orderId=jsonObject.optString("id");
        String type=jsonObject.optString("type");
        String status=jsonObject.optString("status");
        String customerNo=jsonObject.optString("customerNo");
        String phone=jsonObject.optString("phone");
        JSONArray jsonArray=jsonObject.optJSONArray("dataList");
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String description = jsonObject.getString("description");
                String name = jsonObject.getString("name");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("description", description);
                map.put("name", name);
                dataList.add(map);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        btnStatus.setVisibility(View.VISIBLE);
        onSetType(type);
        onSetStatus(status);
        adapter.notifyDataSetChanged();
    }
    private void onSetType(String type) {
        switch (type){
            case ConstantUtil.VALUE_THREE:
                imgType.setImageResource(R.drawable.homepage_gas_dianhuo3x);
                mType.setText("报装通气");
                break;
            case ConstantUtil.VALUE_FIVE:
                imgType.setImageResource(R.drawable.homepage_gas_baoxiu3x);
                mType.setText("报修服务");
                break;
            case ConstantUtil.VALUE_SEVER:
                imgType.setImageResource(R.drawable.homepage_gas_tousu3x);
                mType.setText("咨询投诉");
                break;
            case ConstantUtil.VALUE_SIX:
                imgType.setImageResource(R.drawable.homepage_gas_tousu3x);
                mType.setText("咨询投诉");
                break;
            default:
                imgType.setImageResource(R.drawable.homepage_gas_tousu3x);
                mType.setText("咨询投诉");
                break;
        }
    }
    private void onSetStatus(String status) {
        switch (status){
            case ConstantUtil.VALUE_ONE:
                btnStatus.setText("待受理");
                btnCancel.setVisibility(View.VISIBLE);
                btnEvaluate.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_TWO:
                btnStatus.setText("已取消");
                btnCancel.setVisibility(View.GONE);
                btnEvaluate.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_THREE:
                btnStatus.setText("已驳回");
                btnCancel.setVisibility(View.GONE);
                btnEvaluate.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_FOUR:
                btnStatus.setText("已受理");
                btnCancel.setVisibility(View.VISIBLE);
                btnEvaluate.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_FIVE:
                btnStatus.setText("待支付");
                btnCancel.setVisibility(View.GONE);
                btnEvaluate.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_SIX:
                btnStatus.setText("待评价");
                btnEvaluate.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_SEVER:
                btnStatus.setTextColor(Color.parseColor("#ffbfbfbf"));
                btnStatus.setText("已完成");
                btnStatus.setBackgroundResource(R.drawable.shape_gray_corner_border_inwhite);
                btnCancel.setVisibility(View.GONE);
                btnEvaluate.setVisibility(View.GONE);
                break;
            case ConstantUtil.VALUE_EIGHT:
                btnStatus.setText("待生成账单");
                btnCancel.setVisibility(View.GONE);
                btnEvaluate.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasservirce_detail);
        context=this;
        mPageName="燃气服务详情";
        setCommonHeader(mPageName);
        customDialog=new CustomDialog(this, "正在加载");
        Intent info=getIntent();
        id=info.getStringExtra("id");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initView();
        initData();   //获取数据
        adapter = new DetailServiceAdapter(this,dataList);
        mListView.setAdapter(adapter);
        initEvent();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            if (resultCode==0x002){
                setResult(0x002);
                finish();
            }
        }
    }
    private void initView() {
        imgType=(ImageView)findViewById(R.id.id_img_type);
        btnStatus =(Button)findViewById(R.id.id_btn_dealwith);
        btnCancel =(Button)findViewById(R.id.id_cancel);
        btnEvaluate =(Button)findViewById(R.id.id_evaluate);
        mType=(TextView)findViewById(R.id.id_tv_type);
        mListView=(ListViewForScrollView)findViewById(R.id.id_detail_listview);
    }
    private void initData() {
        customDialog.show();
        requestCode =0;
        requestService();
    }
    private void requestService() {
        String validateURL="";
        if (requestCode ==0) {
            validateURL = UrlUtil.Servirce_detailUrl;
        }else if (requestCode ==1){
            validateURL = UrlUtil.GasCancel_workUrl;
        }
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);

    }
    private void initEvent() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();//确认是否区取消工单
            }
        });
        btnEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assess = new Intent(context, GasEvaluateWorkOrderActivity.class);
                assess.putExtra("id", orderId);
                startActivityForResult(assess,2);
            }
        });
    }
    private void verify() {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("请确认是否取消工单")
                .setButton1("取消", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        requestCode =1;
                        requestService();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
