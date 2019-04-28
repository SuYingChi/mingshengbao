package com.msht.minshengbao.functionActivity.repairService;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.GsonImpl;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.ViewUI.Dialog.QuestionDescribeDialog;
import com.msht.minshengbao.ViewUI.Dialog.SelectDateDialog;
import com.msht.minshengbao.ViewUI.Dialog.SelectDialog;
import com.msht.minshengbao.ViewUI.widget.CustomToast;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.adapter.PhotoPickerAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.widget.MultiLineChooseLayout;
import com.msht.minshengbao.adapter.RepairAdditionalInfoAdapter;
import com.msht.minshengbao.events.UpdateDataEvent;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
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
 * @date 2018/7/2  
 */
public class RepeatFixActivity extends BaseActivity implements View.OnClickListener {
    private String reid,orderId;
    private String address,info="", parentCategory;
    private String phone,title,orderNo, finishTime;
    private String appointDate;
    private String repeatId;
    private String userId,password;
    private String categoryDesc;
    private String additionalInfo;
    private View layoutCategory;
    private View layoutCategoryButton;
    private ImageView downwardImg;
    private int thisPosition =-1;
    private int mPosition=-1;
    private Button btnSend;
    private TextView appointmentData, appointmentTime;
    private TextView tvDescribe;
    private MyNoScrollGridView mPhotoGridView;
    private PhotoPickerAdapter mAdapter;
    private int k=0;
    private int requestType =0;
    private ArrayList<String> imgPaths = new ArrayList<>();
    private ArrayList<String> mDataList=new ArrayList<>();
    private ArrayList<HashMap<String,String>> mList = new ArrayList<HashMap<String, String>>();
    private String[] appointTime={"08:30-11:30","11:30-14:30","14:30-17:30","17:30-20:30","20:30-23:30"};
    private CustomDialog customDialog;
    private final RequestHandler requestHandler=new RequestHandler(this);
    private final BitmapHandler bitmapHandler=new BitmapHandler(this);
    private static class  RequestHandler extends Handler{
        private WeakReference<RepeatFixActivity> mWeakReference;
        public RequestHandler(RepeatFixActivity activity) {
            mWeakReference = new WeakReference<RepeatFixActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RepeatFixActivity reference =mWeakReference.get();
            if (reference == null||reference.isFinishing()) {
                return;
            }
            if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String result=object.optString("result");
                        String error = object.optString("error");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            if (reference.requestType ==0){
                                JSONArray jsonArray=object.optJSONArray("data");
                                reference.questionData(jsonArray);
                            }else if (reference.requestType ==1){
                                JSONObject jsonObject =object.optJSONObject("data");
                                reference.initShow(jsonObject);
                            }
                        }else {
                            reference.btnSend.setEnabled(true);
                            reference.onFailure(error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    ToastUtil.ToastText(reference.context,msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private static class BitmapHandler extends Handler{
        private WeakReference<RepeatFixActivity> mWeakReference;
        private BitmapHandler(RepeatFixActivity activity) {
            mWeakReference = new WeakReference<RepeatFixActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final RepeatFixActivity reference =mWeakReference.get();
            if (reference == null||reference.isFinishing()) {
                return;
            }
            if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                reference.customDialog.dismiss();
            }
            switch (msg.what) {
                case SendRequestUtil.SUCCESS:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String results = jsonObject.optString("result");
                        String error=jsonObject.optString("error");
                        if (results.equals(SendRequestUtil.SUCCESS_VALUE)){
                            reference.k++;
                            if(reference.k==reference.imgPaths.size()){
                                if (reference.customDialog!=null&&reference.customDialog.isShowing()){
                                    reference.customDialog.dismiss();
                                }
                                reference.btnSend.setEnabled(true);
                                reference.setResult(0x004);
                                EventBus.getDefault().post(new UpdateDataEvent(true));
                                reference.onShowDialog("您的返修申请已经提交");
                            }
                        }else {
                            reference.btnSend.setEnabled(true);
                            reference.onFailure(error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SendRequestUtil.FAILURE:
                    reference.btnSend.setEnabled(true);
                    ToastUtil.ToastText(reference.context, msg.obj.toString());
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private void questionData(JSONArray jsonArray) {
        try{
            for (int i = 0; i< jsonArray.length(); i++){
                JSONObject json = jsonArray.getJSONObject(i);
                String content = json.getString("content");
                mDataList.add(content);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    private void initShow(JSONObject jsonObject) {
        repeatId= jsonObject.optString("id");
        if (imgPaths.size()!=0){
            for(int i=0;i<imgPaths.size();i++){
                File files=new File(imgPaths.get(i));
                compressImg(files);
            }
        }else {
            customDialog.dismiss();
            btnSend.setEnabled(true);
            setResult(0x004);
            onShowDialog("您的返修申请已经提交");
        }
    }
    private void compressImg(final File files) {
        Luban.with(context)
                .load(files)
               // .putGear(Luban.THIRD_GEAR)
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

    private void uploadImage(File files) {
        String validateURL = UrlUtil.UploadImage_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        Map<String, File> fileparams = new HashMap<String, File>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id", repeatId);
        fileparams.put("img",files);
        SendRequestUtil.postFileToServer(textParams, fileparams,validateURL,bitmapHandler);
    }
    private void onShowDialog(String s) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
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
        setContentView(R.layout.activity_repeat_fix);
        setCommonHeader("返修订单");
        context=this;
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        customDialog=new CustomDialog(this, "正在加载");
        String parentCode="";
        if (data!=null){
            reid=data.getStringExtra("reid");
            orderId=data.getStringExtra("id");
            orderNo=data.getStringExtra("orderNo");
            title=data.getStringExtra("title");
            phone=data.getStringExtra("phone");
            finishTime =data.getStringExtra("finishTime");
            address=data.getStringExtra("address");
            parentCategory =data.getStringExtra("parentCategory");
            parentCode=data.getStringExtra("parentCode");
            categoryDesc=data.getStringExtra("categoryDesc");
            additionalInfo=data.getStringExtra("additionalInfo");
        }
        initView();
        initSpecDetail();
        initSetCodeImage(parentCode);
        initTimeData();
        initData();
        initEvent();
    }

    private void initSpecDetail() {
        ArrayList<HashMap<String ,String>> additionalList=new ArrayList <HashMap<String ,String>>();
        additionalList.clear();
        additionalList.addAll(GsonImpl.getAdditionalList(additionalInfo));
        RecyclerView mRecyclerView=(RecyclerView)findViewById(R.id.id_category_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        RepairAdditionalInfoAdapter mAdditionalAdapter=new RepairAdditionalInfoAdapter(context,additionalList);
        mRecyclerView.setAdapter(mAdditionalAdapter);
    }

    private void initSetCodeImage(String parentCode) {
        ImageView typeImg =(ImageView)findViewById(R.id.id_img_type);
        switch (parentCode) {
            case ConstantUtil.SANITARY_WARE:
                typeImg.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.HOUSEHOLD_CLEAN:
                typeImg.setImageResource(R.drawable.home_appliance_clean_xh);
                break;
            case ConstantUtil.HOUSEHOLD_REPAIR:
                typeImg.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.OTHER_REPAIR:
                typeImg.setImageResource(R.drawable.home_otherfix_xh);
                break;
            case ConstantUtil.HOUSEKEEPING_CLEAN:
                typeImg.setImageResource(R.drawable.housekeeping_clean_xh);
                break;
            default:
                typeImg.setImageResource(R.drawable.home_appliance_fix_xh);
                break;
        }
    }

    private void initTimeData() {
        for (String appointTimeText : appointTime) {
            String moduleType ="3";
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("moduleType", moduleType);
            map.put("name", appointTimeText);
            mList.add(map);
        }
    }
    private void initData() {
        customDialog.show();
        requestType =0;
        String validateURL = UrlUtil.RepairOrder_QuestionUrl;
        String getUrl=validateURL+"?rc_id="+reid;
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(getUrl, OkHttpRequestUtil.TYPE_GET,null,requestHandler);
    }
    private void initView() {
        ((TextView)findViewById(R.id.id_orderNo)).setText(orderNo);
        ((TextView)findViewById(R.id.id_tv_type)).setText(parentCategory);
        ((TextView)findViewById(R.id.id_tv_title)).setText(categoryDesc);
        ((TextView)findViewById(R.id.id_create_time)).setText(finishTime);
        ((TextView)findViewById(R.id.id_phone)).setText(phone);
        ((TextView)findViewById(R.id.id_tv_address)).setText(address);
        findViewById(R.id.id_question_layout).setOnClickListener(this);
        downwardImg=(ImageView)findViewById(R.id.id_downward_img) ;
        layoutCategory=findViewById(R.id.id_category_layout);
        layoutCategoryButton=findViewById(R.id.id_category_button);
        tvDescribe=(TextView)findViewById(R.id.id_tv_describe);
        mPhotoGridView =(MyNoScrollGridView)findViewById(R.id.noScrollgridview);
        appointmentData =(TextView)findViewById(R.id.id_data);
        appointmentTime =(TextView)findViewById(R.id.id_time);
        btnSend =(Button)findViewById(R.id.id_btn_send);
        appointmentData.setOnClickListener(this);
        appointmentTime.setOnClickListener(this);
        if (TextUtils.isEmpty(additionalInfo)){
            layoutCategoryButton.setVisibility(View.GONE);
        }else {
            layoutCategoryButton.setVisibility(View.VISIBLE);
        }
    }
    private void initEvent() {
        btnSend.setOnClickListener(this);
        mAdapter = new PhotoPickerAdapter(imgPaths);
        mPhotoGridView.setAdapter(mAdapter);
        mPhotoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= 23) {
                    thisPosition =position;
                    onRequestLimitPhoto(position);
                } else {
                    if (position == imgPaths.size()) {
                        PhotoPicker.builder()
                                .setPhotoCount(4)
                                .setShowCamera(true)
                                .setSelected(imgPaths)
                                .setShowGif(true)
                                .setPreviewEnabled(true)
                                .start(RepeatFixActivity.this, PhotoPicker.REQUEST_CODE);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("imgPaths", imgPaths);
                        bundle.putInt("position", position);
                        Intent intent = new Intent(RepeatFixActivity.this, EnlargePicActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, position);
                    }
                }
            }
        });
        layoutCategoryButton.setTag(0);
        layoutCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag=(Integer)view.getTag();
                switch (tag){
                    case 0:
                        layoutCategory.setVisibility(View.VISIBLE);
                        downwardImg.setRotation(180);
                        view.setTag(1);
                        break;
                    case 1:
                        layoutCategory.setVisibility(View.GONE);
                        downwardImg.setRotation(0);
                        view.setTag(0);
                        break;
                    default:
                        break;
                }
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
                mAdapter.notifyDataSetChanged();
            }
        }
        if (resultCode == RESULT_OK && requestCode >= 0 && requestCode <= 3) {
            imgPaths.remove(requestCode);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void onRequestLimitPhoto(int position) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            AndPermission.with(this)
                    .runtime()
                    .permission(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            onShowPhoto();
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            CustomToast.showErrorDialog("授权失败");
                        }
                    }).start();
        }else {
            if (position == imgPaths.size()) {
                PhotoPicker.builder()
                        .setPhotoCount(4)
                        .setShowCamera(true)
                        .setSelected(imgPaths)
                        .setShowGif(true)
                        .setPreviewEnabled(true)
                        .start(RepeatFixActivity.this, PhotoPicker.REQUEST_CODE);
            } else {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imgPaths",imgPaths);
                bundle.putInt("position",position);
                Intent intent=new Intent(RepeatFixActivity.this, EnlargePicActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,position);
            }
        }
    }
    private void onShowPhoto() {
        if (thisPosition == imgPaths.size()) {
            PhotoPicker.builder()
                    .setPhotoCount(4)
                    .setShowCamera(true)
                    .setSelected(imgPaths)
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start(RepeatFixActivity.this, PhotoPicker.REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imgPaths",imgPaths);
            bundle.putInt("position", thisPosition);
            Intent intent=new Intent(RepeatFixActivity.this, EnlargePicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, thisPosition);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_data:
                onsSelectDate();
                break;
            case R.id.id_time:
                onSelectTime();
                break;
            case R.id.id_btn_send:
                onOrderSend();
                break;
            case R.id.id_question_layout:
                onQuestionDescribe();
                break;
            default:
                break;
        }
    }

    private void onQuestionDescribe() {
        new QuestionDescribeDialog(context,mDataList).builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .setOnPositiveClickListener(new QuestionDescribeDialog.OnPositiveClickListener() {
                    @Override
                    public void onClick(View v, String text) {
                        tvDescribe.setText(text);
                        info=text;
                    }
                }).show();
    }

    private void onsSelectDate() {
        new SelectDateDialog.Builder(this)
                .setTitle("选择预约日期")
                .setButton1("取消", new SelectDateDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which, String s) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new SelectDateDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which, String s) {
                        appointmentData.setText(s);
                        appointmentTime.setText("08:30-11:30");
                        dialog.dismiss();
                    }
                }).show();
    }
    private void onSelectTime() {
        new SelectDialog(context,mList,mPosition).builder()
                .setTitleText("选择时间")
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setOnSheetItemClickListener(new SelectDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        mPosition=position;
                        String time=mList.get(position).get("name");
                        appointmentTime.setText(time);
                    }
                })
                .show();
    }
    private void onOrderSend() {
        String date= appointmentData.getText().toString().trim();
        String time= appointmentTime.getText().toString().trim();
        appointDate =date+"  "+time;
        customDialog.show();
        requestType =1;
        btnSend.setEnabled(false);
        requestService();
    }
    private void requestService() {
        String source="4";
        String validateURL = UrlUtil.PublishOrder_Url;
        HashMap<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("cid",reid);
        textParams.put("phone",phone);
        textParams.put("address",address);
        textParams.put("info",info);
        textParams.put("appoint_time", appointDate);
        textParams.put("source",source);
        textParams.put("raw_order_id",orderId);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
