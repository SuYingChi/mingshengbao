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
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScanCodeResult extends BaseActivity {
    private View layout_result;
    private View layout_error;
    private ImageView result_img;
    private TextView  tv_result;
    private TextView  tv_notice;
    private TextView  tv_equipment;
    private TextView  tv_estate;
    private String    equipmentNo;
    private String    userphone="";
    private String    sign,extParams;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    Handler chargesHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    customDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String results=object.optString("result");
                        String error = object.optString("message");
                        String code=object.optString("code");
                        jsonObject =object.optJSONObject("data");
                        if(results.equals("success")) {
                            layout_result.setVisibility(View.VISIBLE);
                            layout_error.setVisibility(View.GONE);
                            tv_result.setText(R.string.scan_result1);
                            tv_notice.setText(R.string.result_notice1);
                            showcharge();
                        }else {
                            showcharge();
                            layout_result.setVisibility(View.VISIBLE);
                            layout_error.setVisibility(View.GONE);
                            if(code.equals("0001")){
                                tv_result.setText(R.string.scan_result2);
                                tv_notice.setText(R.string.result_notice2);
                            }else {
                                tv_result.setText(error);
                                tv_notice.setText(R.string.result_notice3);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context,msg.obj.toString() ,
                            Toast.LENGTH_SHORT).show();
                    layout_result.setVisibility(View.GONE);
                    layout_error.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };
    private void showcharge() {
        String  equipment=jsonObject.optString("equipmentNo");
        String  address=jsonObject.optString("address");
        String  communityName=jsonObject.optString("communityName");
        tv_equipment.setText(equipment);
        tv_estate.setText(communityName);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code_result);
        context=this;
        setCommonHeader("扫码结果");
        customDialog=new CustomDialog(this, "正在加载");
        equipmentNo=getIntent().getStringExtra("equipmentNo");
        userphone= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        WaterAppBean bean=new WaterAppBean();
        bean.setAccount(userphone);
        bean.setEquipmentNo(equipmentNo);
        sign= SecretKeyUtil.getStringSign(bean);
        extParams=SecretKeyUtil.getextParams(bean);
        initView();
        initData();
    }
    private void initView() {
        layout_error=findViewById(R.id.id_layout_neterror);
        layout_result=findViewById(R.id.id_layout_success);
        result_img=(ImageView)findViewById(R.id.id_result_img);
        tv_result=(TextView)findViewById(R.id.id_scan_result) ;
        tv_notice=(TextView)findViewById(R.id.id_result_notice);
        tv_estate=(TextView)findViewById(R.id.id_tv_estate);
        tv_equipment=(TextView)findViewById(R.id.id_tv_equipment);
        findViewById(R.id.id_btn_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initData() {
        customDialog.show();
        String validateURL= UrlUtil.ScanCode_Buy;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("sign",sign);
        textParams.put("extParams",extParams);
        textParams.put("account",userphone);
        textParams.put("equipmentNo",equipmentNo);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                chargesHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                chargesHandler.sendMessage(msg);
            }
        });
    }
}
