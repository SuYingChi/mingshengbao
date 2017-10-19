package com.msht.minshengbao.FunctionView.repairService;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.PhotoPickerAdapter;
import com.msht.minshengbao.Adapter.appointAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.HttpUrlconnectionUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.ViewUI.Dialog.SelectTable;
import com.msht.minshengbao.ViewUI.widget.MultiLineChooseLayout;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class RepeatFix extends BaseActivity implements View.OnClickListener {
    private String reid,orderId,status,status_info;
    private String address,type,info,parent_category;
    private String phone,title,orderNo,finish_time;
    private String appoint_time;
    private String textString="";
    private String source="4",repeatId;
    private String userId,password;
    private int pos=-1;
    private int   thisposition=-1;
    private ImageView  typeimg;
    private Button     btn_send;
    private TextView   appointment_data,appointment_time;
    private TextView   et_problem;
    private DatePicker datePicker;
    private GridView   photogridview;
    private View pickview;
    private PhotoPickerAdapter mAdapter;
    private MultiLineChooseLayout multiChoose;
    private int k=0;
    private JSONObject jsonbject;   //数据解析
    private JSONArray jsonArray;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private int requesttype=0;
    private static  final int MY_PERMISSIONS_REQUEST=1;
    private ArrayList<String>multiResult=new ArrayList<>();
    private ArrayList<String> imgPaths = new ArrayList<>();
    private ArrayList<String> mDataList=new ArrayList<>();
    private String[] appointTime={"08:30-11:30","11:30-14:30","14:30-17:30","17:30-20:30","20:30-23:30"};
    private CustomDialog customDialog;
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String Error = object.optString("error");
                        if(Results.equals("success")) {
                            if (requesttype==0){
                                customDialog.dismiss();
                                jsonArray=object.optJSONArray("data");
                                questionData();
                            }else if (requesttype==1){
                                jsonbject =object.optJSONObject("data");
                                initShow();
                            }
                        }else {
                            customDialog.dismiss();
                            faifure(Error);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    customDialog.dismiss();
                    Toast.makeText(context, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    Handler Bitmaphandler= new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        String results = jsonObject.optString("result");
                        String error=jsonObject.optString("error");
                        if (results.equals("success")){
                            k++;
                            if(k==imgPaths.size()){
                                customDialog.dismiss();
                                btn_send.setEnabled(true);
                                showDialogs("您的返修申请已经提交");
                            }
                        }else {
                            customDialog.dismiss();
                            btn_send.setEnabled(true);
                            faifure(error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    btn_send.setEnabled(true);
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    };
    private void questionData() {
        try{
            for (int i=0;i<jsonArray.length();i++){
                JSONObject json = jsonArray.getJSONObject(i);
                String content = json.getString("content");
                mDataList.add(content);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        multiChoose.setList(mDataList);      //显示数据
    }
    private void initShow() {
        repeatId=jsonbject.optString("id");
        if (imgPaths.size()!=0){
            for(int i=0;i<imgPaths.size();i++){
                File files=new File(imgPaths.get(i));
                compressImg(files);
            }
        }else {
            customDialog.dismiss();
            btn_send.setEnabled(true);
            showDialogs("您的返修申请已经提交");
        }
    }
    private void compressImg(final File files) {
        Luban.with(this)
                .load(files)                     //传人要压缩的图片
               // .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调
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
                        Toast.makeText(context,"图片压缩失败!",
                                Toast.LENGTH_SHORT).show();
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
        HttpUrlconnectionUtil.multipleFileParameters(validateURL, textParams, fileparams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                Bitmaphandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.what = FAILURE;
                msg.obj =fail;
                Bitmaphandler.sendMessage(msg);
            }
        });
    }
    private void showDialogs(String s) {
        new PromptDialog.Builder(this)
                .setTitle("民生宝")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage(s)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        setResult(0x004);
                        finish();
                    }
                }).show();
    }
    private void faifure(String error) {
        new PromptDialog.Builder(this)
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
        reid=data.getStringExtra("reid");
        orderId=data.getStringExtra("id");
        orderNo=data.getStringExtra("orderNo");
        type= data.getStringExtra("type");
        title=data.getStringExtra("title");
        phone=data.getStringExtra("phone");
        finish_time=data.getStringExtra("finish_time");
        address=data.getStringExtra("address");
        parent_category=data.getStringExtra("parent_category");
        initView();
        initData();
        initEvent();
    }
    private void initData() {
        customDialog.show();
        requesttype=0;
        String validateURL = UrlUtil.RepairOrder_QuestionUrl;
        String geturl=validateURL+"?rc_id="+reid;
        HttpUrlconnectionUtil.executeGet(geturl, new ResultListener(){
            @Override
            public void onResultSuccess(String success) {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = success;
                requestHandler.sendMessage(message);
            }
            @Override
            public void onResultFail(String fail) {
                Message message = new Message();
                message.what = FAILURE;
                message.obj = fail;
                requestHandler.sendMessage(message);
            }
        });
    }
    private void initView() {
        ((TextView)findViewById(R.id.id_orderNo)).setText(orderNo);
        ((TextView)findViewById(R.id.id_tv_type)).setText(parent_category);
        ((TextView)findViewById(R.id.id_tv_title)).setText(title);
        ((TextView)findViewById(R.id.id_create_time)).setText(finish_time);
        ((TextView)findViewById(R.id.id_phone)).setText(phone);
        ((TextView)findViewById(R.id.id_tv_address)).setText(address);
        et_problem=(EditText)findViewById(R.id.id_et_info);
        multiChoose=(MultiLineChooseLayout)findViewById(R.id.id_multiChoose);
        photogridview=(GridView)findViewById(R.id.noScrollgridview);
        appointment_data=(TextView)findViewById(R.id.id_data);
        appointment_time=(TextView)findViewById(R.id.id_time);
        typeimg=(ImageView)findViewById(R.id.id_img_type);
        btn_send=(Button)findViewById(R.id.id_btn_send);
        if (type.equals("1")){
            typeimg.setImageResource(R.drawable.home_sanitary_xh);
        }else if (type.equals("2")){
            typeimg.setImageResource(R.drawable.home_appliance_fix_xh);
        }else if (type.equals("3")){
            typeimg.setImageResource(R.drawable.home_lanterns_xh);
        }else if (type.equals("4")){
            typeimg.setImageResource(R.drawable.home_otherfix_xh);
        }else if (type.equals("36")){
            typeimg.setImageResource(R.drawable.home_appliance_clean_xh);
        }
        appointment_data.setOnClickListener(this);
        appointment_time.setOnClickListener(this);

    }
    private void initEvent() {
        btn_send.setOnClickListener(this);
        mAdapter = new PhotoPickerAdapter(imgPaths);
        photogridview.setAdapter(mAdapter);
        photogridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= 23) {
                    thisposition=position;
                    initphoto(position);
                } else {
                    if (position == imgPaths.size()) {
                        PhotoPicker.builder()
                                .setPhotoCount(4)
                                .setShowCamera(true)
                                .setSelected(imgPaths)
                                .setShowGif(true)
                                .setPreviewEnabled(true)
                                .start(RepeatFix.this, PhotoPicker.REQUEST_CODE);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("imgPaths", imgPaths);
                        bundle.putInt("position", position);
                        Intent intent = new Intent(RepeatFix.this, EnlargePicActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, position);
                    }
                }
            }
        });
        multiChoose.setOnItemClickListener(new MultiLineChooseLayout.onItemClickListener() {
            @Override
            public void onItemClick(int position, String text) {
                multiResult=multiChoose.getAllItemSelectedTextWithListArray();
                if (multiResult!=null&&multiResult.size()>0){
                    String textselect ="";
                    for (int i=0;i<multiResult.size();i++){
                        textselect +=multiResult.get(i)+",";
                    }
                    textString=textselect;
                }else {
                    textString="";
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
    private void initphoto(int position) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            AndPermission.with(this)
                    .requestCode(MY_PERMISSIONS_REQUEST)
                    .permission(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .send();
        }else {
            if (position == imgPaths.size()) {
                PhotoPicker.builder()
                        .setPhotoCount(4)
                        .setShowCamera(true)
                        .setSelected(imgPaths)
                        .setShowGif(true)
                        .setPreviewEnabled(true)
                        .start(RepeatFix.this, PhotoPicker.REQUEST_CODE);
            } else {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imgPaths",imgPaths);
                bundle.putInt("position",position);
                Intent intent=new Intent(RepeatFix.this, EnlargePicActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,position);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if(requestCode==MY_PERMISSIONS_REQUEST) {
                showphoto();
            }
        }
        @Override
        public void onFailed(int requestCode) {
            if(requestCode==MY_PERMISSIONS_REQUEST) {
                Toast.makeText(RepeatFix.this,"授权失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void showphoto() {
        if (thisposition== imgPaths.size()) {
            PhotoPicker.builder()
                    .setPhotoCount(4)
                    .setShowCamera(true)
                    .setSelected(imgPaths)
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start(RepeatFix.this, PhotoPicker.REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imgPaths",imgPaths);
            bundle.putInt("position",thisposition);
            Intent intent=new Intent(RepeatFix.this, EnlargePicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,thisposition);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_data:
                selectdate();
                break;
            case R.id.id_time:
                selecttime();
                break;
            case R.id.id_btn_send:
                if (TextUtils.isEmpty(appointment_data.getText().toString())){
                    faifure("请选择预约时间");
                }else {
                    ordersend();
                }
                break;
            default:
                break;
        }
    }

    private void selectdate() {
        LayoutInflater l = LayoutInflater.from(this);
        pickview= l.inflate(R.layout.item_pickerdata_dialog, null);
        datePicker=(DatePicker) pickview.findViewById(R.id.datepicker);
        Calendar mcurrent=Calendar.getInstance();
        datePicker.init(mcurrent.get(Calendar.YEAR), mcurrent.get(Calendar.MONTH),  mcurrent.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {//滑动日期进行对日期的方位进行判断
                Calendar mAfter=Calendar.getInstance();
                Calendar mBefore=Calendar.getInstance();
                mAfter.add(mAfter.DAY_OF_MONTH,6);
                if (isDateAfter(view)) {
                    view.init(mAfter.get(Calendar.YEAR),mAfter.get(Calendar.MONTH),mAfter.get(Calendar.DAY_OF_MONTH), this);
                }
                if (isDateBefore(view)) {
                    view.init(mBefore.get(Calendar.YEAR), mBefore.get(Calendar.MONTH),mBefore.get(Calendar.DAY_OF_MONTH), this);
                }
            }
        });
        new PromptDialog.Builder(this)
                .setTitle("选择预约日期")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                //.setMessage("2016年")
                .setView(pickview)    //pickview  日期选择布局
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        appointment_data.setText(datePicker.getYear() + "-"
                                + (datePicker.getMonth()+1) + "-"
                                + datePicker.getDayOfMonth());
                        appointment_time.setText("08:30-11:30");
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private boolean isDateBefore(DatePicker tempView) {
        Calendar mCalendar = Calendar.getInstance();
        Calendar temCalendar = Calendar.getInstance();
        temCalendar.set(tempView.getYear(), tempView.getMonth(), tempView.getDayOfMonth(), 0, 0, 0);
        if (temCalendar.before(mCalendar)) {     //2016年9月21 后
            return true;
        } else {
            return false;
        }
    }

    private boolean isDateAfter(DatePicker tempView) {
        Calendar mCalendar=Calendar.getInstance();
        mCalendar.add(mCalendar.DAY_OF_MONTH,6);
        Calendar temCalendar=Calendar.getInstance();
        temCalendar.add(temCalendar.DAY_OF_MONTH,6);
        temCalendar.set(tempView.getYear(),tempView.getMonth(),tempView.getDayOfMonth(),0,0,0);
        if (temCalendar.after(mCalendar)){  //2016年9月21 前
            return true;
        }else {
            return false;
        }
    }
    private void selecttime() {
        final SelectTable selectTable=new SelectTable(context);
        final TextView tv_title=(TextView)selectTable.getTitle();
        final ListView mListView=(ListView) selectTable.getListview();
        tv_title.setText("选择时间");
        final appointAdapter adapter=new appointAdapter(context,appointTime,pos);
        mListView.setAdapter(adapter);
        selectTable.show();
        selectTable.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTable.dismiss();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                adapter.notifyDataSetChanged();
                String time=appointTime[position];
                appointment_time.setText(time);
                selectTable.dismiss();
            }
        });
    }
    private void ordersend() {
        String date=appointment_data.getText().toString().trim();
        String time=appointment_time.getText().toString().trim();
        String otherinfo= et_problem.getText().toString().trim();
        appoint_time=date+"  "+time;
        info=textString+otherinfo;
        customDialog.show();
        requesttype=1;
        btn_send.setEnabled(false);
        requstSevices();
    }
    private void requstSevices() {
        String validateURL = UrlUtil.PublishOrder_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("cid",reid);
        textParams.put("phone",phone);
        textParams.put("address",address);
        textParams.put("info",info);
        textParams.put("appoint_time",appoint_time);
        textParams.put("source",source);
        textParams.put("raw_order_id",orderId);
        String FF="userId="+userId+"password="+password+"cid="+reid+"phone="+phone
                +"address="+address+"info="+info+"appoint_time="+appoint_time
                +"source="+source+"raw_order_id="+orderId;
        HttpUrlconnectionUtil.executepost(validateURL,textParams, new ResultListener() {
            @Override
            public void onResultSuccess(String success) {
                Message msg = new Message();
                msg.obj = success;
                msg.what = SUCCESS;
                requestHandler.sendMessage(msg);
            }
            @Override
            public void onResultFail(String fail) {
                Message msg = new Message();
                msg.obj = fail;
                msg.what = FAILURE;
                requestHandler.sendMessage(msg);
            }
        });
    }
}
