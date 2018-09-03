package com.msht.minshengbao.functionActivity.WaterApp;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Bean.WaterAppBean;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SecretKeyUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2017/1/2  
 */
public class ScanCodeResultActivity extends BaseActivity {
    private View layoutResult;
    private View layoutError;
    private ImageView resultImg;
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
        private WeakReference<ScanCodeResultActivity> mWeakReference;
        private ResultHandler(ScanCodeResultActivity activity) {
            mWeakReference = new WeakReference<ScanCodeResultActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {

            final ScanCodeResultActivity activity=mWeakReference.get();
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
                        String error = object.optString("message");
                        String code=object.optString("code");
                        activity.jsonObject =object.optJSONObject("data");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            activity.onResultData();
                            activity.onResultTip(code,error);
                        }else {
                            activity.onResultData();
                            activity.onResultTip(code,error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
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
    private void onResultTip(String code, String error) {
        layoutResult.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
        switch (code){
            case ConstantUtil.VALUE_CODE_0000:
                resultImg.setImageResource(R.drawable.pay_success_xh);
                tvResult.setText(R.string.scan_result0);
                tvNotice.setText(R.string.result_notice1);
                break;
            case ConstantUtil.VALUE_CODE_0009:
                tvResult.setText(R.string.scan_result9);
                tvNotice.setText(R.string.result_notice4);
                resultImg.setImageResource(R.drawable.payfailure_3xh);
                break;
            case ConstantUtil.VALUE_CODE_0010:
                tvResult.setText(R.string.scan_result10);
                tvNotice.setText(R.string.result_notice3);
                resultImg.setImageResource(R.drawable.payfailure_3xh);
                break;
            case ConstantUtil.VALUE_CODE_0011:
                tvResult.setText(R.string.scan_result11);
                tvNotice.setText(R.string.result_notice5);
                resultImg.setImageResource(R.drawable.payfailure_3xh);
                break;
            case ConstantUtil.VALUE_CODE_0012:
                tvResult.setText(R.string.scan_result12);
                tvNotice.setText(R.string.result_notice4);
                resultImg.setImageResource(R.drawable.payfailure_3xh);
                break;
            case ConstantUtil.VALUE_CODE_1008:
                tvResult.setText(R.string.scan_result1008);
                tvNotice.setText(R.string.result_notice2);
                resultImg.setImageResource(R.drawable.payfailure_3xh);
                break;
            case ConstantUtil.VALUE_CODE_1009:
                tvResult.setText(R.string.scan_result1009);
                tvNotice.setText(R.string.result_notice6);
                resultImg.setImageResource(R.drawable.payfailure_3xh);
                break;
                default:
                    tvResult.setText(error);
                    tvNotice.setText(R.string.result_notice3);
                    resultImg.setImageResource(R.drawable.payfailure_3xh);
                    break;
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
        resultImg =(ImageView)findViewById(R.id.id_result_img);
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
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        textParams.put("account", userPhone);
        textParams.put("equipmentNo",equipmentNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,resultHandler);
    }

    @Override
    protected void onDestroy() {
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
        super.onDestroy();
    }
}
