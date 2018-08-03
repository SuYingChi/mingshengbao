package com.msht.minshengbao.functionActivity.LPGActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.adapter.LpgDeliveryInfoAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestManager;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/17  
 */
public class LpgSteelBottleQueryActivity extends BaseActivity {
    private TextView tvSteelBottleNo;
    private TextView tvSteelNo;
    private TextView tvPropertyRight;
    private TextView tvManufactureEnterprise;
    private TextView tvSteelBottleNorms;
    private TextView tvManufactureDate;
    private TextView tvLastCheck;
    private TextView tvScrappedDate;
    private ListViewForScrollView mStepView;
    private LpgDeliveryInfoAdapter mAdapter;
    private View     layoutHeader;
    private View     layoutTransferTrajectory;
    private String   bottleId;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private CustomDialog customDialog;
    private RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler {
        private WeakReference<LpgSteelBottleQueryActivity> mWeakReference;
        public RequestHandler(LpgSteelBottleQueryActivity activity) {
            mWeakReference = new WeakReference<LpgSteelBottleQueryActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LpgSteelBottleQueryActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                activity.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendrequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("msg");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            JSONObject jsonObject=object.optJSONObject("lpgBottleInfo");
                            JSONArray  jsonArray=object.optJSONArray("lpgFlowList");
                            activity.onReceiveData(jsonObject);
                            activity.onReceiveFlowList(jsonArray);
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
    /**
     *  钢瓶流转轨迹图
     * @param jsonArray 流转轨迹数据
     */
    private void onReceiveFlowList(JSONArray jsonArray) {
        if (jsonArray.length()>0){
            layoutTransferTrajectory.setVisibility(View.VISIBLE);
        }else {
            layoutTransferTrajectory.setVisibility(View.GONE);
        }
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String status=jsonObject.optString("status");
                String  name=jsonObject.optString("name");
                String createDate=jsonObject.optString("createDate");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("status", status);
                map.put("describe", name);
                map.put("createDate", createDate);
                mList.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
       /* //设置完成的步数
        mStepView.setStepsViewIndicatorComplectingPosition(1)
                .reverseDraw(true)
                .setTextSize(14)
                .setStepViewTexts(mList)
                //设置StepsViewIndicator未完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(context, R.color.uncompleted_color))
                //设置StepsView text未完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(context, R.color.color_383838))
                //设置StepsView text完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(context, R.color.color_9e9e9e));*/
    }

    private void onReceiveData(JSONObject object) {
        String id=object.optString("id");
        String bottleCode=object.optString("bottleCode");
        String propertyUnit=object.optString("propertyUnit");
        String producer=object.optString("producer");
        String bottleWeight=object.optString("bottleWeight");
        String createTime=object.optString("createTime");
        String checkTime=object.optString("checkTime");
        String lastCheckTime=object.optString("lastCheckTime");
        String nextCheckTime=object.optString("nextCheckTime");
        String discardTime=object.optString("discardTime");
        int checkStatus=object.optInt("checkStatus");
        if (checkStatus!=1){
            layoutHeader.setBackgroundResource(R.drawable.qualified_image_xh);
        }else {
            layoutHeader.setBackgroundResource(R.drawable.disqualified_image_xh);
        }
        String bottleWeightNorms="YSP-16.5("+bottleWeight+"kg液化气)";
        tvSteelBottleNo.setText(id);
        tvSteelNo.setText(bottleCode);
        tvPropertyRight.setText(propertyUnit);
        tvManufactureEnterprise.setText(producer);
        tvSteelBottleNorms.setText(bottleWeightNorms);
        tvManufactureDate.setText(createTime);
        tvLastCheck.setText(lastCheckTime);
        tvScrappedDate.setText(discardTime);

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpg_steel_bottle_query_acitivity);
        context=this;
        setCommonHeader("钢瓶查询");
        customDialog=new CustomDialog(this, "正在加载");
        Intent data=getIntent();
        if (data!=null){
            bottleId=data.getStringExtra("bottleId");
        }
        initFindViewId();
        mAdapter=new LpgDeliveryInfoAdapter(context,mList);
        mStepView.setAdapter(mAdapter);
        initBottleData();
    }


    private void initFindViewId() {
        tvSteelBottleNo=(TextView)findViewById(R.id.id_tv_bottleNo);
        tvSteelNo=(TextView)findViewById(R.id.id_tv_steelNo);
        tvPropertyRight=(TextView)findViewById(R.id.id_property_right);
        tvManufactureEnterprise=(TextView)findViewById(R.id.id_manufacture_enterprise);
        tvSteelBottleNorms=(TextView)findViewById(R.id.id_steel_norms);
        tvManufactureDate=(TextView)findViewById(R.id.id_manufacture_date);
        tvLastCheck=(TextView)findViewById(R.id.id_last_check);
        tvScrappedDate=(TextView)findViewById(R.id.id_scrapped_date);
        mStepView = (ListViewForScrollView)findViewById(R.id.id_step_view);
        layoutHeader=findViewById(R.id.id_layout_header);
        layoutTransferTrajectory=findViewById(R.id.id_transfer_trajectory);
    }
    private void initBottleData() {
        String requestUrl= UrlUtil.LPG_BOTTLE_INFO_URL;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("id",bottleId);
        OkHttpRequestManager.getInstance(context).requestAsyn(requestUrl,OkHttpRequestManager.TYPE_GET,textParams,requestHandler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
