package com.msht.minshengbao.functionActivity.WaterApp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.MPermissionUtils;
import com.msht.minshengbao.Utils.RegularExpressionUtil;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.Utils.TypeConvertUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.adapter.WaterPhotoPickerAdapter;
import com.msht.minshengbao.functionActivity.repairService.EnlargePicActivity;
import com.msht.minshengbao.functionActivity.repairService.PublishOrderActivity;
import com.msht.minshengbao.functionActivity.repairService.PublishSuccessActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Demo class
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * @author hong
 * @date 2018/10/14  
 */
public class WaterMalfunctionActivity extends BaseActivity {

    private EditText  etEquipmentNo;
    private TextView  tvEstate;
    private EditText  etQuestion;
    private Button    btnSend;
    private TextView  tvNum;
    private MyNoScrollGridView mPhotoGridView;
    private WaterPhotoPickerAdapter mPhotoPickerAdapter;
    private String account;
    private String numText="0/200";
    private int k=0;
    private int requestType=0;
    private ArrayList<String> imgPaths = new ArrayList<>();
    private ArrayList<String> imgUrlList = new ArrayList<>();
    private CustomDialog customDialog;

    private final RequestHandler requestHandler=new RequestHandler(this);
    private final BitmapHandler bitmapHandler=new BitmapHandler(this);

    private static class BitmapHandler extends Handler {
        private WeakReference<WaterMalfunctionActivity> mWeakReference;
        private BitmapHandler(WaterMalfunctionActivity activity) {
            mWeakReference = new WeakReference<WaterMalfunctionActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterMalfunctionActivity activity=mWeakReference.get();
            if (activity==null||activity.isFinishing()){
                return;
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String results = jsonObject.optString("result");
                        String error=jsonObject.optString("message");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)){
                            activity.imgUrlList.add(jsonObject.optString("data"));
                            activity.k++;
                            if(activity.k==activity.imgPaths.size()){
                                if (activity.customDialog!=null&&activity.customDialog.isShowing()){
                                    activity.customDialog.dismiss();
                                }
                                activity.k=0;
                                activity.onSendDataService();
                            }
                        }else {
                            ToastUtil.ToastText(activity.context,error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    activity.btnSend.setEnabled(true);
                    ToastUtil.ToastText(activity.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    }
    private static class RequestHandler extends Handler{
        private WeakReference<WaterMalfunctionActivity> mWeakReference;
        public RequestHandler(WaterMalfunctionActivity activity) {
            mWeakReference = new WeakReference<WaterMalfunctionActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final WaterMalfunctionActivity activity=mWeakReference.get();
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
                        String  results=object.optString("result");
                        String error = object.optString("message");
                        if(results.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (activity.requestType==0){
                                JSONObject jsonObject=object.optJSONObject("data");
                                activity.onEquipmentInfo(jsonObject);
                            }else {
                                activity.imgUrlList.clear();
                                activity.onSuccessSend();
                            }
                        }else {
                            if (activity.requestType==1){
                                activity.imgUrlList.clear();
                                activity.onFailure(error);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    if (activity.requestType==1){
                        activity.btnSend.setEnabled(true);
                        activity.imgUrlList.clear();
                    }
                    ToastUtil.ToastText(activity.context, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void onEquipmentInfo(JSONObject jsonObject) {
        String  communityName=jsonObject.optString("communityName");
        tvEstate.setText(communityName);
    }
    private void onSuccessSend() {
        btnSend.setEnabled(true);
        Intent success=new Intent(context,PublishSuccessActivity.class);
        success.putExtra("navigation","故障提交");
        success.putExtra("type",3);
        startActivity(success);
        finish();
    }
    private void onFailure(String error) {
        new PromptDialog.Builder(context)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(error)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        btnSend.setEnabled(true);
                        dialog.dismiss();

                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_malfunction);
        context=this;
        mPageName="故障上报";
        customDialog=new CustomDialog(this, "正在加载");
        account= SharedPreferencesUtil.getUserName(this, SharedPreferencesUtil.UserName,"");
        setCommonHeader(mPageName);
        initFindViewId();
        mPhotoPickerAdapter = new WaterPhotoPickerAdapter(imgPaths);
        mPhotoGridView.setAdapter(mPhotoPickerAdapter);
        mPhotoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onRequestLimit(position);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                imgPaths.clear();
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                imgPaths.addAll(photos);
                mPhotoPickerAdapter.notifyDataSetChanged();
            }
        }
        if (resultCode == RESULT_OK && requestCode >= 0 && requestCode <= 3) {
            imgPaths.remove(requestCode);
            mPhotoPickerAdapter.notifyDataSetChanged();
        }
        if (requestCode==ConstantUtil.VALUE3&&resultCode==ConstantUtil.VALUE3){
            String equipmentNo=data.getStringExtra("equipmentNo");
            etEquipmentNo.setText(equipmentNo);
        }
    }
    private void initFindViewId() {
        mPhotoGridView=(MyNoScrollGridView) findViewById(R.id.id_noScrollGridView);
        tvNum=(TextView)findViewById(R.id.id_tv_num);
        etEquipmentNo=(EditText)findViewById(R.id.id_et_equipmentNo);
        tvEstate=(TextView) findViewById(R.id.id_tv_estate);
        etQuestion=(EditText)findViewById(R.id.id_et_question);
        btnSend=(Button)findViewById(R.id.id_button_send);
        btnSend.setEnabled(false);
        tvNum.setText(numText);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        tvEstate.addTextChangedListener(myTextWatcher);
        etEquipmentNo.addTextChangedListener(myTextWatcher);
        etQuestion.addTextChangedListener(myTextWatcher);
        findViewById(R.id.id_scan_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDataCalculate();
            }
        });
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                onStartScanActivity();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtil.ToastText(context,"没有权限您将无法进行扫描操作！");
                            }
                        }).start();
            }else {
                onStartScanActivity();
            }
        }else {
            onStartScanActivity();
        }
    }

    private void onStartScanActivity() {
        Intent intent=new Intent(context,WaterScanCodeActivity.class);
        startActivityForResult(intent,3);
    }
    private void onDataCalculate() {
        btnSend.setEnabled(false);
        if (imgPaths.size()!=0){
            for(int i=0;i<imgPaths.size();i++){
                File files=new File(imgPaths.get(i));
                onPhotoCompressImg(files);
            }
        }else {
            customDialog.dismiss();
            onSendDataService();
        }
    }
    private void onPhotoCompressImg(final File files) {
        Luban.with(this)
                .load(files)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {}
                    @Override
                    public void onSuccess(File file) {
                        uploadImage(file);
                    }
                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用
                        uploadImage(files);
                        ToastUtil.ToastText(context,"图片压缩失败!");
                    }
                }).launch();
    }

    private void uploadImage(File file) {
        customDialog.show();
        String validateURL = UrlUtil.WATER_IMAGE_UPLOAD;
        Map<String, File> fileParams = new HashMap<String, File>();
        fileParams.put("file",file);
        SendRequestUtil.postSingleFileToServer(validateURL,fileParams,bitmapHandler);
    }
    private void onSendDataService() {
        String imgUrl=TypeConvertUtil.listToString(imgUrlList);
        String validateURL=UrlUtil.WATER_MALFUNCTION_UPLOAD;
        String estate=tvEstate.getText().toString().trim();
        String equipmentNo=etEquipmentNo.getText().toString().trim();
        String questionDescribe=etQuestion.getText().toString().trim();
        HashMap<String, String> textParams = new HashMap<String, String>(5);
        textParams.put("equipmentNo",equipmentNo);
        textParams.put("equipmentName",estate);
        textParams.put("desc",questionDescribe);
        textParams.put("imgUrl",imgUrl);
        textParams.put("account",account);
        requestType=1;
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL,OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    private void onRequestLimit(int position) {
        final int thisPosition=position;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(this)
                        .runtime()
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                onPickPhoto(thisPosition);
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                ToastUtil.ToastText(context,"未允许权限通过，部分功能将无法使用");
                            }
                        }).start();
            }else {
                onPickPhoto(thisPosition);
            }
        }else {
            onPickPhoto(position);
        }
    }

    private void onPickPhoto(int position) {
        if (position == imgPaths.size()) {
            PhotoPicker.builder()
                    .setPhotoCount(3)
                    .setShowCamera(true)
                    .setSelected(imgPaths)
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start(WaterMalfunctionActivity.this, PhotoPicker.REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imgPaths", imgPaths);
            bundle.putInt("position", position);
            Intent intent = new Intent(context, EnlargePicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, position);
        }
    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(TextUtils.isEmpty(etEquipmentNo.getText().toString())||TextUtils.isEmpty(etQuestion.getText().toString())){
                btnSend.setEnabled(false);
            }else {
                btnSend.setEnabled(true);
            }
            if (s.length()==ConstantUtil.VALUE8&& RegularExpressionUtil.isNumeric(etEquipmentNo.getText().toString())){
                onEquipmentInformation();
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(etQuestion.getText().toString())){
                int number=s.length();
                String mNum=String.valueOf(number)+"/200";
                tvNum.setText(mNum);
            }else {
                tvNum.setText(numText);
            }
        }
    }

    private void onEquipmentInformation() {
        requestType=0;
        String equipmentNo=etEquipmentNo.getText().toString().trim();
        String validateURL= UrlUtil.WATER_EQUIPMENT_INFORMATION;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("equipmentNo",equipmentNo);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams ,requestHandler);
    }
}
