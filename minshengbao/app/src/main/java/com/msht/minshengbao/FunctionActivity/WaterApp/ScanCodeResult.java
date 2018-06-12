package com.msht.minshengbao.FunctionActivity.WaterApp;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Bean.WaterAppBean;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SecretKeyUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class ScanCodeResult extends BaseActivity {
    private View layoutResult;
    private View layoutError;
    private ImageView result_img;
    private TextView tvResult;
    private TextView tvNotice;
    private TextView tvEquipment;
    private TextView tvEstate;
    private String   equipmentNo;
    private String   userPhone ="";
    private String   sign,extParams;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private final ResultHandler resultHandler=new ResultHandler(this);
    private static class ResultHandler extends Handler{
        private WeakReference<ScanCodeResult> mWeakReference;
        public ResultHandler(ScanCodeResult activity) {
            mWeakReference = new WeakReference<ScanCodeResult>(activity);
        }
        @Override
        public void handleMessage(Message msg) {

            final ScanCodeResult activity=mWeakReference.get();
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
                        String error = object.optString("message");
                        String code=object.optString("code");
                        activity.jsonObject =object.optJSONObject("data");
                        if(results.equals(SendrequestUtil.SUCCESS_VALUE)) {
                            activity.layoutResult.setVisibility(View.VISIBLE);
                            activity.layoutError.setVisibility(View.GONE);
                            activity.tvResult.setText(R.string.scan_result1);
                            activity.tvNotice.setText(R.string.result_notice1);
                            activity.onResultData();
                        }else {
                            activity.onResultData();
                            activity.layoutResult.setVisibility(View.VISIBLE);
                            activity.layoutError.setVisibility(View.GONE);
                            if(code.equals("0010")){
                                activity.tvResult.setText(R.string.scan_result2);
                                activity.tvNotice.setText(R.string.result_notice2);
                            }else {
                                activity.tvResult.setText(R.string.scan_result3);
                                activity.tvNotice.setText(R.string.result_notice3);
                            }
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
                    activity.layoutResult.setVisibility(View.GONE);
                    activity.layoutError.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onResultData() {
        String  equipment=jsonObject.optString("equipmentNo");
        String  communityName=jsonObject.optString("communityName");
        tvEquipment.setText(equipment);
        tvEstate.setText(communityName);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code_result);
        context=this;
        setCommonHeader("扫码结果");
        customDialog=new CustomDialog(this, "正在加载");
        equipmentNo=getIntent().getStringExtra("equipmentNo");
        userPhone = SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        WaterAppBean bean=new WaterAppBean();
        bean.setAccount(userPhone);
        bean.setEquipmentNo(equipmentNo);
        sign= SecretKeyUtil.getStringSign(bean);
        extParams=SecretKeyUtil.getextParams(bean);
        initView();
        initData();
    }
    private void initView() {
        layoutError =findViewById(R.id.id_layout_neterror);
        layoutResult =findViewById(R.id.id_layout_success);
        result_img=(ImageView)findViewById(R.id.id_result_img);
        tvResult =(TextView)findViewById(R.id.id_scan_result) ;
        tvNotice =(TextView)findViewById(R.id.id_result_notice);
        tvEstate =(TextView)findViewById(R.id.id_tv_estate);
        tvEquipment =(TextView)findViewById(R.id.id_tv_equipment);
        findViewById(R.id.id_btn_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initData() {
        customDialog.show();
        String validateURL= UrlUtil.SCAN_CODE_BUY;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        textParams.put("account", userPhone);
        textParams.put("equipmentNo",equipmentNo);
        SendrequestUtil.postDataFromService(validateURL,textParams,resultHandler);
    }
}
