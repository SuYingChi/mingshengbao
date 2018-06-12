package com.msht.minshengbao.FunctionActivity.Public;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.Result;
import com.google.zxing.client.android.AnimeViewCallback;
import com.google.zxing.client.android.AutoScannerView;
import com.google.zxing.client.android.BaseCaptureActivity;
import com.google.zxing.client.android.FlowLineView;
import com.msht.minshengbao.FunctionActivity.GasService.IcCardExpense;
import com.msht.minshengbao.FunctionActivity.WaterApp.ScanCodeResult;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.StatusBarCompat;
import com.msht.minshengbao.Utils.VariableUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class QRCodeScan extends BaseCaptureActivity {
    private ImageView backImg;
    private TextView  tvNavigationTitle;
    private SurfaceView surfaceView;
    private AutoScannerView autoScannerView;
    private FlowLineView flowLineView;
    private boolean isPause = false;
    private static final String mPageName="二维码";
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);
        StatusBarCompat.setStatusBar(this);
        context=this;
        setCommonHeader(mPageName);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        autoScannerView = (AutoScannerView) findViewById(R.id.autoscanner_view);
        flowLineView = (FlowLineView) findViewById(R.id.id_autoscanner_view);
        initView();
    }
    private void setCommonHeader(String title) {
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT){
            findViewById(R.id.id_status_view).setVisibility(View.GONE);
        }
        backImg = (ImageView) findViewById(R.id.id_goback);
        tvNavigationTitle = (TextView) findViewById(R.id.tv_navigation);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvNavigationTitle.setText(title);
    }
    private void initView() {
        findViewById(R.id.id_erwei).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPause=false;
                flowLineView.setVisibility(View.GONE);
                autoScannerView.setVisibility(View.VISIBLE);
                reScan();
            }
        });
        findViewById(R.id.id_ar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPause=true;
                flowLineView.setVisibility(View.VISIBLE);
                autoScannerView.setVisibility(View.GONE);
                reScan();
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
        ResultActions(rawResult.getText());
    }
    private void ResultActions(String result) {
        String downloadUrl="http://msbapp.cn/download/success.html?QRCodeJson=";
        String reverseResult=result.replace(downloadUrl,"");
        if (reverseResult.contains("QrcodeType")){
            try{
                JSONObject object = new JSONObject(reverseResult);
                String QrcodeType=object.optString("QrcodeType");
                JSONObject jsonObject=object.optJSONObject("data");
                if (QrcodeType.equals(VariableUtil.VALUE_ONE)){
                    String equipmentNo=jsonObject.optString("equipmentNo");
                    Intent intent=new Intent(context, ScanCodeResult.class);
                    intent.putExtra("equipmentNo",equipmentNo);
                    startActivity(intent);
                    finish();
                }else if (QrcodeType.equals(VariableUtil.VALUE_TWO)){
                    MakeMistakes("0");
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
                    Intent intent=new Intent(QRCodeScan.this, IcCardExpense.class);
                    intent.putExtra("payId",payId);
                    intent.putExtra("payTime",payTime);
                    startActivity(intent);
                    finish();
                }else {
                    MakeMistakes("0");
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else {
            MakeMistakes("1");
        }
    }

    private void MakeMistakes(String s) {
        Intent error=new Intent(QRCodeScan.this,QrCodeError.class);
        error.putExtra("error_type",s);
        startActivity(error);
    }
    @Override
    protected void onResume() {
        super.onResume();
        autoScannerView.setCameraManager(cameraManager);
        flowLineView.setCameraManager(cameraManager);
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(context);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
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
