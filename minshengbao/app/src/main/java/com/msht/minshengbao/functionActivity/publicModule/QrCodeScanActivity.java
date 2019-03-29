package com.msht.minshengbao.functionActivity.publicModule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.zxing.Result;
import com.google.zxing.client.android.AnimeViewCallback;
import com.google.zxing.client.android.AutoScannerView;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.google.zxing.client.android.FlowLineView;
import com.msht.minshengbao.Utils.AppActivityUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.functionActivity.gasService.IcCardExpenseActivity;
import com.msht.minshengbao.functionActivity.htmlWeb.LpgBottleWebViewActivity;
import com.msht.minshengbao.functionActivity.htmlWeb.ShopActivity;
import com.msht.minshengbao.functionActivity.waterApp.ScanCodeResultActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.Utils.VariableUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/3/10  
 */
public class QrCodeScanActivity extends BaseCaptureActivity {
    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;
    private FlowLineView flowLineView;
    private CheckBox flashLightImg;
    private RadioButton qrCodeRadio;
    private RadioButton barCodeRadio;
    private int codeType=0;
    private boolean isPause = false;
    private boolean mSurfaceCreated = false;
    private static final String PAGE_NAME="二维码";
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);
        StatusBarCompat.setStatusBar(this);
        context=this;
        setCommonHeader(PAGE_NAME);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        autoScannerView = (AutoScannerView) findViewById(R.id.autoscanner_view);
        flowLineView = (FlowLineView) findViewById(R.id.id_autoscanner_view);
        initView();
    }
    private void setCommonHeader(String title) {
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
        tvNavigationTitle.setText(title);
    }
    private void initView() {
        qrCodeRadio=(RadioButton)findViewById(R.id.id_qr_code);
        barCodeRadio=(RadioButton) findViewById(R.id.id_bar_code);
        qrCodeRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isPause=false;
                    flowLineView.setVisibility(View.GONE);
                    autoScannerView.setVisibility(View.VISIBLE);
                    codeType=0;
                    barCodeRadio.setChecked(false);
                    reScan();

                }
            }
        });
        barCodeRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isPause=true;
                    flowLineView.setVisibility(View.VISIBLE);
                    autoScannerView.setVisibility(View.GONE);
                    codeType=1;
                    qrCodeRadio.setChecked(false);
                    reScan();
                }
            }
        });
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
    @Override
    protected void onRestart() {
        super.onRestart();
        if(isPause){
            flowLineView.StartRunning();
        }
    }
    @Override
    public SurfaceView getSurfaceView() {
        mSurfaceCreated=true;
        return (surfaceView == null) ? (SurfaceView) findViewById(R.id.preview_view) : surfaceView;
    }
    @Override
    public AnimeViewCallback getViewfinderHolder() {
        return (flowLineView == null) ? (FlowLineView) findViewById(R.id.id_autoscanner_view) : flowLineView;
    }
    @Override
    public void dealDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        if(isPause){
            flowLineView.Pause();
        }
        playBeepSoundAndVibrate(true, true);
        if (codeType==0){
            resultActions(rawResult.getText());
        }else {
            onBarCodeOperation(rawResult.getText());
        }

    }
    private void onBarCodeOperation(String text) {
        String mUrl=UrlUtil.LPG_QR_CODE_SCAN_URL+"?id="+text;
        Intent intent=new Intent(context, LpgBottleWebViewActivity.class);
        intent.putExtra("url",mUrl);
        startActivity(intent);
        flashLightImg.setChecked(false);
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
                        Intent intent=new Intent(context, ScanCodeResultActivity.class);
                        intent.putExtra("equipmentNo",equipmentNo);
                        startActivity(intent);
                        finish();
                        break;
                    case VariableUtil.VALUE_TWO:
                        makeMistakes(VariableUtil.VALUE_ZERO);
                        break;
                    case VariableUtil.VALUE_THREE:
                        String url=jsonObject.optString("url");
                        AppActivityUtil.onStartUrl(this,url,"");
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
                    Intent intent=new Intent(QrCodeScanActivity.this, IcCardExpenseActivity.class);
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
    private void makeMistakes(String s) {
        ToastUtil.ToastText(context,"非民生宝业务二维码");
        Intent error=new Intent(QrCodeScanActivity.this,QrCodeErrorActivity.class);
        error.putExtra("error_type",s);
        startActivity(error);
        flashLightImg.setChecked(false);
    }
    @Override
    protected void onResume() {
        super.onResume();
        autoScannerView.setCameraManager(cameraManager);
        flowLineView.setCameraManager(cameraManager);
        MobclickAgent.onPageStart(PAGE_NAME);
        MobclickAgent.onResume(context);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
        MobclickAgent.onPause(context);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isPause){
            flowLineView.Pause();
        }
    }
}
