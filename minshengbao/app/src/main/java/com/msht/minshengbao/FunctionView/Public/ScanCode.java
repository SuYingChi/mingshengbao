package com.msht.minshengbao.FunctionView.Public;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.DatePicker.Utils.DataUtils;
import com.msht.minshengbao.FunctionView.GasService.IcCardExpense;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanCode extends BaseActivity implements QRCodeView.Delegate {
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private QRCodeView mQRCodeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        context=this;
        setCommonHeader("二维码");
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        mQRCodeView.startSpot();
        ResultAction(result);
    }
    private void ResultAction(String result) {
        if (result.contains("payType")){
            try {
                JSONObject object = new JSONObject(result);
                String payType =object.getString("payType");
                String payId  = object.getString("payId");
                String payTime=object.getString("payTime");
                if (payType.equals("1")){
                    String pattern="yyyyMMddHHmmss";
                    long time1= DateUtils.getCurTimeLong();
                    long time2=DateUtils.getStringToDate(payTime,pattern);
                    long time=time1-time2;
                    long second=time/1000;
                    Intent intent=new Intent(ScanCode.this, IcCardExpense.class);
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
        Intent error=new Intent(ScanCode.this,QrCodeError.class);
        error.putExtra("error_type",s);
        startActivity(error);
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {}
    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();//开始扫描
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

}
