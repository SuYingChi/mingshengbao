package com.msht.minshengbao.FunctionActivity.GasService;

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
import android.widget.Toast;

import com.msht.minshengbao.Adapter.DetailseviceAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GasservirceDetail extends BaseActivity {
    private ImageView imgType;
    private Button btn_status, btn_cancel,btn_evaluate;
    private ListViewForScrollView mListView;
    private TextView mType;
    private DetailseviceAdapter adapter;
    private String   userId,password,id;
    private int requstCode=0;
    private String type,ID,status;
    private String customerNo,phone;
    private final int  SUCCESS   = 1;
    private final int  FAILURE   = 0;
    private JSONArray jsonArray;//数据解析
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            if (requstCode==0){
                                jsonObject =object.optJSONObject("data");
                                initShow();
                            }else if (requstCode==1){
                                success();
                            }
                        }else {
                            faifure(Error);
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
    private void faifure(String error) {
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
    private void success() {
        setResult(0x001);
        finish();
    }
    private void initShow() {
        ID=jsonObject.optString("id");
        type=jsonObject.optString("type");
        status=jsonObject.optString("status");
        customerNo=jsonObject.optString("customerNo");
        phone=jsonObject.optString("phone");
        jsonArray=jsonObject.optJSONArray("dataList");
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
        if (type.equals("3")){
            imgType.setImageResource(R.drawable.homepage_gas_dianhuo3x);
            mType.setText("报装通气");

        }else if (type.equals("5")){
            imgType.setImageResource(R.drawable.homepage_gas_baoxiu3x);
            mType.setText("报修服务");
        }else if (type.equals("6")||type.equals("7")){
            imgType.setImageResource(R.drawable.homepage_gas_tousu3x);
            mType.setText("咨询投诉");
        }
        btn_status.setVisibility(View.VISIBLE);
        if (status.equals("1")) {
            btn_status.setText("待受理");
            btn_cancel.setVisibility(View.VISIBLE);   //显示取消按钮
            btn_evaluate.setVisibility(View.GONE);    //隐藏评价按钮
        }else if (status.equals("2")){
            btn_status.setText("已取消");
        }else if (status.equals("3")){
            btn_status.setText("已驳回");
        }else if (status.equals("4")){
            btn_status.setText("已受理");
        }else if (status.equals("5")){
            btn_status.setText("待支付");
        }else if (status.equals("6")){
            btn_status.setText("待评价");
            btn_evaluate.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.GONE);
        }else if (status.equals("7")){
            btn_status.setTextColor(Color.parseColor("#ffbfbfbf"));
            btn_status.setText("已完成");
            btn_status.setBackgroundResource(R.drawable.shape_gray_corner_boder_inwhite);
        }else if (status.equals("8")){
            btn_status.setText("待生成账单");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasservirce_detail);
        context=this;
        setCommonHeader("燃气服务详情");
        customDialog=new CustomDialog(this, "正在加载");
        Intent info=getIntent();     //获取传值id
        id=info.getStringExtra("id");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        initView();
        initData();   //获取数据
        adapter = new  DetailseviceAdapter(this,dataList);
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
        btn_status=(Button)findViewById(R.id.id_btn_dealwith);
        btn_cancel=(Button)findViewById(R.id.id_cancel);
        btn_evaluate=(Button)findViewById(R.id.id_evaluate);
        mType=(TextView)findViewById(R.id.id_tv_type);
        mListView=(ListViewForScrollView)findViewById(R.id.id_detail_listview);
    }
    private void initData() {
        customDialog.show();
        requstCode=0;
        requestSevice();
    }
    private void requestSevice() {
        String validateURL="";
        if (requstCode==0) {
            validateURL = UrlUtil.Servirce_detailUrl;
        }else if (requstCode==1){
            validateURL = UrlUtil.GasCancel_workUrl;
        }
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener(){
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
    private void initEvent() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();//确认是否区取消工单
            }
        });
        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assess = new Intent(context, GasEvaluateWorkorder.class);
                assess.putExtra("id", ID);
                startActivityForResult(assess,2);
            }
        });
    }
    private void verify() {
        new PromptDialog.Builder(this)
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
                        requstCode=1;
                        requestSevice();
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
