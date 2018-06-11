package com.msht.minshengbao.FunctionActivity.repairService;

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
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.Adapter.PhotoPickerAdapter;
import com.msht.minshengbao.Base.BaseActivity;
import com.msht.minshengbao.Callback.ResultListener;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendrequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class RefundApply extends BaseActivity {
    private ImageView typeimg;
    private EditText  et_problem;
    private Button    btn_send;
    private GridView  photogridview;
    private PhotoPickerAdapter mAdapter;
    private Context   context;
    private String    orderNo,refundId;
    private String    userId,password;
    private String    id,parent_category;
    private String    type,finish_time;
    private String    title,mdescribe;
    private int   thisposition=-1;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private int k=0;
    private JSONObject jsonbject;   //数据解析
    private CustomDialog customDialog;
    private static  final int MY_PERMISSIONS_REQUEST=1;
    private ArrayList<String> imgPaths = new ArrayList<>();
    private final String mPageName ="退款申请";
    Handler requestHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        String Results=object.optString("result");
                        String error = object.optString("error");
                        jsonbject =object.optJSONObject("data");
                        if(Results.equals("success")) {
                            initShow();
                        }else {
                            faifure(error);
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
                                showDialog("您的退款申请已经提交");
                            }
                        }else {
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
    private void  faifure(String error) {
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
    private void showDialog(String s) {
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
    private void initShow() {
        refundId=jsonbject.optString("id");
        if (imgPaths.size()!=0){
            for(int i=0;i<imgPaths.size();i++){
                File files=new File(imgPaths.get(i));
                compressImg(files);
            }
        }else {
            customDialog.dismiss();
            btn_send.setEnabled(true);
            showDialog("您的退款申请已经提交");
        }
    }

    private void compressImg(final File files) {
        Luban.with(this)
                .load(files)                     //传人要压缩的图片
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
        String validateURL = UrlUtil.RefundImg_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        Map<String, File> fileparams = new HashMap<String, File>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id", refundId);
        fileparams.put("img",files);
        SendrequestUtil.multipleFileParameters(validateURL, textParams, fileparams, new ResultListener() {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_apply);
        context=this;
        setCommonHeader("退款申请");
        customDialog=new CustomDialog(this, "正在加载");
        userId= SharedPreferencesUtil.getUserId(this, SharedPreferencesUtil.UserId,"");
        password=SharedPreferencesUtil.getPassword(this, SharedPreferencesUtil.Password,"");
        Intent data=getIntent();
        id=data.getStringExtra("id");
        orderNo=data.getStringExtra("orderNo");
        type= data.getStringExtra("type");
        title=data.getStringExtra("title");
        finish_time=data.getStringExtra("finish_time");
        parent_category=data.getStringExtra("parent_category");
        initView();
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
                                .start(RefundApply.this, PhotoPicker.REQUEST_CODE);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("imgPaths", imgPaths);
                        bundle.putInt("position", position);
                        Intent intent = new Intent(RefundApply.this, EnlargePicActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, position);
                    }
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
                Toast.makeText(RefundApply.this,"授权失败",Toast.LENGTH_SHORT).show();
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
                    .start(RefundApply.this, PhotoPicker.REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imgPaths",imgPaths);
            bundle.putInt("position",thisposition);
            Intent intent=new Intent(RefundApply.this, EnlargePicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent,thisposition);
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
                        .start(RefundApply.this, PhotoPicker.REQUEST_CODE);
            } else {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imgPaths",imgPaths);
                bundle.putInt("position",position);
                Intent intent=new Intent(RefundApply.this, EnlargePicActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,position);
            }
        }
    }
    private void initView() {
        ((TextView)findViewById(R.id.id_orderNo)).setText(orderNo);
        ((TextView)findViewById(R.id.id_tv_type)).setText(parent_category);
        ((TextView)findViewById(R.id.id_tv_title)).setText(title);
        ((TextView)findViewById(R.id.id_create_time)).setText(finish_time);
        et_problem=(EditText)findViewById(R.id.id_et_problem);
        typeimg=(ImageView)findViewById(R.id.id_img_type);
        btn_send=(Button)findViewById(R.id.id_btn_send);
        photogridview=(GridView)findViewById(R.id.noScrollgridview);
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
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_send.setEnabled(false);
                requestService();
            }
        });
    }
    private void requestService() {
        customDialog.show();
        mdescribe=et_problem.getText().toString().trim();
        String validateURL = UrlUtil.RefundApply_Url;
        Map<String, String> textParams = new HashMap<String, String>();
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        textParams.put("info",mdescribe);
        SendrequestUtil.executepost(validateURL,textParams, new ResultListener() {
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
