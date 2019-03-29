package com.msht.minshengbao.functionActivity.repairService;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.msht.minshengbao.OkhttpUtil.OkHttpRequestUtil;
import com.msht.minshengbao.Utils.ConstantUtil;
import com.msht.minshengbao.Utils.GsonImpl;
import com.msht.minshengbao.Utils.ToastUtil;
import com.msht.minshengbao.ViewUI.widget.MyNoScrollGridView;
import com.msht.minshengbao.adapter.PhotoPickerAdapter;
import com.msht.minshengbao.base.BaseActivity;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.SendRequestUtil;
import com.msht.minshengbao.Utils.SharedPreferencesUtil;
import com.msht.minshengbao.Utils.UrlUtil;
import com.msht.minshengbao.ViewUI.Dialog.CustomDialog;
import com.msht.minshengbao.ViewUI.Dialog.PromptDialog;
import com.msht.minshengbao.adapter.RepairAdditionalInfoAdapter;
import com.msht.minshengbao.extra.CashierInputFilter;
import com.umeng.analytics.MobclickAgent;
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
 * @date 2017/8/10  
 */
public class RefundApplyActivity extends BaseActivity {
    private EditText etProblem;
    private EditText etRefundAmount;
    private Button btnSend;
    private ImageView downwardImg;
    private View layoutCategory;
    private View layoutCategoryButton;
    private MyNoScrollGridView mPhotoGridView;
    private PhotoPickerAdapter mAdapter;
    private String parentCode;
    private Context   context;
    private String    orderNo,refundId;
    private String    userId,password;
    private String    id, parentCategory;
    private String    categoryDesc;
    private String    additionalInfo;
    private String    finishTime;
    private String    realAmount;
    private String    title;
    private int thisPosition =-1;
    private int k=0;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private ArrayList<String> imgPaths = new ArrayList<>();
    private final String mPageName ="退款申请";
    private final RequestHandler requestHandler=new RequestHandler(this);
    private final BitmapHandler bitmapHandler=new BitmapHandler(this);
    private static class  RequestHandler extends Handler{
        private WeakReference<RefundApplyActivity> mWeakReference;
        public RequestHandler(RefundApplyActivity activity) {
            mWeakReference = new WeakReference<RefundApplyActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final RefundApplyActivity reference =mWeakReference.get();
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
                        reference.jsonObject =object.optJSONObject("data");
                        if(result.equals(SendRequestUtil.SUCCESS_VALUE)) {
                            reference.initShow();
                        }else {
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
        private WeakReference<RefundApplyActivity> mWeakReference;
        private BitmapHandler(RefundApplyActivity activity) {
            mWeakReference = new WeakReference<RefundApplyActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final RefundApplyActivity reference =mWeakReference.get();
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
                                reference.showDialog("您的退款申请已经提交");
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
    private void showDialog(String s) {
        new PromptDialog.Builder(context)
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
        refundId= jsonObject.optString("id");
        if (imgPaths.size()!=0){
            for(int i=0;i<imgPaths.size();i++){
                File files=new File(imgPaths.get(i));
                compressImg(files);
            }
        }else {
            customDialog.dismiss();
            btnSend.setEnabled(true);
            showDialog("您的退款申请已经提交");
        }
    }

    private void compressImg(final File files) {
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
        SendRequestUtil.postFileToServer(textParams,fileparams,validateURL,bitmapHandler);
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
        if (data!=null){
            id=data.getStringExtra("id");
            orderNo=data.getStringExtra("orderNo");
            title=data.getStringExtra("title");
            categoryDesc=data.getStringExtra("categoryDesc");
            additionalInfo=data.getStringExtra("additionalInfo");
            finishTime =data.getStringExtra("finishTime");
            parentCategory =data.getStringExtra("parentCategory");
            parentCode=data.getStringExtra("parentCode");
            realAmount=data.getStringExtra("realAmount");
        }
        initSpecDetail();
        initView();
        initSetCodeImage(parentCode);
        initEvent();
        if (!TextUtils.isEmpty(realAmount)){
            double maxValue = Double.parseDouble(realAmount);
            InputFilter[] filters={new CashierInputFilter(0,maxValue)};
            //设置金额输入的过滤器，保证只能输入金额类型
            etRefundAmount.setFilters(filters);
            etRefundAmount.setHint(realAmount);
        }
        mAdapter = new PhotoPickerAdapter(imgPaths);
        mPhotoGridView.setAdapter(mAdapter);
        mPhotoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= 23) {
                    thisPosition =position;
                    initPhoto(position);
                } else {
                    if (position == imgPaths.size()) {
                        PhotoPicker.builder()
                                .setPhotoCount(4)
                                .setShowCamera(true)
                                .setSelected(imgPaths)
                                .setShowGif(true)
                                .setPreviewEnabled(true)
                                .start(RefundApplyActivity.this, PhotoPicker.REQUEST_CODE);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("imgPaths", imgPaths);
                        bundle.putInt("position", position);
                        Intent intent = new Intent(RefundApplyActivity.this, EnlargePicActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, position);
                    }
                }
            }
        });

    }

    private void initEvent() {
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
    private void onShowPhoto() {
        if (thisPosition == imgPaths.size()) {
            PhotoPicker.builder()
                    .setPhotoCount(4)
                    .setShowCamera(true)
                    .setSelected(imgPaths)
                    .setShowGif(true)
                    .setPreviewEnabled(true)
                    .start(RefundApplyActivity.this, PhotoPicker.REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("imgPaths",imgPaths);
            bundle.putInt("position", thisPosition);
            Intent intent=new Intent(RefundApplyActivity.this, EnlargePicActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, thisPosition);
        }
    }
    private void initPhoto(int position) {
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
                            ToastUtil.ToastText(context,"授权失败");
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
                        .start(RefundApplyActivity.this, PhotoPicker.REQUEST_CODE);
            } else {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imgPaths",imgPaths);
                bundle.putInt("position",position);
                Intent intent=new Intent(RefundApplyActivity.this, EnlargePicActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,position);
            }
        }
    }
    private void initView() {
        ((TextView)findViewById(R.id.id_orderNo)).setText(orderNo);
        ((TextView)findViewById(R.id.id_tv_type)).setText(parentCategory);
        ((TextView)findViewById(R.id.id_tv_title)).setText(categoryDesc);
        ((TextView)findViewById(R.id.id_create_time)).setText(finishTime);
        downwardImg=(ImageView)findViewById(R.id.id_downward_img) ;
        layoutCategory=findViewById(R.id.id_category_layout);
        layoutCategoryButton=findViewById(R.id.id_category_button);
        String realAmountText=realAmount+"元";
        ((TextView)findViewById(R.id.id_max_amount)).setText(realAmountText);
        etProblem =(EditText)findViewById(R.id.id_et_problem);
        etRefundAmount=(EditText)findViewById(R.id.id_refund_amount);
        btnSend =(Button)findViewById(R.id.id_btn_send);
        mPhotoGridView =(MyNoScrollGridView)findViewById(R.id.noScrollgridview);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etProblem.getText().toString())){
                    btnSend.setEnabled(false);
                    requestService();
                }else {
                    ToastUtil.ToastText(context,"请您输入退款说明");
                }
            }
        });
    }
    private void requestService() {
        customDialog.show();
        String mDescribe = etProblem.getText().toString().trim();
        String refundAmount=etRefundAmount.getText().toString().trim();
        if (TextUtils.isEmpty(refundAmount)){
            refundAmount=realAmount;
        }
        String validateURL = UrlUtil.RefundApply_Url;
        HashMap<String, String> textParams = new HashMap<String, String>(6);
        textParams.put("userId",userId);
        textParams.put("password",password);
        textParams.put("id",id);
        textParams.put("info", mDescribe);
        textParams.put("refund_amount",refundAmount);
        OkHttpRequestUtil.getInstance(getApplicationContext()).requestAsyn(validateURL, OkHttpRequestUtil.TYPE_POST_MULTIPART,textParams,requestHandler);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customDialog!=null&&customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}
