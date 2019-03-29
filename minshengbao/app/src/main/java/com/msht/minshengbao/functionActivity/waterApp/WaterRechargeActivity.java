package com.msht.minshengbao.functionActivity.waterApp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.msht.minshengbao.ViewUI.widget.CustomToast;
import com.msht.minshengbao.adapter.WaterMealAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.functionActivity.htmlWeb.PrizesGiftsActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class
 * 直饮水充值
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/3/2  
 */
public class WaterRechargeActivity extends BaseActivity {
    private String amount="0.0" ;
    private String giveFee;
    private Button btnSend;
    private String mAccount;
    private String packId;
    private MyNoScrollGridView gridView;
    private WaterMealAdapter waterMealAdapter;
    private ArrayList<HashMap<String, String>> mListType = new ArrayList<HashMap<String, String>>();
    private static final String PAGE_NAME="直饮水账户充值";
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private static class RequestHandler extends Handler{
        private WeakReference<WaterRechargeActivity> mWeakReference;
        public RequestHandler(WaterRechargeActivity acitvity) {
            mWeakReference=new WeakReference<WaterRechargeActivity>(acitvity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterRechargeActivity activity=mWeakReference.get();
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
                        String results=object.optString("result");
                        String message = object.optString("message");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            JSONArray jsonArray =object.optJSONArray("data");
                            activity.saveData(jsonArray);
                        }else {
                            CustomToast.showWarningLong(message);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    CustomToast.showErrorLong(msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void saveData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String id = json.getString("id");
                String type=json.getString("type");
                String amount=json.getString("amount");
                String activityId="";
                String title="";
                String activityType="";
                String scope="";
                String giveFee="0";
                if (!json.isNull("activity")){
                    JSONObject object=json.getJSONObject("activity");
                    activityId=object.optString("id");
                    title=object.optString("title");
                    activityType=object.optString("type");
                    scope=object.optString("scope");
                    giveFee=object.optString("giveFee");
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("type",type);
                map.put("amount",amount);
                map.put("activityId",activityId);
                map.put("title",title);
                map.put("activityType",activityType);
                map.put("scoped",scope);
                map.put("giveFee",giveFee);
                mListType.add(map);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (mListType.size()!=0){
            waterMealAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_recharge);
        context=this;
        customDialog=new CustomDialog(this, "正在加载");
        VariableUtil.balance=null;
        VariableUtil.MealPos=-1;
        setCommonHeader(PAGE_NAME);
        mAccount=getIntent().getStringExtra("account");
        initHead();
        initView();
        waterMealAdapter=new WaterMealAdapter(context, mListType);
        gridView.setAdapter(waterMealAdapter);
        initData();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btnSend.setEnabled(true);
                amount= mListType.get(position).get("amount");
                giveFee = mListType.get(position).get("giveFee");
                packId=mListType.get(position).get("id");
                VariableUtil.MealPos=position;
                waterMealAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==0x002){
            setResult(0x001);
        }
    }
    private void initHead() {
        View view=findViewById(R.id.id_re_layout);
        view.setBackgroundResource(R.drawable.shape_change_blue_bg);
        TextView tvTip=(TextView)findViewById(R.id.id_tip);
        tvTip.setText(context.getString(R.string.recharge_award_tip));
        TextView clickLook=(TextView)findViewById(R.id.id_look_detail);
        clickLook.setText(context.getString(R.string.click_look));
        clickLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPrizesGifts();
            }
        });
    }
    private void onPrizesGifts() {
        String url=UrlUtil.WATER_PRIZES_GIFTS+"?phone="+mAccount;
        Intent intent=new Intent(context, PrizesGiftsActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("navigate","领取礼品");
        intent.putExtra("flag",0);
        startActivity(intent);
    }

    private void initData() {
        customDialog.show();
        String validateURL= UrlUtil.WATER_RECHARGE_MEAL;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("type","1");
        SendRequestUtil.postDataFromService(validateURL,textParams,requestHandler);
    }
    private void initView() {
        btnSend =(Button)findViewById(R.id.id_btn_send);
        btnSend.setEnabled(false);
        gridView=(MyNoScrollGridView)findViewById(R.id.id_recharge_view);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextStart();
            }
        });
    }
    private void onNextStart() {
        Intent intent=new Intent(context,WaterPayRechargeActivity.class);
        intent.putExtra("amount",amount);
        intent.putExtra("giveFee",giveFee);
        intent.putExtra("packId",packId);
        startActivityForResult(intent,1);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
