package com.msht.minshengbao.functionActivity.WaterApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.zxing.Result;
import com.google.zxing.client.android.AutoScannerView;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.msht.minshengbao.functionActivity.GasService.IcCardExpenseActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.LpgBottleWebViewActivity;
import com.msht.minshengbao.functionActivity.HtmlWeb.ShopActivity;
import com.msht.minshengbao.functionActivity.Public.QrCodeErrorActivity;
import com.msht.minshengbao.functionActivity.Public.QrCodeScanActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/7/2  
 */
public class WaterScanCodeActivity extends BaseCaptureActivity {
    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;
    private boolean mSurfaceCreated = false;
    private CheckBox flashLightImg;
    private Context context;
    private String mPageName="二维码";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_scan_code);
        context=this;
        StatusBarCompat.setStatusBar(this);
        setCommonHeader(mPageName);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        autoScannerView = (AutoScannerView) findViewById(R.id.autoscanner_view);
        initView();
    }

    private void initView() {
        flashLightImg=(CheckBox)findViewById(R.id.id_flash_check);
        flashLightImg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (mSurfaceCreated){
                        /**打开手电筒*/
                        autoScannerView.turnOnFlashLight();
                    }
                }else {
                    if (mSurfaceCreated){
                        /**关闭手电筒*/
                        autoScannerView.turnOffFlashLight();

                    }
                }
            }
        });
    }
    private void setCommonHeader(String mPageName) {
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            findViewById(R.id.id_status_view).setVisibility(View.GONE);
        }
        TextView tvNavigationTitle = (TextView) findViewById(R.id.tv_navigation);
        findViewById(R.id.id_goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvNavigationTitle.setText(mPageName);
    }

    @Override
    public SurfaceView getSurfaceView() {
        mSurfaceCreated=true;
        return (surfaceView == null) ? (SurfaceView) findViewById(R.id.preview_view) : surfaceView;
    }
    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        playBeepSoundAndVibrate(true, true);
        resultActions(rawResult.getText());
    }

    private void resultActions(String result) {
        String downloadUrl="http://msbapp.cn/download/success.html?QRCodeJson=";
        String reverseResult=result.replace(downloadUrl,"");
        if (reverseResult.contains("QrcodeType")){
            try{
                JSONObject object = new JSONObject(reverseResult);
                String qrCodeType=object.optString("QrcodeType");
                JSONObject jsonObject=object.optJSONObject("data");
                switch (qrCodeType){
                    case VariableUtil.VALUE_ONE:
                        String equipmentNo=jsonObject.optString("equipmentNo");
                        Intent intent=new Intent();
                        intent.putExtra("equipmentNo",equipmentNo);
                        setResult(3,intent);
                        finish();
                        break;
                    case VariableUtil.VALUE_TWO:
                        makeMistakes(VariableUtil.VALUE_ZERO);
                        break;
                    case VariableUtil.VALUE_THREE:
                        String url=jsonObject.optString("url");
                        goHtmlWeb(url);
                        break;
                    default:
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else if(reverseResult.contains("payType")){
            try {
                JSONObject object = new JSONObject(reverseResult);
                String payType =object.getString("payType");
                String payId  = object.getString("payId");
                String payTime=object.getString("payTime");
                if (payType.equals(VariableUtil.VALUE_ONE)){
                    Intent intent=new Intent(context, IcCardExpenseActivity.class);
                    intent.putExtra("payId",payId);
                    intent.putExtra("payTime",payTime);
                    startActivity(intent);
                    finish();
                }else {
                    makeMistakes("0");
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else if (result.contains(UrlUtil.LPG_QR_CODE_SCAN_URL)){
            startBottleWebView(result);
        }else {
            makeMistakes("1");
        }
    }

    private void startBottleWebView(String result) {
        Intent intent=new Intent(context, LpgBottleWebViewActivity.class);
        intent.putExtra("url",result);
        startActivity(intent);
        flashLightImg.setChecked(false);
    }
    private void goHtmlWeb(String url) {
        Intent intent=new Intent(context, ShopActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("first",1);
        startActivity(intent);
        flashLightImg.setChecked(false);
    }
    private void makeMistakes(String valueZero) {
        ToastUtil.ToastText(context,"非民生宝业务二维码");
        Intent error=new Intent(context,QrCodeErrorActivity.class);
        error.putExtra("error_type",valueZero);
        startActivity(error);
        flashLightImg.setChecked(false);
    }
    @Override
    protected void onResume() {
        super.onResume();
        autoScannerView.setCameraManager(cameraManager);
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(context);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(context);
    }
}
